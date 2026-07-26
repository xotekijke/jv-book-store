package com.example.jvbookstore.service.impl;

import com.example.jvbookstore.dto.order.CreateOrderRequestDto;
import com.example.jvbookstore.dto.order.OrderDto;
import com.example.jvbookstore.dto.order.OrderItemDto;
import com.example.jvbookstore.dto.order.UpdateOrderStatusRequestDto;
import com.example.jvbookstore.exception.DataProcessingException;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.mapper.OrderItemMapper;
import com.example.jvbookstore.mapper.OrderMapper;
import com.example.jvbookstore.model.CartItem;
import com.example.jvbookstore.model.Order;
import com.example.jvbookstore.model.OrderItem;
import com.example.jvbookstore.model.ShoppingCart;
import com.example.jvbookstore.model.User;
import com.example.jvbookstore.repository.CartItemRepository;
import com.example.jvbookstore.repository.OrderItemRepository;
import com.example.jvbookstore.repository.OrderRepository;
import com.example.jvbookstore.repository.ShoppingCartRepository;
import com.example.jvbookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(User user, CreateOrderRequestDto requestDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id: " + user.getId()));

        if (cart.getCartItems().isEmpty()) {
            throw new DataProcessingException("Can't place an order with an empty cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            order.getOrderItems().add(orderItem);
            total = total.add(orderItem.getPrice());
        }
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAllByShoppingCartId(cart.getId());

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDto> getOrderHistory(User user) {
        return orderRepository.findAllByUserId(user.getId()).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getOrderItems(User user, Long orderId) {
        Order order = getUserOrderOrThrow(user, orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto getOrderItem(User user, Long orderId, Long itemId) {
        getUserOrderOrThrow(user, orderId);
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with id: " + itemId));
        return orderItemMapper.toDto(orderItem);
    }

    private Order getUserOrderOrThrow(User user, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
    }
}

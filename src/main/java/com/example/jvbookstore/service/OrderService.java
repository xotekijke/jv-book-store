package com.example.jvbookstore.service;

import com.example.jvbookstore.dto.order.CreateOrderRequestDto;
import com.example.jvbookstore.dto.order.OrderDto;
import com.example.jvbookstore.dto.order.OrderItemDto;
import com.example.jvbookstore.dto.order.UpdateOrderStatusRequestDto;
import com.example.jvbookstore.model.User;
import java.util.List;

public interface OrderService {
    OrderDto placeOrder(User user, CreateOrderRequestDto requestDto);

    List<OrderDto> getOrderHistory(User user);

    OrderDto updateStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemDto> getOrderItems(User user, Long orderId);

    OrderItemDto getOrderItem(User user, Long orderId, Long itemId);
}

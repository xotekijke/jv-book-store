package com.example.jvbookstore.controller;

import com.example.jvbookstore.dto.order.CreateOrderRequestDto;
import com.example.jvbookstore.dto.order.OrderDto;
import com.example.jvbookstore.dto.order.OrderItemDto;
import com.example.jvbookstore.dto.order.UpdateOrderStatusRequestDto;
import com.example.jvbookstore.model.User;
import com.example.jvbookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for placing and managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place an order", description = "Place an order using the current cart")
    public OrderDto placeOrder(@AuthenticationPrincipal User user,
                                @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.placeOrder(user, requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order history", description = "Retrieve the current user's orders")
    public List<OrderDto> getOrderHistory(@AuthenticationPrincipal User user) {
        return orderService.getOrderHistory(user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Update the status of an order")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                       @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateStatus(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order items", description = "Retrieve all items for a specific order")
    public List<OrderItemDto> getOrderItems(@AuthenticationPrincipal User user,
                                             @PathVariable Long orderId) {
        return orderService.getOrderItems(user, orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order item", description = "Retrieve a specific item from an order")
    public OrderItemDto getOrderItem(@AuthenticationPrincipal User user,
                                      @PathVariable Long orderId,
                                      @PathVariable Long itemId) {
        return orderService.getOrderItem(user, orderId, itemId);
    }
}

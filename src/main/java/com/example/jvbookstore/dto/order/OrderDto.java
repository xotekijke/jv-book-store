package com.example.jvbookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItems = new HashSet<>();
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
}

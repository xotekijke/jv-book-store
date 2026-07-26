package com.example.jvbookstore.dto.order;

import com.example.jvbookstore.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private Order.Status status;
}

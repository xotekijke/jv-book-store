package com.example.jvbookstore.mapper;

import com.example.jvbookstore.config.MapperConfig;
import com.example.jvbookstore.dto.order.OrderItemDto;
import com.example.jvbookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);
}

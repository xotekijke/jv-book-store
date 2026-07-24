package com.example.jvbookstore.service;

import com.example.jvbookstore.dto.cart.CreateCartItemRequestDto;
import com.example.jvbookstore.dto.cart.ShoppingCartDto;
import com.example.jvbookstore.dto.cart.UpdateCartItemRequestDto;
import com.example.jvbookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getByUser(User user);

    ShoppingCartDto addBook(User user, CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateItemQuantity(User user, Long cartItemId,
                                        UpdateCartItemRequestDto requestDto);

    void removeItem(User user, Long cartItemId);
}

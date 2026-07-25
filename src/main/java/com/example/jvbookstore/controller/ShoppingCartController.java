package com.example.jvbookstore.controller;

import com.example.jvbookstore.dto.cart.CreateCartItemRequestDto;
import com.example.jvbookstore.dto.cart.ShoppingCartDto;
import com.example.jvbookstore.dto.cart.UpdateCartItemRequestDto;
import com.example.jvbookstore.model.User;
import com.example.jvbookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing the shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart", description = "Retrieve the current user's cart")
    public ShoppingCartDto getCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getByUser(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to cart", description = "Add a book to the shopping cart")
    public ShoppingCartDto addBookToCart(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.addBook(user, requestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update cart item quantity",
            description = "Update the quantity of a book in the shopping cart")
    public ShoppingCartDto updateCartItem(@AuthenticationPrincipal User user,
                                           @PathVariable Long cartItemId,
                                           @RequestBody @Valid UpdateCartItemRequestDto
                                                      requestDto) {
        return shoppingCartService.updateItemQuantity(user, cartItemId, requestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove cart item",
            description = "Remove a book from the shopping cart")
    public void removeCartItem(@AuthenticationPrincipal User user,
                                @PathVariable Long cartItemId) {
        shoppingCartService.removeItem(user, cartItemId);
    }
}

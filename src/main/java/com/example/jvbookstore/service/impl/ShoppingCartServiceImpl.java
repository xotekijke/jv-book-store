package com.example.jvbookstore.service.impl;

import com.example.jvbookstore.dto.cart.CreateCartItemRequestDto;
import com.example.jvbookstore.dto.cart.ShoppingCartDto;
import com.example.jvbookstore.dto.cart.UpdateCartItemRequestDto;
import com.example.jvbookstore.exception.EntityNotFoundException;
import com.example.jvbookstore.mapper.CartItemMapper;
import com.example.jvbookstore.mapper.ShoppingCartMapper;
import com.example.jvbookstore.model.Book;
import com.example.jvbookstore.model.CartItem;
import com.example.jvbookstore.model.ShoppingCart;
import com.example.jvbookstore.model.User;
import com.example.jvbookstore.repository.BookRepository;
import com.example.jvbookstore.repository.CartItemRepository;
import com.example.jvbookstore.repository.ShoppingCartRepository;
import com.example.jvbookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getByUser(User user) {
        return shoppingCartMapper.toDto(getOrCreateCart(user));
    }

    @Override
    @Transactional
    public ShoppingCartDto addBook(User user, CreateCartItemRequestDto requestDto) {
        ShoppingCart cart = getOrCreateCart(user);

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + requestDto.getBookId()));

        CartItem cartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(cart.getId(), book.getId())
                .orElseGet(() -> {
                    CartItem newItem = cartItemMapper.toEntity(requestDto);
                    newItem.setShoppingCart(cart);
                    newItem.setBook(book);
                    newItem.setQuantity(0);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCartRepository.findById(cart.getId())
                .orElseThrow());
    }

    @Override
    @Transactional
    public ShoppingCartDto updateItemQuantity(User user, Long cartItemId,
                                               UpdateCartItemRequestDto requestDto) {
        ShoppingCart cart = getOrCreateCart(user);
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id: " + cartItemId));

        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCartRepository.findById(cart.getId())
                .orElseThrow());
    }

    @Override
    @Transactional
    public void removeItem(User user, Long cartItemId) {
        ShoppingCart cart = getOrCreateCart(user);
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id: " + cartItemId));

        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getOrCreateCart(User user) {
        return shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });
    }
}

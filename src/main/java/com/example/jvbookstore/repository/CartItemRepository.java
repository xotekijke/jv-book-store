package com.example.jvbookstore.repository;

import com.example.jvbookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    void deleteAllByShoppingCartId(Long shoppingCartId);
}

package com.bookmarket.web.repository;

import com.bookmarket.web.entity.Book;
import com.bookmarket.web.entity.Cart;
import com.bookmarket.web.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndBook(Cart cart, Book book);
}

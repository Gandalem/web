package com.bookmarket.web.service;

import com.bookmarket.web.entity.Book;
import com.bookmarket.web.entity.Cart;
import com.bookmarket.web.entity.CartItem;
import com.bookmarket.web.entity.User;
import com.bookmarket.web.repository.BookRepository;
import com.bookmarket.web.repository.CartItemRepository;
import com.bookmarket.web.repository.CartRepository;
import com.bookmarket.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // 장바구니에 도서 추가
    public Long addCart(Long bookId, int count, String username) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 없습니다. id=" + bookId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.createCart(user);
            return cartRepository.save(newCart);
        });

        CartItem cartItem = cartItemRepository.findByCartAndBook(cart, book).orElse(null);

        if (cartItem == null) {
            cartItem = CartItem.createCartItem(cart, book, count);
            cartItemRepository.save(cartItem);
        } else {
            cartItem.addCount(count);
        }
        return cartItem.getId();
    }

    // 장바구니 조회
    @Transactional(readOnly = true)
    public Cart findCartByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        return cartRepository.findByUser(user).orElse(null);
    }

    // 장바구니 상품 수량 변경
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 상품이 없습니다. id=" + cartItemId));
        cartItem.setCount(count);
    }

    // 장바구니 상품 삭제
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}

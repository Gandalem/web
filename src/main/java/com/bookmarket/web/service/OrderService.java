package com.bookmarket.web.service;

import com.bookmarket.web.entity.*;
import com.bookmarket.web.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // 장바구니 기반 주문 생성
    public Long createOrderFromCart(String username) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 비어있습니다."));

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            Book book = cartItem.getBook();
            int count = cartItem.getCount();

            // 재고 확인 및 감소
            if (book.getStock() < count) {
                throw new IllegalArgumentException(book.getTitle() + "의 재고가 부족합니다.");
            }
            book.setStock(book.getStock() - count);
            bookRepository.save(book);

            OrderItem orderItem = OrderItem.createOrderItem(book, count);
            orderItems.add(orderItem);
        }

        Order order = Order.createOrder(user, orderItems);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // 장바구니 비우기
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return order.getId();
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 없습니다. id=" + orderId));
    }

    // 사용자 주문 목록 조회
    @Transactional(readOnly = true)
    public List<Order> findOrdersByUser(String username) {
        User user = userRepository.findByUsername(username);
        return orderRepository.findByUser(user);
    }
}

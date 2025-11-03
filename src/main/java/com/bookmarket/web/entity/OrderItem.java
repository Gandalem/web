package com.bookmarket.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private int orderPrice; // 주문 당시 가격
    private int count; // 수량

    public static OrderItem createOrderItem(Book book, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(book);
        orderItem.setCount(count);
        orderItem.setOrderPrice(book.getPrice()); // 주문 당시 가격 저장
        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}

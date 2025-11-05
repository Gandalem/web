package com.bookmarket.web.repository;

import com.bookmarket.web.entity.Order;
import com.bookmarket.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.user u JOIN FETCH o.orderItems oi JOIN FETCH oi.book WHERE u = :user ORDER BY o.orderDate DESC")
    List<Order> findByUser(@Param("user") User user);
}

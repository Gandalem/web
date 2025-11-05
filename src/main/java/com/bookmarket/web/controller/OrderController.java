package com.bookmarket.web.controller;

import com.bookmarket.web.entity.Cart;
import com.bookmarket.web.entity.Order;
import com.bookmarket.web.service.CartService;
import com.bookmarket.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    // 주문서 작성 페이지 (장바구니에서 넘어옴)
    @GetMapping("/new")
    public String orderForm(Authentication authentication, Model model) {
        // 여기서는 간단히 장바구니 내용을 보여주고 바로 주문 생성으로 넘어가는 흐름을 가정합니다.
        // 실제로는 배송지 정보 등을 입력받는 폼이 필요합니다.
        String username = authentication.getName();
        // CartService를 통해 장바구니 정보를 가져와 모델에 추가할 수 있습니다.
        // model.addAttribute("cart", cartService.findCartByUser(username));
        return "order_form";
    }

    // 주문 생성 처리
    @PostMapping
    public String createOrder(Authentication authentication, Model model) {
        String username = authentication.getName();
        try {
            Long orderId = orderService.createOrderFromCart(username);
            return "redirect:/orders/" + orderId;
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            // 장바구니 페이지를 다시 렌더링하기 위해 필요한 데이터를 모델에 추가
            Cart cart = cartService.findCartByUser(username);
            model.addAttribute("cart", cart);
            if (cart != null) {
                int totalPrice = cart.getCartItems().stream()
                                     .mapToInt(item -> item.getTotalPrice())
                                     .sum();
                model.addAttribute("totalPrice", totalPrice);
            }
            return "cart";
        }
    }

    // 사용자 주문 목록
    @GetMapping("/")
    public String orderList(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Order> orders = orderService.findOrdersByUser(username);
        model.addAttribute("orders", orders);
        return "order_list"; // order_list.html 뷰가 필요합니다. (요청에 없으므로 추후 생성)
    }

    // 주문 상세 페이지
    @GetMapping("/{id}")
    public String orderDetail(@PathVariable("id") Long id, Authentication authentication, Model model) {
        Order order = orderService.findOrderById(id);
        if (!order.getUser().getUsername().equals(authentication.getName())) {
            // 본인 주문이 아니면 접근 불가 처리
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        model.addAttribute("order", order);
        return "order_detail";
    }
}

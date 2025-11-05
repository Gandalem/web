package com.bookmarket.web.controller;

import com.bookmarket.web.entity.Cart;
import com.bookmarket.web.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    // 장바구니 페이지
    @GetMapping
    public String cartPage(Authentication authentication, Model model) {
        String username = authentication.getName();
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

    // 장바구니에 도서 추가
    @PostMapping("/add")
    public String addCart(@RequestParam("bookId") Long bookId,
                          @RequestParam(value = "count", defaultValue = "1") int count,
                          Authentication authentication) {
        String username = authentication.getName();
        cartService.addCart(bookId, count, username);
        return "redirect:/cart";
    }

    // 장바구니 상품 수량 변경
    @PostMapping("/update")
    public String updateCartItemCount(@RequestParam("cartItemId") Long cartItemId,
                                      @RequestParam("count") int count) {
        cartService.updateCartItemCount(cartItemId, count);
        return "redirect:/cart";
    }

    // 장바구니 상품 삭제
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam("cartItemId") Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return "redirect:/cart";
    }
}

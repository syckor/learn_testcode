package com.example.cafekiosk.spring.api.controller.order;


import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.example.cafekiosk.spring.api.service.order.OrderService;
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.example.cafekiosk.spring.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request){
        LocalDateTime registeredDateTime = LocalDateTime.now();
        return orderService.createOrder(request, registeredDateTime);
    }
}

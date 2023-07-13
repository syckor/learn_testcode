package com.example.cafekiosk.spring.api.service.order;

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.example.cafekiosk.spring.domain.order.Order;
import com.example.cafekiosk.spring.domain.order.OrderRepository;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductType;
import com.example.cafekiosk.spring.domain.stock.Stock;
import com.example.cafekiosk.spring.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductBy(productNumbers);

        deductStockQuantities(products);

        Order order = Order.create(products, registeredDateTime);
        Order saveOrder = orderRepository.save(order);

        //order
        return OrderResponse.of(saveOrder);
    }

    private void deductStockQuantities(List<Product> products) {

        List<String> stockProductNumbers = extractStockProductNumbers(products);
        
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);

        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
        
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        
        for(String stockProductNumber : new HashSet<>(stockProductNumbers)){
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            System.out.println("quantity : " + stockProductNumber + " " + quantity);

            if(stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }

            stock.deductQuantity(quantity);
        }
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber).toList();
    }

    private List<Product> findProductBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));
        List<Product> duplicateProducts = productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
        return duplicateProducts;
    }
}

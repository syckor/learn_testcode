package com.example.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.*;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatus(){
        //given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(3000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(ProductType.HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();
        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", "아메리카노", SELLING),
                        Tuple.tuple("002", "카페라떼", HOLD)
                );

    }


    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn(){
        //given
        Product product1 = createProduct("001", ProductType.HANDMADE, SELLING, "아메리카노", 3000);
        Product product2 = createProduct("002", ProductType.HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", ProductType.HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", "아메리카노", SELLING),
                        Tuple.tuple("002", "카페라떼", HOLD)
                );

    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    @Test
    void findLatestProduct(){
        //given
        String targetProductNumber = "003";

        Product product1 = createProduct("001", ProductType.HANDMADE,SELLING, "아메리카노", 3000);
        Product product2 = createProduct("002", ProductType.HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct(targetProductNumber, ProductType.HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        String latestProductNumber = productRepository.findLatestProduct();

        //then
        Assertions.assertThat(latestProductNumber).isEqualTo(targetProductNumber);

    }


    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환")
    @Test
    void findLatestProductWhenProductIsEmpty(){
        //when
        String latestProductNumber = productRepository.findLatestProduct();

        //then
        Assertions.assertThat(latestProductNumber).isNull();

    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus selling, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(selling)
                .name(name)
                .price(price)
                .build();
    }


}
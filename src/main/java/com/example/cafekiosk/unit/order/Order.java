package com.example.cafekiosk.unit.order;

import com.example.cafekiosk.unit.Beverage;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Entity
public class Order {

    private final LocalDateTime orderDateTIme;
    private final List<Beverage> beverages;

}

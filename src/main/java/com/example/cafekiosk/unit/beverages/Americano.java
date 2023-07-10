package com.example.cafekiosk.unit.beverages;

import com.example.cafekiosk.unit.Beverage;

public class Americano implements Beverage {
    @Override
    public String getName() {
        return "아메리카노";
    }

    @Override
    public int getPrice() {
        return 3000;
    }
}

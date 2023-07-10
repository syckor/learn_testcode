package com.example.cafekiosk.unit.beverages;

import com.example.cafekiosk.unit.Beverage;

public class Latte implements Beverage {
    @Override
    public String getName() {
        return "라떼";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}

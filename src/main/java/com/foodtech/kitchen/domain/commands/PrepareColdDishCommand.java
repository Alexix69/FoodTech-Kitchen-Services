package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.Product;

import java.util.ArrayList;
import java.util.List;

public class PrepareColdDishCommand implements Command {

    private final List<Product> products;

    public PrepareColdDishCommand(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    @Override
    public void execute() {
        // FR-029: simulation removed — task state is now changed only through explicit user action
        System.out.println("[COLD_KITCHEN] Cold dish command registered for " + products.size() + " product(s)");
    }
}
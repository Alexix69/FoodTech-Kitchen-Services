package com.foodtech.kitchen.domain.commands;

import com.foodtech.kitchen.domain.model.Product;

import java.util.ArrayList;
import java.util.List;

public class PrepareDrinkCommand implements Command {

    private final List<Product> products;

    public PrepareDrinkCommand(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    @Override
    public void execute() {
        // FR-029: simulation removed — task state is now changed only through explicit user action
        System.out.println("[BAR] Drink command registered for " + products.size() + " product(s)");
    }
}
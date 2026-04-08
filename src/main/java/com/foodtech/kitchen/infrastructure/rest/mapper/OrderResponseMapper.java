package com.foodtech.kitchen.infrastructure.rest.mapper;

import com.foodtech.kitchen.domain.model.Order;
import com.foodtech.kitchen.infrastructure.rest.dto.OrderResponse;
import com.foodtech.kitchen.infrastructure.rest.dto.ProductRequest;

import java.util.List;

public class OrderResponseMapper {

    private OrderResponseMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        List<ProductRequest> products = order.getProducts().stream()
                .map(product -> new ProductRequest(product.getName(), product.getType().name()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTableNumber(),
                order.getStatus().name(),
                products
        );
    }
}

package com.foodtech.kitchen.infrastructure.rest;

import com.foodtech.kitchen.application.ports.in.GetCompletedOrdersPort;
import com.foodtech.kitchen.application.ports.in.GetOrderByIdPort;
import com.foodtech.kitchen.application.ports.in.GetOrderStatusPort;
import com.foodtech.kitchen.application.ports.in.UpdateOrderPort;
import com.foodtech.kitchen.application.ports.in.DeleteOrderPort;
import com.foodtech.kitchen.application.ports.in.ProcessOrderPort;
import com.foodtech.kitchen.application.ports.in.RequestOrderInvoicePort;
import com.foodtech.kitchen.application.usecases.dto.CompletedOrderView;
import com.foodtech.kitchen.domain.model.Order;
import com.foodtech.kitchen.domain.model.Task;
import com.foodtech.kitchen.domain.model.TaskStatus;
import com.foodtech.kitchen.infrastructure.rest.dto.CompletedOrderResponse;
import com.foodtech.kitchen.infrastructure.rest.dto.CreateOrderRequest;
import com.foodtech.kitchen.infrastructure.rest.dto.CreateOrderResponse;
import com.foodtech.kitchen.infrastructure.rest.dto.OrderResponse;
import com.foodtech.kitchen.infrastructure.rest.dto.UpdateOrderRequest;
import com.foodtech.kitchen.infrastructure.rest.mapper.CompletedOrderMapper;
import com.foodtech.kitchen.infrastructure.rest.mapper.OrderMapper;
import com.foodtech.kitchen.infrastructure.rest.mapper.OrderResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final String ORDER_SUCCESS_MESSAGE = "Order processed successfully";

    private final ProcessOrderPort processOrderPort;
    private final GetOrderByIdPort getOrderByIdPort;
    private final UpdateOrderPort updateOrderPort;
    private final DeleteOrderPort deleteOrderPort;
    private final GetOrderStatusPort getOrderStatusPort;
    private final GetCompletedOrdersPort getCompletedOrdersPort;
    private final RequestOrderInvoicePort requestOrderInvoicePort;

    public OrderController(ProcessOrderPort processOrderPort,
                           GetOrderByIdPort getOrderByIdPort,
                           UpdateOrderPort updateOrderPort,
                           DeleteOrderPort deleteOrderPort,
                           GetOrderStatusPort getOrderStatusPort,
                           GetCompletedOrdersPort getCompletedOrdersPort,
                           RequestOrderInvoicePort requestOrderInvoicePort) {
        this.processOrderPort = processOrderPort;
        this.getOrderByIdPort = getOrderByIdPort;
        this.updateOrderPort = updateOrderPort;
        this.deleteOrderPort = deleteOrderPort;
        this.getOrderStatusPort = getOrderStatusPort;
        this.getCompletedOrdersPort = getCompletedOrdersPort;
        this.requestOrderInvoicePort = requestOrderInvoicePort;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = OrderMapper.toDomain(request);
        List<Task> tasks = processOrderPort.execute(order);
        Long orderId = tasks.isEmpty() ? order.getId() : tasks.get(0).getOrderId();
        CreateOrderResponse response = new CreateOrderResponse(
            orderId,
                order.getTableNumber(),
                tasks.size(),
                ORDER_SUCCESS_MESSAGE
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        Order order = getOrderByIdPort.execute(orderId);
        return ResponseEntity.ok(OrderResponseMapper.toResponse(order));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long orderId,
                                                     @RequestBody UpdateOrderRequest request) {
        Order updatedOrder = updateOrderPort.execute(orderId, request.tableNumber());
        return ResponseEntity.ok(OrderResponseMapper.toResponse(updatedOrder));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        deleteOrderPort.execute(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<Map<String, String>> getOrderStatus(@PathVariable Long orderId) {
        TaskStatus status = getOrderStatusPort.execute(orderId);
        return ResponseEntity.ok(Map.of(
                "orderId", orderId.toString(),
                "status", status.name()
        ));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<CompletedOrderResponse>> getCompletedOrders() {
        List<CompletedOrderView> views = getCompletedOrdersPort.execute();
        return ResponseEntity.ok(CompletedOrderMapper.toResponseList(views));
    }

    @PostMapping("/{orderId}/invoice")
    public ResponseEntity<Void> requestInvoice(@PathVariable Long orderId) {
        requestOrderInvoicePort.execute(orderId);
        return ResponseEntity.accepted().build();
    }
}
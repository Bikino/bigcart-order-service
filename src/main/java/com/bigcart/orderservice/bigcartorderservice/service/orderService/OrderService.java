package com.bigcart.orderservice.bigcartorderservice.service.orderService;

import com.bigcart.orderservice.bigcartorderservice.model.Orders;

import java.util.Optional;
import java.util.Set;

public interface OrderService {
    public Set<Orders> getOrders(long userId);
    public Optional<Orders> getOrder(long orderId);
    public Orders addOrder(Orders orders);
}

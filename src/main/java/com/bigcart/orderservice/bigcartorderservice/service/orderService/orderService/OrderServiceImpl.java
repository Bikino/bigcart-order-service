package com.bigcart.orderservice.bigcartorderservice.service.orderService.orderService;

import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class OrderServiceImpl {

    @Autowired
    OrderRepository orderRepository;

    public Set<Orders> getOrders(long userId){
        return orderRepository.findByUserId(userId);
    }

    public Optional<Orders> getOrder(long orderId){
        return orderRepository.findById(orderId);
    }

    public Orders addOrder(Orders orders){
        orders.setCreationDate(LocalDate.now());
        return orderRepository.save(orders);
    }
}

package com.bigcart.orderservice.bigcartorderservice.service.orderService;

import com.bigcart.orderservice.bigcartorderservice.model.OrderDetails;
import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderDetailsRepository;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderRepository;
import com.bigcart.orderservice.bigcartorderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

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

    public List<Orders> getVendorOrders(Long vendorId) {
        return orderRepository.findOrdersByVendorId(vendorId);
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

}

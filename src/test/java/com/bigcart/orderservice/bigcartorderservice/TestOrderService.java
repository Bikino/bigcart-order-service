package com.bigcart.orderservice.bigcartorderservice;

import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderRepository;
import com.bigcart.orderservice.bigcartorderservice.service.OrderService;
import com.bigcart.orderservice.bigcartorderservice.service.orderService.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyLong;
//import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class TestOrderService {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testGetOrder() {
        Orders orders = new Orders();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orders));
        //assertEquals(orders,orderService.getOrder( 1l).get());
    }

    @Test
    public void testGetOrdersByUserId() {
        Set<Orders> ordersSet = new HashSet<>();
        ordersSet.add(new Orders());
        when(orderRepository.findByUserId(anyLong())).thenReturn(ordersSet);
        //assertEquals(ordersSet, orderService.getOrders(2l));
    }

    @Test
    public void testGetByVendorId() {
        List<Orders> ordersList = new ArrayList<>(Arrays.asList(new Orders(), new Orders()));
        when(orderRepository.findOrdersByVendorId(anyLong())).thenReturn(ordersList);
        //assertEquals(ordersList, orderService.getVendorOrders(3l));
    }

    @Test
    public void testVerifyInvocationOfGetOrder() {
        Set<Orders> ordersSet = new HashSet<>();
        ordersSet.add(new Orders());
        when(orderRepository.findByUserId(anyLong())).thenReturn(ordersSet);
        orderService.getOrders(2l);
        Mockito.verify(orderRepository,times(1)).findByUserId(2l);
    }

    @Test
    public void testVerifyInvocationOfGetByVendorId() {
        List<Orders> ordersList = new ArrayList<>(Arrays.asList(new Orders(), new Orders()));
        when(orderRepository.findOrdersByVendorId(anyLong())).thenReturn(ordersList);
        orderService.getVendorOrders(3l);
        Mockito.verify(orderRepository,times(1)).findOrdersByVendorId(3l);

    }

}

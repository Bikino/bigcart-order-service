package com.bigcart.orderservice.bigcartorderservice;

import com.bigcart.orderservice.bigcartorderservice.controller.OrderConroller;

import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderRepository;
import com.bigcart.orderservice.bigcartorderservice.service.OrderService;
import com.bigcart.orderservice.bigcartorderservice.service.orderService.OrderServiceImpl;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class TestOrderController {
    @Mock
    private OrderServiceImpl orderService;
    @InjectMocks
    OrderConroller conroller;

    @Test
    public void TestGetOrdersbyVendorId() {
        List<Orders> list  = new ArrayList<>(Arrays.asList(new Orders(), new Orders()));
        when(orderService.getVendorOrders(9l)).thenReturn(list);
        assertEquals(list, conroller.getVendorOrders(9l).getBody());
    }

    @Test
    public void TestGetOrder() {
        Orders order = new Orders();
        Set<Orders> set = new HashSet<>();
        set.add(order);
        when(orderService.getOrders(anyLong())).thenReturn(set);
        conroller.getOrder(5l);
        Mockito.verify(orderService, times(1)).getOrder(5l);
    }

    @Test
    public void testGetOrderWithgetByUserIdis0() {
        Set<Orders> set = new HashSet<>();
        Orders order = new Orders();
        set.add(order);
        when(orderService.getOrders(anyLong())).thenReturn(set);
        conroller.getOrder(1l);
        Mockito.verify(orderService, times(0)).getVendorOrders(1l);
    }

}

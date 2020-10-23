package com.bigcart.orderservice.bigcartorderservice.OrderController;

import com.bigcart.orderservice.bigcartorderservice.FeignController.ProductProxy;
import com.bigcart.orderservice.bigcartorderservice.model.OrderDetails;
import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderRepository;
import com.bigcart.orderservice.bigcartorderservice.service.orderService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/order")
public class OrderConroller {
    @Autowired
    ProductProxy productProxy;
    @Autowired
    OrderService orderService;


    @PostMapping("/")
    public ResponseEntity<Orders> addOrders(@RequestBody Orders orders) {

        return Optional.
                ofNullable(orderService.addOrder(orders)).
                map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order)).
                orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrder(@PathVariable Long orderId) {

        return Optional.
                ofNullable(orderService.getOrder(orderId)).
                map(orders -> ResponseEntity.ok().body(orders.get())).
                orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("allOrders/{userId}")
        public ResponseEntity<Set<Orders>> getAllOrders(@PathVariable Long userId) {
        return Optional.
                ofNullable(orderService.getOrders(userId)).
                        map(orders -> ResponseEntity.ok().body(orders)).
                        orElseGet(() -> ResponseEntity.badRequest().build());
    }

//    @PutMapping("order/{orderID}")
//    public ResponseEntity<Orders> updateOrder(@RequestBody Orders orders, @PathVariable Long orderId) {
//        return orderService.addOrder(orders);
//    }
//    @PostMapping(value = "/")
//    public ResponseEntity<Object> addOrder(@RequestBody Object object) {
//        //orderService.addOrder(object);
//        //productProxy.modifyProducts(1, 3);
//    }
}


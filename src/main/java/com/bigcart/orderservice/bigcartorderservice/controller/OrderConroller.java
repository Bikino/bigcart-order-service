package com.bigcart.orderservice.bigcartorderservice.controller;

import com.bigcart.orderservice.bigcartorderservice.FeignController.ProductProxy;
import com.bigcart.orderservice.bigcartorderservice.dto.ItemDto;
import com.bigcart.orderservice.bigcartorderservice.dto.ListDto;
import com.bigcart.orderservice.bigcartorderservice.model.OrderDetails;
import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderConroller {
    @Autowired
    ProductProxy productProxy;
    @Autowired
    OrderService orderService;


    @PostMapping("/")
    public ResponseEntity<Orders> persistOrders(@RequestBody Orders orders) {

        return Optional.
                ofNullable(orderService.addOrder(orders)).
                map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order)).
                orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrder(@PathVariable Long orderId) {

        return orderService.getOrder(orderId).
                map(orders -> ResponseEntity.ok().body(orders)).
                orElseGet(() -> ResponseEntity.badRequest().build());
    }
    @GetMapping("/userOrders/{userId}")
    public ResponseEntity<Set<Orders>> userOrders(@PathVariable Long userId) {
        return Optional.
                ofNullable(orderService.getOrders(userId)).
                map(orders -> ResponseEntity.ok().body(orders)).
                orElseGet(() -> ResponseEntity.badRequest().build());
    }


    // Youssopha
    @GetMapping("/allOrders")
        public ResponseEntity<List<Orders>> getAllOrders() {
            List<Orders> orders = orderService.getAllOrders();
            if(orders == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<List<Orders>>(orders, HttpStatus.OK);
    }
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Orders>> getVendorOrders(@PathVariable Long vendorId) {
        List<Orders> orders = orderService.getVendorOrders(vendorId);
        return new ResponseEntity<List<Orders>>(orders, HttpStatus.OK);
    }

    @PostMapping("/shoppingCart")
    public ResponseEntity shoppingCart(@RequestBody OrderDetails orderDetails,@ApiIgnore HttpSession session) {
        if (session.isNew()) {
            Orders shoppingCard = new Orders();
            session.setAttribute("shoppingCart", shoppingCard);
        }
        Orders shoppingCard = (Orders) session.getAttribute("shoppingCart");
        shoppingCard.addOrderDetail(orderDetails);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/shoppingCart/{productId/{quantity}")
    public void updateShoppingCart(@PathVariable Long productId, @PathVariable int quantity,@ApiIgnore HttpSession session) {
        Orders shoppingCard = (Orders) session.getAttribute("shoppingCart");
        Set<OrderDetails> set = shoppingCard.getOrderDetails();
        Iterator<OrderDetails> detailsIterator = set.iterator();
        while(detailsIterator.hasNext()) {
            OrderDetails orderDetails = detailsIterator.next();
            if(orderDetails.getProductId() == productId) {
                orderDetails.setPrice(orderDetails.getPrice() -
                        orderDetails.getPrice()/ orderDetails.getQuantity() * quantity);
                orderDetails.setQuantity(quantity);
                if(quantity == 0)
                    set.remove(orderDetails);
            }
        }
    }

    @PostMapping("/placeOrder")
    public Orders placeOrder(@ApiIgnore HttpSession session) {

        Orders orders = (Orders)(session.getAttribute("shoppingCart"));
        System.out.println("placeOrder");
        Orders savedOrders = orderService.addOrder(orders);
        System.out.println("savedOrders");
        ListDto listDto = new ListDto();
        Iterator<OrderDetails> ordIterator = savedOrders.getOrderDetails().iterator();
        while(ordIterator.hasNext()) {
            OrderDetails details = ordIterator.next();
            ItemDto itemDto = new ItemDto(details.getProductId(), details.getVendorId(), details.getQuantity());
            listDto.addToList(itemDto);
        }
        System.out.println(listDto);
        System.out.println("------------------");
//        productProxy.placeProducts(listDto);
        session.setAttribute("shoppingCart", new Orders());
        return savedOrders;
    }
//    @GetMapping("/addPayment")
//    public ResponseEntity addPayment(Long paymentId, Long orderId) {
//
//    }
}


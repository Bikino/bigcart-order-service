package com.bigcart.orderservice.bigcartorderservice.controller;

import com.bigcart.orderservice.bigcartorderservice.FeignController.ProductProxy;
import com.bigcart.orderservice.bigcartorderservice.dto.ItemDto;
import com.bigcart.orderservice.bigcartorderservice.dto.ListDto;
import com.bigcart.orderservice.bigcartorderservice.model.OrderDetails;
import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderRepository;
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
    @Autowired
    OrderRepository orderRepository;

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

    @PostMapping("/addToCart")
    public ResponseEntity shoppingCart(@RequestBody OrderDetails orderDetails,@ApiIgnore HttpSession session) {
        if (session.isNew()) {
            Orders shoppingCard = new Orders();
            session.setAttribute("shoppingCart", shoppingCard);
        }
        Orders shoppingCard = (Orders) session.getAttribute("shoppingCart");
        shoppingCard.addOrderDetail(orderDetails);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/shoppingCart/{productId}/{quantity}")
    public ResponseEntity updateShoppingCart(@PathVariable Long productId, @PathVariable int quantity,@ApiIgnore HttpSession session) {
        Orders shoppingCart = (Orders) session.getAttribute("shoppingCart");
        Set<OrderDetails> set = shoppingCart.getOrderDetails();
        Iterator<OrderDetails> detailsIterator = set.iterator();

        while(detailsIterator.hasNext()) {
            OrderDetails orderDetails = detailsIterator.next();
            if(orderDetails.getProductId() == productId) {
                System.out.println(shoppingCart.getTotalAmount());//
                shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() -
                        orderDetails.getPrice() * orderDetails.getQuantity());
                System.out.println(shoppingCart.getTotalAmount());//
                orderDetails.setQuantity(quantity);
                shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() +
                        orderDetails.getPrice() * quantity);
                System.out.println(shoppingCart.getTotalAmount());//
                if(quantity == 0) {
                    set.remove(orderDetails);
                }
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/checkOut")
    public ResponseEntity<Long> checkOut(@ApiIgnore HttpSession session) {

        Orders orders = (Orders)(session.getAttribute("shoppingCart"));
        Orders savedOrders = orderService.addOrder(orders);
        ListDto listDto = new ListDto();
        Iterator<OrderDetails> ordIterator = savedOrders.getOrderDetails().iterator();
        while(ordIterator.hasNext()) {
            OrderDetails details = ordIterator.next();
            ItemDto itemDto = new ItemDto(details.getProductId(), details.getVendorId(), details.getQuantity());
            listDto.addToList(itemDto);
        }
        productProxy.placeProducts(listDto);
        session.setAttribute("shoppingCart", new Orders());
        return new ResponseEntity<Long>(savedOrders.getId(), HttpStatus.OK);
    }

    @GetMapping("/addPayment/{paymentId}/{orderId}")
    public ResponseEntity<Long> addPayment(@PathVariable Long paymentId,@PathVariable Long orderId) {
        return  orderService.getOrder(orderId).
                map(o -> {
                    o.setPaymentId(paymentId);
                    orderRepository.save(o);
                    return ResponseEntity.ok().body(orderId);
                }).
                orElseGet(() -> ResponseEntity.notFound().build());

    }
}


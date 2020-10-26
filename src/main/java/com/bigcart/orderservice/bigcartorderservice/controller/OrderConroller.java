package com.bigcart.orderservice.bigcartorderservice.controller;

import com.bigcart.orderservice.bigcartorderservice.FeignController.ProductProxy;
import com.bigcart.orderservice.bigcartorderservice.dto.ItemDto;
import com.bigcart.orderservice.bigcartorderservice.dto.ListDto;
import com.bigcart.orderservice.bigcartorderservice.model.OrderDetails;
import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.service.OrderService;
import javassist.tools.web.BadHttpRequest;
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

    @GetMapping("allOrders/{userId}")
        public ResponseEntity<Set<Orders>> getAllOrders(@PathVariable Long userId) {
        return Optional.
                ofNullable(orderService.getOrders(userId)).
                        map(orders -> ResponseEntity.ok().body(orders)).
                        orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/shoppingCard")
    public void shoppingCard(@RequestBody OrderDetails orderDetails,@ApiIgnore HttpSession session) {
        if (session.isNew()) {
            Orders shoppingCard = new Orders();
//            shoppingCard.setTotalAmount(1);
//            shoppingCard.setAddressId(1);
//            shoppingCard.setPaymentId(1);
//            shoppingCard.setUserId(3);
            session.setAttribute("shoppingCard", shoppingCard);
        }
        Orders shoppingCard = (Orders) session.getAttribute("shoppingCard");
        shoppingCard.addOrderDetail(orderDetails);
        System.out.println(orderDetails);
        System.out.println(shoppingCard);
        System.out.println("------------------");
    }
    @PutMapping("/shoppingCard/{productId/{quantity}")
    public void updateShoppingCard(@PathVariable Long productId, @PathVariable int quantity,@ApiIgnore HttpSession session) {
        Orders shoppingCard = (Orders) session.getAttribute("shoppingCard");
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
        System.out.println(set);
        System.out.println(shoppingCard);
        System.out.println("------------------");
    }

    @PostMapping("/placeOrder")
    public Orders placeOrder(@ApiIgnore HttpSession session) {

        Orders orders = (Orders)(session.getAttribute("shoppingCard"));
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
        productProxy.placeProducts(listDto);
        return savedOrders;
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


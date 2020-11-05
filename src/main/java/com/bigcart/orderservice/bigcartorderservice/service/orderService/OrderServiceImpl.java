package com.bigcart.orderservice.bigcartorderservice.service.orderService;

import com.bigcart.orderservice.bigcartorderservice.FeignController.ProductProxy;
import com.bigcart.orderservice.bigcartorderservice.dto.BuyerDTO;
import com.bigcart.orderservice.bigcartorderservice.dto.Email;
import com.bigcart.orderservice.bigcartorderservice.dto.ListDto;
import com.bigcart.orderservice.bigcartorderservice.model.OrderDetails;
import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderDetailsRepository;
import com.bigcart.orderservice.bigcartorderservice.repository.OrderRepository;
import com.bigcart.orderservice.bigcartorderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    ProductProxy productProxy;
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

    public void sendNotification(Orders orders) {
        Set<OrderDetails> orderDetailsSet = orders.getOrderDetails();
        List<OrderDetails> orderedList = new ArrayList<>();

        for(OrderDetails o1 : orderDetailsSet) {
            orderedList.add(o1);
        }
        List<Long> list = new ArrayList<>();
        for(OrderDetails o : orderedList) {
            list.add(o.getProductId());
        }
        RestTemplate restTemplate = new RestTemplate();
        List<String> names=restTemplate.postForObject("http://localhost:8001/product/productNames",list, List.class);
        //ResponseEntity responseEntity = productProxy.getProductName(list);
   //     = (List<String>) responseEntity.getBody();

        System.out.println("before stringBuilder");
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < names.size(); i++) {
            String s = names.get(i);
            if(s.length() > 10)
                s = s.substring(0, 11);
            builder.append( "<pre>"+ s +  "                " + orderedList.get(i).getQuantity() + "          " + orderedList.get(i).getPrice() +" </pre>");
        }
        String text = " OrderNumber : " + orders.getId() +
                        "<pre>" + "ProductName" +"               " + "Quantity" + "    " + "Price" +  "<pre>" +
                        builder + "<br>" +
                        "Total = " +  orders.getTotalAmount();
        RestTemplate rest = new RestTemplate();
        //ResponseEntity<BuyerDTO> buyer = rest.getForEntity("http://localhost:9988/buyer/" + orders.getUserId(), BuyerDTO.class);
        Email email = new Email();
        email.setBody(text);
        email.setSubject("Order Detials");
        //email.setUserName(buyer.getBody().getFirstName() + " " + buyer.getBody().getLastName());
        email.setUserName("Customer");
        email.setTo("mohamed.farahat4544@gmai.com");

        System.out.println("before send email");
        RestTemplate restTemplates = new RestTemplate();
        restTemplates.postForEntity("http://localhost:8006/notify/", email, Email.class);
        System.out.println("after send email");
    }

}

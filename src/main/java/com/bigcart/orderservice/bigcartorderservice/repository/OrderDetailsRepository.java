package com.bigcart.orderservice.bigcartorderservice.repository;

import com.bigcart.orderservice.bigcartorderservice.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

}

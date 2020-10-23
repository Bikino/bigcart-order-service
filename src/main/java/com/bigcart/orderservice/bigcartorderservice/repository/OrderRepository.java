package com.bigcart.orderservice.bigcartorderservice.repository;

import com.bigcart.orderservice.bigcartorderservice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    public Set<Orders> findByUserId(long userId);
}

package com.bigcart.orderservice.bigcartorderservice.FeignController;

import com.bigcart.orderservice.bigcartorderservice.dto.ListDto;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "api-gateway-service")
@RibbonClient(name= "product-service")
public interface ProductProxy {

    @PostMapping("/product-service/product/remove")
    public void placeProducts(@RequestBody ListDto listDto);
}

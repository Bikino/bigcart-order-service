package com.bigcart.orderservice.bigcartorderservice.FeignController;

import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "product-sercvice")
public interface ProductProxy {
//
//    @PostMapping("/product/{id}/qauntity/{quantity}")
//    public boolean modifyProducts(@PathVariable long id, @PathVariable long quantity);
}

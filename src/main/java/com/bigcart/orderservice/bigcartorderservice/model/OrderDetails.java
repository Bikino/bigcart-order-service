package com.bigcart.orderservice.bigcartorderservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orders_id")
    @JsonIgnore
    private Orders orders;

    private long productId;
    private double price;
    private long vendorId;
    private int quantity;

    public OrderDetails(){
    }

    public OrderDetails(long productId, double price, int quantity,long vendorId) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.vendorId = vendorId;
    }

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id=" + id +
                ", orders="  +
                ", productId=" + productId +
                ", price=" + price +
                ", vendorId=" + vendorId +
                ", quantity=" + quantity +
                '}';
    }
}

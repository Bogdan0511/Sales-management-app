package com.example.sales_management.domain;

import jakarta.persistence.*;

import java.util.Objects;


public class Order {

    private String product;

    private String agent;

    private Integer quantity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public Order(String product, String agent, Integer quantity, String status) {
        this.product = product;
        this.agent = agent;
        this.quantity = quantity;
        this.status = status;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return product.equals(order.product) && agent.equals(order.agent) && quantity.equals(order.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, agent, quantity);
    }
}

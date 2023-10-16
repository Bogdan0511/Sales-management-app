package com.example.sales_management.service;

import com.example.sales_management.domain.Agent;
import com.example.sales_management.domain.Order;
import com.example.sales_management.domain.Product;
import com.example.sales_management.exceptions.ValidationException;
import com.example.sales_management.observer.ConcreteObservable;
import com.example.sales_management.observer.Observable;
import com.example.sales_management.observer.Observer;
import com.example.sales_management.persistence.AgentDBRepo;
import com.example.sales_management.persistence.OrderDBRepo;
import com.example.sales_management.persistence.ProductDBRepo;

import java.util.ArrayList;
import java.util.List;

public class Service extends ConcreteObservable implements Observable {
    private final List<Observer> observers = new ArrayList<>();
    private final AgentDBRepo agentDBRepo;
    private final ProductDBRepo productDBRepo;
    private final OrderDBRepo orderDBRepo;

    public Service(AgentDBRepo agentDBRepo, ProductDBRepo productDBRepo, OrderDBRepo orderDBRepo) {
        this.agentDBRepo = agentDBRepo;
        this.productDBRepo = productDBRepo;
        this.orderDBRepo = orderDBRepo;
    }

    private static Service service = null;
    private Agent agent = null;

    public synchronized static Service getInstance(AgentDBRepo agentDBRepo, ProductDBRepo productDBRepo, OrderDBRepo orderDBRepo) {
        if (service == null) {
            service = new Service(agentDBRepo, productDBRepo, orderDBRepo);
        }
        return service;
    }
    public String getAgent() {
        return agent.getUsername();
    }

    public Agent login(String username, String password) {
        Agent agent = agentDBRepo.find(username);
        if (agent != null && agent.getPassword().equals(password)) {
            this.agent = agent;
            return agent;
        }
        return null;
    }

    public boolean isLoggedIn() {
        return agent != null;
    }

    public List<Product> getAllProducts() {
        return productDBRepo.findAll();
    }

    public void saveProduct(String n, int p, int s) throws ValidationException {
        Product product = new Product();
        product.setName(n);
        product.setPrice(p);
        product.setStock(s);
        productDBRepo.save(product);
    }

    public void removeProduct(Product selectat) throws ValidationException {
        productDBRepo.delete(selectat);
    }

    public void modifyProduct(Product selectat, int p, int s) {
        productDBRepo.update(selectat, p, s);
    }

    public void saveOrder(Order order) throws ValidationException {
        orderDBRepo.save(order);
    }

    public List<Order> getAllOrders() {
        return orderDBRepo.findAll();
    }

    public void modifyOrder(Order order) {
        orderDBRepo.update(order);
    }
}

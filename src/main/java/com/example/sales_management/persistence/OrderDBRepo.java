package com.example.sales_management.persistence;

import com.example.sales_management.domain.Order;
import com.example.sales_management.domain.Product;
import com.example.sales_management.domain.validation.OrderValidator;
import com.example.sales_management.domain.validation.ProductValidator;
import com.example.sales_management.exceptions.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDBRepo {
    private final DBAccess dbAccess = new DBAccess();

    private final OrderValidator validator;

    public OrderDBRepo(OrderValidator validator) {
        this.validator = validator;
    }

    public Order save(Order order) throws ValidationException {
        String sql = "INSERT INTO orders(product,agent,quantity,status) VALUES(?,?,?,?)";
        validator.validate(order);
        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, order.getProduct());
            preparedStatement.setString(2,order.getAgent());
            preparedStatement.setInt(3,order.getQuantity());
            preparedStatement.setString(4,order.getStatus());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();

        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                String p = resultSet.getString("product");
                String a = resultSet.getString("agent");
                int q = resultSet.getInt("quantity");
                String s = resultSet.getString("status");
                Order order = new Order(p, a, q, s);
                orders.add(order);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    public Order update(Order order) {
        String sql = "UPDATE orders set status=? where (product=? and agent=? and quantity=?)";

        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,"yes");
            preparedStatement.setString(2,order.getProduct());
            preparedStatement.setString(3,order.getAgent());
            preparedStatement.setInt(4,order.getQuantity());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}

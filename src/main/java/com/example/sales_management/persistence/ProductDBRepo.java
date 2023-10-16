package com.example.sales_management.persistence;

import com.example.sales_management.domain.Agent;
import com.example.sales_management.domain.Product;
import com.example.sales_management.domain.validation.AgentValidator;
import com.example.sales_management.domain.validation.ProductValidator;
import com.example.sales_management.exceptions.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDBRepo {
    private final DBAccess dbAccess = new DBAccess();

    private final ProductValidator validator;

    public ProductDBRepo(ProductValidator validator) {
        this.validator = validator;
    }

    public Product find(String name) {
        String sql = "SELECT * FROM products WHERE name = ?";
        Product product = new Product();
        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String n = resultSet.getString("name");
                int p = resultSet.getInt("price");
                int s = resultSet.getInt("stock");
                product.setName(n);
                product.setPrice(p);
                product.setStock(s);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()){
                String n = resultSet.getString("name");
                int p = resultSet.getInt("price");
                int s = resultSet.getInt("stock");
                Product product = new Product();
                product.setName(n);
                product.setPrice(p);
                product.setStock(s);
                products.add(product);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    public Product save(Product product) throws ValidationException {
        String sql = "INSERT INTO products(name,price,stock) VALUES(?,?,?)";
        validator.validate(product);
        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2,product.getPrice());
            preparedStatement.setInt(3,product.getStock());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Product delete(Product product) throws ValidationException {
        String sql = "DELETE FROM products WHERE name = ?";
        validator.validate(product);
        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, product.getName());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Product update(Product product, int p, int s) {
        String sql = "UPDATE products set price = ?, stock = ? where name = ?";

        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,p);
            preparedStatement.setInt(2,s);
            preparedStatement.setString(3,product.getName());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

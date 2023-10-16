package com.example.sales_management.persistence;

import com.example.sales_management.domain.Agent;
import com.example.sales_management.domain.validation.AgentValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgentDBRepo {
    private final DBAccess dbAccess = new DBAccess();

    private final AgentValidator validator;

    public AgentDBRepo(AgentValidator validator) {
        this.validator = validator;
    }

    public Agent find(String username) {
        String sql = "SELECT * FROM agents WHERE username = ?";
        Agent agent = null;
        try(Connection connection = dbAccess.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String u = resultSet.getString("username");
                String p = resultSet.getString("password");

                agent = new Agent(u, p);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return agent;
    }


}

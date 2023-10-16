package com.example.sales_management;

import com.example.sales_management.domain.Agent;
import com.example.sales_management.domain.validation.AgentValidator;
import com.example.sales_management.domain.validation.OrderValidator;
import com.example.sales_management.domain.validation.ProductValidator;
import com.example.sales_management.persistence.AgentDBRepo;
import com.example.sales_management.persistence.OrderDBRepo;
import com.example.sales_management.persistence.ProductDBRepo;
import com.example.sales_management.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    AgentValidator agentValidator = new AgentValidator();
    AgentDBRepo agentDBRepo = new AgentDBRepo(agentValidator);
    ProductValidator productValidator = new ProductValidator();
    ProductDBRepo productDBRepo = new ProductDBRepo(productValidator);
    OrderValidator orderValidator = new OrderValidator();
    OrderDBRepo orderDBRepo = new OrderDBRepo(orderValidator);
    Service service = Service.getInstance(agentDBRepo, productDBRepo, orderDBRepo);

    @FXML
    TextField usernameText;

    @FXML
    PasswordField passwordText;

    @FXML
    public void login(ActionEvent event) throws IOException {
        String username = usernameText.getText();
        String parola = passwordText.getText();

        if(username.isEmpty() || parola.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid data!");
            alert.setContentText("Username and password can't be empty!");
            alert.show();

            usernameText.clear();
            passwordText.clear();
        }
        else {
            Agent agent = service.login(username, parola);
            if(!service.isLoggedIn()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Invalid data!");
                alert.setContentText("Username or password is incorrect!");
                alert.show();

                usernameText.clear();
                passwordText.clear();
            }
            else {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("products-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                stage.setTitle("Welcome, " + agent.getUsername() + "!");
                stage.setScene(scene);
                stage.show();
                usernameText.clear();
                passwordText.clear();
            }
        }
    }
}

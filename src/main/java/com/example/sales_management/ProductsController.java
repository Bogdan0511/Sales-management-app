package com.example.sales_management;

import com.example.sales_management.domain.Order;
import com.example.sales_management.domain.Product;
import com.example.sales_management.domain.validation.AgentValidator;
import com.example.sales_management.domain.validation.OrderValidator;
import com.example.sales_management.domain.validation.ProductValidator;
import com.example.sales_management.exceptions.ValidationException;
import com.example.sales_management.observer.Observer;
import com.example.sales_management.persistence.AgentDBRepo;
import com.example.sales_management.persistence.OrderDBRepo;
import com.example.sales_management.persistence.ProductDBRepo;
import com.example.sales_management.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductsController implements Observer {
    AgentValidator agentValidator = new AgentValidator();
    AgentDBRepo agentDBRepo = new AgentDBRepo(agentValidator);
    ProductValidator productValidator = new ProductValidator();
    ProductDBRepo productDBRepo = new ProductDBRepo(productValidator);
    OrderValidator orderValidator = new OrderValidator();
    OrderDBRepo orderDBRepo = new OrderDBRepo(orderValidator);
    Service service = Service.getInstance(agentDBRepo, productDBRepo, orderDBRepo);

    private final ObservableList<Product> productModel = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> products;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, Integer> price;
    @FXML
    private TableColumn<Product, Integer> stock;
    @FXML
    private TextField addName;
    @FXML
    private TextField addPrice;
    @FXML
    private TextField addStock;
    @FXML
    private TextField updatePrice;
    @FXML
    private TextField updateStock;
    @FXML
    private TextField q;

    public void initmodel() {
        List<Product> produse = new ArrayList<>();
        for(Product product : service.getAllProducts()) {
            produse.add(product);
        }
        Collections.sort(produse, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        productModel.setAll(produse);
    }

    @FXML
    private void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        products.setItems(productModel);
        initmodel();
        service.addObserver(this);
    }

    public void addProduct() throws ValidationException {
        String n = addName.getText();
        if(n.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid input!");
            alert.setContentText("Name can't be empty!");
            alert.show();

            addName.clear();
            addPrice.clear();
            addStock.clear();
            return;
        }
        int p;
        int s;
        try {
            p = Integer.parseInt(addPrice.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid input!");
            alert.setContentText("Price must be a number!");
            alert.show();

            addName.clear();
            addPrice.clear();
            addStock.clear();
            return;
        }
        try {
            s = Integer.parseInt(addStock.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid input!");
            alert.setContentText("Stock must be a number!");
            alert.show();

            addName.clear();
            addPrice.clear();
            addStock.clear();
            return;
        }
        service.saveProduct(n, p, s);
        addName.clear();
        addPrice.clear();
        addStock.clear();
        initmodel();
        service.notifyObservers();
    }

    public void deleteProduct() throws ValidationException {
        Product selectat = products.getSelectionModel().getSelectedItem();
        if(selectat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid action!");
            alert.setContentText("You must select a product to delete!");
            alert.show();
            return;
        }
        service.removeProduct(selectat);
        initmodel();
        service.notifyObservers();
    }
    public void updateProduct() {
        Product selectat = products.getSelectionModel().getSelectedItem();
        if(selectat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid action!");
            alert.setContentText("You must select a product to update!");
            alert.show();
            return;
        }
        int p;
        int s;
        try {
            p = Integer.parseInt(updatePrice.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid input!");
            alert.setContentText("Price must be a number!");
            alert.show();

            updatePrice.clear();
            updateStock.clear();
            return;
        }
        try {
            s = Integer.parseInt(updateStock.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid input!");
            alert.setContentText("Stock must be a number!");
            alert.show();

            updatePrice.clear();
            updateStock.clear();
            return;
        }
        service.modifyProduct(selectat, p, s);
        updatePrice.clear();
        updateStock.clear();
        initmodel();
        service.notifyObservers();
    }

    public void placeOrder() throws ValidationException {
        Product selectat = products.getSelectionModel().getSelectedItem();
        if(selectat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid action!");
            alert.setContentText("You must select a product to place an order!");
            alert.show();
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(q.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid input!");
            alert.setContentText("Quantity must be a number!");
            alert.show();

            q.clear();
            return;
        }
        if(quantity> selectat.getStock() || quantity<1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid action!");
            alert.setContentText("Stock too low or negative quantity!");
            alert.show();

            q.clear();
            return;
        }
        service.modifyProduct(selectat, selectat.getPrice(), selectat.getStock()-quantity);
        Order newOrder = new Order(selectat.getName(), service.getAgent(), quantity, "no");
        service.saveOrder(newOrder);
        q.clear();
        initmodel();
        service.notifyObservers();

    }

    public void viewOrders() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("orders-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Orders history");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update() {
        initialize();
    }
    //#30ea6e
}

package com.example.sales_management;

import com.example.sales_management.domain.Order;
import com.example.sales_management.domain.Product;
import com.example.sales_management.domain.validation.AgentValidator;
import com.example.sales_management.domain.validation.OrderValidator;
import com.example.sales_management.domain.validation.ProductValidator;
import com.example.sales_management.observer.Observer;
import com.example.sales_management.persistence.AgentDBRepo;
import com.example.sales_management.persistence.OrderDBRepo;
import com.example.sales_management.persistence.ProductDBRepo;
import com.example.sales_management.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class OrdersController implements Observer {
    AgentValidator agentValidator = new AgentValidator();
    AgentDBRepo agentDBRepo = new AgentDBRepo(agentValidator);
    ProductValidator productValidator = new ProductValidator();
    ProductDBRepo productDBRepo = new ProductDBRepo(productValidator);
    OrderValidator orderValidator = new OrderValidator();
    OrderDBRepo orderDBRepo = new OrderDBRepo(orderValidator);
    Service service = Service.getInstance(agentDBRepo, productDBRepo, orderDBRepo);

    private final ObservableList<Order> orderModel = FXCollections.observableArrayList();

    @FXML
    private TableView<Order> orders;
    @FXML
    private TableColumn<Order, String> product;
    @FXML
    private TableColumn<Order, String> agent;
    @FXML
    private TableColumn<Order, Integer> quantity;

    public void initmodel() {
        List<Order> orders = new ArrayList<>();
        for(Order order : service.getAllOrders()) {
            orders.add(order);
        }
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getAgent().compareTo(o2.getAgent());
            }
        });
        orderModel.setAll(orders);
    }

    @FXML
    private void initialize() {
        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        agent.setCellValueFactory(new PropertyValueFactory<>("agent"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        orders.setRowFactory(tv -> new TableRow<Order>() {
            @Override
            protected void updateItem(Order item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (Objects.equals(item.getStatus(), "yes")) {
                        setStyle("-fx-background-color: #30ea6e;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        orders.setItems(orderModel);
        initmodel();
        service.addObserver(this);
    }

    public void fulfillOrder() {
        Order selectat = orders.getSelectionModel().getSelectedItem();
        if(selectat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid action!");
            alert.setContentText("You must select an order to fulfill!");
            alert.show();
            return;
        }
        service.modifyOrder(selectat);
        initialize();
        service.notifyObservers();
    }

    @Override
    public void update() {
        initialize();
    }
}

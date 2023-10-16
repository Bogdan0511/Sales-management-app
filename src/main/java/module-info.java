module com.example.sales_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.junit.jupiter.api;
    requires java.naming;


    opens com.example.sales_management to javafx.fxml;
    opens com.example.sales_management.domain to javafx.base, javafx.fxml;
    exports com.example.sales_management;
}
package com.example.sales_management.domain.validation;

import com.example.sales_management.domain.Order;
import com.example.sales_management.domain.Product;
import com.example.sales_management.exceptions.ValidationException;

public class OrderValidator implements Validator<Order>{

    @Override
    public void validate(Order entity) throws ValidationException {
        String message = "";
        if (entity.getProduct() == null || entity.getProduct().trim().length() == 0) {
            message += "Product can not be empty!\n";
        }
        if (entity.getAgent() == null || entity.getAgent().trim().length() == 0) {
            message += "Agent can not be empty!\n";
        }
        if (entity.getQuantity() <=0) {
            message += "Quantity can not be less than 0!\n";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}

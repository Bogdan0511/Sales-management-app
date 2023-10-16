package com.example.sales_management.domain.validation;

import com.example.sales_management.domain.Agent;
import com.example.sales_management.domain.Product;
import com.example.sales_management.exceptions.ValidationException;

public class ProductValidator implements Validator<Product> {
    @Override
    public void validate(Product entity) throws ValidationException {
        String message = "";
        if (entity.getName() == null || entity.getName().trim().length() == 0) {
            message += "Name can not be empty!\n";
        }
        if (entity.getPrice() <=0) {
            message += "Price can not be less than 0!\n";
        }
        if (entity.getStock() <=0) {
            message += "Stock can not be less than 0!\n";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}

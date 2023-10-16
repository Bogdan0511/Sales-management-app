package com.example.sales_management.domain.validation;

import com.example.sales_management.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}

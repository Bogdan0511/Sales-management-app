package com.example.sales_management.persistence.interfaces;

import com.example.sales_management.exceptions.RepositoryException;
import com.example.sales_management.exceptions.ValidationException;

import java.util.List;

public interface Repository<E, ID>{

    E find(ID id) throws RepositoryException;
    List<E> findAll();
    E save(E entity) throws RepositoryException, ValidationException;
    E delete(E entity) throws RepositoryException, ValidationException;
    E update(E updateEntity) throws RepositoryException, ValidationException;
}

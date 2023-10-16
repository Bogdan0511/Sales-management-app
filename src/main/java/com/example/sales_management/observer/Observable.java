package com.example.sales_management.observer;

public interface Observable {
    void addObserver(Observer e);

    void removeObserver(Observer e);

    void notifyObservers();
}

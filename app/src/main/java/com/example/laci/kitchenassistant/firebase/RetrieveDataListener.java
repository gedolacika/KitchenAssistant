package com.example.laci.kitchenassistant.firebase;

public interface RetrieveDataListener<T> {
    void onSuccess(T data);
    void onFailure(String message);
}

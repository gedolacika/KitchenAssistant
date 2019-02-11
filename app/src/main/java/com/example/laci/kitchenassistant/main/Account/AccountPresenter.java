package com.example.laci.kitchenassistant.main.Account;

public class AccountPresenter implements AccountContract.Presenter {
    private AccountContract.View view;

    public AccountPresenter(AccountContract.View view) {
        this.view = view;
    }
}

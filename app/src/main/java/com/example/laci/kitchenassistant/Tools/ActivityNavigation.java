package com.example.laci.kitchenassistant.Tools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.laci.kitchenassistant.ConfirmActivity;
import com.example.laci.kitchenassistant.LoginActivity;

public class ActivityNavigation {
    private static String PHONE_NUMBER = "phoneNumber";

    public static void navigateToLogin(Context fromWhere)
    {
        fromWhere.startActivity( new Intent(fromWhere,LoginActivity.class));
    }

    public static void navigateToConfirm(Context fromWhere, String phoneNumber)
    {
        Intent intent = new Intent(fromWhere,ConfirmActivity.class);
        intent.putExtra(PHONE_NUMBER,phoneNumber);
        fromWhere.startActivity( intent );
    }

    public static String getPhoneNumberFromIntent(Intent intent)
    {
        return intent.getStringExtra(PHONE_NUMBER);
    }
}

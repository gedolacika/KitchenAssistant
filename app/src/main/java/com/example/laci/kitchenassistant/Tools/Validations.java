package com.example.laci.kitchenassistant.Tools;

public class Validations {
    //Return values:
    //              0 - the given data is valid
    //              1 - problem with length
    //              2 - the pattern doesn't match


    //Laws for input
    // ---------------------------------------------------------
    //Phone number
    private static int PHONE_NUMBER_LENGTH = 12;
    private static String PHONE_NUMBER_PATTERN = "[+]{1}[0123456789]+";


    public static int validatePhoneNumber(String phoneNumber)
    {
        if(phoneNumber.length()!= PHONE_NUMBER_LENGTH)
            return 1;
        if(!phoneNumber.matches(PHONE_NUMBER_PATTERN))
            return 2;
        return 0;
    }
}

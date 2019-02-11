package com.example.laci.kitchenassistant.Tools;

public class Validations {
    //Return values:
    //              0 - the given data is valid
    //              1 - problem with length
    //              2 - the pattern doesn't match
    //              3 - null
    //              4 - not a real number


    //Laws for input
    // ---------------------------------------------------------
    //Phone number
    private static int PHONE_NUMBER_LENGTH = 12;
    private static String PHONE_NUMBER_PATTERN = "[+]{1}[0123456789]+";

    //Verification code
    private static String VERIFICATION_CODE_PATTERN = "[0123456789]+";

    private static String NAME_PATTERN = "[0123456789a-zA-Z áéőúűöüóí]+";
    private static int NAME_MAX_LENGTH = 30;

    private static String AGE_PATTERN = "[0123456789]+";
    private static int MAX_AGE = 150;
    private static int MIN_AGE = 5;

    private static String HEIGHT_PATTERN = "[0123456789]+";
    private static int MAX_HEIGHT = 250;
    private static int MIN_HEIGHT = 100;

    private static String WEIGHT_PATTERN = "[0123456789]+";
    private static int MAX_WEIGHT = 400;
    private static int MIN_WEIGHT = 30;


    public static int validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != PHONE_NUMBER_LENGTH)
            return 1;
        if (!phoneNumber.matches(PHONE_NUMBER_PATTERN))
            return 2;
        return 0;
    }

    public static boolean validateVerificationCode(String code) {
        if (code == null) return false;
        if (code.length() < 1) return false;
        if (!code.matches(VERIFICATION_CODE_PATTERN)) return false;

        return true;
    }

    public static int validateName(String name) {
        if (name == null) return 3;
        if (name.length() > NAME_MAX_LENGTH) return 1;
        if (!name.matches(NAME_PATTERN)) return 2;
        return 0;
    }

    public static int validateAge(String age) {
        if(age.isEmpty())return 3;
        if (!age.matches(AGE_PATTERN)) return 2;
        if (Integer.parseInt(age) > MAX_AGE || Integer.parseInt(age) < MIN_AGE) return 4;
        return 0;
    }

    public static int validateHeight(String height) {
        if(height == null) return 3;
        if(!height.matches(HEIGHT_PATTERN))return 2;
        if(Integer.parseInt(height) < MIN_HEIGHT || Integer.parseInt(height)> MAX_HEIGHT) return 4;
        return 0;
    }

    public static int validateWeight(String weight) {
        if(weight == null)return 3;
        if(!weight.matches(WEIGHT_PATTERN))return 2;
        if(Integer.parseInt(weight) < MIN_WEIGHT || Integer.parseInt(weight)> MAX_WEIGHT) return 4;
        return 0;
    }


}

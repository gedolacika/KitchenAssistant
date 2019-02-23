package com.example.laci.kitchenassistant.BaseClasses;

public class BasicFoodQuantity extends BasicFood {

    private int quantity;

    public BasicFoodQuantity(){
        super();
        quantity = -1;
    }

    public BasicFoodQuantity(String name, int calorie, int protein, int carbohydrate, int fat) {
        super(name, calorie, protein, carbohydrate, fat);
    }

    public BasicFoodQuantity(String name, String picture, int calorie, int protein, int carbohydrate, int fat) {
        super(name, picture, calorie, protein, carbohydrate, fat);
    }

    public BasicFoodQuantity(String name, String picture) {
        super(name, picture);
    }

    public BasicFoodQuantity(String name, int calorie, int protein, int carbohydrate, int fat, int quantity) {
        super(name, calorie, protein, carbohydrate, fat);
        this.quantity = quantity;
    }

    public BasicFoodQuantity(String name, String picture, int quantity) {
        super(name, picture);
        this.quantity = quantity;
    }

    public BasicFoodQuantity(BasicFood food, int quantity) {
        super(food.getName(),food.getPicture(),food.getCalorie(),food.getProtein(),food.getCarbohydrate(),food.getFat());
        this.quantity = quantity;
    }



    public BasicFoodQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

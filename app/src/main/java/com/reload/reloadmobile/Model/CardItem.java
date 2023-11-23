package com.reload.reloadmobile.Model;

public class CardItem {

    private String cardNumber;
    private String cardHolderName;
    private String cardAmount;

    public CardItem(String cardNumber, String cardHolderName, String cardAmount) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardAmount = cardAmount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardAmount() {
        return cardAmount;
    }

}

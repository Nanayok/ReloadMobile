package com.reload.reloadmobile.Model;

public class Transaction {

    private String id;
    private String logourl;
    private String productname;
    private String productDescription;

    private int productCategoryId;

    private String billerCode;
    private String paymentProcessor;

    public Transaction(String id, String logourl, String productname, String productDescription, int productCategoryId, String billerCode, String paymentProcessor) {
        this.id = id;
        this.logourl = logourl;
        this.productname = productname;
        this.productDescription = productDescription;
        this.productCategoryId = productCategoryId;
        this.billerCode = billerCode;
        this.paymentProcessor = paymentProcessor;
    }

    public String getId() {
        return id;
    }

    public String getLogourl() {
        return logourl;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public String getBillerCode() {
        return billerCode;
    }

    public String getPaymentProcessor() {
        return paymentProcessor;
    }

}

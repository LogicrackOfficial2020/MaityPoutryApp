package com.logicrack.MaityPoultry.model;


public class Cart {
    String id;
    String image;
    String title;
    String currency;
    String price;
    String attribute;
    String quantity;
    String subTotal;
int ProductPriceId;
    String ProductQuantity;
    String Pincode;

    public Cart() {
    }


    public Cart(String id, String title, String image, String currency, String price, String attribute, String quantity, String subTotal,String Pincode) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.currency = currency;
        this.price = price;
        this.attribute = attribute;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.Pincode=Pincode;
     /*   this.ProductPriceId = ProductPriceId;
        this.ProductQuantity = ProductQuantity;*/
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String ProductQuantity) {
        this.ProductQuantity = ProductQuantity;
    }

    public int getProductPriceId() {
        return ProductPriceId;
    }

    public void setProductPriceId(int ProductPriceId) {
        this.ProductPriceId = ProductPriceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String Pincode) {
        this.Pincode = Pincode;
    }
}

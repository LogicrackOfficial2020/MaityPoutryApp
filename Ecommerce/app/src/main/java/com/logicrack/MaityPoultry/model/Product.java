package com.logicrack.MaityPoultry.model;


import java.text.DateFormat;

public class Product {
    String id;
    String categoryId;
    String title;
    String description;
    String attribute;
    String currency;
    String price;
    String discount;
    String image;
    int PurchaseQuantity;
    int SellQuantity;

     int ProdId ;
     int SubCategoryId ;
    int CategoryId ;
    String ProdName ;
    public String ProdImage ;
    public String ProdDetails ;


    public String PackageQty ;
    public String PackageUnit ;
    public String ProdMRP ;
    public String ProdDiscount ;
    public String ProdMRPType ;
    public String ProdManufacturedBy ;

   public int ProductPriceId;

    String ProdQuantity;
    String Product_PriceId;
    String TotalAmount;
    String Savings;
    String OrderId;
    public DateFormat ProdManufacturedDate ;
public  float MarketPrice;

    public boolean ProdIsActive ;

    public Product() {
    }

    public Product(String id, String categoryId, String title, String description, String attribute, String price, String discount, String image) {
        this.ProdId = Integer.parseInt(id);
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.attribute = attribute;
        this.price = price;
        this.discount = discount;
        this.image = image;
    }
    public Product(int id, int categoryId,int SubCategoryId , String ProdName, String ProdImage, String ProdDetails, String PackageQty, String PackageUnit, String ProdMRP,String ProdDiscount,String ProdMRPType,String ProdManufacturedBy,float MarketPrice,int ProductPriceId,int PurchaseQuantity,int SellQuantity) {
        this.ProdId = id;
        this.CategoryId = categoryId;
        this.SubCategoryId = SubCategoryId;
        this.ProdName = ProdName;
        this.ProdImage = ProdImage;
        this.ProdDetails = ProdDetails;
        this.PackageQty = PackageQty;
        this.PackageUnit = PackageUnit;
        this.ProdMRP = ProdMRP;
        this.ProdDiscount = ProdDiscount;
        this.ProdMRPType = ProdMRPType;
        this.ProdManufacturedBy =  ProdManufacturedBy;
        this.MarketPrice = MarketPrice;

        this.ProductPriceId = ProductPriceId;

        this.PurchaseQuantity = PurchaseQuantity;
        this.SellQuantity = SellQuantity;
    }
    public Product(String OrderId,String ProdName, String ProdQuantity,String Product_PriceId,String TotalAmount,String Savings ){
        this.OrderId=OrderId;
        this.ProdName=ProdName;
        this.ProdQuantity=ProdQuantity;

        this.Product_PriceId=Product_PriceId;
        this.TotalAmount=TotalAmount;
        this.Savings=Savings;
    }
    public Product(String id, String categoryId, String title, String description, String attribute, String currency, String price, String discount, String image) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.attribute = attribute;
        this.currency = currency;
        this.price = price;
        this.discount = discount;
        this.image = image;
    }

    public int getPurchaseQuantity() {
        return PurchaseQuantity;
    }

    public void setPurchaseQuantity(int PurchaseQuantity) {
        this.PurchaseQuantity = PurchaseQuantity;
    }


    public int getSellQuantity() {
        return SellQuantity;
    }

    public void setSellQuantity(String id) {
        this.SellQuantity = SellQuantity;
    }


    public String getMarketPrice() {
        return String.valueOf(MarketPrice);
    }

    public void setMarketPrice(String id) {
        this.ProdId = Integer.parseInt(String.valueOf(MarketPrice));
    }


    public String getId() {
        return String.valueOf(ProdId);
    }

    public void setId(String id) {
        this.ProdId = Integer.parseInt(String.valueOf(ProdId));
    }

    public String getCategoryId() {
        return String.valueOf(CategoryId);
    }

    public void setCategoryId(String categoryId) {
        this.CategoryId = Integer.parseInt(String.valueOf(CategoryId));
    }

    public String getTitle() {
        return ProdName;
    }

    public void setTitle(String title) {
        this.ProdName = title;
    }

    public String getDescription() {
        return ProdDetails;
    }

    public void setDescription(String description) {
        this.ProdDetails = description;
    }

   public String getAttribute() {
        return PackageQty + PackageUnit;
    }

    public void setAttribute(String attribute) {
        this.PackageQty = attribute;
    }

    public String getPrice() {
        return ProdMRP;
    }

    public void setPrice(String price) {
        this.ProdMRP = price;
    }

    public String getDiscount() {
        return ProdDiscount;
    }

    public void setDiscount(String discount) {
        this.ProdDiscount = discount;
    }

    public String getImage() {
        return ProdImage;
    }

    public void setImage(String image) {
        this.ProdImage = image;
    }

    public String getCurrency() {
        return ProdMRPType;
    }

    public void setCurrency(String currency) {
        this.PackageUnit = ProdMRPType;
    }

    public String getProductPriceId() {
        return String.valueOf(ProductPriceId);
    }

    public void setProductPriceId(int ProductPriceId) {
        this.ProductPriceId = ProductPriceId;
    }


    public String getProdQuantity() {
        return String.valueOf(ProdQuantity);
    }

    public void setProdQuantity(String ProdQuantity) {
        this.ProdQuantity = ProdQuantity;
    }

    public String getProduct_PriceId() {
        return String.valueOf(Product_PriceId);
    }

    public void setProduct_PriceId(String Product_PriceId) {
        this.Product_PriceId = Product_PriceId;
    }

    public String getTotalAmount() {
        return String.valueOf(TotalAmount);
    }

    public void setTotalAmount(String TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getSavings() {
        return String.valueOf(Savings);
    }

    public void setSavings(String Savings) {
        this.Savings = Savings;
    }
    public String getOrderId() {
        return String.valueOf(OrderId);
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }
}

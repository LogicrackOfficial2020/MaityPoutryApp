package com.logicrack.MaityPoultry.model;

import java.util.List;

public class OrderModel {

    public int orderId ;

    public int CustomerId_FK ;

    public String OrderDate ;
    public String OrderAmount ;
    public int OrderStatus ;
    public int  OrderPaymentMode ;
    public String Cdate ;


    public String OrderShippingFname ;
    public String OrderShippingAddress ;
    public String OrderShippingContactNo ;
    public String OrderShippingEmailId ;
    public String CouponCode;

    public  String  ConsumerIdentifier ;
    public  String  ConsumerEmailID ;
    public  String  ConsumerMobileNumber  ;

    public  int  TransactionStatus  ;
    public  String  TransactionIdentifier  ;
    public  String  Pincode;


    public List<Cart> OrderList ;

    public OrderModel(int  TransactionStatus, String  TransactionIdentifier)
    {
        this.TransactionStatus = TransactionStatus;
        this.TransactionIdentifier = TransactionIdentifier;
    }

    public  OrderModel(String OrderAmount,int CustomerId_FK,String OrderShippingFname,String OrderShippingAddress,String OrderShippingContactNo,String OrderShippingEmailId,List<Cart> OrderList,String ConsumerIdentifier,String  ConsumerEmailID,String  ConsumerMobileNumber,String CouponCode,String Pincode)
    {
        this.OrderAmount = OrderAmount;
        this.CustomerId_FK = CustomerId_FK;
        this.OrderShippingFname = OrderShippingFname;
        this.OrderShippingAddress = OrderShippingAddress;
        this.OrderShippingContactNo = OrderShippingContactNo;
        this.OrderShippingEmailId = OrderShippingEmailId;
        this.OrderList = OrderList;
        this.ConsumerIdentifier = ConsumerIdentifier;
        this.ConsumerEmailID = ConsumerEmailID;
        this.ConsumerMobileNumber = ConsumerMobileNumber;
        this.CouponCode = CouponCode;
        this.Pincode=Pincode;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }
    public void setOrderAmount(String OrderAmount) {
        this.OrderAmount = OrderAmount;
    }

    public String getCouponCode() {
        return CouponCode;
    }
    public void setCouponCode(String CouponCode) {
        this.CouponCode = CouponCode;
    }



    public int getCustomerId_FK() {
        return CustomerId_FK;
    }
    public void setCustomerId_FK(int CustomerId_FK) {
        this.CustomerId_FK = CustomerId_FK;
    }


    public String getOrderShippingFname() {
        return OrderShippingFname;
    }

    public void setOrderShippingFname(String OrderShippingFname) {
        this.OrderShippingFname = OrderShippingFname;
    }

    public String getOrderShippingAddress() {
        return OrderShippingAddress;
    }

    public void setOrderShippingAddress(String OrderShippingAddress) {
        this.OrderShippingAddress = OrderShippingAddress;
    }

    public String getOrderShippingContactNo() {
        return OrderShippingContactNo;
    }

    public void setOrderShippingContactNo(String OrderShippingContactNo) {
        this.OrderShippingContactNo = OrderShippingContactNo;
    }

    public String getPincode() {
        return Pincode;
    }
    public void setPincode(String Pincode) {
        this.Pincode = Pincode;
    }


    public String getOrderShippingEmailId() {
        return OrderShippingEmailId;
    }

    public void setOrderShippingEmailId(String OrderShippingEmailId) {
        this.OrderShippingEmailId = OrderShippingEmailId;
    }

    public List<Cart> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<Cart> OrderList) {
        this.OrderList = OrderList;
    }
}

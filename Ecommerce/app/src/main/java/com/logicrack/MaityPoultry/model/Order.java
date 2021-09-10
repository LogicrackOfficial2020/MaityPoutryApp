package com.logicrack.MaityPoultry.model;


public class Order {
    String id;
    String orderNo;
    String date;
    String total;
    String status;
    String OrderStatus;
    int DeliveryOTP;
    String OrderPaymentMode;
    String TransactionStatus;
    String OrderShippingContactNo;
    String OrderShippingAddress;
    String OrderShippingEmailId;
    String CustomerName;

    public Order() {
    }
    public Order(String id, String orderNo, String date, String total, String status,String OrderStatus,int DeliveryOTP) {
        this.id = id;
        this.orderNo = orderNo;
        this.date = date;
        this.total = total;
        this.status = status;
        this.OrderStatus = OrderStatus;
        this.DeliveryOTP = DeliveryOTP;
    }
    public Order(String id, String orderNo, String date, String total, String status,String OrderStatus,int DeliveryOTP,String OrderPaymentMode,String TransactionStatus,String OrderShippingContactNo,String OrderShippingAddress,String OrderShippingEmailId,String CustomerName) {
        this.id = id;
        this.orderNo = orderNo;
        this.date = date;
        this.total = total;
        this.status = status;
        this.OrderStatus = OrderStatus;
        this.DeliveryOTP = DeliveryOTP;
        this.OrderPaymentMode = OrderPaymentMode;
        this.TransactionStatus=TransactionStatus;
        this.OrderShippingAddress = OrderShippingAddress;
        this.OrderShippingContactNo = OrderShippingContactNo;
        this.OrderShippingEmailId=OrderShippingEmailId;
        this.CustomerName =CustomerName ;

    }
    public Order(String id, String orderNo, String date, String total, String status,String OrderStatus) {
        this.id = id;
        this.orderNo = orderNo;
        this.date = date;
        this.total = total;
        this.status = status;
        this.OrderStatus = OrderStatus;
    }

    public int getDeliveryOTP() {
        return DeliveryOTP;
    }

    public void setDeliveryOTP(int DeliveryOTP) {
        this.DeliveryOTP = DeliveryOTP;
    }


    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String iOrderStatus) {
        this.OrderStatus = OrderStatus;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderNo;
    }

    public void setOrderId(String orderId) {
        this.orderNo = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getOrderPaymentMode() {
        return OrderPaymentMode;
    }

    public void setOrderPaymentMode(String OrderPaymentMode) {
        this.OrderPaymentMode = OrderPaymentMode; }

    public String getTransactionStatus() {
        return TransactionStatus;
    }

    public void setTransactionStatus(String TransactionStatus) {
        this.TransactionStatus = TransactionStatus;
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

    public String getOrderShippingEmailId() {
        return OrderShippingEmailId;
    }

    public void setOrderShippingEmailId(String OrderShippingEmailId) {
        this.OrderShippingEmailId = OrderShippingEmailId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }
}

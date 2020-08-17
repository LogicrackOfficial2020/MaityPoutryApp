package com.logicrack.MaityPoultry.model;


public class Order {
    String id;
    String orderNo;
    String date;
    String total;
    String status;
    String OrderStatus;


    public Order() {
    }

    public Order(String id, String orderNo, String date, String total, String status,String OrderStatus) {
        this.id = id;
        this.orderNo = orderNo;
        this.date = date;
        this.total = total;
        this.status = status;
        this.OrderStatus = OrderStatus;
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
}

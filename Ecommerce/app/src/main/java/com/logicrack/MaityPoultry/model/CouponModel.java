package com.logicrack.MaityPoultry.model;

public class CouponModel {


    int CouponId ;
    int CouponTypeId ;

    boolean IsRedeemed ;

    int CustomerId ;

    String CouponCode ;
    String ImageUrl ;
    String CouponAmount;



    public CouponModel() {
    }

    public CouponModel(int CouponId, int CouponTypeId, boolean IsRedeemed, int CustomerId, String CouponCode,String CouponAmount) {
        this.CouponId = CouponId;
        this.CouponTypeId = CouponTypeId;
        this.IsRedeemed = IsRedeemed;
        this.CustomerId = CustomerId;
        this.CouponCode = CouponCode;
        this.CouponAmount = CouponAmount;
    }

    public String getCouponAmount() {
        return CouponAmount;
    }

    public void setCouponAmount(String CouponAmount) {
        this.CouponAmount = CouponAmount;
    }

    public int getCouponId() {
        return CouponId;
    }

    public void setCouponId(int id) {
        this.CouponId = id;
    }

    public int getCouponTypeId() {
        return CouponTypeId;
    }

    public void setCouponTypeId(int CouponTypeId) {
        this.CouponTypeId = CouponTypeId;
    }


    public boolean getIsRedeemed() {
        return IsRedeemed;
    }

    public void setIsRedeemed(boolean IsRedeemed) {
        this.IsRedeemed = IsRedeemed;
    }

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String CouponCode) {
        this.CouponCode = CouponCode;
    }
}

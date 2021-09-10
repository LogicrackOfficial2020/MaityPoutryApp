package com.logicrack.MaityPoultry.model;

public class CustomerPinCode {

    String ZoneName ;
    String PinCode ;
    String BrachName;

    public CustomerPinCode() {
    }

    public CustomerPinCode(String ZoneName,String PinCode,String BrachName) {
        this.ZoneName=ZoneName;
        this.BrachName=BrachName;
        this.PinCode=PinCode;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public String getBrachName() {
        return BrachName;
    }

    public void setBrachName(String BrachName) {
        this.BrachName = BrachName;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String PinCode) {
        this.PinCode = PinCode;
    }

}

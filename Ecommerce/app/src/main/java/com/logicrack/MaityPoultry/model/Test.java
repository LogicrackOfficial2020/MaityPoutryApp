package com.logicrack.MaityPoultry.model;

public class Test {

    private String name;
    private int Roll;

    private  int Avg;
    private  String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    private String birth;

    public Test(String name, int roll, int avg, String email, String birth) {
        this.name = name;
        Roll = roll;
        Avg = avg;
        this.email = email;
        this.birth = birth;
    }


    public int getRoll() {
        return Roll;
    }

    public void setRoll(int roll) {
        Roll = roll;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAvg() {
        return Avg;
    }

    public void setAvg(int avg) {
        Avg = avg;
    }
}

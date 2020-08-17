package com.logicrack.MaityPoultry.model;

public class CateGoryModel {

    int CategoryId ;
    String CategoryName ;
    String CategoryDesc ;
    String CategoryRoot ;
    String ImageUrl ;



    public CateGoryModel() {
    }

    public CateGoryModel(int id, String CategoryName, String CategoryDesc, String categoryRoot, String Url) {
        this.CategoryId = id;
        this.CategoryName = CategoryName;
        this.CategoryDesc = CategoryDesc;
        this.CategoryRoot = categoryRoot;
        this.ImageUrl = Url;
    }


    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int id) {
        this.CategoryId = id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setTitle(String title) {
        this.CategoryName = title;
    }

    public String getImage() {
        return ImageUrl;
    }


    public String getCategoryRoot() {
        return CategoryRoot;
    }
    public void setCategoryRoot(String CategoryRoot) {
        this.CategoryRoot = CategoryRoot;
    }


    public String getCategoryDesc() {
        return CategoryDesc;
    }
    public void setCategoryDesc(String CategoryDesc) {
        this.CategoryDesc = CategoryDesc;
    }



    public void setImage(String image) {
        this.ImageUrl = image;
    }
}

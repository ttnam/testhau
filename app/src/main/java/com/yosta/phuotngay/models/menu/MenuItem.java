package com.yosta.phuotngay.models.menu;

public class MenuItem {

    private int img;
    private String name;

    public MenuItem(String name) {
        this.img = -1;
        this.name = name;
    }

    public MenuItem(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public int getImage() {
        return img;
    }

    public void setImage(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

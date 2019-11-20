package com.example.neyesek;

public class User {

    private String name;
    private String surname;
    private String favRest[];
    private String prevRest[];
    private int level;

    public User() {
    }

    public String[] getPrevRest() {
        return prevRest;
    }

    public void setPrevRest(String[] prevRest) {
        this.prevRest = prevRest;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String[] getFavRest() {
        return favRest;
    }

    public void setFavRest(String[] favRest) {
        this.favRest = favRest;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}

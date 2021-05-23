package com.chatGB.chat;

public class User {
    private String name;
    private String lastname;
    private String email;
    private String pass;
    private String phone1;
    private String phone2;
    private String userID;
    private String position;
    private String gender;
    private String photo;
    //private ArrayList<Remind> remindArrayList;

    public User() {
    }

    public User(String name, String email, String pass, String phone1) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone1 = phone1;
    }

    public User(String email, String pass, String lastname) {
        this.email = email;
        this.pass = pass;
        this.lastname = lastname;
    }

    //    public ArrayList<Remind> getRemindArrayList() {
//        return remindArrayList;
//    }
//
//    public void setRemindArrayList(ArrayList<Remind> remindArrayList) {
//        this.remindArrayList = remindArrayList;
//    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }
}
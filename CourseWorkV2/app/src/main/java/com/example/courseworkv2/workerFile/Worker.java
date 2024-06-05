package com.example.courseworkv2.workerFile;

import java.io.Serializable;

public class Worker implements Serializable {
    private String name;
    private String secondName;
    private String fatherName;
    private String phoneNumber;
    private String mail;
    private int age;
    private String dateOfBirth;

    private static Worker instance;

    public Worker(String name, String fatherName, String secondName, String phoneNumber, String mail, int age, String dateOfBirth){
        this.name = name;
        this.fatherName = fatherName;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
    }




    public String getName(){
        return this.name;
    }
    public void setName(String name ) {
        this.name = name;
    }



    public String getSecondName(){
        return this.secondName;
    }
    public void setSecondName(String SecondName ) {
        this.secondName = SecondName;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }


    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getFatherName() {
        return fatherName;
    }
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
}

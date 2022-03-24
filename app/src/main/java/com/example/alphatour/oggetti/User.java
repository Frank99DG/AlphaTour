package com.example.alphatour.oggetti;

public class User {

    public String name,surname,dateBirth,username,email;

    public User(){

    }


    public User(String name, String surname, String dateBirth, String username, String email){
        this.name = name;
        this.surname = surname;
        this.dateBirth = dateBirth;
        this.username = username;
        this.email = email;

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

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

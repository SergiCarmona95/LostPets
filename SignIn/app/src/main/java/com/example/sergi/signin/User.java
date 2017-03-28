package com.example.sergi.signin;

import com.google.firebase.database.Exclude;

/**
 * Created by Sergi on 26/03/2017.
 */

public class User {
    public String id;
    public String username;
    public String email;


    public User(){}

    public User(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
    @Exclude
    public String getUsername() {
        return username;
    }

    @Exclude
    public void setUsername(String username) {
        this.username = username;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    @Exclude
    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getId() {return id;}

    @Exclude
    public void setId(String id) {this.id = id;}

    @Exclude
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

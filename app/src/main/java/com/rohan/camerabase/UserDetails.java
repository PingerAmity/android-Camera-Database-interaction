package com.rohan.camerabase;

/**
 * Created by Rohan Sampson on 5/20/2016.
 */
public class UserDetails {
    String Name, Email, Username, Password;
    int Age;

    public UserDetails(String name, String email, String username, String password, int age) {
        Name = name;
        Email = email;
        Username = username;
        Password = password;
        Age = age;
    }

    public UserDetails() {
    }

    public void setName(String name) {

        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public int getAge() {
        return Age;
    }
}

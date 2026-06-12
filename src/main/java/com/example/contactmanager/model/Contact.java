package com.example.contactmanager.model;

public class Contact {

    private String name;
    private String phone;
    private String email;
    private String id;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}

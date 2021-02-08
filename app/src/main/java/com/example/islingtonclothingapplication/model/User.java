package com.example.islingtonclothingapplication.model;

class User {

    private String name ;
    private String email ;
    private String phone;
    private String created_at ;
    private String updated_at = "" ;

    public User() {
    }

    public User(String name, String email, String phone, String created_at, String updated_at) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}

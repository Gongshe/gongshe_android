package com.gongshe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    public int id;
    public String password;
    public String phone;
    public String email;
    public String name;
    public String avatar;
    public String introduction;
    public String time;
    public int privilege;
    public int weibo_id;
    public String token;



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", introduction='" + introduction + '\'' +
                ", time='" + time + '\'' +
                ", privilege=" + privilege +
                ", weibo_id=" + weibo_id +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public int getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(int weibo_id) {
        this.weibo_id = weibo_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonIgnore
    public boolean isValidUser() {
        if (password == null || password == "") {
            return false;
        }
        if (name == null || name == "") {
            return false;
        }
        if ((phone == null || phone == "") && (email == null || email == "")) {
            return false;
        }
        return true;
    }
}

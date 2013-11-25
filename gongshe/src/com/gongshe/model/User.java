package com.gongshe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    public Integer id;
    public String password;
    public String phone;
    public String email;
    public String name;
    public String avatar;
    public String introduction;
    public String time;
    public Integer privilege;
    public Integer weibo_id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(Integer weibo_id) {
        this.weibo_id = weibo_id;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getId() {

        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIntroduction() {
        return introduction;
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

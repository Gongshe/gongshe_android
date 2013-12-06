package com.gongshe.model;

public class ClientPost extends Post {
    // owner name
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClientPost{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }

}

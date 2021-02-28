package com.koral.application.model;

public class CurrentUser {
    private Long id;

    public CurrentUser(Long id) {
        this.id = id;
    }

    public CurrentUser(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

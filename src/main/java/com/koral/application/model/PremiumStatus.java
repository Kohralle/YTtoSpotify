package com.koral.application.model;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.*;

@Entity
@Table(name = "premium")
public class PremiumStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "premium_id")
    private Long id;

    private String status;
    private Integer price;

    public PremiumStatus() {
    }

    public PremiumStatus(String status, Integer price) {
        this.status = status;
        this.price = price;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


}

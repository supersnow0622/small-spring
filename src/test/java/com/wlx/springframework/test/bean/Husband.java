package com.wlx.springframework.test.bean;

public class Husband {

    private Wife wife;

    public String queryWife(){
        return "Husband.wife";
    }

}
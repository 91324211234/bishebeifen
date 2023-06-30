package com.example.test_app1;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private int id;
    private String phoneNumber;
    private String password;
    private String userName;
    private String emailAddress;
    private Date registerTime;
    //用于URL传参和取参时的key
    public static String PHONENUMBER = "phoneNumber";
    public static String PASSWORD = "passWord";
    public static String USERNAME = "userName";
    public static String PICTURE = "picture";
    //构造函数
    public User(String phoneNumber,String password,String userName){
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userName = userName;
        registerTime = new Date();
    }
    //属性的get与set方法不再贴出
}
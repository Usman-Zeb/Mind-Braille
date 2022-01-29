package com.example.mindbraille.models;

public class UserAccounts {
    private String id;
    private String accountID;
    private String accountType;
    private String accountName;
    private String age;
    private String sos_email;
    private String sos_phone;
    private String permanent_home_address;
    private boolean talk_status;
    private boolean isColorBlind;
    private String eye_status;
    private String average_attention_index;

    public UserAccounts(String id, String accountID, String accountType, String accountName, String age, String sos_email, String sos_phone, String permanent_home_address, boolean talk_status, boolean isColorBlind, String eye_status, String average_attention_index) {
        this.id = id;
        this.accountID = accountID;
        this.accountType = accountType;
        this.accountName = accountName;
        this.age = age;
        this.sos_email = sos_email;
        this.sos_phone = sos_phone;
        this.permanent_home_address = permanent_home_address;
        this.talk_status = talk_status;
        this.isColorBlind = isColorBlind;
        this.eye_status = eye_status;
        this.average_attention_index = average_attention_index;
    }

    public String getAccountType() {
        return accountType;
    }
    public String getID() {
        return id;
    }
    public String getAccountID() {
        return accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAge() {
        return age;
    }

    public String getSos_email() {
        return sos_email;
    }

    public String getSos_phone() {
        return sos_phone;
    }

    public String getPermanent_home_address() {
        return permanent_home_address;
    }

    public String getAverage_attention_index() {
        return average_attention_index;
    }

    public boolean isTalk_status() {
        return talk_status;
    }

    public void setTalk_status(boolean talk_status) {
        this.talk_status = talk_status;
    }

    public boolean isColorBlind() {
        return isColorBlind;
    }

    public void setColorBlind(boolean colorBlind) {
        isColorBlind = colorBlind;
    }

    public String getEye_status() {
        return eye_status;
    }

    public void setEye_status(String eye_status) {
        this.eye_status = eye_status;
    }
}

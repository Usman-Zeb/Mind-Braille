package com.example.mindbraille.models;

import com.microsoft.graph.models.extensions.IGraphServiceClient;

import java.io.Serializable;

public class AuthInfo implements Serializable {

    private String accessToken;
    private String UID;
    private String userName;
    private String userEmail;
    public static IGraphServiceClient graphClient;

    public AuthInfo() {
    }

    public String getUserName() {
        return userName;
    }

    public IGraphServiceClient getGraphClient() {
        return graphClient;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

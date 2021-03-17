package util;

import android.app.Application;

public class SmartStrengthLogAPI extends Application {

    //We will make it a singleton
    private static SmartStrengthLogAPI instance; //Instance for the singleton

    private String username;
    private String userId;

    public static SmartStrengthLogAPI getInstance(){
        if (instance == null){
            instance = new SmartStrengthLogAPI();

        }
        return instance;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

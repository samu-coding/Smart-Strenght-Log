package util;

import android.app.Application;

public class SmartStrengthLogAPI extends Application {

    private static SmartStrengthLogAPI instance; //Instancia para el singleton

    private String username;
    private String userId;

    public static SmartStrengthLogAPI getInstance(){
        if (instance == null)
            instance = new SmartStrengthLogAPI();
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

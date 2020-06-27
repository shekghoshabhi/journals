package util;

import android.app.Application;

public class journalApi extends Application {
    private static journalApi instance ;

    public String userName ;
    public String userID ;

    public static journalApi getInstance(){
         if(instance==null)
         {
             instance = new journalApi();
         }
         return instance ;

    }

    public journalApi() {
    }

    public static void setInstance(journalApi instance) {
        journalApi.instance = instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

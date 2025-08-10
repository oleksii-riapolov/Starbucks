package com.example.Starbucks;

public class Response {
    private UserData userData;
    private String userToken;
    private String message;

    public Object getUserData() {
        return userData;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getMessage() {
        return message;
    }

    public void setUserData(String userEmail, String userName) {
        this.userData = new UserData();
        userData.setUserEmail(userEmail);
        userData.setUserName(userName);
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

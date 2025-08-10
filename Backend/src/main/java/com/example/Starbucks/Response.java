package com.example.Starbucks;

public class Response {
    private User user;
    private String userToken;
    private String message;

    public Object getUserData() {
        return user;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getMessage() {
        return message;
    }

    public void setUserData(String userEmail, String userName) {
        this.user = new User();
        user.setUserEmail(userEmail);
        user.setUserName(userName);
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

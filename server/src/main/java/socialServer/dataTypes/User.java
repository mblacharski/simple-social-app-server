package main.java.socialServer.dataTypes;

import java.util.LinkedList;

public class User {
    private String username;
    private LinkedList<User> subscriptions = new LinkedList<>();

    public User(){}
    public User(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LinkedList<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(LinkedList<User> subscriptions) {
        this.subscriptions = subscriptions;
    }
}

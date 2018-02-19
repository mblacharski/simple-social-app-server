package main.java.socialServer.messages;

import main.java.socialServer.dataTypes.Events;

import java.util.LinkedList;
import java.util.List;

public class ActiveUsersMsg {
    private String eventCode = Events.ACTIVE_USERS.getValue();
    private LinkedList<String> users = new LinkedList<>();

    public ActiveUsersMsg(List<String> users){
        this.users.addAll(users);
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public LinkedList<String> getUsers() {
        return users;
    }

    public void setUsers(LinkedList<String> users) {
        this.users = users;
    }
}

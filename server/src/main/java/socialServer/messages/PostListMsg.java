package main.java.socialServer.messages;


import java.util.LinkedList;

public class PostListMsg {
    private String eventCode;
    private LinkedList<PostMsg> posts = new LinkedList<>();

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public LinkedList<PostMsg> getPosts() {
        return posts;
    }

    public void setPosts(LinkedList<PostMsg> posts) {
        this.posts = posts;
    }
}

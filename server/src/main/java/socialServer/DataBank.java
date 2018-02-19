package main.java.socialServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.socialServer.messages.PostMsg;

import java.util.LinkedList;

public class DataBank {
    private static ObservableList<PostMsg> posts = FXCollections.observableArrayList(new LinkedList<>());



    public static ObservableList<PostMsg> getPosts() {
        return posts;
    }
}

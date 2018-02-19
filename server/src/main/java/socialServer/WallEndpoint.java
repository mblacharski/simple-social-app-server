package main.java.socialServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.socialServer.dataTypes.Events;
import main.java.socialServer.dataTypes.User;
import main.java.socialServer.decoders.PostMsgDecoder;
import main.java.socialServer.decoders.SubscribeMsgDecoder;
import main.java.socialServer.messages.*;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@ServerEndpoint(
        value = "/wall/{username}",
        decoders = {
                SubscribeMsgDecoder.class,
                PostMsgDecoder.class
        }
)
public class WallEndpoint {
    private Session session;
    private User user;
    private static Set<WallEndpoint> wallEndpoints = new HashSet<>();
    private static HashMap<String, User> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        this.session = session;
        wallEndpoints.add(this);
        Optional<User> userOp = users.values().
                stream().
                filter(us -> username.equals(us.getUsername())).
                findFirst();
        User user = userOp.orElseGet(() -> new User(username));
        users.put(session.getId(), user);
        this.user = user;
        try {
            broadcastActiveUsers();
            broadcastPosts();
            broadcastWall();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(){
        wallEndpoints.remove(this);
        users.remove(session.getId());
        try {
            broadcastActiveUsers();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(WebsocketMsg msg){
        if(msg instanceof PostMsg){
            handleNewPost((PostMsg) msg);
        } else if(msg instanceof SubscribeMsg){
            handleNewSubscription((SubscribeMsg) msg);
        }
    }

    private void handleNewSubscription(SubscribeMsg msg) {
        boolean add = Events.FOLLOW.getValue().equals(msg.getEventCode());
        Optional<User> subscribedOpt = users.values().stream().filter(us -> us.getUsername().equals(msg.getFollowed())).findAny();
        User subscribed = subscribedOpt.orElseGet(() -> new User(msg.getFollowed()));

        users.values().forEach(us -> {
            if(us.getUsername().equals(msg.getUser())){
                processSubscription(subscribed, us, add);
                System.out.println(add);
            }
        });
        broadcastPosts();
    }

    private void processSubscription(User subscribed, User user, boolean add) {
        if(add) {
            user.getSubscriptions().add(subscribed);
        } else {
            user.getSubscriptions().remove(subscribed);
        }
    }

    private void handleNewPost(PostMsg msg) {
        DataBank.getPosts().add(msg);
        broadcastPosts();
        broadcastWall();
    }

    private void broadcastWall(){
        ObjectMapper mapper = new ObjectMapper();
        LinkedList<PostMsg> postsToSend = new LinkedList<>();
        DataBank.getPosts().forEach(post -> {
            if(post.getUser().equals(user.getUsername()) && !postsToSend.contains(post))
                postsToSend.add(post);
        });
        try {
            PostListMsg postsMsg = new PostListMsg();
            postsToSend.sort((p1, p2) -> -1* p1.getTimestamp().compareTo(p2.getTimestamp()));
            postsMsg.getPosts().addAll(postsToSend);
            postsMsg.setEventCode(Events.OWN_POSTS.getValue());
            String postsMsgJson = mapper.writeValueAsString(postsMsg);
            if(!postsMsgJson.isEmpty() && postsToSend.size()>0)
                session.getAsyncRemote().sendText(postsMsgJson);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void broadcastPosts() {
        ObjectMapper mapper = new ObjectMapper();
        wallEndpoints.forEach(wall -> {
            LinkedList<PostMsg> postsToSend = new LinkedList<>();
            wall.user.getSubscriptions().forEach(sub ->
                    //when database available -> find subscribed users' posts in database
                    DataBank.getPosts().forEach(post -> {
                        if(post.getUser().equals(sub.getUsername()) && !postsToSend.contains(post))
                            postsToSend.add(post);
                    })
            );
            try {
                PostListMsg postsMsg = new PostListMsg();
                postsToSend.sort((p1, p2) -> -1* p1.getTimestamp().compareTo(p2.getTimestamp()));
                postsMsg.getPosts().addAll(postsToSend);
                postsMsg.setEventCode(Events.POSTS.getValue());
                String postsMsgJson = mapper.writeValueAsString(postsMsg);
                if(!postsMsgJson.isEmpty() && postsToSend.size()>0)
                    wall.session.getAsyncRemote().sendText(postsMsgJson);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        });
    }

    private static void broadcastActiveUsers() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        wallEndpoints.forEach(wall ->{

            LinkedList<String> usernames = new LinkedList<>();
            users.values().forEach(user -> {
                if(!user.getUsername().equals(wall.user.getUsername())){
                    usernames.add(user.getUsername());
                }
            });
            String activeUsersMsg = null;
            try {
                activeUsersMsg = mapper.writeValueAsString(new ActiveUsersMsg(usernames));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if(activeUsersMsg != null) {
                wall.session.getAsyncRemote().sendText(activeUsersMsg);
            }
        });
    }
}

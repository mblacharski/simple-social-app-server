package main.java.socialServer.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.socialServer.dataTypes.Events;
import main.java.socialServer.messages.PostMsg;
import main.java.socialServer.messages.WebsocketMsg;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class PostMsgDecoder implements Decoder.Text<PostMsg> {
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public PostMsg decode(String s) throws DecodeException {
        try {
            PostMsg msg = mapper.readValue(s, PostMsg.class);
            if(msg.getContent().length()>140)
            msg.setContent(msg.getContent().substring(0, 140));
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean willDecode(String s) {
        try {
            WebsocketMsg msg = mapper.readValue(s, WebsocketMsg.class);
            if(msg != null){
                return Events.NEW_POST.getValue().equals(msg.getEventCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

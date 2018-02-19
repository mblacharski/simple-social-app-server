package main.java.socialServer.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.socialServer.dataTypes.Events;
import main.java.socialServer.messages.SubscribeMsg;
import main.java.socialServer.messages.WebsocketMsg;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class SubscribeMsgDecoder implements Decoder.Text<SubscribeMsg> {
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public SubscribeMsg decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, SubscribeMsg.class);
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
                return Events.FOLLOW.getValue().equals(msg.getEventCode()) || Events.UNFOLLOW.getValue().equals(msg.getEventCode());
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

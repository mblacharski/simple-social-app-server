package main.java.socialServer.encoders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.socialServer.messages.PostMsg;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class PostMsgEncoder implements Encoder.Text<PostMsg> {
    @Override
    public String encode(PostMsg postMsg) throws EncodeException {
        try {
            return new ObjectMapper().writeValueAsString(postMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

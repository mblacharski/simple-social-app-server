package main.java.socialServer.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.socialServer.messages.WebsocketMsg;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class WebsocketMsgDecoder implements Decoder.Text<WebsocketMsg> {
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public WebsocketMsg decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, WebsocketMsg.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean willDecode(String s) {
        try {
            WebsocketMsg msg = mapper.readValue(s, WebsocketMsg.class);
            return msg != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

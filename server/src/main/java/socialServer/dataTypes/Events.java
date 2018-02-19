package main.java.socialServer.dataTypes;

public enum Events {
    FOLLOW("FO"), UNFOLLOW("UF"), ACTIVE_USERS("AU"), NEW_POST("NP"), POSTS("PO"), OWN_POSTS("OP");

    private final String value;

    Events(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString(){
        return value;
    }

}

package io.yostajsc.core.code;

/**
 * Created by Phuc-Hau Nguyen on 12/8/2016.
 */

public class MessageInfo {

    @MessageType
    private int mType;

    public MessageInfo(@MessageType int type) {
        this.mType = type;
    }


    @MessageType
    public int getMessage() {
        return this.mType;
    }
}

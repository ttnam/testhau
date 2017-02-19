package com.yosta.phuotngay.models.app;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */

public class MessageInfo {

    @MessageType
    private int mType;

    private int data;

    public MessageInfo(@MessageType int type) {
        this.mType = type;
    }

    @MessageType
    public int getMessage() {
        return this.mType;
    }


    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

}

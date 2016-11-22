package com.yosta.phuotngay.configs.message;

import java.io.Serializable;

/**
 * Created by Phuc-Hau Nguyen on 9/29/2016.
 */

public class NetworkMessage implements Serializable {

    private boolean IsConnected = false;
    public NetworkMessage(boolean isConnected) {
        this.IsConnected = isConnected;
    }

    public boolean IsConnected() {
        return this.IsConnected;
    }
}

package com.yosta.interfaces;

import com.yosta.phuotngay.models.Friend;
import com.yosta.phuotngay.models.trip.BaseTrip;

import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 12/4/2016.
 */

public interface CallBackFriendsParam {
    void run(List<Friend> friends);
}

package com.yosta.phuotngay.firebase;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yosta.phuotngay.helpers.app.SearchTripHelper;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;
import com.yosta.phuotngay.models.location.FirebaseLocation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
public class FirebaseUtils {

    private static final String FIREBASE_TRIP = "TRIP";
    private static final String FIREBASE_USER = "USER";
    public static final String FIREBASE_LOCATION = "LOCATION";

    private Context mContext = null;
    private DatabaseReference mReference = null;
    private static FirebaseUtils mInstance = null;

    private FirebaseUtils(Context context) {
        this.mContext = context;
        if (this.mReference == null) {
            this.mReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    public static FirebaseUtils initializeWith(Context context) {
        if (mInstance == null) {
            mInstance = new FirebaseUtils(context);
        }
        return mInstance;
    }

    public DatabaseReference TRIP() {
        return this.mReference.child(FIREBASE_TRIP);
    }

    public DatabaseReference USER() {
        return this.mReference.child(FIREBASE_USER);
    }

    public DatabaseReference LOCATION() {
        return this.mReference.child(FIREBASE_LOCATION);
    }

    public DatabaseReference TRIPRef() {
        DatabaseReference ref = TRIP().getRef();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> trips = dataSnapshot.getChildren().iterator();
                FirebaseTrip trip;
                List<FirebaseTrip> result = new ArrayList<>();
                while (trips.hasNext()) {
                    trip = trips.next().getValue(FirebaseTrip.class);
                    result.add(trip);
                }
                SearchTripHelper.init(result);
                EventBus.getDefault().post(new MessageInfo(MessageType.LOAD_DONE));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                EventBus.getDefault().post(new MessageInfo(MessageType.LOAD_DONE));
            }
        });
        return ref;
    }

    public DatabaseReference USERRef() {
        return TRIP().getRef();
    }


    public void getLocation(String locId) {

        LOCATION().child(locId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

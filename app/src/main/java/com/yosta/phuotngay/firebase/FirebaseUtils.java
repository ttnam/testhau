package com.yosta.phuotngay.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yosta.phuotngay.helpers.app.SearchTripHelper;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.interfaces.CallBackListener;
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

    private DatabaseReference mReference = null;
    private static FirebaseUtils mInstance = null;

    private FirebaseUtils() {
        if (this.mReference == null) {
            this.mReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    public static FirebaseUtils getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseUtils();
        }
        return mInstance;
    }

    // DatabaseReference
    public DatabaseReference COMMENT(String tripId) {
        return TRIP().child(tripId).child("comment");
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


    // Query
    public Query TRIPRef() {
        Query ref = TRIP().getRef().orderByChild("ranking");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*Iterator<DataSnapshot> trips = dataSnapshot.getChildren().iterator();
                FirebaseTrip trip;
                List<FirebaseTrip> result = new ArrayList<>();
                while (trips.hasNext()) {
                    // Save data
                    DataSnapshot snapshot = trips.next();
                    trip = snapshot.getValue(FirebaseTrip.class);
                    // Save tripId
                    String tripId = snapshot.getKey();
                    trip.setTripId(tripId);

                    result.add(trip);
                }
                SearchTripHelper.init(result);*/

                EventBus.getDefault().post(new MessageInfo(MessageType.LOAD_DONE));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                EventBus.getDefault().post(new MessageInfo(MessageType.LOAD_DONE));
            }
        });
        return ref;
    }
    public Query ACTIVITYRef(String tripId) {
        return TRIP().child(tripId).child("activity").getRef();
    }
    public Query COMMENTRef(String tripId) {
        return COMMENT(tripId).getRef();
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

    public void onChangeRanking(String tripId, long value, final CallBackListener listener) {
        TRIP().child(tripId).child("ranking").setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("Data couldn't be saved.", databaseError.getMessage());
                } else {
                    listener.run();
                }
            }
        });
    }
}
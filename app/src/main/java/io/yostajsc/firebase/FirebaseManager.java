package io.yostajsc.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import io.yostajsc.core.interfaces.CallBackWith;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
public class FirebaseManager {

    public static final String FIRE_BASE_TOKEN = "FIRE_BASE_TOKEN";
    private static final String FIRE_BASE_TRIP = "TRIP";
    private static final String FIRE_BASE_TRACK = "TRACK";

    private DatabaseReference mReference = null;
    private static FirebaseManager mInstance = null;

    // Track
    private static ChildEventListener mOnValueChangeOnTrack = null;

    private FirebaseManager() {
        if (this.mReference == null) {
            this.mReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    public static FirebaseManager inject() {
        if (mInstance == null) {
            mInstance = new FirebaseManager();
        }
        return mInstance;
    }

    // DatabaseReference
    public DatabaseReference COMMENT(String tripId) {
        return TRIP().child(tripId).child("comment");
    }

    public DatabaseReference TRIP() {
        return this.mReference.child(FIRE_BASE_TRIP);
    }


    public DatabaseReference TRACK() {
        return this.mReference.child(FIRE_BASE_TRACK);
    }

    public void registerListenerOnTrack(
            String tripId,
            final CallBackWith<DataSnapshot> onChildAdded,
            final CallBackWith<DataSnapshot> onChildChanged,
            final CallBackWith<String> onCancelled) {

        mOnValueChangeOnTrack = TRACK().child(tripId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onChildAdded.run(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onChildChanged.run(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onCancelled.run(databaseError.getMessage());
            }
        });


        /*mOnValueChangeOnTrack = TRACK().child(tripId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    onSuccessful.run(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onCancelled.run(databaseError.getMessage());
            }
        });*/
    }

    public void unregisterListenerOnTrack() {
        if (mOnValueChangeOnTrack != null)
            TRACK().removeEventListener(mOnValueChangeOnTrack);
    }
}
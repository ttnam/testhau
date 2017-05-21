package io.yostajsc.izigo.usecase.service.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.yostajsc.core.interfaces.CallBackWith;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
public class FirebaseManager {

    public static final String FIRE_BASE_TOKEN = "FIRE_BASE_TOKEN";
    private static final String FIRE_BASE_TRACK = "TRACK";

    private DatabaseReference mReference = null;
    private static FirebaseManager mInstance = null;

    private static ChildEventListener mOnLastGpsListener = null;

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

    public DatabaseReference TRACK() {
        return this.mReference.child(FIRE_BASE_TRACK);
    }


    public void unregisterListenerOnTrack() {
        if (mOnLastGpsListener != null)
            TRACK().removeEventListener(mOnLastGpsListener);
    }

    public void subscribeLastGps(String tripId,
                                 final OnSuccessListener onChildAdded,
                                 final OnSuccessListener onChildChanged,
                                 final OnFailureListener onFailure) {

        mOnLastGpsListener = TRACK().child(tripId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String fbId = dataSnapshot.getKey();

                String[] dataChild = null;
                if (dataSnapshot.hasChild("lastGps"))
                    dataChild = ((String) dataSnapshot.child("lastGps").getValue()).split(", ");

                long isOnline = 0;
                if (dataSnapshot.hasChild("isOnline"))
                    isOnline = ((Long) dataSnapshot.child("isOnline").getValue());

                if (dataChild != null) {
                    onChildAdded.success(
                            fbId,
                            Double.valueOf(dataChild[0]),
                            Double.valueOf(dataChild[1]),
                            dataChild[2], isOnline == 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String fbId = dataSnapshot.getKey();

                String[] dataChild = null;
                if (dataSnapshot.hasChild("lastGps"))
                    dataChild = ((String) dataSnapshot.child("lastGps").getValue()).split(", ");

                long isOnline = 0;
                if (dataSnapshot.hasChild("isOnline"))
                    isOnline = ((Long) dataSnapshot.child("isOnline").getValue());

                if (dataChild != null) {
                    onChildChanged.success(
                            fbId,
                            Double.valueOf(dataChild[0]),
                            Double.valueOf(dataChild[1]),
                            dataChild[2], isOnline == 1);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onFailure.error(databaseError.getMessage());
            }
        });
    }

    public interface OnFailureListener {
        void error(String error);
    }

    public interface OnSuccessListener {
        void success(String fbId, double lat, double lng, String time, boolean isOnline);
    }
}
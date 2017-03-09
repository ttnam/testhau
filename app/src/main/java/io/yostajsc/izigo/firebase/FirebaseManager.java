package io.yostajsc.izigo.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.yostajsc.interfaces.CallBack;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
public class FirebaseManager {

    public static final String FIRE_BASE_TOKEN = "FIRE_BASE_TOKEN";

    private static final String FIRE_BASE_TRIP = "TRIP";
    private static final String FIRE_BASE_USER = "USER";
    private static final String FIRE_BASE_LOCATION = "LOCATION";

    private Context mContext;
    private DatabaseReference mReference = null;
    private static FirebaseManager mInstance = null;

    private FirebaseManager(Context context) {
        if (this.mReference == null) {
            this.mReference = FirebaseDatabase.getInstance().getReference();
        }
        mContext = context;
    }

    public static FirebaseManager inject(Context context) {
        if (mInstance == null) {
            mInstance = new FirebaseManager(context);
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

    public DatabaseReference USER() {
        return this.mReference.child(FIRE_BASE_USER);
    }

    public DatabaseReference LOCATION() {
        return this.mReference.child(FIRE_BASE_LOCATION);
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

    public void onChangeRanking(String tripId, long value, final CallBack listener) {
        TRIP().child(tripId).child("ranking").setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("LocationRe saved.", databaseError.getMessage());
                } else {
                    listener.run();
                }
            }
        });
    }
}
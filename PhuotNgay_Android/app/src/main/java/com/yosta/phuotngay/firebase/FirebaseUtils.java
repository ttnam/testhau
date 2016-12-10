package com.yosta.phuotngay.firebase;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yosta.phuotngay.helpers.app.SearchTripHelper;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 12/1/2016.
 */
public class FirebaseUtils {

    private Activity mActivity = null;
    private Context mContext = null;
    private DatabaseReference mReference = null;
    private static FirebaseUtils mInstance = null;

    private FirebaseUtils(Activity activity) {
        this.mActivity = activity;
        if (this.mReference == null) {
            this.mReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    private FirebaseUtils(Context context) {
        this.mContext = context;
        if (this.mReference == null) {
            this.mReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    public static FirebaseUtils initializeWith(Activity activity) {
        if (mInstance == null) {
            mInstance = new FirebaseUtils(activity);
        }
        return mInstance;
    }

    public static FirebaseUtils initializeWith(Context context) {
        if (mInstance == null) {
            mInstance = new FirebaseUtils(context);
        }
        return mInstance;
    }

    public DatabaseReference TRIP() {
        return this.mReference.child(FirebaseDefine.FIREBASE_TRIP);
    }

    public DatabaseReference TRIPRef() {
        DatabaseReference ref = TRIP().getRef();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> trips = dataSnapshot.getChildren().iterator();
                FirebaseTrip trip = null;
                List<FirebaseTrip> result = new ArrayList<>();
                while (trips.hasNext()) {
                    trip = trips.next().getValue(FirebaseTrip.class);
                    result.add(trip);
                }
                SearchTripHelper.init(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  ref;
    }

    public DatabaseReference USER() {
        return this.mReference.child(FirebaseDefine.FIREBASE_USER);
    }

    public DatabaseReference USERRef() {
        return TRIP().getRef();
    }
}

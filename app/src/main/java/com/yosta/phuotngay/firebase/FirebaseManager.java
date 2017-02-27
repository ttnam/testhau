package com.yosta.phuotngay.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yosta.phuotngay.firebase.model.FirebaseTrip;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.helpers.SearchTripHelper;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.interfaces.CallBack;
import com.yosta.phuotngay.models.app.MessageInfo;
import com.yosta.phuotngay.models.app.MessageType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    // Query
    public Query TRIPRef() {
        Query ref = TRIP().getRef().orderByChild("ranking");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> trips = dataSnapshot.getChildren().iterator();
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
                    Log.e("LocationResponse couldn't be saved.", databaseError.getMessage());
                } else {
                    listener.run();
                }
            }
        });
    }
}

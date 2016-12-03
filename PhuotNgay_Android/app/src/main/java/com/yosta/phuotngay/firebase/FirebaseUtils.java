package com.yosta.phuotngay.firebase;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        return TRIP().getRef();
    }

    public DatabaseReference USER() {
        return this.mReference.child(FirebaseDefine.FIREBASE_USER);
    }

    public DatabaseReference USERRef() {
        return TRIP().getRef();
    }
}

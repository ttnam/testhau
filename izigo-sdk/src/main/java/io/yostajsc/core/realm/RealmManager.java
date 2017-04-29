package io.yostajsc.core.realm;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.realm.trip.OwnTrip;
import io.yostajsc.core.realm.trip.OwnTrips;
import io.yostajsc.sdk.model.Timeline;
import io.yostajsc.sdk.model.Timelines;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class RealmManager {

    public static void insertOrUpdate(final RealmObject realmLObject) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(realmLObject);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static void insertOrUpdate(@NonNull final RealmList realmList) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(realmList);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static void findAllOwnTrips(final CallBackWith<OwnTrips> onSuccessful) {

        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<OwnTrip> res = realm.where(OwnTrip.class).findAll();
                    if (res.isValid()) {
                        OwnTrips trips = new OwnTrips();
                        trips.addAll(res);
                        onSuccessful.run(trips);
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static void findActivities(final CallBackWith<Timelines> callBack) {

        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Timeline> res = realm.where(Timeline.class).findAll();
                    if (res.isValid()) {
                        Timelines timelines = new Timelines();
                        timelines.addAll(res);
                        callBack.run(timelines);
                    }

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}

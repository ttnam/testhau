package io.yostajsc.usecase.realm;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.izigo.models.Timeline;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.izigo.models.user.BaseUserInfo;
import io.yostajsc.usecase.realm.trip.OwnTrip;
import io.yostajsc.usecase.realm.trip.OwnTrips;
import io.yostajsc.usecase.realm.trip.PublicTrip;
import io.yostajsc.usecase.realm.trip.PublicTrips;

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

    public static void insertOrUpdate(final BaseUserInfo userInfo, final CallBack onSuccessful) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(userInfo);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    onSuccessful.run();
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

    public static void clearAllTrips() {

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Trip> results = realm.where(Trip.class).findAll();
            realm.beginTransaction();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static void deleteTripById(String id) {

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            Trip trip = realm.where(Trip.class).equalTo(Trip.TRIP_ID, id).findFirst();
            realm.beginTransaction();
            trip.deleteFromRealm();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static void findAllPublicTrips(final CallBackWith<PublicTrips> onSuccessful) {

        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<PublicTrip> res = realm.where(PublicTrip.class).findAll();
                    if (res.isValid()) {
                        PublicTrips trips = new PublicTrips();
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

    public static void findTripById(final String tripId, final CallBackWith<Trip> callBack) {

        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    callBack.run(realm.where(Trip.class).equalTo(Trip.TRIP_ID, tripId).findFirst());
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}

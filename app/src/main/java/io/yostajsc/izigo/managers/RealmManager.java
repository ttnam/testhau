package io.yostajsc.izigo.managers;

import io.realm.Realm;
import io.realm.RealmResults;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.models.Timeline;
import io.yostajsc.izigo.models.Timelines;
import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.izigo.models.trip.Trips;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class RealmManager {

    public static void insertOrUpdate(final Trip trip) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
/*

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Trip.class).findAll().deleteAllFromRealm();
                }
            });
*/

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(trip);
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

    public static void insertOrUpdate(final Trips trips) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            /*realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Trip.class).findAll().deleteAllFromRealm();
                }
            });*/

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(trips);
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

    public static void insertOrUpdate(final Timelines timelines) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(timelines);
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

    public static void findTrips(final CallBackWith<Trips> callBack) {

        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Trip> res = realm.where(Trip.class).findAll();
                    if (res.isValid()) {
                        Trips trips = new Trips();
                        trips.addAll(res);
                        callBack.run(trips);
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

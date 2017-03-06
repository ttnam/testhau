package io.yostajsc.izigo.managers;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.yostajsc.izigo.models.base.Location;
import io.yostajsc.izigo.models.base.Locations;

/**
 * Created by Phuc-Hau Nguyen on 2/20/2017.
 */

public class RealmManager {

    public static void insertOrUpdate(final RealmObject object) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(object);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public static void insertOrUpdate(final Locations object) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(object);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private static RealmResults<Location> mLocations = null;

    public static RealmResults<Location> getLocations() {
        return mLocations;
    }

    public static Location getLocationById(int pos) {
        if (mLocations == null)
            return null;
        return mLocations.get(pos);
    }

    public static void findNations() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mLocations = realm.where(Location.class).findAll();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}

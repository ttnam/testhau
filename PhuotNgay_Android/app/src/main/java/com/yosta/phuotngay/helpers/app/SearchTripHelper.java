package com.yosta.phuotngay.helpers.app;

import com.yosta.phuotngay.models.trip.FirebaseTrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranNam on 12/9/2016.
 */

public class SearchTripHelper {
    private static List<FirebaseTrip> trips;

    public static void init(List<FirebaseTrip> trips) {
        SearchTripHelper.trips = trips;
    }

    public static List<FirebaseTrip> search(String arrive, String depart, String time, String transfer) {
        List<FirebaseTrip> result = new ArrayList();

        arrive = arrive.toLowerCase();
        depart = depart.toLowerCase();
        for(FirebaseTrip trip : trips)
            if (compare(arrive, trip.getArrive().getName().toLowerCase()) && compare(depart, trip.getDepart().getName().toLowerCase()))
                result.add(trip);

        return result;
    }

    private static boolean compare(String item, String _item) {
        if (item.equals(""))
            return true;

        String itemWord[] = item.split("[., ]");
        String _itemWord[] = _item.split("[., ]");

        for (String word : itemWord) {
            for (String _word : _itemWord) {
                if (word.equalsIgnoreCase(_word))
                    return true;
            }
        }

        return false;
    }
}

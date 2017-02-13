package com.yosta.phuotngay.helpers;

import com.yosta.phuotngay.firebase.model.FirebaseTrip;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by TranNam on 12/9/2016.
 */

public class SearchTripHelper {

    private static List<FirebaseTrip> trips;

    public static void init(List<FirebaseTrip> trips) {
        SearchTripHelper.trips = trips;
    }

    private static String unicodeToAscii(String s) throws UnsupportedEncodingException {
        String s1 = Normalizer.normalize(s, Normalizer.Form.NFKD);
        String regex = Pattern.quote("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");
        String s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"), "ascii");

        return s2. replaceAll("[?]", "");
    }

    public static List<FirebaseTrip> search(String arrive, String depart, String time, String transfer) {

        List<FirebaseTrip> result = new ArrayList();

       try {
            arrive = unicodeToAscii(arrive);
            depart = unicodeToAscii(depart);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for(FirebaseTrip trip : trips) {
            String _arrive = null;
            String _depart = null;
            try {
                String[] name = trip.getName().split(" - ");
                _arrive = unicodeToAscii(name[0]);
                _depart = unicodeToAscii(name[1]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (compare(arrive, _arrive) && compare(depart, _depart))
                result.add(trip);
        }

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

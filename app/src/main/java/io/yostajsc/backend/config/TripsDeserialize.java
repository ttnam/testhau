package io.yostajsc.backend.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.yostajsc.izigo.models.trip.Trip;
import io.yostajsc.izigo.models.trip.Trips;

/**
 * Created by Phuc-Hau Nguyen on 2/21/2017.
 */

public class TripsDeserialize implements JsonDeserializer<Trips> {

    private Gson gson = new Gson();

    @Override
    public Trips deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Trips trips = new Trips();
        try {
            if (json != null) {
                JsonArray jsonRes = json.getAsJsonArray();
                for (int i = 0; i < jsonRes.size(); i++) {
                    String jsons = jsonRes.get(i).getAsString();
                    Trip trip = gson.fromJson(jsons, Trip.class);
                    trips.add(trip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trips;
    }
}
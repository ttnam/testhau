package io.yostajsc.utils.maps;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.yostajsc.core.interfaces.CallBackWith;

/**
 * Created by nphau on 5/6/17.
 */

public class RouteParserTask extends AsyncTask<Object, Void, String> {

    private boolean draw = false;

    private GoogleMap mMap = null;
    private CallBackWith<Info> callback;

    public RouteParserTask(GoogleMap map) {
        this.mMap = map;
    }

    @Override
    protected String doInBackground(Object... obj) {

        draw = (boolean) obj[1];
        callback = (CallBackWith<Info>) obj[2];

        String data = "";
        try {
            data = downloadUrl((String) obj[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSONObject jObject;
        List<Route> routes;

        try {
            jObject = new JSONObject(result);

            RouteParser parser = new RouteParser();
            routes = parser.parse(jObject, draw);

            callback.run(routes.get(0).info);

            if (draw)
                draw(routes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void draw(List<Route> routes) {

        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        for (int i = 0; i < routes.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = routes.get(i).route;

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(15);
            lineOptions.color(Color.RED);
        }

        if (lineOptions != null) {
            mMap.addPolyline(lineOptions);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
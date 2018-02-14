package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


//  TODO (1) Create a class called SunshineSyncTask
//  TODO (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      TODO (3) Within syncWeather, fetch new weather data
//      TODO (4) If we have valid results, delete the old data and insert the new

public class SunshineSyncTask   {

    synchronized public static void syncWeather(Context context) {
        // TODO fetch new weather data
        try {
            URL weatherUrl = NetworkUtils.getUrl(context);
            String jasonWeathergetResponse = NetworkUtils.getResponseFromHttpUrl(weatherUrl);

            /* Parse the JSON into a list of weather values */
            ContentValues[] weatherValues = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jasonWeathergetResponse);

         /*   NullPointerExceptions being thrown. We also have no reason to insert fresh data if
            there isn't any to insert.*/
            if (weatherValues != null && weatherValues.length != 0){
                ContentResolver weatherContentResolver = context.getContentResolver();
                // delete previous data
                weatherContentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI,null,null);
                // Insert new data in to ContentProvider
                weatherContentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI,weatherValues);
                /* If the code reaches this point, we have successfully performed our sync */
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
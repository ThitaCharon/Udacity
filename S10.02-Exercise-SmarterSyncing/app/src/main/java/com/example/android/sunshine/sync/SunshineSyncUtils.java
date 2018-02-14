/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.data.WeatherProvider;


public class SunshineSyncUtils {

//  TODO (1) Declare a private static boolean field called sInitialized
    private static boolean sInitialized;

    //  TODO (2) Create a synchronized public static void method called initialize
    //  TODO (3) Only execute this method body if sInitialized is false
    //  TODO (4) If the method body is executed, set sInitialized to true
    //  TODO (5) Check to see if our weather ContentProvider is empty
        //  TODO (6) If it is empty or we have a null Cursor, sync the weather now!
    synchronized public static void initialize(final Context context){
        if (sInitialized) return;
        sInitialized = true;
        /*check ContentProvider
        We need to check to see if our ContentProvider has data to display in our forecast
          list. However, performing a query on the main thread is a bad idea as this may
          cause our UI to lag. Therefore, we create a thread in which we will run the query
          to check the contents of our ContentProvider.*/
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();
                String [] projection = {WeatherContract.WeatherEntry.COLUMN_WEATHER_ID};

                Cursor cursor = context.getContentResolver().query(
                        forecastQueryUri,projection,selection,null,null);
                /* Becarefull when Cursor return
                 * A Cursor object can be null for various different reasons. A few are
                 * listed below.
                 *
                 *   1) Invalid URI
                 *   2) A certain ContentProvider's query method returns null
                 *   3) A RemoteException was thrown.
                 *
                 * Bottom line, it is generally a good idea to check if a Cursor returned
                 * from a ContentResolver is null.
                 *
                 * If the Cursor was null OR if it was empty, we need to sync immediately to
                 * be able to display data to the user.
                 */
                if (cursor == null || cursor.getCount() == 0){
                    startImmediateSync(context);       // if it null then sync theweather data right now
                } //otherwise close cursor
                cursor.close(); // close cursor anytime to avoid memory leaks after eveything is good call exceute at the end

                return null;
            }
        }.execute();

    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
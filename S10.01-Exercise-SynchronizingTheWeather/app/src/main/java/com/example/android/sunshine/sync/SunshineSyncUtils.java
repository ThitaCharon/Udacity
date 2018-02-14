package com.example.android.sunshine.sync;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.android.sunshine.MainActivity;

// TODO (9) Create a class called SunshineSyncUtils
//  TODO (10) Create a public static void method called startImmediateSync
//  TODO (11) Within that method, start the SunshineSyncIntentService

public class SunshineSyncUtils {

    //  COMPLETED (10) Create a public static void method called startImmediateSync
    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context
     */
    public static void startImmediateSync(final Context context){
        Intent intentToStartImmediate = new Intent(context,SunshineSyncIntentService.class);
        context.startService(intentToStartImmediate);

    }
}

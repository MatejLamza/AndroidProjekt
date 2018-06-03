package test.vjezbe.myweatherapp.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import test.vjezbe.myweatherapp.helper.ApplicationHelper;

/**
 * Created by Matej on 6/3/2018.
 */

public class NetworkChangeReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ApplicationHelper.checkForConnectivity(context)){
            Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_LONG).show();
        }
    }
}

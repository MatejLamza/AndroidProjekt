package test.vjezbe.myweatherapp.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.widget.ImageView;

import java.util.regex.Pattern;

import test.vjezbe.myweatherapp.R;

/**
 * Created by Matej on 6/1/2018.
 */

public class ApplicationHelper {

    public static boolean checkForConnectivity(Context context){
        ConnectivityManager manager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork    = manager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void iconSetter(String desc,ImageView ivAvatar){
        //This function takes description and checks string if contains any of these words to find appropriate image
        if (Pattern.compile(Pattern.quote("cloud"),Pattern.CASE_INSENSITIVE).matcher(desc).find()){
            ivAvatar.setImageResource(R.drawable.cloud);

        }else if (Pattern.compile(Pattern.quote("sun"),Pattern.CASE_INSENSITIVE).matcher(desc).find()){
            ivAvatar.setImageResource(R.drawable.sun);

        } else if(Pattern.compile(Pattern.quote("rain"),Pattern.CASE_INSENSITIVE).matcher(desc).find()){
            ivAvatar.setImageResource(R.drawable.rain);
        }
    }

}

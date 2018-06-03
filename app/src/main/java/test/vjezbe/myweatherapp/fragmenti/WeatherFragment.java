package test.vjezbe.myweatherapp.fragmenti;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import test.vjezbe.myweatherapp.MainActivity;
import test.vjezbe.myweatherapp.helper.ApplicationHelper;
import test.vjezbe.myweatherapp.helper.WeatherAdapter;
import test.vjezbe.myweatherapp.models.ListWeather;
import test.vjezbe.myweatherapp.models.Weather;
import test.vjezbe.myweatherapp.R;
import test.vjezbe.myweatherapp.YoutubeActivity;
import test.vjezbe.myweatherapp.common.MyConsts;

/**
 * Created by Matej on 6/1/2018.
 */

public class WeatherFragment extends Fragment {

    private TextView    tvTemperature;
    private TextView    tvDescription;
    private TextView    tvCity;
    private TextView    tvSimbol;
    private ImageView   ivAvatar;
    private ListView    lvWeatherItems;
    private TextView    tvCelsius;

    private WeatherAdapter  adapter;
    private String          city;
    private String          description;

    private double x1, x2, y1,y2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            city = getArguments().getString(MyConsts.CITY_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_layout,container,false);

        initComponents(view);
        city = this.getArguments().getString(MyConsts.CITY_KEY);

        getCurrentWeather(city);
        getForecast(city);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
             return onTouchEvent(event);
            }
        });

        return view;
    }

    private void initComponents(View view){
        tvTemperature   = view.findViewById(R.id.tvTemperature);
        tvDescription   = view.findViewById(R.id.tvDescription);
        tvCity          = view.findViewById(R.id.tvCity);
        tvCelsius       = view.findViewById(R.id.tvCelsius);
        tvSimbol        = view.findViewById(R.id.tvSimbol);
        ivAvatar        = view.findViewById(R.id.ivAvatar);
        lvWeatherItems  = view.findViewById(R.id.lvWeatherItems);
        adapter         = new WeatherAdapter(getActivity(),R.layout.weather_list_item_layout,new ListWeather());
        lvWeatherItems.setAdapter(adapter);

    }


    public void getCurrentWeather(String city){
        String url = MyConsts.URL + city + MyConsts.APPID;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONObject mainObject = response.getJSONObject(MyConsts.MAIN);
                    JSONArray array = response.getJSONArray(MyConsts.WEATHER);
                    JSONObject object = array.getJSONObject(0);

                    String temp = String.valueOf(mainObject.getInt(MyConsts.TEMP));

                    tvCity.setText(response.getString(MyConsts.NAME));
                    tvDescription.setText(object.getString(MyConsts.DESCRIPTION));
                    tvTemperature.setText(temp);
                    ivAvatar.setImageResource(R.drawable.sun);
                    tvSimbol.setText("°");
                    tvCelsius.setText("C");

                    String desc = tvDescription.getText().toString();
                    ApplicationHelper.iconSetter(desc,ivAvatar);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getActivity(),"You entered the wrong city, check your spelling!.", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getActivity(), MainActivity.class);
               startActivity(intent);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjRequest);
    }

    public void getForecast(String city){
        String url = MyConsts.URL_FORECAST + city + MyConsts.APPID;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //Main object in json file
                    JSONArray array = response.getJSONArray(MyConsts.LIST);
                    List<Weather> listOfWeathers = new ArrayList<>();
                    processJsonArray(listOfWeathers,array);

                    Calendar targetDate = Calendar.getInstance();
                    targetDate.add(Calendar.DATE,3);

                    List<Weather> filteredWeather = new ArrayList<>();
                    weatherFilter(filteredWeather,listOfWeathers,targetDate);

                    for (Weather w: filteredWeather){
                        adapter.add(w);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            private void weatherFilter(List<Weather> filteredWeather, List<Weather> listOfWeathers, Calendar targetDate) {
                for (Weather w: listOfWeathers){
                    if (filteredWeather.size() > 4 ){
                        break;
                    }

                    // checking if date is whitin range of 3 to return forecast for next 3 days
                    if (w.getDate().get(Calendar.DAY_OF_MONTH) <= targetDate.get(Calendar.DAY_OF_MONTH)){
                        if (filteredWeather.isEmpty()){
                            filteredWeather.add(w);
                        }

                        boolean shouldAdd = true;

                        for (Weather we: filteredWeather){
                            //Check if there is a object in filtered list with same date. If there is don't add it.
                            if (w.getDate().get(Calendar.DAY_OF_MONTH) == we.getDate().get(Calendar.DAY_OF_MONTH)) {
                                shouldAdd = false;
                            }
                        }
                        if(shouldAdd){
                            filteredWeather.add(w);
                        }
                    }
                }
            }

            private void processJsonArray(List<Weather> listOfWeathers, JSONArray array)throws JSONException {
                for(int i=0; i<array.length(); i++){
                    //Single Object from list file
                    JSONObject currentObj = array.getJSONObject(i);
                    //Date in mililiseconds from current object
                    long dt = currentObj.getLong(MyConsts.DT);
                    //Weather array inside current object
                    JSONArray weatherObj = currentObj.getJSONArray(MyConsts.WEATHER);
                    //Main object to extract min temp and max temp
                    JSONObject tempObj = currentObj.getJSONObject(MyConsts.MAIN);

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(dt*1000);


                    Weather weather = new Weather();
                    weather.setDate(cal);
                    weather.setDescription(weatherObj.getJSONObject(0).getString(MyConsts.DESCRIPTION));
                    weather.setMinTemperature(tempObj.getInt(MyConsts.TEMP_MIN));
                    weather.setTemperature(tempObj.getInt(MyConsts.TEMP_MAX));

                    listOfWeathers.add(weather);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjRequest);
        Toast.makeText(getActivity(),"Swipe right to see Youtube video!",Toast.LENGTH_SHORT).show();
    }

    private void sendDataToYoutubeActivity(){
        description = tvDescription.getText().toString() + " " + tvCity.getText().toString();

        Intent intent = new Intent(getActivity(),YoutubeActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(MyConsts.VIDEO_ID,description);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getActionMasked()){

            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                //y1 = touchEvent.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                //y2 = touchEvent.getY();
                if (x1 > x2){
                    sendDataToYoutubeActivity();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }




}

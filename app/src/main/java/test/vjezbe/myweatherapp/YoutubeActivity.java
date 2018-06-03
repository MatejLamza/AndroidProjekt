package test.vjezbe.myweatherapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.vjezbe.myweatherapp.common.MyConsts;

/**
 * Created by Matej on 6/1/2018.
 */

public class YoutubeActivity extends YouTubeBaseActivity {

    private YouTubePlayerView   ytPlayerView;
    private String              videoID;
    private String              description;
    private Button              btnPlay;

    private YouTubePlayer.OnInitializedListener initListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_layout);

        initComponents();

        Bundle bundle = getIntent().getExtras();
        description = bundle.getString(MyConsts.VIDEO_ID);
        getYoutubeVideoID(description);

        initListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoID);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ytPlayerView.initialize(MyConsts.YT_API_KEY,initListener);
            }
        });
    }

    private void initComponents(){
        ytPlayerView = findViewById(R.id.ytPlayerView);
        btnPlay      = findViewById(R.id.btnPlay);
        Toast.makeText(this,"Press back button to go to previous screen.",Toast.LENGTH_LONG).show();
    }

    public void getYoutubeVideoID(String desc){
        String url = MyConsts.YT_URL + desc + MyConsts.YT_APPID;
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray items = response.getJSONArray(MyConsts.ITEMS);
                    JSONObject itemsObj = items.getJSONObject(0);
                    JSONObject id = itemsObj.getJSONObject(MyConsts.ID);
                    videoID = id.get(MyConsts.VIDEO_ID).toString();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue que = Volley.newRequestQueue(this);
        que.add(json);
    }

}

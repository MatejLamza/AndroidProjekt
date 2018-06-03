package test.vjezbe.myweatherapp.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import test.vjezbe.myweatherapp.models.Weather;
import test.vjezbe.myweatherapp.R;

/**
 * Created by Matej on 6/1/2018.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private LayoutInflater inflater;

    ImageView   ivIcon;
    TextView    tvDateListItem;
    TextView    tvDescriptionListItem;
    TextView    tvMaxTemp;
    TextView    tvMinTemp;


    public WeatherAdapter(@NonNull Context context, int resource, @NonNull List<Weather> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Weather weather = this.getItem(position);

        if (convertView == null){
            convertView = this.inflater.inflate(R.layout.weather_list_item_layout,null);
        }

        initComponents(convertView);

        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        String dayOfMonthStr = symbols.getWeekdays()[weather.getDate().get(Calendar.DAY_OF_WEEK)];

        tvDescriptionListItem.setText(weather.getDescription());
        tvMinTemp.setText(String.valueOf(weather.getMinTemperature()) + "°");
        tvMaxTemp.setText(String.valueOf(weather.getTemperature()) + "°");
        tvDateListItem.setText(dayOfMonthStr);

        ApplicationHelper.iconSetter(tvDescriptionListItem.getText().toString(),ivIcon);

        return convertView;
    }

    private void initComponents(View view){
        ivIcon                  = view.findViewById(R.id.ivIcon);
        tvDateListItem          = view.findViewById(R.id.tvDateListItem);
        tvDescriptionListItem   = view.findViewById(R.id.tvDescriptionListItem);
        tvMaxTemp               = view.findViewById(R.id.tvMaxTemp);
        tvMinTemp               = view.findViewById(R.id.tvMinTemp);
    }
}

package in.internity.crowdtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.internity.crowdtracker.Constants.Constants;
import in.internity.crowdtracker.R;

public class BookActivity extends AppCompatActivity {

    String rickshawNum, stationName, driverName, booking_id;
    int capacity;
    @BindView(R.id.check_tick)
    ImageView checkTick;
    @BindView(R.id.amount_in_inr_tv)
    TextView amountInInrTv;
    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.rickshaw_num)
    TextView rickshawNumtv;
    @BindView(R.id.capacity)
    TextView capacitytv;
    @BindView(R.id.DriverName)
    TextView DriverName;
    @BindView(R.id.destination)
    TextView destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        rickshawNum = intent.getStringExtra(Constants.RICSHAW_NUM);
        driverName = intent.getStringExtra(Constants.DRIVER_NAME);
        capacity = intent.getIntExtra(Constants.CAPACITY, 0);
        stationName = intent.getStringExtra(Constants.STATION_NAME_FOR_INTENT);
        booking_id = intent.getStringExtra(Constants.Book_ID);

        bookingId.setText(""+booking_id);
        rickshawNumtv.setText(""+rickshawNum);
        DriverName.setText(""+driverName);
        destination.setText("Traveling To "+stationName);
        if(capacity<2){
            capacitytv.setText("Only You");
        }else{
            capacitytv.setText("Traveling with "+(capacity-1)+" other");
        }

    }
}

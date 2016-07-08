package com.akkaratanapat.altear.driversbehavior;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient googleApiClient;
    private TextView textView;
    private SensorManager mySensorManager;
    private Sensor myAccSensor, myGyroSensor;
    private TextView accX, accY, accZ, gyroX, gyroY, gyroZ, avSpeed, maxSpeed, minSpeed,currentSpeed;
    NumberFormat formatter = new DecimalFormat("#0.000");
    private int counter=1;
    private double  maxSp = 0.0,minSp = 0.0,avSp = 0.0;
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private Button logButton;
    private TextView tvHeading,textlatlong;
    private Spinner spinner;
    private ToggleButton toggle;
    private DBHelper dbHelper;
    private static String[] COLUMN ={Constant._ID,Constant.COLUMN_DATE,Constant.COLUMN_DATE,Constant.COLUMN_ACC_X
            ,Constant.COLUMN_ACC_Y,Constant.COLUMN_ACC_Z,Constant.COLUMN_GYRO_X,Constant.COLUMN_GYRO_Y,Constant.COLUMN_GYRO_Z
            ,Constant.COLUMN_SPEED,Constant.COLUMN_ANGLE,Constant.COLUMN_EVENT};
    private static String ORDERBY = Constant.COLUMN_DATE + " DESC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = (TextView) findViewById(R.id.text_view);

        // Create Google API Client instance
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myAccSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myGyroSensor  = mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mySensorManager.registerListener(accelListener, myAccSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(gyroListener, myGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        image = (ImageView) findViewById(R.id.imageViewCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accX = (TextView)findViewById(R.id.textView2);
        accY = (TextView)findViewById(R.id.textView4);
        accZ = (TextView)findViewById(R.id.textView6);

        gyroX = (TextView)findViewById(R.id.textView02);
        gyroY = (TextView)findViewById(R.id.textView04);
        gyroZ = (TextView)findViewById(R.id.textView06);

        avSpeed = (TextView)findViewById(R.id.textView9);
        maxSpeed = (TextView)findViewById(R.id.textView11);
        minSpeed = (TextView)findViewById(R.id.textView13);
        currentSpeed = (TextView)findViewById(R.id.textView15);

        textlatlong = (TextView)findViewById(R.id.textView16);

        logButton = (Button)findViewById(R.id.button);

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LogActivity.class);
                startActivity(i);
            }
        });

        dbHelper = new DBHelper(this);

        try{
//            Cursor cursor = getAllNote();
//            showNote(cursor);
        }
        finally {
            dbHelper.close();
        }

        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            private long time = 0;

            @Override
            public void run()
            {
                // do stuff then
                // can call h again after work!
                try{
                    if(toggle.isChecked()){
                        addNote(new Date().toString(),accX.getText().toString(),accY.getText().toString(),accZ.getText().toString()
                                ,gyroX.getText().toString(),gyroY.getText().toString(),gyroZ.getText().toString(),currentSpeed.getText().toString()
                                ,tvHeading.getText().toString(),spinner.getSelectedItem().toString());
//                        Cursor cursor = getAllNote();
//                        showNote(cursor);
                    }
                }finally {
                    dbHelper.close();
                }

                time += 1000;
                Log.d("TimerExample", "Going for... " + time);
                h.postDelayed(this, 300);
            }
        }, 300);
    }

    SensorEventListener accelListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            accX.setText(formatter.format(x) +" m/s^2");
            accY.setText(formatter.format(y) +" m/s^2");
            accZ.setText(formatter.format(z) +" m/s^2");
        }
    };

    SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];


            gyroX.setText(formatter.format(x) + " rad/s");
            gyroY.setText(formatter.format(y) + " rad/s");
            gyroZ.setText(formatter.format(z) + " rad/s");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(accelListener, myAccSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mySensorManager.registerListener(gyroListener, myGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(accelListener);
        mySensorManager.unregisterListener(gyroListener);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Connect to Google API Client
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            // Disconnect Google API Client if available and connected
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Do something when connected with Google API Client

        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            // Call Location Services
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(500);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            // Do something when Location Provider not available
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do something when Google API Client connection was suspended

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Do something when Google API Client connection failed

    }

    @Override
    public void onLocationChanged(Location location) {
        textlatlong.setText("Latitude : " + location.getLatitude() + "\n" + "Longitude : " + location.getLongitude() +
                "\n" + "Provider : " + location.getProvider());
        double speed = location.getSpeed();
        if(speed>maxSp){
            maxSp = speed;
        }

        avSp = ((avSp*counter)+speed)/(counter+1);

        if(minSp == 0.0){
            minSp =speed;
        }
        else if(minSp != 0&& minSp>speed && speed != 0.0){
            minSp = speed;
        }
        avSpeed.setText(formatter.format(avSp) + " m/s");
        maxSpeed.setText(formatter.format(maxSp)+"");
        minSpeed.setText(formatter.format(minSp)+"");
        currentSpeed.setText(formatter.format(speed) + " m/s");
        counter++;
        // Do something when got new current location
//        textView.setText("Latitude : " + location.getLatitude() + "\n" +
//                "Longitude : " + location.getLongitude() + "\n" + "speed : " + location.getSpeed() + "\n" + location.getProvider());
    }



    public void addNote(String date, String accx, String accy, String accz, String gyrox, String gyroy, String gyroz, String speed, String angle, String event){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_DATE,date);
        values.put(Constant.COLUMN_ACC_X,accx);
        values.put(Constant.COLUMN_ACC_Y,accy);
        values.put(Constant.COLUMN_ACC_Z,accz);
        values.put(Constant.COLUMN_GYRO_X,gyrox);
        values.put(Constant.COLUMN_GYRO_Y,gyroy);
        values.put(Constant.COLUMN_GYRO_Z,gyroz);
        values.put(Constant.COLUMN_SPEED,speed);
        values.put(Constant.COLUMN_ANGLE,angle);
        values.put(Constant.COLUMN_EVENT,event);
        db.insertOrThrow(Constant.TABLE_NAME,null,values);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

package com.haugsand.fredrikh.activitynavigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RunActivity extends BaseActivity implements SensorEventListener{
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private SensorManager mSensorManager;
	private Sensor mStepCounterSensor;
	private Sensor mStepDetectorSensor;
    private int value = 0;
    private int value1 = 0;
    private int value2 = 0;
    private int value3 = 0;
    DBAdapter DatabaseDaily;
    DBAdapter1 DatabaseRunHistory;
	private TextView textSteps;
	private TextView textCalories;
	private TextView textHelp;
    //Variabel for å sette gjøre skrittene til 0 ved klikk

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		setContentView(R.layout.activity_run);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml
		textSteps = (TextView) findViewById(R.id.textSteps);
		textCalories = (TextView)findViewById(R.id.textCalories);
		textHelp = (TextView)findViewById(R.id.textHelp);
		openDB();
		set(navMenuTitles, navMenuIcons);
	}
    private void openDB(){
        DatabaseDaily = new DBAdapter(this);
        DatabaseDaily.open();
        DatabaseRunHistory = new DBAdapter1(this);
        DatabaseRunHistory.open();
    }
    public void onClick_StartRun (View v){
        textHelp.setText("" + value);
        DatabaseDaily.updateRow(6, textHelp.getText().toString());
        value3 = Integer.parseInt(textSteps.getText().toString());
    }
	public void onClick_StopRun (View v) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        System.out.println(value1);
        System.out.println(textSteps.getText().toString());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM HH:mm");
        String formattedDate = df.format(c.getTime());
        if (!TextUtils.isEmpty(textSteps.getText().toString())) {
            DatabaseRunHistory.insertRow(textSteps.getText().toString(),textCalories.getText().toString(), formattedDate );
            DatabaseDaily.updateRow(6, textHelp.getText().toString());

        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (values.length >0) {
                value = (int) values[0];
                value2 = value3;
                value1 = value - value2;
                textSteps.setText("" + value1);
                textCalories.setText(String.format("%.1f",  value1*0.05));
            }

        }
        else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            value2++;
        }
        //value1 = (int) values[0] - value;
        //  textView2.setText("Antall skritt idag : " + value2);
    }




    @Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }
}

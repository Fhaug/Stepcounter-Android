package com.haugsand.fredrikh.activitynavigation;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class StepActivity extends BaseActivity implements SensorEventListener {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private int value = -1;
	private int value1 = 0;
	private int value2 = 0;
	private int value3 = 0;

	private static TextView textView;
	private static TextView textHelp;

	private SensorManager mSensorManager;
	private Sensor mStepCounterSensor;
	private Sensor mStepDetectorSensor;

	static DBAdapter DatabaseDaily;
	static DBAdapter2 DatabaseTotal;

	private DonutProgress DonutProgress;
	private int progressStatus = 0;
	private int chosenDailySteps = 10000;
	private TextView textView4;
	private TextView kalorierTextView;

	private Handler handler = new Handler();

    ComponentName myServiceComponent;
    MyService myService;
    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            myService = (MyService) msg.obj;
            myService.setUICallback(StepActivity.this);
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        setContentView(R.layout.activity_steps);
        myServiceComponent = new ComponentName(this, MyService.class);
        Intent myServiceIntent = new Intent(this, MyService.class);
        myServiceIntent.putExtra("messenger", new Messenger(myHandler));
        startService(myServiceIntent);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml
		textView = (TextView) findViewById(R.id.skrittText);
		kalorierTextView = (TextView) findViewById(R.id.kalorierText);
		scheduleAlarm();
		openDB();
        set(navMenuTitles, navMenuIcons);



		DonutProgress = (DonutProgress) findViewById(R.id.donut_progress);
		// Start long running operation in a background thread
		new Thread(new Runnable() {
			public void run() {
				while (progressStatus < 10000) {
					progressStatus = value1/chosenDailySteps;
					// Update the progress bar and display the
					//current value in the text view
					handler.post(new Runnable() {
						public void run() {
							DonutProgress.setProgress(progressStatus);
						}
					});
					try {
						// Sleep for 200 milliseconds.
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	private void openDB(){
		DatabaseDaily = new DBAdapter(this);
		DatabaseTotal = new DBAdapter2(this);
		DatabaseDaily.open();
		DatabaseTotal.open();
	}

	public static void dailySteps(){
		/* RAD 1 = TOTALT SÅ LANGT IDAG
			RAD 5 = RESERVE
			RAD 6 = Kalkulasjon
			RAD 7 = ENDELIG IDAG
			RAD 8 =TOM
		 */
		DatabaseDaily.updateRow(1, textHelp.getText().toString());
		DatabaseDaily.updateRow(5, textHelp.getText().toString());

	}

	public static void onClick_AddTask(){

		dailySteps();

		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		if(!TextUtils.isEmpty(textView.getText().toString())){
			DatabaseTotal.insertRow(textView.getText().toString(), formattedDate);
			dailySteps();
		}
	}

	public void setValue(){
		//value3 = Integer.parseInt(DatabaseDaily.getRow(1).getString(1));
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		setValue();
				Sensor sensor = event.sensor;
		float[] values = event.values;
		if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
			if (values.length >0) {
				value = (int) values[0];
                value1 = value-value3;
                textView.setText("" + value1);
				kalorierTextView.setText("" + String.format("%.2f", value*0.05));
			}

		}
		else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
			// For test only. Only allowed value is 1.0 i.e. for step taken
			value2++;

		}
		//value1 = (int) values[0] - value;
		//  textView2.setText("Antall skritt idag : " + value2);
	}

	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mStepCounterSensor,SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this, mStepDetectorSensor,SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void onStop() {
		super.onStop();
		mSensorManager.unregisterListener(this, mStepCounterSensor);
		mSensorManager.unregisterListener(this, mStepDetectorSensor);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	public void scheduleAlarm()
	{
		Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
		Intent intentAlarm = new Intent(this, AlarmReciever.class);

		// create the object
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		//set the alarm for particular time
		alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
	}
}

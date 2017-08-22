package com.haugsand.fredrikh.activitynavigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by FredrikH on 12.05.2017.
 */
public class AlarmReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        StepActivity.onClick_AddTask();
    }

}
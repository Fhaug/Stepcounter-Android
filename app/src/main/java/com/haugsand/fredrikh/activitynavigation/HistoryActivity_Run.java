package com.haugsand.fredrikh.activitynavigation;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by FredrikH on 13.04.2017.
 */
public class HistoryActivity_Run extends BaseActivity {
    Time today = new Time(Time.getCurrentTimezone());
    DBAdapter1 myDb;
    EditText etTasks;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_run);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml
        set(navMenuTitles, navMenuIcons);
        openDB();
        populateListView();
    }
    private void openDB(){
        myDb = new DBAdapter1(this);
        myDb.open();
    }

    private void populateListView(){
        Cursor cursor = myDb.getAllRows();
        String[] fromFieldNames = new String[]{ DBAdapter1.KEY_CALORIES, DBAdapter1.KEY_DATE,  DBAdapter1.KEY_STEPS};
        int[] toViewIDs = new int[]{ R.id.textViewItemDate1, R.id.textViewItemCalories1, R.id.textViewItemSteps1};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_run, cursor, fromFieldNames, toViewIDs,0 );
        ListView myList = (ListView) findViewById(R.id.listViewTasks);
        myList.setAdapter(myCursorAdapter);


    }
}

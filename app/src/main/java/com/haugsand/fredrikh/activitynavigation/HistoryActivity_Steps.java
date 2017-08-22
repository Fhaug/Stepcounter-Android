package com.haugsand.fredrikh.activitynavigation;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by FredrikH on 13.04.2017.
 */
public class HistoryActivity_Steps extends BaseActivity {
    Time today = new Time(Time.getCurrentTimezone());
    DBAdapter2 myDb;
    EditText etTasks;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_steps);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);
        openDB();
        populateListView();
    }
    private void openDB(){
        myDb = new DBAdapter2(this);
        myDb.open();
    }

    private void populateListView(){
        Cursor cursor = myDb.getAllRows();
        String[] fromFieldNames = new String[]{ DBAdapter2.KEY_STEPS, DBAdapter2.KEY_DATE};
        int[] toViewIDs = new int[]{R.id.textViewItemTask, R.id.textViewItemDate};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_steps, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.listViewTasks);
        myList.setAdapter(myCursorAdapter);


    }
}

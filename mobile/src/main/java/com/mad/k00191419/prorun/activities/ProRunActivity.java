package com.mad.k00191419.prorun.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.Goal;
import com.mad.k00191419.prorun.db.ProRunDB;
import com.mad.k00191419.prorun.db.Run;
import com.mad.k00191419.prorun.utils.RunListAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class ProRunActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Consts
    private final String PREF_KEY_ISRUNNING = "isRunning";

    // View references
    private TextView    tvProgress;
    private ProgressBar pbGoalProgress;
    private Spinner     spinGoalPeriod;
    private ImageButton ibStart;
    private ImageButton ibStop;
    private ListView    lvHistory;
    private TextView    tvPercentage;

    // Fields
    private String[]            goal_periods = {"Daily", "Weekly", "Monthly"};
    private int                 spinId = 0;
    private Goal[]              goals = new Goal[3];
    private ProRunDB            mDb;
    private RunListAdapter      mAdapter;
    private SharedPreferences   mPrefs;
    private Handler             mUiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_run);

        mDb = new ProRunDB(getApplicationContext());
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setupReferences();
        setupSpinner();
        setupGoals();
        setupListView();
        setupListeners();
    }

    private void setupListView() {
        ArrayList<Run> runs = mDb.getRuns();

        mAdapter = new RunListAdapter(this, runs);

        lvHistory.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private void setupGoals() {
        goals[1] = mDb.getWeeklyGoal();
        goals[2] = mDb.getMonthlyGoal();
        goals[0] = mDb.getDailyGoal();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListView();
        setupGoals();
        updateProgressBar();
        // check if running and move to CurrentActivity if true
        checkPrefs();
    }

    private void checkPrefs() {
        if( mPrefs.getBoolean(PREF_KEY_ISRUNNING, false) ){
            Intent intent = new Intent(this, CurrentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prorun_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupReferences() {
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        pbGoalProgress = (ProgressBar)findViewById(R.id.pbGoalProgress);
        spinGoalPeriod = (Spinner) findViewById(R.id.spinGoalPeriod);
        ibStart = (ImageButton) findViewById(R.id.ibStart);
        ibStop = (ImageButton) findViewById(R.id.ibStop);
        lvHistory = (ListView) findViewById(R.id.lvHistory);
        tvPercentage = (TextView) findViewById(R.id.tvPercentage);

    }

    private void setupListeners() {
        ibStart.setOnClickListener(this);
        spinGoalPeriod.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.ibStart:
                Intent intent = new Intent(this, CurrentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                checkLocationPerms();
                startActivity(intent);
                finish();
                break;

        }
    }

    private void checkLocationPerms() {
        // check permissions
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                return;
            else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuGoals:
                Intent intent = new Intent(this, ViewGoalsActivity.class);
                startActivity(intent);
                break;
            case R.id.menuSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menuAbout:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, goal_periods);
        spinGoalPeriod.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateProgressBar();
    }

    private void updateProgressBar() {
        spinId = spinGoalPeriod.getSelectedItemPosition();
        // set progress values
        Goal g = goals[spinId];
        if(g == null) return;
        final int progress = g.getTotalProgress();

        pbGoalProgress.setProgress(progress);
        tvPercentage.setText(String.format("%d %%", progress));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinGoalPeriod.setSelection(spinId);
    }
}

package com.mad.k00191419.prorun;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProRunDisplayActivity extends Activity {

    // Views
    TextView tvWearSpeed;
    TextView tvWearDistance;
    TextView tvWearCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setupReferences();

    }

    private void setupReferences() {
        tvWearSpeed = (TextView)findViewById(R.id.tvWearSpeed);
        tvWearDistance = (TextView)findViewById(R.id.tvWearDistance);
        tvWearCalories = (TextView)findViewById(R.id.tvWearCalories);
    }
}

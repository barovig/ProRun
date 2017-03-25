package com.mad.k00191419.prorun.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.k00191419.prorun.R;

public class SummaryActivity extends AppCompatActivity implements View.OnClickListener{


    // View References
    Button btnDetails;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        setupReferences();
        setupListeners();
    }

    private void setupReferences() {
        btnDetails = (Button)findViewById(R.id.btnDetails);
        btnOk = (Button)findViewById(R.id.btnOk);
    }

    private void setupListeners() {
        btnOk.setOnClickListener(this);
        btnDetails.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btnDetails:
                Intent intent = new Intent(this, DetailsActivity.class);
                startActivity(intent);
        }
    }
}

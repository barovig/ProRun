package com.mad.k00191419.prorun;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class ProRunDisplayActivity extends WearableActivity
implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    // Views
    TextView tvWearSpeed;
    TextView tvWearDistance;
    TextView tvWearTime;
    private BoxInsetLayout mContainerView;

    // Consts
    public static final int INTENT_REQCODE_SEND = 154;
    private final String    MSGAPI_MSG_KEY = "/message_prorun";
    private final String    MSGAPI_MSG_STOP = "/prorun_stop";

    private final int       NOTIF_ID = 12365;

    // Fields
    GoogleApiClient           mApi;
    NotificationManagerCompat mNotifManager;
    boolean                   isGoing;
    long                      mStartTime = 0;
    Handler                   mUiHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setAmbientEnabled();
        setupReferences();
        // setup GAPI client, CONNECT and SETUP LISTeners!
        mApi = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApi.connect();
        mNotifManager = NotificationManagerCompat.from(this);
    }

    private void setupReferences() {
        tvWearSpeed = (TextView)findViewById(R.id.tvWearSpeed);
        tvWearDistance = (TextView)findViewById(R.id.tvWearDistance);
        tvWearTime = (TextView)findViewById(R.id.tvWearTime);
        mContainerView = (BoxInsetLayout) findViewById(R.id.mContainerView);

    }
    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            tvWearTime.setTextColor(getResources().getColor(android.R.color.white));
            tvWearDistance.setTextColor(getResources().getColor(android.R.color.white));
            tvWearSpeed.setTextColor(getResources().getColor(android.R.color.white));

        } else {
            mContainerView.setBackground(null);
            tvWearTime.setTextColor(getResources().getColor(android.R.color.black));
            tvWearDistance.setTextColor(getResources().getColor(android.R.color.black));
            tvWearSpeed.setTextColor(getResources().getColor(android.R.color.black));
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.MessageApi.addListener(mApi, this);
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        //Log.d("PRORUN_WEAR", "Received message");

        // if we are recepient
        if (messageEvent.getPath().equalsIgnoreCase(MSGAPI_MSG_KEY)) {
            // set flag as ongoing

            if(!isGoing){
                // create intent and build ongoing notification
                Intent intent = new Intent(this, ProRunDisplayActivity.class);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.string_notification_text))
                        .setContentIntent(
                                PendingIntent.getActivity(this, INTENT_REQCODE_SEND,
                                        intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        )
                        .setOngoing(true);
                isGoing = true;
                mNotifManager.notify(NOTIF_ID, builder.build());
                // grab current time and start UI updater for time
                Calendar c = Calendar.getInstance();
                mStartTime = c.getTimeInMillis();

            }
            // need to run on UI thread to be able to update it
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // update time
                    Calendar c = Calendar.getInstance();
                    long time = c.getTimeInMillis();
                    tvWearTime.setText(formatInterval(time - mStartTime));

                    String data = new String(messageEvent.getData());
                    Log.d("PRORUN_WEAR", "Updated labels with: "+data);

                    String[] dataArr = data.split("\\|");
                    tvWearDistance.setText(dataArr[0]);
                    tvWearSpeed.setText(dataArr[1]);
                }
            });
        }

        // STOP NOTIF
        if(messageEvent.getPath().equalsIgnoreCase(MSGAPI_MSG_STOP)){
            Log.d("WEARTEST", "Received STOP message");

            isGoing = false;
            mNotifManager.cancel(NOTIF_ID);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    private String formatInterval(final long interval)
    {
        final long hr = TimeUnit.MILLISECONDS.toHours(interval);
        final long min = TimeUnit.MILLISECONDS.toMinutes(interval - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(interval - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }
}

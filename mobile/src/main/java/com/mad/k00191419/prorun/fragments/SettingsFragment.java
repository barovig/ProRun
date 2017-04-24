package com.mad.k00191419.prorun.fragments;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.k00191419.prorun.R;
import com.mad.k00191419.prorun.db.ProRunDB;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mPrefs;

    public SettingsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setupClearClickListener();
    }

    private void setupClearClickListener() {
        Preference clearBtn = findPreference("PREF_CLEAR_HISTORY");
        clearBtn.setOnPreferenceClickListener( new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference pref){
                // build alert yes/no alert box
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.string_alert_title_clear_history))
                        .setMessage(getString(R.string.string_alert_message_clear_history))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ProRunDB db = new ProRunDB(getActivity());
                                db.clearRuns();
                                Toast.makeText(getActivity(), getString(R.string.string_alert_toast_clear_history), Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });
    }

    @Override
    public void onPause() {
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}

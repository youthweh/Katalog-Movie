package com.example.fiona.moviecatalogue5;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import static com.example.fiona.moviecatalogue5.AlarmReceiver.DAILY_RELEASE;

public class MyPreferenceFragment extends PreferenceFragmentCompat  implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String DAILY ;
    private SwitchPreference isDailyPreference;

    private String RELEASE ;
    private SwitchPreference isDailyRelease;

    private AlarmReceiver alarmReceiver;
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();

    }
    private void init() {
        DAILY = getResources().getString(R.string.daily);
        isDailyPreference = (SwitchPreference) findPreference(DAILY);

        RELEASE = getResources().getString(R.string.release);
        isDailyRelease = (SwitchPreference) findPreference(RELEASE);
    }
    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        isDailyPreference.setChecked(sh.getBoolean(DAILY, false));
        isDailyRelease.setChecked(sh.getBoolean(RELEASE, false));
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DAILY)) {
            isDailyPreference.setChecked(sharedPreferences.getBoolean(DAILY, false));
        }
        if (key.equals(RELEASE)) {
            isDailyRelease.setChecked(sharedPreferences.getBoolean(RELEASE, false));
        }
        alarmReceiver = new AlarmReceiver();

        if(isDailyPreference.isChecked()){
            alarmReceiver.setRepeatingAlarm2(getContext(), AlarmReceiver.DAILY_REMINDER, "Ayuk Cek Aplikasi Katalog Film");
            Toast.makeText(getContext(),getString(R.string.daily_reminder_info),Toast.LENGTH_SHORT).show();

        }else {
            alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.DAILY_REMINDER);

        }
        if (isDailyRelease.isChecked()){
           alarmReceiver.setReleaseAlarm2(getContext(),AlarmReceiver.DAILY_RELEASE,"Notifikasi Release Katalog Film");
        }else {
            alarmReceiver.cancelAlarm(getContext(), DAILY_RELEASE);
        }
    }

}

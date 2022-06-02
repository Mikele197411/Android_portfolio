package com.mshilkov.cool_timer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.timer_preferences);

        SharedPreferences sharedPreferences= getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen=getPreferenceScreen();
        int count=preferenceScreen.getPreferenceCount();
        for (int i=0; i<count; i++)
        {
            Preference preference=preferenceScreen.getPreference(i);
            if(!(preference instanceof CheckBoxPreference))
            {
                String value=sharedPreferences.getString(preference.getKey(), "");
                setPreferenceLabel(preference, value);
            }
        }
        Preference preference=findPreference("default_interval");
        preference.setOnPreferenceChangeListener(this);
    }
    private void setPreferenceLabel(Preference preference, String value)
    {
        if(preference instanceof ListPreference)
        {
            ListPreference listPreference=(ListPreference) preference;
            int index =listPreference.findIndexOfValue(value);
            if(index>=0)
            {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }
        else if(preference instanceof EditTextPreference)
        {
            preference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference=findPreference(s);
        if(!(preference instanceof CheckBoxPreference))
        {
            String value=sharedPreferences.getString(preference.getKey(), "");
            setPreferenceLabel(preference, value);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals("default_interval"))
        {
            String defIntervalString=(String) newValue;
            try {
                int defaultInterval=Integer.parseInt(defIntervalString);
            }
            catch (NumberFormatException ex)
            {
                Toast.makeText(getContext(), "Value is not correct", Toast.LENGTH_LONG).show();
                return  false;

            }
        }
        return true;
    }
}

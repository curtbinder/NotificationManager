package info.curtbinder.notificationmanager;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by binder on 9/16/14.
 */
public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefsFragment p = new PrefsFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, p)
                .commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.prefs);
            Preference p = findPreference(getString(R.string.username_key));
            p.setSummary(((EditTextPreference) p).getText());
            p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    Log.d("Settings", "Updated username: " + newValue.toString());
                    preference.setSummary(newValue.toString());
                    return true;
                }
            });
        }
    }
}

package info.curtbinder.notificationmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

/**
 * Created by binder on 9/12/14.
 */
public class UpdateTask extends AsyncTask<Context, Void, Void> {
    @Override
    protected Void doInBackground(Context... ctx) {
        Context c = ctx[0];
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        String username = prefs.getString(c.getString(R.string.username_key), "");
        if (username.isEmpty()) {
            // empty username
            return null;
        }
        Host host = new Host(username);
        CommTask t = new CommTask(c, host);
        t.run();
        return null;
    }
}

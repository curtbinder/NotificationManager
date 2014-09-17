package info.curtbinder.notificationmanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class Main extends Activity {

    private static final String TAG = Main.class.getSimpleName();
    private NotificationReceiver receiver;
    private IntentFilter filter;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        receiver = new NotificationReceiver();
        filter = new IntentFilter(MessageCommands.UPDATE_DISPLAY_ALERTS);
        filter.addAction(MessageCommands.RELOAD_ALERTS);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getFragmentManager().beginTransaction()
                .add(R.id.frag_container, NotificationListFragment.newInstance(null))
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
            return true;
        } else if ( id == R.id.action_refresh ) {
            // refresh the alerts
            getAlerts();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getUsername() {
        return prefs.getString(getString(R.string.username_key), "");
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = getUsername();
            if (username.isEmpty()) {
                // empty username
                return null;
            }
            Host host = new Host(username);
            CommTask t = new CommTask(getApplication().getBaseContext(), host);
            t.run();
            return null;
        }
    }

    private void getAlerts() {
        // run on background thread, not main thread
        String username = getUsername();
        if (username.isEmpty()) {
            // empty username
            Toast.makeText(this, R.string.no_username_set, Toast.LENGTH_SHORT).show();
            return;
        }
        new UpdateTask().execute();
    }

    private class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ( action.equals(MessageCommands.UPDATE_DISPLAY_ALERTS) ) {
                ArrayList<Alert> alerts = (ArrayList)intent.getParcelableArrayListExtra(MessageCommands.MSG_ALERT);
                RefreshInterface r = (RefreshInterface) getFragmentManager().findFragmentById(R.id.frag_container);
                r.refreshList(alerts);
            } else if ( action.equals(MessageCommands.RELOAD_ALERTS) ) {
                getAlerts();
            }
        }
    }
}

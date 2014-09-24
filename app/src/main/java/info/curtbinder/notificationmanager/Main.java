/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Curt Binder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package info.curtbinder.notificationmanager;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;


public class Main extends Activity {

    private static final String TAG = Main.class.getSimpleName();
    private static final String FTAG = "notifications";
    SharedPreferences prefs;
    Alert mAlert;
    int mAlertType;
    private NotificationReceiver receiver;
    private IntentFilter filter;
    private BaseApplication ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        ba = (BaseApplication) getApplication();
        createMessageReceiverAndFilter();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        FragmentManager fm = getFragmentManager();
        NotificationListFragment nlf = (NotificationListFragment) fm.findFragmentByTag(FTAG);
        if (nlf == null) {
            fm.beginTransaction()
                    .add(R.id.frag_container, NotificationListFragment.newInstance(null), FTAG)
                    .commit();
        }
        //new DownloadTriggersTask().execute();
        try {
            ba.parseTriggersResource(R.raw.specialtags, "RA//TAG");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMessageReceiverAndFilter() {
        receiver = new NotificationReceiver();
        filter = new IntentFilter(MessageCommands.UPDATE_DISPLAY_ALERTS);
        filter.addAction(MessageCommands.RELOAD_ALERTS);
        filter.addAction(MessageCommands.ADD_ALERT);
        filter.addAction(MessageCommands.UPDATE_ALERT);
        filter.addAction(MessageCommands.DELETE_ALERT);
        filter.addAction(MessageCommands.SERVER_RESPONSE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        if ( prefs.getBoolean(getString(R.string.autoload_key), false) ) {
            getAlerts();
        }
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
        } else if (id == R.id.action_refresh) {
            // refresh the alerts
            getAlerts();
            return true;
        } else if (id == R.id.action_about) {
            DialogAbout a = DialogAbout.newInstance();
            a.show(getFragmentManager(), "dlgabout");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getUsername() {
        return prefs.getString(getString(R.string.username_key), "");
    }

    private void getAlerts() {
        // run on background thread, not main thread
        String username = getUsername();
        if (username.isEmpty()) {
            // empty username
            Toast.makeText(this, R.string.no_username_set, Toast.LENGTH_SHORT).show();
            return;
        }
        new GetAlertsTask().execute();
    }

    private class GetAlertsTask extends AsyncTask<Void, Void, Void> {
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

    private class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MessageCommands.UPDATE_DISPLAY_ALERTS)) {
                ArrayList<Alert> alerts = (ArrayList) intent.getParcelableArrayListExtra(MessageCommands.MSG_ALERTS);
                RefreshInterface r = (RefreshInterface) getFragmentManager().findFragmentById(R.id.frag_container);
                r.refreshList(alerts);
            } else if (action.equals(MessageCommands.RELOAD_ALERTS)) {
                getAlerts();
            } else if (action.equals(MessageCommands.SERVER_RESPONSE)) {
                String response = intent.getStringExtra(MessageCommands.MSG_RESPONSE);
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
            } else if (action.equals(MessageCommands.ADD_ALERT) ||
                    action.equals(MessageCommands.UPDATE_ALERT) ||
                    action.equals(MessageCommands.DELETE_ALERT)) {
                mAlert = (Alert) intent.getParcelableExtra(MessageCommands.MSG_ALERT_DATA);
                mAlertType = intent.getIntExtra(MessageCommands.MSG_ALERT_TYPE, Host.ADD);
                new UpdateAlertTask().execute();
            }
        }
    }

    private class UpdateAlertTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setProgressBarIndeterminateVisibility(false);
            // reload the alerts
            getAlerts();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String username = getUsername();
            if (username.isEmpty()) {
                // empty username
                return null;
            }
            Host host = new Host(username);
            // set the host type
            host.setType(mAlertType);
            host.setAlert(mAlert);
            CommTask t = new CommTask(getApplication().getBaseContext(), host);
            t.run();
            return null;
        }
    }

    /*
    private class DownloadTriggersTask extends AsyncTask<Void, Void, Void> {
        // TODO class fails to download full XML response
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
            URL url = null;
            try {
                url = new URL(Host.TRIGGERS_URL);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                ba.parseTriggersString(body.charStream());
                response.body().close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    */
}

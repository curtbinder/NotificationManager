package info.curtbinder.notificationmanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class Main extends Activity implements Button.OnClickListener {

    private static final String TAG = Main.class.getSimpleName();
    private NotificationReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new NotificationReceiver();
        filter = new IntentFilter(MessageCommands.GET_ALERTS);
        Button b = (Button) findViewById(R.id.button_add);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.button_add_all);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.button_cancel);
        b.setOnClickListener(this);
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
            return true;
        } else if ( id == R.id.action_refresh ) {
            // refresh the alerts
            getAlerts();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAlerts() {
        // run on background thread, not main thread
        new UpdateTask().execute(getBaseContext());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.button_add:
                break;
            case R.id.button_add_all:
                break;
            case R.id.button_cancel:
                break;
        }
    }

    private class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ( action.equals(MessageCommands.GET_ALERTS) ) {
                ArrayList<Alert> alerts = (ArrayList)intent.getParcelableArrayListExtra(MessageCommands.MSG_ALERT);
//                for (Alert a : alerts) {
//                    Log.d(TAG, a.toString());
//                }
                RefreshInterface r = (RefreshInterface) getFragmentManager().findFragmentById(R.id.frag_container);
                r.refreshList(alerts);
            }
        }
    }
}

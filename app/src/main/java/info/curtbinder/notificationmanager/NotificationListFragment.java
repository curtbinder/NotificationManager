package info.curtbinder.notificationmanager;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by binder on 9/13/14.
 */
public class NotificationListFragment extends ListFragment
        implements RefreshInterface {

    private static final String TAG = "List";

    private ArrayList<Alert> alerts;

    public static NotificationListFragment newInstance ( ArrayList<Alert> alerts ) {
        NotificationListFragment f = new NotificationListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MessageCommands.MSG_ALERT, alerts);
        f.setArguments(args);
        return f;
    }

    public NotificationListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_alerts_list, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MessageCommands.MSG_ALERT, alerts);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ( savedInstanceState != null ) {
            alerts = savedInstanceState.getParcelableArrayList(MessageCommands.MSG_ALERT);
        } else {
            alerts = getArguments().getParcelableArrayList(MessageCommands.MSG_ALERT);
        }
        // set the list adapter for our list
        NotificationListAdapter adapter = new NotificationListAdapter(getActivity().getBaseContext(), alerts);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }

    @Override
    public void refreshList(ArrayList<Alert> alerts) {
        this.alerts = alerts;
        NotificationListAdapter adapter = (NotificationListAdapter) getListAdapter();
        adapter.setAlerts(alerts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.row_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_item:
                Log.d(TAG, "Edit item");
                reloadAlerts();
                return true;
            case R.id.delete_item:
                Log.d(TAG, "Delete item");
                reloadAlerts();
                return true;
        }
        return false;
    }

    private void reloadAlerts() {
        Intent i = new Intent(MessageCommands.RELOAD_ALERTS);
        getActivity().sendBroadcast(i);
    }
}

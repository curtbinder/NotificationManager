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
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * Created by binder on 9/13/14.
 */
public class NotificationListFragment extends ListFragment
        implements RefreshInterface {

    private static final String TAG = "List";

    private ArrayList<Alert> alerts;

    public NotificationListFragment() {
    }

    public static NotificationListFragment newInstance(ArrayList<Alert> alerts) {
        NotificationListFragment f = new NotificationListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MessageCommands.MSG_ALERTS, alerts);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_alerts_list, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MessageCommands.MSG_ALERTS, alerts);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            alerts = savedInstanceState.getParcelableArrayList(MessageCommands.MSG_ALERTS);
        } else {
            alerts = getArguments().getParcelableArrayList(MessageCommands.MSG_ALERTS);
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Alert a = alerts.get(info.position);
        switch (item.getItemId()) {
            case R.id.edit_item:
                DialogAddEditTrigger dlg = DialogAddEditTrigger.newInstance(a);
                dlg.setTargetFragment(this, DialogAddEditTrigger.DLG_ADDEDIT_CALL);
                dlg.show(getFragmentManager(), "dlg");
                return true;
            case R.id.delete_item:
                deleteNotification(a);
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                DialogAddEditTrigger dlg = DialogAddEditTrigger.newInstance();
                dlg.setTargetFragment(this, DialogAddEditTrigger.DLG_ADDEDIT_CALL);
                dlg.show(getFragmentManager(), "dlg");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == DialogAddEditTrigger.DLG_ADDEDIT_CALL) &&
                (resultCode == Activity.RESULT_OK)) {
            // User pressed the Add/Update button
            if (data != null) {
                Alert a = data.getParcelableExtra(DialogAddEditTrigger.ALERT);
                int id = a.getId();
                if (id == -1) {
                    addNotification(a);
                } else {
                    updateNotification(a);
                }
            }
        }
    }

    private void sendNotification(String action, int type, Alert a) {
        Intent i = new Intent(action);
        i.putExtra(MessageCommands.MSG_ALERT_DATA, a);
        i.putExtra(MessageCommands.MSG_ALERT_TYPE, type);
        getActivity().sendBroadcast(i);
    }

    private void addNotification(Alert a) {
        sendNotification(MessageCommands.ADD_ALERT, Host.ADD, a);
    }

    private void updateNotification(Alert a) {
        sendNotification(MessageCommands.UPDATE_ALERT, Host.UPDATE, a);
    }

    private void deleteNotification(Alert a) {
        sendNotification(MessageCommands.DELETE_ALERT, Host.DELETE, a);
    }
}

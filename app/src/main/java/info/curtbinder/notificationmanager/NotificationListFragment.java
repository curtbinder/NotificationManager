package info.curtbinder.notificationmanager;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by binder on 9/13/14.
 */
public class NotificationListFragment extends ListFragment implements RefreshInterface {

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // set the list adapter for our list
        ArrayList<Alert> alerts = getArguments().getParcelableArrayList(MessageCommands.MSG_ALERT);
        NotificationListAdapter adapter = new NotificationListAdapter(getActivity().getBaseContext(), alerts);
        setListAdapter(adapter);
    }

    @Override
    public void refreshList(ArrayList<Alert> alerts) {
        NotificationListAdapter adapter = (NotificationListAdapter) getListAdapter();
        adapter.setAlerts(alerts);
        adapter.notifyDataSetChanged();
    }
}

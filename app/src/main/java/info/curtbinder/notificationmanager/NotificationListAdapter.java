package info.curtbinder.notificationmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by binder on 9/13/14.
 */
public class NotificationListAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<Alert> alerts;

    public NotificationListAdapter(Context ctx, ArrayList<Alert> alerts) {
        context = ctx;
        this.alerts = alerts;
    }

    public void setAlerts(ArrayList<Alert> alerts) {
        this.alerts = alerts;
    }

    @Override
    public int getCount() {
        if (alerts == null) return 0;
        return alerts.size();
    }

    @Override
    public Object getItem(int i) {
        return alerts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // TODO inflate the layout, set the items/info, return the row
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_notification_item, viewGroup, false);
        TextView tv1 = (TextView) row.findViewById(R.id.textName);
        TextView tv2 = (TextView) row.findViewById(R.id.textDescription);
        TextView tv3 = (TextView) row.findViewById(R.id.textComparison);
        Alert a = alerts.get(i);
        String name = a.getAlertName();
        String pName = a.getParamName();
        if (name.equals("null")) name = pName;
        tv1.setText(name);
        String description = a.getAlertDescription();
        if (description.equals("null")) description = a.getParamDescription();
        tv2.setText(description);
        // ParamName Comparision Value
        int c = a.getComparison();
        int v = a.getValue();
        String s = String.format("%s %s %d", pName, Alert.getComparisonString(c), v);
        tv3.setText(s);
        return row;
    }
}

package info.curtbinder.notificationmanager;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binder on 9/7/14.
 */
public interface RefreshInterface {
    public void refreshList(ArrayList<Alert> alerts);
}

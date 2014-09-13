package info.curtbinder.notificationmanager;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by binder on 9/12/14.
 */
public class UpdateTask extends AsyncTask<Context, Void, Void> {
    @Override
    protected Void doInBackground(Context... ctx) {
        Host host = new Host("reefangel");
        CommTask t = new CommTask(ctx[0], host);
        t.run();
        return null;
    }
}

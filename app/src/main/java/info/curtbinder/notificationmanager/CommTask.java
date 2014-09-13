package info.curtbinder.notificationmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by binder on 9/11/14.
 */
public class CommTask implements Runnable {

    private final Host host;
    private final Context ctx;

    public CommTask(Context ctx, Host h) {
        this.ctx = ctx;
        host = h;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(host.toString());
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            XMLNotificationHandler xml = new XMLNotificationHandler();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            XMLReader xr = spf.newSAXParser().getXMLReader();
            xr.setContentHandler(xml);
//            String s = response.body().string();
//            Log.d("XML", s);
//            xr.parse(new InputSource(new StringReader(s)));
            xr.parse(new InputSource(new StringReader(response.body().string())));
            response.body().close();
            // send message to main thread with the data
//            List<Alert> alerts = xml.getAlerts();
//            for (Alert a : alerts) {
//                Log.d("COMM", "Alert: " + a.getAlertName() + ", " + a.getAlertDescription());
//            }

            // TODO figure out how to pass the alerts to the main activity
            Intent i = new Intent(MessageCommands.GET_ALERTS);
            ArrayList<Alert> alerts = xml.getAlerts();
            i.putParcelableArrayListExtra("ALERTS", (ArrayList) alerts);
            Log.d("COMM", "Send Broadcast");
            ctx.sendBroadcast(i);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

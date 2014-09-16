package info.curtbinder.notificationmanager;

import android.content.Context;
import android.content.Intent;

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
            Intent i = new Intent(MessageCommands.UPDATE_DISPLAY_ALERTS);
            i.putParcelableArrayListExtra("ALERTS", (ArrayList)xml.getAlerts());
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

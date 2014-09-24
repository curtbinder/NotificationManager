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
            String r = response.body().string();
            Intent i = new Intent();
            if ( r.equals("Done") ) {
                // we got a positive response from our update
                i.setAction(MessageCommands.SERVER_RESPONSE);
                String msg;
                int p1Id;
                String fmt = ctx.getString(R.string.success_msg);
                switch(host.getType()) {
                    default:
                    case Host.ADD:
                        p1Id = R.string.added;
                        break;
                    case Host.UPDATE:
                        p1Id = R.string.updated;
                        break;
                    case Host.DELETE:
                        p1Id = R.string.deleted;
                        break;
                }
                msg = String.format(fmt, ctx.getString(p1Id), host.getAlertName());
                i.putExtra(MessageCommands.MSG_RESPONSE, msg);
            } else {
                xr.parse(new InputSource(new StringReader(r)));
                i.setAction(MessageCommands.UPDATE_DISPLAY_ALERTS);
                i.putParcelableArrayListExtra("ALERTS", (ArrayList) xml.getAlerts());
            }
            response.body().close();
            // send message to main thread with the data
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

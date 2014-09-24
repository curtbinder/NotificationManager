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

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by binder on 9/10/14.
 */
public class XMLNotificationHandler extends DefaultHandler {

    private static String TAG = XMLNotificationTags.class.getSimpleName();
    private String currentElement;
    private ArrayList<Alert> alerts;
    private Alert currentAlert;

    public XMLNotificationHandler() {
        currentElement = "";
        alerts = new ArrayList<Alert>();
    }

    public ArrayList<Alert> getAlerts() {
        return this.alerts;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String tag;
        if (!qName.equals("")) {
            tag = qName;
        } else {
            tag = localName;
        }
        if (tag.equals(XMLNotificationTags.ALERTS)) {
            currentAlert = new Alert();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String tag;
        if (!qName.equals("")) {
            tag = qName;
        } else {
            tag = localName;
        }
        if (tag.equals(XMLNotificationTags.ALERTS)) {
            alerts.add(currentAlert);
            currentAlert = null;
        } else {
            processNotification(tag);
        }
        currentElement = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        currentElement += s;
    }

    private void processNotification(String tag) {
        if (tag.equals(XMLNotificationTags.ID)) {
            currentAlert.setId(Integer.parseInt(currentElement));
        } else if (tag.equals(XMLNotificationTags.NAME)) {
            currentAlert.setParamName(currentElement);
        } else if (tag.equals(XMLNotificationTags.DESCRIPTION)) {
            currentAlert.setParamDescription(currentElement);
        } else if (tag.equals(XMLNotificationTags.COMPARISON)) {
            currentAlert.setComparison(Integer.parseInt(currentElement));
        } else if (tag.equals(XMLNotificationTags.VALUE)) {
            currentAlert.setValue(Integer.parseInt(currentElement));
        } else if (tag.equals(XMLNotificationTags.LASTALERT)) {
            currentAlert.setLastAlert(currentElement);
        } else if (tag.equals(XMLNotificationTags.ALERT_NAME)) {
            currentAlert.setAlertName(currentElement);
        } else if (tag.equals(XMLNotificationTags.ALERT_DESCRIPTION)) {
            currentAlert.setAlertDescription(currentElement);
        } else if (tag.equals(XMLNotificationTags.RA)) {
        } else {
            Log.d(TAG, "Unknown tag: " + tag + ": " + currentElement);
        }
    }
}

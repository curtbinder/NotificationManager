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

import android.app.Application;
import android.preference.PreferenceManager;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by binder on 9/24/14.
 */
public class BaseApplication extends Application {

    public ArrayList<String> mTriggerDescription = new ArrayList<String>();
    public Map<String, Integer> mapTriggers = new LinkedHashMap<String, Integer>();

    /*
    public void parseTriggersString(Reader r) throws Exception {
        InputSource inputSource = new InputSource(r);
        XPath xpath = XPathFactory.newInstance().newXPath();
        String exp = "ra//tag";
        NodeList nodes = (NodeList) xpath.evaluate(exp, inputSource, XPathConstants.NODESET);
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                NodeList children = n.getChildNodes();
                String desc = "";
                String name = "";
                int id = 0;
                for (int j = 0; j < children.getLength(); j++) {
                    Node nc = children.item(j);
                    String localName = nc.getLocalName();
                    if (localName == null) continue;
                    if (localName.equals("description")) {
                        desc = nc.getTextContent();
                    } else if (localName.equals("id")) {
                        id = Integer.parseInt(nc.getTextContent());
                    } else if (localName.equals("name")) {
                        name = nc.getTextContent();
                    }
                }
                mTriggerDescription.add(desc);
                mapTriggers.put(name, id);
            }
        }
    }
    */

    public void parseTriggersResource(int resourceId, String tagExpression) throws Exception {
        InputSource inputSource = new InputSource(getResources().openRawResource(resourceId));
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xpath.evaluate(tagExpression, inputSource, XPathConstants.NODESET);
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                NodeList children = n.getChildNodes();
                String desc = "";
                String name = "";
                int id = 0;
                for (int j = 0; j < children.getLength(); j++) {
                    Node nc = children.item(j);
                    String localName = nc.getLocalName();
                    if (localName == null) continue;
                    if (localName.equals("DESCRIPTION")) {
                        desc = nc.getTextContent();
                    } else if (localName.equals("ID")) {
                        id = Integer.parseInt(nc.getTextContent());
                    } else if (localName.equals("NAME")) {
                        name = nc.getTextContent();
                    }
                }
                mTriggerDescription.add(desc);
                mapTriggers.put(name, id);
            }
        }
    }

    public int findKeyPosition(String key) {
        // Get the position based on the key
        Iterator<String> i = mapTriggers.keySet().iterator();
        int count = 0;
        boolean fFound = false;
        while (i.hasNext()) {
            String iKey = i.next();
            //Log.d(TAG, count + ": " + iKey);
            if (key.equals(iKey)) {
                fFound = true;
                break;
            }
            count++;
        }
        return (fFound) ? count : 0;
    }

    public String findPositionKey(int pos) {
        // Get the key based on the position in the list
        Iterator<String> i = mapTriggers.keySet().iterator();
        int count = 0;
        String sValue = "";
        while (i.hasNext()) {
            String iKey = i.next();
            //Log.d(TAG, count + ": " + iKey);
            if (pos == count) {
                sValue = iKey;
                break;
            }
            count++;
        }
        return sValue;
    }

    public int getParamId(String key) {
        return mapTriggers.get(key).intValue();
    }
}

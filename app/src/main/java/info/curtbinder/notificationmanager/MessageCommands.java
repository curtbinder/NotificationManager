/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Curt Binder
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

/**
 * Created by binder on 9/13/14.
 */
public class MessageCommands {
    public static final String PACKAGE_BASE = MessageCommands.class.getPackage().getName();

    public static final String UPDATE_DISPLAY_ALERTS = PACKAGE_BASE + ".UPDATE_DISPLAY_ALERTS";
    public static final String RELOAD_ALERTS = PACKAGE_BASE + ".RELOAD_ALERTS";
    public static final String ADD_ALERT = PACKAGE_BASE + ".ADD_ALERT";
    public static final String UPDATE_ALERT = PACKAGE_BASE + ".UPDATE_ALERT";
    public static final String DELETE_ALERT = PACKAGE_BASE + ".DELETE_ALERT";
    public static final String SERVER_RESPONSE = PACKAGE_BASE + ".SERVER_RESPONSE";
    public static final String MSG_ALERTS = "ALERTS";
    public static final String MSG_ALERT_DATA = "ALERT";
    public static final String MSG_ALERT_TYPE = "ALERT_TYPE";
    public static final String MSG_RESPONSE = "RESPONSE";
}

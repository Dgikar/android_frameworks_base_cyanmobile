/**
 * Copyright (c) 2007, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 
package com.android.internal.statusbar;

import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.statusbar.StatusBarNotification;

/** @hide */
oneway interface IStatusBar
{
    void setIcon(int index, in StatusBarIcon icon);
    void removeIcon(int index);
    void addNotification(IBinder key, in StatusBarNotification notification);
    void updateNotification(IBinder key, in StatusBarNotification notification);
    void removeNotification(IBinder key);
    void disable(int state);
    void animateExpand();
    void animateCollapse();
    void setIMEVisible(boolean visible);
    void showNaviBar(boolean visible);
    void toggleQwikWidgets();
}


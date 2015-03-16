/*
 * Copyright (c) 2015. Sevan Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sevanjoe.library.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.os.Build;
import android.os.Process;

import com.sevanjoe.library.tools.CrashHandler;
import com.sevanjoe.library.utils.LogUtil;

import java.util.List;

/**
 * Created by Sevan Joe on 3/16/2015.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler.getInstance().init(this);
    }

    protected boolean checkLaunch(String activityName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // use traditional way
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfoList = activityManager.getRunningTasks(1);
            ComponentName componentName = runningTaskInfoList.get(0).topActivity;
            if (!activityName.equals(componentName.getClassName())) {
                LogUtil.d("prevent restart after application crash");
                Process.killProcess(Process.myPid());
                System.exit(0);
                return false;
            }
        }
        return true;
    }
}

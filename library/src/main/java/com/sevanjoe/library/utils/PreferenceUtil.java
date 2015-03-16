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

package com.sevanjoe.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sevan Joe on 3/16/2015.
 */
public class PreferenceUtil {
    private static SharedPreferences sharedPreferences;

    public static void init(Context context, String fileName) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static boolean getBooleanForKey(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static int getIntForKey(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static float getFloatForKey(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static long getLongForKey(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static String getStringForKey(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void setBooleanForKey(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setIntForKey(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setFloatForKey(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void setLongForKey(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void setStringForKey(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

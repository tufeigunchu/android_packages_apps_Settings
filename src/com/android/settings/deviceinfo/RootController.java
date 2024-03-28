/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.DeviceConfig;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.TogglePreferenceController;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/** Controller for location indicators toggle. */
public class RootController extends TogglePreferenceController {

    public RootController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public boolean isChecked() {
        return new File("/system/bin/su").exists();
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        String cmd;
        if(isChecked){
            cmd = "ROOT";
        } else {
            cmd = "TOOR";
        }
        Log.e("GGGGG", "cmd is"+cmd);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try (DatagramSocket socket = new DatagramSocket()) {
                    InetAddress destinationAddress = InetAddress.getByName("localhost");
                    byte[] data = cmd.getBytes();
        
                    DatagramPacket packet = new DatagramPacket(
                        data, data.length, destinationAddress, 25252);
        
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start(); 
        
        return true;
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return R.string.menu_key_location;
    }
}

/*
 * Copyright (c) 2017  XngPro <xngpro@outlook.com>
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

package com.xngpro.upload2firim;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by XngPro (xngpro@outlook.com) on 2017/11/23.
 */
public class SignaturesUtil {

    public static String getMD5(Context context) {
        return getMD5(context, BuildConfig.APPLICATION_ID);
    }

    public static String getMD5(Context context, String packageName) {
        String rst = null;
        if (context != null) {
            try {
                PackageInfo pi = context.getPackageManager()
                        .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                rst = bytes2HexString(encryptMD5(pi.signatures[0].toByteArray()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        return rst;
    }

    private static byte[] encryptMD5(final byte[] data) {
        return hash(data, "MD5");
    }

    private static byte[] hash(final byte[] data, final String algorithm) {
        if (data == null || data.length == 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final char HEX_DIGITS[] = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len == 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >>> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}

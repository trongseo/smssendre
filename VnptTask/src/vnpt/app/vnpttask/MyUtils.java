/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 *
 * Copyright 2011 - 2015 Aris-vn, Inc. All rights reserved.
 * Author: Maxwell
 * Location: BestAct - com.arisvn.bestact.utils - BestActUtils.java
 * Date create: 10:51:00 AM - Mar 17, 2015 - 2015
 *
 *
 */
package vnpt.app.vnpttask;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;





import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * The Class BestActUtils.
 */
public class MyUtils {

    /** The Constant TAG. */
    public static final String TAG = MyUtils.class.getSimpleName();

    /**
     * Gets the device id.
     *
     * @param context
     *
     * @return the device id
     */
    public static String getDeviceId(Context context) {
        return generateMd5(Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID));
    }

    /**
     * Draw underline text.
     *
     * @param textview
     *            the textview
     */
    public static void drawUnderlineText(TextView textview) {
        textview.setPaintFlags(textview.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * Strike through text.
     *
     * @param textview
     *            the textview
     * @param enable
     *            the enable
     */
    public static void strikeThroughText(TextView textview, boolean enable) {
        if (enable) {
            textview.setPaintFlags(textview.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textview.setPaintFlags(0);
        }
    }

    /**
     * Min leght valid.
     *
     * @param pass
     *            the pass
     * @return true, if successful
     */
    public static boolean minLeghtValid(String pass) {
        return pass.trim().length() > 6;
    }

    /**
     * Confirm pass.
     *
     * @param pass
     *            the pass
     * @param confirmPass
     *            the confirm pass
     * @return true, if successful
     */
    public static boolean confirmPass(String pass, String confirmPass) {
        return pass.equals(confirmPass);
    }

    /**
     * Email valid.
     *
     * @param email
     *            the email
     * @return true, if successful
     */
    public static boolean emailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
                Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }

    /**
     * Show toast.
     *
     * @param msg
     *            the msg
     * @param context
     *            the context
     */
    public static void showToast(int msg, Context context) {
        showToast(context.getString(msg), context);
    }

    /**
     * Show toast.
     *
     * @param msg
     *            the msg
     * @param context
     *            the context
     */
    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Generate md5.
     *
     * @param deviceId
     *            the device id
     * @return the string
     */
    public static String generateMd5(String deviceId) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(deviceId.getBytes());
            byte[] update = digester.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < update.length; i++) {
                hexString.append(Integer
                        .toHexString((update[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            Log.i("MD5", hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    
    /**
     * Checks if is online.
     *
     * @param context
     *            the context
     * @return true, if is online
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

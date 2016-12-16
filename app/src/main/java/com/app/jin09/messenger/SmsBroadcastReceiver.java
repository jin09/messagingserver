package com.app.jin09.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by gautam on 16-11-2016.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String sender = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                sender += address;
                smsMessageStr += smsBody ;
            }
            Toast.makeText(context,sender + "  " + smsMessageStr, Toast.LENGTH_SHORT).show();
            Log.d(TAG,smsMessageStr + "  " + sender);
            String[] info = smsMessageStr.split(",");
            Log.d(TAG,info.toString());
            if(info[0].equals("108")){
                Log.d(TAG,"initial number matched");
                String type = info[1];
                String injured = info[2];
                String latitude = info[3];
                String longitude = info[4];
                String name = info[5];
                String number = info[6];
                String smsnumber = sender;
                new MakeRequest().execute(type,injured, latitude, longitude, name, number, smsnumber);
                Log.d(TAG, "making request in progress");
                SmsManager smsManager = SmsManager.getDefault();
                String smsToSend = "Thank You for contacting 108 Emergency Services. " +
                        "We will revert back to you as soon as possible";
                smsManager.sendTextMessage(smsnumber,null,smsToSend,null,null);
            }
        }
    }
}

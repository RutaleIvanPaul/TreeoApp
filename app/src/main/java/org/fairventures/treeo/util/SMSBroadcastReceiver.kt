package org.fairventures.treeo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SMSBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.getAction()) {
            val extras: Bundle? = intent.getExtras()
            val status: Status? = extras?.get(SmsRetriever.EXTRA_STATUS) as Status?
            var  message: String?
            when (status?.getStatusCode()) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    message = extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String?
                    Log.d("MESSAGE", message!!)
                }
                CommonStatusCodes.TIMEOUT -> {
                    Log.d("MESSAGE", "Message Wait timed out.")
                }
            }
        }
    }
}

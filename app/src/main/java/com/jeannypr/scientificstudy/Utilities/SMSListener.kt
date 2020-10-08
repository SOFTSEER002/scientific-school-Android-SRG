package com.jeannypr.scientificstudy.Utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import com.google.android.gms.common.internal.service.Common
import com.jeannypr.scientificstudy.Login.navigator.OTPListener


/**
 *Created by Kannu
 **/
class SMSListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) { // this function is trigged when each time a new SMS is received on device.
        val data: Bundle = intent.extras
        var pdus = arrayOfNulls<Any>(0)

        pdus = data.get("pdus") as Array<Any?>
        for (pdu in pdus) { // loop through and pick up the SMS of interest
            val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray?)
            // your custom logic to filter and extract the OTP from relevant SMS - with regex or any other way.
            mListener?.onOTPReceived("Extracted OTP")
            break
        }
    }

    companion object {
        // this listener will do the magic of throwing the extracted OTP to all the bound views.
        private var mListener: OTPListener? = null

        fun bindListener(listener: OTPListener?) {
            mListener = listener
        }

        fun unbindListener() {
            mListener = null
        }
    }
}
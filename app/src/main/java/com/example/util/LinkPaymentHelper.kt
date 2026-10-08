package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object LinkPaymentHelper {

    fun launchUpiPayment(
        context: Context,
        upiId: String,
        payeeName: String = "AuraQuest Admin",
        amount: Double,
        note: String
    ) {
        try {
            val formattedAmount = String.format("%.2f", amount)
            val uri = Uri.parse(
                "upi://pay?pa=$upiId&pn=${Uri.encode(payeeName)}&am=$formattedAmount&cu=INR&tn=${Uri.encode(note)}"
            )
            val intent = Intent(Intent.ACTION_VIEW, uri)
            val chooser = Intent.createChooser(intent, "Pay ₹$formattedAmount via UPI (GPay/PhonePe/Paytm)")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "No UPI app found on this device! Please install GPay/PhonePe/Paytm.", Toast.LENGTH_LONG).show()
        }
    }

    fun launchCardWebPayment(
        context: Context,
        webPaymentUrl: String,
        amount: Double,
        itemTitle: String
    ) {
        try {
            val fullUrl = if (webPaymentUrl.contains("?")) {
                "$webPaymentUrl&amount=${amount.toInt()}&item=${Uri.encode(itemTitle)}"
            } else {
                "$webPaymentUrl?amount=${amount.toInt()}&item=${Uri.encode(itemTitle)}"
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open payment page: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openExternalLink(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

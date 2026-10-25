package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object WhatsAppHelper {

    const val PHONE_NUMBER = "+923135526057"
    const val CLEAN_NUMBER = "923135526057"

    fun openWhatsApp(context: Context, message: String) {
        try {
            val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://wa.me/$CLEAN_NUMBER?text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    fun getGeneralInquiryMessage(): String {
        return "Hello Hasnat Khan! I am interested in your creative digital services. Please share your service packages and pricing."
    }

    fun getServiceInquiryMessage(serviceTitle: String, startingPrice: Long): String {
        return "Hello Hasnat Khan! I would like to inquire about your \"$serviceTitle\" service (Starting from PKR $startingPrice). Could you please share more details?"
    }

    fun getPackageInquiryMessage(serviceTitle: String, packageName: String, price: Long): String {
        return "Hello Hasnat Khan! I am interested in the \"$packageName\" package for \"$serviceTitle\" (PKR $price). What are the next steps to start?"
    }

    fun getBookingFollowUpMessage(bookingRef: String, serviceTitle: String, packageName: String, projectTitle: String): String {
        return "Hello Hasnat Khan! I have submitted a booking with Reference: [$bookingRef].\n" +
                "• Service: $serviceTitle ($packageName Package)\n" +
                "• Project: $projectTitle\n" +
                "Please confirm receipt and let me know when work begins. Thank you!"
    }

    fun getQuotationInquiryMessage(quoteId: String, serviceType: String, budget: String, deadline: String): String {
        return "Hello Hasnat Khan! I submitted a Custom Quotation Request [Ref: $quoteId].\n" +
                "• Service: $serviceType\n" +
                "• Budget: $budget\n" +
                "• Deadline: $deadline\n" +
                "I would like to discuss project specifications directly with you."
    }

    fun getStatusUpdateMessage(bookingRef: String, currentStatus: String): String {
        return "Hello Hasnat Khan! I am inquiring about the latest progress of my project [Ref: $bookingRef] currently marked as \"$currentStatus\". Could you please share a quick update?"
    }
}

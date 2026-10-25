package com.example

import com.example.data.DefaultData
import com.example.util.WhatsAppHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AgencyUnitTest {

    @Test
    fun testDefaultServicesCount() {
        assertEquals(10, DefaultData.defaultServices.size)
    }

    @Test
    fun testWhatsAppHelperMessages() {
        val generalMsg = WhatsAppHelper.getGeneralInquiryMessage()
        assertTrue(generalMsg.contains("Hasnat Khan"))
        assertTrue(generalMsg.contains("services"))

        val serviceMsg = WhatsAppHelper.getServiceInquiryMessage("Logo Design", 4500L)
        assertTrue(serviceMsg.contains("Logo Design"))
        assertTrue(serviceMsg.contains("4500"))

        val bookingMsg = WhatsAppHelper.getBookingFollowUpMessage("HK-2026-1234", "Video Editing", "Standard", "Promo Commercial")
        assertTrue(bookingMsg.contains("HK-2026-1234"))
        assertTrue(bookingMsg.contains("Video Editing"))
    }
}

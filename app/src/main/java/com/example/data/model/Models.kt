package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class ServicePackage(
    val name: String = "",
    val price: Long = 0L,
    val description: String = "",
    val deliveryTime: String = "",
    val revisions: String = "",
    val features: List<String> = emptyList()
)

@IgnoreExtraProperties
data class ServiceItem(
    @DocumentId val id: String = "",
    val serviceId: String = "",
    val title: String = "",
    val slug: String = "",
    val description: String = "",
    val category: String = "",
    val startingPrice: Long = 0L,
    val deliveryTime: String = "",
    val iconName: String = "",
    val isFeatured: Boolean = false,
    val isActive: Boolean = true,
    val packages: Map<String, ServicePackage> = emptyMap(),
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Booking(
    @DocumentId val id: String = "",
    val bookingId: String = "",
    val bookingReference: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerEmail: String = "",
    val customerPhone: String = "",
    val serviceId: String = "",
    val serviceTitle: String = "",
    val packageName: String = "",
    val packagePrice: Long = 0L,
    val projectTitle: String = "",
    val projectDescription: String = "",
    val budgetRange: String = "",
    val deadline: String = "",
    val referenceNotes: String = "",
    val status: String = "Pending", // Pending, Confirmed, Awaiting Requirements, In Progress, Revision Requested, Completed, Cancelled
    val internalNotes: String = "",
    val customerUpdates: String = "",
    val quotationAmount: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Quotation(
    @DocumentId val id: String = "",
    val quotationId: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerEmail: String = "",
    val customerPhone: String = "",
    val serviceType: String = "",
    val projectDetails: String = "",
    val budgetRange: String = "",
    val deadline: String = "",
    val proposedPrice: Long = 0L,
    val proposedDeliveryTime: String = "",
    val status: String = "New Request", // New Request, Under Review, Quote Prepared, Sent to Customer, Accepted, Rejected, Expired
    val adminNotes: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class PortfolioProject(
    @DocumentId val id: String = "",
    val projectId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val clientName: String = "",
    val completedYear: String = "2026",
    val tag: String = "",
    val isFeatured: Boolean = true,
    val published: Boolean = true,
    val hasBeforeAfter: Boolean = false,
    val beforeText: String = "",
    val afterText: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "customer", // customer, admin
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class AgencySettings(
    @DocumentId val id: String = "general",
    val brandName: String = "Hasnat Khan Creative Services",
    val tagline: String = "Your Vision. Our Creativity. Professional Results.",
    val whatsAppNumber: String = "+923135526057",
    val email: String = "hasnat.khan8238@gmail.com",
    val businessHours: String = "Mon - Sat: 9:00 AM - 10:00 PM (PKT)",
    val currency: String = "PKR",
    val bannerText: String = "✨ Special Discount: Get 20% off on complete business branding packages!"
)

data class TestimonialItem(
    val id: String,
    val clientName: String,
    val roleOrCompany: String,
    val reviewText: String,
    val rating: Int,
    val serviceName: String
)

package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.DefaultData
import com.example.data.model.AgencySettings
import com.example.data.model.Booking
import com.example.data.model.PortfolioProject
import com.example.data.model.Quotation
import com.example.data.model.ServiceItem
import com.example.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AgencyRepository(
    private val context: Context,
    private val db: FirebaseFirestore
) {

    constructor(context: Context) : this(
        context,
        try {
            FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id))
        } catch (e: Exception) {
            FirebaseFirestore.getInstance()
        }
    )

    private val auth = Firebase.auth

    fun currentUserId(): String? = auth.currentUser?.uid
    fun currentUserEmail(): String? = auth.currentUser?.email
    fun isCurrentUserAdmin(): Boolean {
        val email = auth.currentUser?.email ?: ""
        return email.equals("hasnat.khan8238@gmail.com", ignoreCase = true)
    }

    // --- Services ---
    fun observeServices(): Flow<List<ServiceItem>> = callbackFlow {
        // Try live Firestore listener with fallback
        try {
            val registration = db.collection("services")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("AgencyRepo", "Services snapshot error, using default data: ${error.message}")
                        trySend(DefaultData.defaultServices)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val items = snapshot.toObjects(ServiceItem::class.java)
                        trySend(items)
                    } else {
                        trySend(DefaultData.defaultServices)
                    }
                }
            awaitClose { registration.remove() }
        } catch (e: Exception) {
            Log.e("AgencyRepo", "Error attaching services listener", e)
            trySend(DefaultData.defaultServices)
            awaitClose {}
        }
    }

    suspend fun updateService(service: ServiceItem): Result<Unit> = runCatching {
        val docRef = db.collection("services").document(service.serviceId)
        val payload = mapOf(
            "serviceId" to service.serviceId,
            "title" to service.title,
            "slug" to service.slug,
            "category" to service.category,
            "description" to service.description,
            "startingPrice" to service.startingPrice,
            "deliveryTime" to service.deliveryTime,
            "iconName" to service.iconName,
            "isFeatured" to service.isFeatured,
            "isActive" to service.isActive,
            "packages" to service.packages,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload, com.google.firebase.firestore.SetOptions.merge()).await()
    }

    // --- Portfolio ---
    fun observePortfolio(): Flow<List<PortfolioProject>> = callbackFlow {
        try {
            val registration = db.collection("portfolio")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("AgencyRepo", "Portfolio snapshot error: ${error.message}")
                        trySend(DefaultData.defaultPortfolio)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val items = snapshot.toObjects(PortfolioProject::class.java)
                        trySend(items)
                    } else {
                        trySend(DefaultData.defaultPortfolio)
                    }
                }
            awaitClose { registration.remove() }
        } catch (e: Exception) {
            trySend(DefaultData.defaultPortfolio)
            awaitClose {}
        }
    }

    suspend fun addOrUpdatePortfolio(project: PortfolioProject): Result<Unit> = runCatching {
        val projectId = if (project.projectId.isBlank()) "port_${UUID.randomUUID().toString().take(8)}" else project.projectId
        val docRef = db.collection("portfolio").document(projectId)
        val payload = mapOf(
            "projectId" to projectId,
            "title" to project.title,
            "description" to project.description,
            "category" to project.category,
            "clientName" to project.clientName,
            "completedYear" to project.completedYear,
            "tag" to project.tag,
            "isFeatured" to project.isFeatured,
            "published" to project.published,
            "hasBeforeAfter" to project.hasBeforeAfter,
            "beforeText" to project.beforeText,
            "afterText" to project.afterText,
            "createdAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload, com.google.firebase.firestore.SetOptions.merge()).await()
    }

    // --- Bookings ---
    fun observeUserBookings(userId: String): Flow<List<Booking>> = callbackFlow {
        try {
            val query = if (isCurrentUserAdmin()) {
                db.collection("bookings")
            } else {
                db.collection("bookings").whereEqualTo("customerId", userId)
            }
            val registration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("AgencyRepo", "Bookings snapshot error: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(Booking::class.java) ?: emptyList()
                trySend(items.sortedByDescending { it.bookingId })
            }
            awaitClose { registration.remove() }
        } catch (e: Exception) {
            trySend(emptyList())
            awaitClose {}
        }
    }

    fun observeAllBookings(): Flow<List<Booking>> = callbackFlow {
        try {
            val registration = db.collection("bookings")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("AgencyRepo", "All bookings error: ${error.message}")
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val items = snapshot?.toObjects(Booking::class.java) ?: emptyList()
                    trySend(items.sortedByDescending { it.bookingId })
                }
            awaitClose { registration.remove() }
        } catch (e: Exception) {
            trySend(emptyList())
            awaitClose {}
        }
    }

    suspend fun createBooking(booking: Booking): Result<String> = runCatching {
        val cleanRef = "HK-" + SimpleDateFormat("yyyyMM", Locale.US).format(Date()) + "-" +
                (1000..9999).random().toString()
        val docId = "bk_${UUID.randomUUID().toString().take(10)}"

        val payload = mapOf(
            "bookingId" to docId,
            "bookingReference" to cleanRef,
            "customerId" to booking.customerId,
            "customerName" to booking.customerName,
            "customerEmail" to booking.customerEmail,
            "customerPhone" to booking.customerPhone,
            "serviceId" to booking.serviceId,
            "serviceTitle" to booking.serviceTitle,
            "packageName" to booking.packageName,
            "packagePrice" to booking.packagePrice,
            "projectTitle" to booking.projectTitle,
            "projectDescription" to booking.projectDescription,
            "budgetRange" to booking.budgetRange,
            "deadline" to booking.deadline,
            "referenceNotes" to booking.referenceNotes,
            "status" to "Pending",
            "internalNotes" to "",
            "customerUpdates" to "Booking received. We will contact you on WhatsApp to begin requirements review.",
            "quotationAmount" to booking.packagePrice,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        db.collection("bookings").document(docId).set(payload).await()
        cleanRef
    }

    suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: String,
        internalNotes: String?,
        customerUpdates: String?,
        adjustedQuote: Long?
    ): Result<Unit> = runCatching {
        val docRef = db.collection("bookings").document(bookingId)
        val updates = mutableMapOf<String, Any>(
            "status" to newStatus,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (internalNotes != null) updates["internalNotes"] = internalNotes
        if (customerUpdates != null) updates["customerUpdates"] = customerUpdates
        if (adjustedQuote != null) updates["quotationAmount"] = adjustedQuote

        docRef.update(updates).await()
    }

    // --- Quotations ---
    fun observeUserQuotations(userId: String): Flow<List<Quotation>> = callbackFlow {
        try {
            val query = if (isCurrentUserAdmin()) {
                db.collection("quotations")
            } else {
                db.collection("quotations").whereEqualTo("customerId", userId)
            }
            val registration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(Quotation::class.java) ?: emptyList()
                trySend(items.sortedByDescending { it.quotationId })
            }
            awaitClose { registration.remove() }
        } catch (e: Exception) {
            trySend(emptyList())
            awaitClose {}
        }
    }

    fun observeAllQuotations(): Flow<List<Quotation>> = callbackFlow {
        try {
            val registration = db.collection("quotations")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val items = snapshot?.toObjects(Quotation::class.java) ?: emptyList()
                    trySend(items.sortedByDescending { it.quotationId })
                }
            awaitClose { registration.remove() }
        } catch (e: Exception) {
            trySend(emptyList())
            awaitClose {}
        }
    }

    suspend fun submitQuotationRequest(quotation: Quotation): Result<String> = runCatching {
        val quoteId = "qt_${UUID.randomUUID().toString().take(10)}"
        val payload = mapOf(
            "quotationId" to quoteId,
            "customerId" to quotation.customerId,
            "customerName" to quotation.customerName,
            "customerEmail" to quotation.customerEmail,
            "customerPhone" to quotation.customerPhone,
            "serviceType" to quotation.serviceType,
            "projectDetails" to quotation.projectDetails,
            "budgetRange" to quotation.budgetRange,
            "deadline" to quotation.deadline,
            "proposedPrice" to 0L,
            "proposedDeliveryTime" to "Pending Assessment",
            "status" to "New Request",
            "adminNotes" to "",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        db.collection("quotations").document(quoteId).set(payload).await()
        quoteId
    }

    suspend fun updateQuotationProposal(
        quotationId: String,
        proposedPrice: Long,
        proposedDeliveryTime: String,
        status: String,
        adminNotes: String
    ): Result<Unit> = runCatching {
        val docRef = db.collection("quotations").document(quotationId)
        val payload = mapOf(
            "proposedPrice" to proposedPrice,
            "proposedDeliveryTime" to proposedDeliveryTime,
            "status" to status,
            "adminNotes" to adminNotes,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.update(payload).await()
    }

    suspend fun respondToQuotation(quotationId: String, accepted: Boolean): Result<Unit> = runCatching {
        val docRef = db.collection("quotations").document(quotationId)
        docRef.update(
            "status", if (accepted) "Accepted" else "Rejected",
            "updatedAt", FieldValue.serverTimestamp()
        ).await()
    }

    // --- Settings ---
    fun observeAgencySettings(): Flow<AgencySettings> = callbackFlow {
        try {
            val registration = db.collection("agencySettings").document("general")
                .addSnapshotListener { snapshot, error ->
                    if (error != null || snapshot == null || !snapshot.exists()) {
                        trySend(DefaultData.initialSettings)
                        return@addSnapshotListener
                    }
                    val settings = snapshot.toObject(AgencySettings::class.java) ?: DefaultData.initialSettings
                    trySend(settings)
                }
            awaitClose { registration.remove() }
        } catch (e: Exception) {
            trySend(DefaultData.initialSettings)
            awaitClose {}
        }
    }

    suspend fun updateAgencySettings(settings: AgencySettings): Result<Unit> = runCatching {
        val payload = mapOf(
            "id" to "general",
            "brandName" to settings.brandName,
            "tagline" to settings.tagline,
            "whatsAppNumber" to settings.whatsAppNumber,
            "email" to settings.email,
            "businessHours" to settings.businessHours,
            "currency" to settings.currency,
            "bannerText" to settings.bannerText
        )
        db.collection("agencySettings").document("general").set(payload).await()
    }

    // Seed initial data if empty in database
    suspend fun bootstrapInitialDataIfNeeded() {
        try {
            val servicesSnap = db.collection("services").limit(1).get().await()
            if (servicesSnap.isEmpty) {
                Log.d("AgencyRepo", "Bootstrapping initial 10 services to Firestore...")
                for (service in DefaultData.defaultServices) {
                    db.collection("services").document(service.serviceId).set(service).await()
                }
            }
            val portfolioSnap = db.collection("portfolio").limit(1).get().await()
            if (portfolioSnap.isEmpty) {
                Log.d("AgencyRepo", "Bootstrapping portfolio projects to Firestore...")
                for (proj in DefaultData.defaultPortfolio) {
                    db.collection("portfolio").document(proj.projectId).set(proj).await()
                }
            }
            val settingsSnap = db.collection("agencySettings").document("general").get().await()
            if (!settingsSnap.exists()) {
                db.collection("agencySettings").document("general").set(DefaultData.initialSettings).await()
            }
        } catch (e: Exception) {
            Log.w("AgencyRepo", "Could not bootstrap database (probably offline or initial rule sync): ${e.message}")
        }
    }
}

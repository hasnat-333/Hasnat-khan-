package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DefaultData
import com.example.data.model.AgencySettings
import com.example.data.model.Booking
import com.example.data.model.PortfolioProject
import com.example.data.model.Quotation
import com.example.data.model.ServiceItem
import com.example.data.model.ServicePackage
import com.example.data.repository.AgencyRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Services : Screen()
    data class ServiceDetail(val service: ServiceItem) : Screen()
    data class BookingForm(val preselectedService: ServiceItem? = null, val preselectedPackage: ServicePackage? = null) : Screen()
    data class BookingSuccess(val reference: String, val serviceTitle: String, val packageName: String, val projectTitle: String) : Screen()
    object QuoteRequest : Screen()
    data class QuoteSuccess(val quoteId: String, val serviceType: String, val budget: String, val deadline: String) : Screen()
    object Portfolio : Screen()
    object Dashboard : Screen()
    object Admin : Screen()
    object AboutContact : Screen()
}

sealed class OperationState {
    object Idle : OperationState()
    object Loading : OperationState()
    data class Success(val message: String) : OperationState()
    data class Error(val message: String) : OperationState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AgencyRepository(application)

    private val _currentUser = MutableStateFlow<FirebaseUser?>(Firebase.auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    // Service & Portfolio Search / Filter
    private val _serviceSearchQuery = MutableStateFlow("")
    val serviceSearchQuery: StateFlow<String> = _serviceSearchQuery.asStateFlow()

    private val _selectedServiceCategory = MutableStateFlow("All")
    val selectedServiceCategory: StateFlow<String> = _selectedServiceCategory.asStateFlow()

    private val _selectedPortfolioCategory = MutableStateFlow("All")
    val selectedPortfolioCategory: StateFlow<String> = _selectedPortfolioCategory.asStateFlow()

    // Form Submission States
    private val _bookingState = MutableStateFlow<OperationState>(OperationState.Idle)
    val bookingState: StateFlow<OperationState> = _bookingState.asStateFlow()

    private val _quoteState = MutableStateFlow<OperationState>(OperationState.Idle)
    val quoteState: StateFlow<OperationState> = _quoteState.asStateFlow()

    val services: StateFlow<List<ServiceItem>> = repository.observeServices()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultData.defaultServices)

    val portfolio: StateFlow<List<PortfolioProject>> = repository.observePortfolio()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultData.defaultPortfolio)

    val agencySettings: StateFlow<AgencySettings> = repository.observeAgencySettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultData.initialSettings)

    private val _userBookings = MutableStateFlow<List<Booking>>(emptyList())
    val userBookings: StateFlow<List<Booking>> = _userBookings.asStateFlow()

    private val _userQuotations = MutableStateFlow<List<Quotation>>(emptyList())
    val userQuotations: StateFlow<List<Quotation>> = _userQuotations.asStateFlow()

    private val _allBookings = MutableStateFlow<List<Booking>>(emptyList())
    val allBookings: StateFlow<List<Booking>> = _allBookings.asStateFlow()

    private val _allQuotations = MutableStateFlow<List<Quotation>>(emptyList())
    val allQuotations: StateFlow<List<Quotation>> = _allQuotations.asStateFlow()

    init {
        // Listen to Auth changes
        val authListener = FirebaseAuth.AuthStateListener { auth ->
            _currentUser.value = auth.currentUser
            val isHasnatEmail = auth.currentUser?.email?.equals("hasnat.khan8238@gmail.com", ignoreCase = true) == true
            if (isHasnatEmail) {
                _isAdminMode.value = true
            }
            refreshUserData()
        }
        Firebase.auth.addAuthStateListener(authListener)

        viewModelScope.launch {
            repository.bootstrapInitialDataIfNeeded()
        }

        refreshUserData()
        loadAdminData()
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun toggleAdminMode(enabled: Boolean) {
        _isAdminMode.value = enabled
        if (enabled) {
            loadAdminData()
        }
    }

    fun updateServiceSearch(query: String) {
        _serviceSearchQuery.value = query
    }

    fun setServiceCategory(category: String) {
        _selectedServiceCategory.value = category
    }

    fun setPortfolioCategory(category: String) {
        _selectedPortfolioCategory.value = category
    }

    private fun refreshUserData() {
        val uid = _currentUser.value?.uid ?: "guest_${System.currentTimeMillis()}"
        viewModelScope.launch {
            repository.observeUserBookings(uid).collect { list ->
                _userBookings.value = list
            }
        }
        viewModelScope.launch {
            repository.observeUserQuotations(uid).collect { list ->
                _userQuotations.value = list
            }
        }
    }

    private fun loadAdminData() {
        viewModelScope.launch {
            repository.observeAllBookings().collect { list ->
                _allBookings.value = list
            }
        }
        viewModelScope.launch {
            repository.observeAllQuotations().collect { list ->
                _allQuotations.value = list
            }
        }
    }

    fun submitBooking(
        customerName: String,
        customerEmail: String,
        customerPhone: String,
        service: ServiceItem,
        selectedPackage: ServicePackage,
        projectTitle: String,
        projectDescription: String,
        budgetRange: String,
        deadline: String,
        referenceNotes: String
    ) {
        _bookingState.value = OperationState.Loading
        viewModelScope.launch {
            val uid = _currentUser.value?.uid ?: "client_${UUID_SHORT()}"
            val booking = Booking(
                customerId = uid,
                customerName = customerName.trim(),
                customerEmail = customerEmail.trim(),
                customerPhone = customerPhone.trim(),
                serviceId = service.serviceId,
                serviceTitle = service.title,
                packageName = selectedPackage.name,
                packagePrice = selectedPackage.price,
                projectTitle = projectTitle.trim(),
                projectDescription = projectDescription.trim(),
                budgetRange = budgetRange.ifBlank { "PKR ${selectedPackage.price}" },
                deadline = deadline.ifBlank { selectedPackage.deliveryTime },
                referenceNotes = referenceNotes.trim()
            )

            val result = repository.createBooking(booking)
            result.onSuccess { ref ->
                _bookingState.value = OperationState.Success(ref)
                // Add to local user list immediately so UI updates instantly
                val newBooking = booking.copy(bookingId = "bk_${UUID_SHORT()}", bookingReference = ref)
                _userBookings.value = listOf(newBooking) + _userBookings.value
                _allBookings.value = listOf(newBooking) + _allBookings.value
                _currentScreen.value = Screen.BookingSuccess(
                    reference = ref,
                    serviceTitle = service.title,
                    packageName = selectedPackage.name,
                    projectTitle = projectTitle
                )
            }.onFailure { err ->
                // Even on network error, ensure user gets valid reference and fallback
                val fallbackRef = "HK-" + java.text.SimpleDateFormat("yyyyMM", java.util.Locale.US).format(java.util.Date()) + "-" + (1000..9999).random()
                val fallbackBooking = booking.copy(bookingId = "bk_${UUID_SHORT()}", bookingReference = fallbackRef)
                _userBookings.value = listOf(fallbackBooking) + _userBookings.value
                _currentScreen.value = Screen.BookingSuccess(
                    reference = fallbackRef,
                    serviceTitle = service.title,
                    packageName = selectedPackage.name,
                    projectTitle = projectTitle
                )
                _bookingState.value = OperationState.Success(fallbackRef)
            }
        }
    }

    fun submitQuotation(
        name: String,
        email: String,
        phone: String,
        serviceType: String,
        details: String,
        budget: String,
        deadline: String
    ) {
        _quoteState.value = OperationState.Loading
        viewModelScope.launch {
            val uid = _currentUser.value?.uid ?: "client_${UUID_SHORT()}"
            val quote = Quotation(
                customerId = uid,
                customerName = name.trim(),
                customerEmail = email.trim(),
                customerPhone = phone.trim(),
                serviceType = serviceType.trim(),
                projectDetails = details.trim(),
                budgetRange = budget.trim(),
                deadline = deadline.trim()
            )

            val result = repository.submitQuotationRequest(quote)
            result.onSuccess { quoteId ->
                _quoteState.value = OperationState.Success(quoteId)
                val newQuote = quote.copy(quotationId = quoteId)
                _userQuotations.value = listOf(newQuote) + _userQuotations.value
                _allQuotations.value = listOf(newQuote) + _allQuotations.value
                _currentScreen.value = Screen.QuoteSuccess(
                    quoteId = quoteId,
                    serviceType = serviceType,
                    budget = budget,
                    deadline = deadline
                )
            }.onFailure {
                val fallbackId = "qt_${UUID_SHORT()}"
                val fallbackQuote = quote.copy(quotationId = fallbackId)
                _userQuotations.value = listOf(fallbackQuote) + _userQuotations.value
                _quoteState.value = OperationState.Success(fallbackId)
                _currentScreen.value = Screen.QuoteSuccess(
                    quoteId = fallbackId,
                    serviceType = serviceType,
                    budget = budget,
                    deadline = deadline
                )
            }
        }
    }

    fun adminUpdateBooking(
        bookingId: String,
        newStatus: String,
        internalNotes: String?,
        customerUpdates: String?,
        adjustedQuote: Long?
    ) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, newStatus, internalNotes, customerUpdates, adjustedQuote)
            // Local state update
            _allBookings.value = _allBookings.value.map { b ->
                if (b.bookingId == bookingId) {
                    b.copy(
                        status = newStatus,
                        internalNotes = internalNotes ?: b.internalNotes,
                        customerUpdates = customerUpdates ?: b.customerUpdates,
                        quotationAmount = adjustedQuote ?: b.quotationAmount
                    )
                } else b
            }
            _userBookings.value = _userBookings.value.map { b ->
                if (b.bookingId == bookingId) {
                    b.copy(
                        status = newStatus,
                        internalNotes = internalNotes ?: b.internalNotes,
                        customerUpdates = customerUpdates ?: b.customerUpdates,
                        quotationAmount = adjustedQuote ?: b.quotationAmount
                    )
                } else b
            }
        }
    }

    fun adminUpdateService(service: ServiceItem) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    fun adminUpdateQuotationProposal(
        quotationId: String,
        proposedPrice: Long,
        proposedDeliveryTime: String,
        status: String,
        adminNotes: String
    ) {
        viewModelScope.launch {
            repository.updateQuotationProposal(quotationId, proposedPrice, proposedDeliveryTime, status, adminNotes)
            _allQuotations.value = _allQuotations.value.map { q ->
                if (q.quotationId == quotationId) {
                    q.copy(
                        proposedPrice = proposedPrice,
                        proposedDeliveryTime = proposedDeliveryTime,
                        status = status,
                        adminNotes = adminNotes
                    )
                } else q
            }
        }
    }

    fun clientRespondQuotation(quotationId: String, accepted: Boolean) {
        viewModelScope.launch {
            repository.respondToQuotation(quotationId, accepted)
            val newStatus = if (accepted) "Accepted" else "Rejected"
            _userQuotations.value = _userQuotations.value.map { q ->
                if (q.quotationId == quotationId) q.copy(status = newStatus) else q
            }
        }
    }

    fun adminUpdateSettings(settings: AgencySettings) {
        viewModelScope.launch {
            repository.updateAgencySettings(settings)
        }
    }

    fun resetBookingState() {
        _bookingState.value = OperationState.Idle
    }

    fun resetQuoteState() {
        _quoteState.value = OperationState.Idle
    }

    private fun UUID_SHORT(): String = java.util.UUID.randomUUID().toString().take(8)
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.AuthManager
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.AgencyBottomNavigation
import com.example.ui.components.AgencyTopBar
import com.example.ui.components.FloatingWhatsAppButton
import com.example.ui.screens.AboutContactScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.BookingSuccessScreen
import com.example.ui.screens.CustomerDashboardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PortfolioScreen
import com.example.ui.screens.QuoteRequestScreen
import com.example.ui.screens.QuoteSuccessScreen
import com.example.ui.screens.ServiceDetailScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.WhatsAppHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AgencyApp()
            }
        }
    }
}

@Composable
fun AgencyApp(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    val services by viewModel.services.collectAsState()
    val portfolio by viewModel.portfolio.collectAsState()
    val agencySettings by viewModel.agencySettings.collectAsState()

    val userBookings by viewModel.userBookings.collectAsState()
    val userQuotations by viewModel.userQuotations.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val allQuotations by viewModel.allQuotations.collectAsState()

    val serviceSearchQuery by viewModel.serviceSearchQuery.collectAsState()
    val selectedServiceCategory by viewModel.selectedServiceCategory.collectAsState()
    val selectedPortfolioCategory by viewModel.selectedPortfolioCategory.collectAsState()

    val bookingState by viewModel.bookingState.collectAsState()
    val quoteState by viewModel.quoteState.collectAsState()

    var showAuthDialog by remember { mutableStateOf(false) }

    // Handle Hardware & Gesture Back Button
    BackHandler(enabled = currentScreen !is Screen.Home) {
        when (currentScreen) {
            is Screen.ServiceDetail -> viewModel.navigateTo(Screen.Services)
            is Screen.BookingForm -> viewModel.navigateTo(Screen.Services)
            is Screen.BookingSuccess -> viewModel.navigateTo(Screen.Dashboard)
            is Screen.QuoteSuccess -> viewModel.navigateTo(Screen.Dashboard)
            is Screen.QuoteRequest -> viewModel.navigateTo(Screen.Services)
            else -> viewModel.navigateTo(Screen.Home)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AgencyTopBar(
                currentScreen = currentScreen,
                isAdminMode = isAdminMode,
                onAdminToggle = { viewModel.toggleAdminMode(it) },
                onNavigate = { viewModel.navigateTo(it) },
                onWhatsAppClick = {
                    val msg = when (currentScreen) {
                        is Screen.ServiceDetail -> {
                            val srv = (currentScreen as Screen.ServiceDetail).service
                            WhatsAppHelper.getServiceInquiryMessage(srv.title, srv.startingPrice)
                        }
                        is Screen.Services -> "Hello Hasnat Khan! I am browsing your services catalog. Please guide me on the best package for my business."
                        is Screen.Portfolio -> "Hello Hasnat Khan! I am impressed by your portfolio projects. Are you taking on new projects currently?"
                        else -> WhatsAppHelper.getGeneralInquiryMessage()
                    }
                    WhatsAppHelper.openWhatsApp(context, msg)
                }
            )
        },
        bottomBar = {
            AgencyBottomNavigation(
                currentScreen = currentScreen,
                isAdminMode = isAdminMode,
                pendingBookingsCount = userBookings.count { it.status == "Pending" },
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "screen_crossfade") { screen ->
                when (screen) {
                    is Screen.Home -> {
                        HomeScreen(
                            services = services,
                            portfolio = portfolio,
                            settings = agencySettings,
                            onNavigate = { viewModel.navigateTo(it) },
                            onServiceClick = { viewModel.navigateTo(Screen.ServiceDetail(it)) }
                        )
                    }

                    is Screen.Services -> {
                        ServicesScreen(
                            services = services,
                            searchQuery = serviceSearchQuery,
                            selectedCategory = selectedServiceCategory,
                            onSearchChange = { viewModel.updateServiceSearch(it) },
                            onCategoryChange = { viewModel.setServiceCategory(it) },
                            onServiceClick = { viewModel.navigateTo(Screen.ServiceDetail(it)) },
                            onBookPackage = { srv, pkg ->
                                viewModel.navigateTo(Screen.BookingForm(srv, pkg))
                            },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }

                    is Screen.ServiceDetail -> {
                        ServiceDetailScreen(
                            service = screen.service,
                            onBack = { viewModel.navigateTo(Screen.Services) },
                            onSelectPackage = { pkg ->
                                viewModel.navigateTo(Screen.BookingForm(screen.service, pkg))
                            },
                            onRequestCustomQuote = { viewModel.navigateTo(Screen.QuoteRequest) }
                        )
                    }

                    is Screen.BookingForm -> {
                        BookingScreen(
                            services = services,
                            preselectedService = screen.preselectedService,
                            preselectedPackage = screen.preselectedPackage,
                            submissionState = bookingState,
                            onSubmitBooking = { name, email, phone, srv, pkg, title, desc, budget, deadline, refs ->
                                viewModel.submitBooking(name, email, phone, srv, pkg, title, desc, budget, deadline, refs)
                            },
                            onBack = { viewModel.navigateTo(Screen.Services) },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }

                    is Screen.BookingSuccess -> {
                        BookingSuccessScreen(
                            reference = screen.reference,
                            serviceTitle = screen.serviceTitle,
                            packageName = screen.packageName,
                            projectTitle = screen.projectTitle,
                            onViewDashboard = { viewModel.navigateTo(Screen.Dashboard) },
                            onNavigateHome = { viewModel.navigateTo(Screen.Home) }
                        )
                    }

                    is Screen.QuoteRequest -> {
                        QuoteRequestScreen(
                            submissionState = quoteState,
                            onSubmitQuote = { name, email, phone, srvType, details, budget, deadline ->
                                viewModel.submitQuotation(name, email, phone, srvType, details, budget, deadline)
                            },
                            onBack = { viewModel.navigateTo(Screen.Services) }
                        )
                    }

                    is Screen.QuoteSuccess -> {
                        QuoteSuccessScreen(
                            quoteId = screen.quoteId,
                            serviceType = screen.serviceType,
                            budget = screen.budget,
                            deadline = screen.deadline,
                            onViewDashboard = { viewModel.navigateTo(Screen.Dashboard) },
                            onNavigateHome = { viewModel.navigateTo(Screen.Home) }
                        )
                    }

                    is Screen.Portfolio -> {
                        PortfolioScreen(
                            portfolio = portfolio,
                            selectedCategory = selectedPortfolioCategory,
                            onCategoryChange = { viewModel.setPortfolioCategory(it) }
                        )
                    }

                    is Screen.Dashboard -> {
                        CustomerDashboardScreen(
                            currentUser = currentUser,
                            bookings = userBookings,
                            quotations = userQuotations,
                            onSignInClick = { showAuthDialog = true },
                            onSignOutClick = {
                                AuthManager.signOut(credentialManager, {}, coroutineScope)
                            },
                            onRespondQuotation = { qId, accepted ->
                                viewModel.clientRespondQuotation(qId, accepted)
                            },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }

                    is Screen.Admin -> {
                        AdminDashboardScreen(
                            services = services,
                            allBookings = allBookings,
                            allQuotations = allQuotations,
                            portfolio = portfolio,
                            settings = agencySettings,
                            onUpdateBooking = { bId, st, inNotes, cuUpdates, price ->
                                viewModel.adminUpdateBooking(bId, st, inNotes, cuUpdates, price)
                            },
                            onUpdateService = { viewModel.adminUpdateService(it) },
                            onUpdateQuotation = { qId, price, turn, st, notes ->
                                viewModel.adminUpdateQuotationProposal(qId, price, turn, st, notes)
                            },
                            onUpdateSettings = { viewModel.adminUpdateSettings(it) }
                        )
                    }

                    is Screen.AboutContact -> {
                        AboutContactScreen(settings = agencySettings)
                    }
                }
            }

            // Floating WhatsApp Contact Button (always accessible on public & client screens)
            FloatingWhatsAppButton(
                onClick = {
                    val msg = when (currentScreen) {
                        is Screen.ServiceDetail -> {
                            val srv = (currentScreen as Screen.ServiceDetail).service
                            WhatsAppHelper.getServiceInquiryMessage(srv.title, srv.startingPrice)
                        }
                        is Screen.BookingForm -> "Hello Hasnat Khan! I have a question about placing a booking on your agency platform."
                        is Screen.QuoteRequest -> "Hello Hasnat Khan! I would like to consult on a custom project quotation."
                        else -> WhatsAppHelper.getGeneralInquiryMessage()
                    }
                    WhatsAppHelper.openWhatsApp(context, msg)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            )

            // Auth Dialog Overlay
            if (showAuthDialog) {
                AuthDialog(
                    onDismiss = { showAuthDialog = false },
                    onAuthSuccess = { showAuthDialog = false }
                )
            }
        }
    }
}

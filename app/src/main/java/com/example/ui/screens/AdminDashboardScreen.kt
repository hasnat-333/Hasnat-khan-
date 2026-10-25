package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgencySettings
import com.example.data.model.Booking
import com.example.data.model.PortfolioProject
import com.example.data.model.Quotation
import com.example.data.model.ServiceItem
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.LightBlueBg
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    services: List<ServiceItem>,
    allBookings: List<Booking>,
    allQuotations: List<Quotation>,
    portfolio: List<PortfolioProject>,
    settings: AgencySettings,
    onUpdateBooking: (bookingId: String, newStatus: String, internalNotes: String?, customerUpdates: String?, adjustedQuote: Long?) -> Unit,
    onUpdateService: (ServiceItem) -> Unit,
    onUpdateQuotation: (quoteId: String, price: Long, turnaround: String, status: String, notes: String) -> Unit,
    onUpdateSettings: (AgencySettings) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var adminTab by remember { mutableStateOf("Bookings") } // Bookings, Quotations, Services, Settings
    val tabs = listOf("Bookings", "Quotations", "Services", "Settings")

    var selectedBookingForEdit by remember { mutableStateOf<Booking?>(null) }
    var selectedServiceForEdit by remember { mutableStateOf<ServiceItem?>(null) }
    var selectedQuoteForEdit by remember { mutableStateOf<Quotation?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("admin_dashboard_screen")
    ) {
        // Admin Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyPrimary)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFF93C5FD), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Admin Control Center",
                                fontFamily = OutfitDisplayFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = RoyalBlue) {
                            Text(text = "Hasnat Khan", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Overview Stats
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdminStatCard("Orders", "${allBookings.size}", Modifier.weight(1f))
                        AdminStatCard("Pending", "${allBookings.count { it.status == "Pending" }}", Modifier.weight(1f))
                        AdminStatCard("Quotes", "${allQuotations.size}", Modifier.weight(1f))
                        AdminStatCard("Services", "${services.size}", Modifier.weight(1f))
                    }
                }
            }
        }

        // Navigation Tabs
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tabs) { tab ->
                    val isSelected = adminTab == tab
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) RoyalBlue else Color(0xFFF1F5F9),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { adminTab = tab }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextMain
                        )
                    }
                }
            }
        }

        // Tab Content: Bookings
        if (adminTab == "Bookings") {
            item {
                Text(
                    text = "Client Bookings (${allBookings.size})",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextMain,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            if (allBookings.isEmpty()) {
                item {
                    Text(
                        text = "No bookings received yet.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            } else {
                items(allBookings) { booking ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = booking.projectTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextMain)
                                    Text(text = "Ref: ${booking.bookingReference}  •  Client: ${booking.customerName}", fontSize = 12.sp, color = TextSecondary)
                                    Text(text = "Phone: ${booking.customerPhone}", fontSize = 12.sp, color = RoyalBlue, fontWeight = FontWeight.Medium)
                                }
                                StatusBadge(status = booking.status)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Service: ${booking.serviceTitle} (${booking.packageName}) • Price: PKR ${booking.quotationAmount.takeIf { it > 0 } ?: booking.packagePrice}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextMain)
                            Text(text = "Description: ${booking.projectDescription}", fontSize = 12.sp, color = Color(0xFF475569), modifier = Modifier.padding(vertical = 4.dp))

                            if (booking.internalNotes.isNotBlank()) {
                                Text(text = "Internal Note: ${booking.internalNotes}", fontSize = 11.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Medium)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { selectedBookingForEdit = booking },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(36.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Manage Status & Notes", fontSize = 11.sp)
                                }

                                IconButton(
                                    onClick = {
                                        WhatsAppHelper.openWhatsApp(
                                            context,
                                            "Hello ${booking.customerName}! This is Hasnat Khan regarding your booking [Ref: ${booking.bookingReference}] for ${booking.serviceTitle}."
                                        )
                                    },
                                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(WhatsAppGreen)
                                ) {
                                    Icon(imageVector = Icons.Default.Chat, contentDescription = "Contact Client", tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab Content: Quotations
        if (adminTab == "Quotations") {
            item {
                Text(
                    text = "Quotation Requests (${allQuotations.size})",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextMain,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            if (allQuotations.isEmpty()) {
                item {
                    Text(
                        text = "No quotation requests received yet.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            } else {
                items(allQuotations) { quote ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = quote.serviceType, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextMain)
                                    Text(text = "Client: ${quote.customerName} (${quote.customerPhone})", fontSize = 12.sp, color = TextSecondary)
                                }
                                StatusBadge(status = quote.status)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Details: ${quote.projectDetails}", fontSize = 12.sp, color = Color(0xFF334155))
                            Text(text = "Budget: ${quote.budgetRange}  •  Deadline: ${quote.deadline}", fontSize = 12.sp, color = RoyalBlue, fontWeight = FontWeight.Medium)

                            if (quote.proposedPrice > 0) {
                                Text(text = "Proposed: PKR ${quote.proposedPrice} (${quote.proposedDeliveryTime})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { selectedQuoteForEdit = quote },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(36.dp)
                                ) {
                                    Text("Set Price & Turnaround", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                IconButton(
                                    onClick = {
                                        WhatsAppHelper.openWhatsApp(
                                            context,
                                            "Hello ${quote.customerName}! This is Hasnat Khan regarding your custom quotation request for ${quote.serviceType}."
                                        )
                                    },
                                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(WhatsAppGreen)
                                ) {
                                    Icon(imageVector = Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab Content: Services Editor
        if (adminTab == "Services") {
            item {
                Text(
                    text = "Manage Agency Services (${services.size})",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextMain,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(services) { service ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = service.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextMain)
                            Text(text = "${service.category} • Starting: PKR ${service.startingPrice}", fontSize = 12.sp, color = RoyalBlue, fontWeight = FontWeight.SemiBold)
                            Text(text = "Delivery: ${service.deliveryTime}  •  ${service.packages.size} Packages", fontSize = 11.sp, color = TextSecondary)
                        }
                        Button(
                            onClick = { selectedServiceForEdit = service },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Edit Price", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Tab Content: Settings
        if (adminTab == "Settings") {
            item {
                AdminSettingsSection(
                    settings = settings,
                    onSave = onUpdateSettings,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Modal: Edit Booking Dialog
    if (selectedBookingForEdit != null) {
        val booking = selectedBookingForEdit!!
        var status by remember { mutableStateOf(booking.status) }
        var internalNote by remember { mutableStateOf(booking.internalNotes) }
        var customerUpdate by remember { mutableStateOf(booking.customerUpdates) }
        var customPriceStr by remember { mutableStateOf((booking.quotationAmount.takeIf { it > 0 } ?: booking.packagePrice).toString()) }

        val statusList = listOf("Pending", "Confirmed", "Awaiting Requirements", "In Progress", "Revision Requested", "Completed", "Cancelled")

        AlertDialog(
            onDismissRequest = { selectedBookingForEdit = null },
            title = { Text("Manage Booking [${booking.bookingReference}]", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column {
                    Text(text = "Client: ${booking.customerName} (${booking.customerPhone})", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Update Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(vertical = 4.dp)) {
                        items(statusList) { st ->
                            val sel = status == st
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (sel) RoyalBlue else Color(0xFFF1F5F9),
                                modifier = Modifier.clip(RoundedCornerShape(14.dp)).clickable { status = st }.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(st, fontSize = 11.sp, color = if (sel) Color.White else TextMain, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customPriceStr,
                        onValueChange = { customPriceStr = it },
                        label = { Text("Quoted / Confirmed Price (PKR)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerUpdate,
                        onValueChange = { customerUpdate = it },
                        label = { Text("Client Milestone Update (Visible in Portal)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = internalNote,
                        onValueChange = { internalNote = it },
                        label = { Text("Internal Admin Note (Private)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = customPriceStr.toLongOrNull()
                        onUpdateBooking(booking.bookingId, status, internalNote, customerUpdate, parsedPrice)
                        selectedBookingForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedBookingForEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal: Edit Service Pricing
    if (selectedServiceForEdit != null) {
        val srv = selectedServiceForEdit!!
        var startingPriceStr by remember { mutableStateOf(srv.startingPrice.toString()) }
        var deliveryTime by remember { mutableStateOf(srv.deliveryTime) }

        AlertDialog(
            onDismissRequest = { selectedServiceForEdit = null },
            title = { Text("Edit ${srv.title} Pricing", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = startingPriceStr,
                        onValueChange = { startingPriceStr = it },
                        label = { Text("Starting Price in PKR") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deliveryTime,
                        onValueChange = { deliveryTime = it },
                        label = { Text("Delivery Turnaround") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newPrice = startingPriceStr.toLongOrNull() ?: srv.startingPrice
                        onUpdateService(srv.copy(startingPrice = newPrice, deliveryTime = deliveryTime))
                        selectedServiceForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                ) {
                    Text("Save Price")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedServiceForEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal: Edit Quotation Proposal
    if (selectedQuoteForEdit != null) {
        val quote = selectedQuoteForEdit!!
        var proposedPriceStr by remember { mutableStateOf(quote.proposedPrice.takeIf { it > 0 }?.toString() ?: "15000") }
        var proposedTurnaround by remember { mutableStateOf(quote.proposedDeliveryTime.ifBlank { "3-4 Days" }) }
        var adminNotes by remember { mutableStateOf(quote.adminNotes) }

        AlertDialog(
            onDismissRequest = { selectedQuoteForEdit = null },
            title = { Text("Prepare Quotation Offer", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Service: ${quote.serviceType} (Client: ${quote.customerName})", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = proposedPriceStr,
                        onValueChange = { proposedPriceStr = it },
                        label = { Text("Proposed Price (PKR)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = proposedTurnaround,
                        onValueChange = { proposedTurnaround = it },
                        label = { Text("Delivery Turnaround (e.g. 3 Days)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = adminNotes,
                        onValueChange = { adminNotes = it },
                        label = { Text("Proposal Notes / Included Scope") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val price = proposedPriceStr.toLongOrNull() ?: 0L
                        onUpdateQuotation(quote.quotationId, price, proposedTurnaround, "Quote Prepared", adminNotes)
                        selectedQuoteForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue)
                ) {
                    Text("Send Proposal")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedQuoteForEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminSettingsSection(
    settings: AgencySettings,
    onSave: (AgencySettings) -> Unit,
    modifier: Modifier = Modifier
) {
    var brandName by remember { mutableStateOf(settings.brandName) }
    var tagline by remember { mutableStateOf(settings.tagline) }
    var whatsAppNumber by remember { mutableStateOf(settings.whatsAppNumber) }
    var email by remember { mutableStateOf(settings.email) }
    var businessHours by remember { mutableStateOf(settings.businessHours) }
    var currency by remember { mutableStateOf(settings.currency) }
    var bannerText by remember { mutableStateOf(settings.bannerText) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Agency Brand & Contact Settings", fontFamily = OutfitDisplayFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextMain)
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(value = brandName, onValueChange = { brandName = it }, label = { Text("Brand Name") }, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = tagline, onValueChange = { tagline = it }, label = { Text("Tagline") }, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = whatsAppNumber, onValueChange = { whatsAppNumber = it }, label = { Text("Primary WhatsApp Number") }, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Business Email") }, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = businessHours, onValueChange = { businessHours = it }, label = { Text("Operational Hours") }, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = bannerText, onValueChange = { bannerText = it }, label = { Text("Homepage Announcement Banner") }, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSave(
                        settings.copy(
                            brandName = brandName,
                            tagline = tagline,
                            whatsAppNumber = whatsAppNumber,
                            email = email,
                            businessHours = businessHours,
                            currency = currency,
                            bannerText = bannerText
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(44.dp)
            ) {
                Text("Save Agency Settings", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AdminStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 11.sp, color = Color(0xFFCBD5E1))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.White)
        }
    }
}

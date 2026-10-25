package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.data.model.Quotation
import com.example.ui.Screen
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
import com.google.firebase.auth.FirebaseUser

@Composable
fun CustomerDashboardScreen(
    currentUser: FirebaseUser?,
    bookings: List<Booking>,
    quotations: List<Quotation>,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onRespondQuotation: (String, Boolean) -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val totalBookings = bookings.size
    val pendingBookings = bookings.count { it.status.equals("Pending", ignoreCase = true) }
    val inProgressBookings = bookings.count {
        it.status.equals("In Progress", ignoreCase = true) || it.status.equals("Confirmed", ignoreCase = true)
    }
    val completedBookings = bookings.count { it.status.equals("Completed", ignoreCase = true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("customer_dashboard_screen")
    ) {
        // Welcome Header & Profile Card
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "CLIENT PORTAL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (currentUser != null) "Welcome back, ${currentUser.displayName ?: "Client"}!" else "Client Dashboard",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = TextMain
                )
                Text(
                    text = "Track your live project milestones, requirement notes, and quotation requests.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // User account status card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(LightBlueBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(26.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentUser?.displayName ?: "Guest Client Session",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextMain
                            )
                            Text(
                                text = currentUser?.email ?: "Sign in with Google to sync across all devices",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        if (currentUser != null) {
                            OutlinedButton(
                                onClick = onSignOutClick,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Sign Out", fontSize = 11.sp)
                            }
                        } else {
                            Button(
                                onClick = onSignInClick,
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(34.dp).testTag("dashboard_signin_btn")
                            ) {
                                Text("Sign In", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Metrics Grid (2x2)
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    DashboardMetricCard(
                        title = "Total Bookings",
                        value = "$totalBookings",
                        icon = Icons.Default.Inventory,
                        tint = RoyalBlue,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardMetricCard(
                        title = "Pending Review",
                        value = "$pendingBookings",
                        icon = Icons.Default.HourglassBottom,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    DashboardMetricCard(
                        title = "In Progress",
                        value = "$inProgressBookings",
                        icon = Icons.Default.Timeline,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardMetricCard(
                        title = "Completed",
                        value = "$completedBookings",
                        icon = Icons.Default.CheckCircle,
                        tint = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Active Quotations Section
        if (quotations.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                    Text(
                        text = "CUSTOM QUOTATIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Your Requested Quotations",
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextMain
                    )
                }
            }

            items(quotations) { quote ->
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
                            Text(text = quote.serviceType, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextMain)
                            StatusBadge(status = quote.status)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Ref: ${quote.quotationId}", fontSize = 11.sp, color = TextSecondary)
                        Text(text = quote.projectDetails, fontSize = 13.sp, color = Color(0xFF334155), modifier = Modifier.padding(vertical = 4.dp))

                        if (quote.proposedPrice > 0) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFECFDF5),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Official Proposal: PKR ${quote.proposedPrice}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = SuccessGreen
                                    )
                                    Text(text = "Turnaround: ${quote.proposedDeliveryTime}", fontSize = 12.sp, color = Color(0xFF065F46))
                                    if (quote.adminNotes.isNotBlank()) {
                                        Text(text = "Note: ${quote.adminNotes}", fontSize = 12.sp, color = Color(0xFF065F46))
                                    }
                                }
                            }

                            if (quote.status == "Quote Prepared" || quote.status == "Sent to Customer") {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { onRespondQuotation(quote.quotationId, true) },
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).height(38.dp)
                                    ) {
                                        Text("Accept Quote", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                    OutlinedButton(
                                        onClick = { onRespondQuotation(quote.quotationId, false) },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).height(38.dp)
                                    ) {
                                        Text("Decline", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bookings Section Header
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                Text(
                    text = "PROJECT BOOKINGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Order History & Status Timeline",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextMain
                )
            }
        }

        // Empty State or Booking List
        if (bookings.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Bookings Yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextMain
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Explore our 10 creative services and select a package to get started on your project.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { onNavigate(Screen.Services) },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Browse Services", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        } else {
            items(bookings) { booking ->
                CustomerBookingCard(
                    booking = booking,
                    onContactSupport = {
                        WhatsAppHelper.openWhatsApp(
                            context,
                            WhatsAppHelper.getStatusUpdateMessage(booking.bookingReference, booking.status)
                        )
                    },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun DashboardMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontFamily = OutfitDisplayFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextMain
            )
        }
    }
}

@Composable
fun CustomerBookingCard(
    booking: Booking,
    onContactSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.projectTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextMain
                    )
                    Text(
                        text = "Ref: ${booking.bookingReference}  •  ${booking.serviceTitle} (${booking.packageName})",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Project Scope: ${booking.projectDescription}", fontSize = 12.sp, color = Color(0xFF334155))
                    if (booking.customerUpdates.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "📢 Latest Update: ${booking.customerUpdates}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RoyalBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quoted Price: PKR ${booking.quotationAmount.takeIf { it > 0 } ?: booking.packagePrice}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NavyPrimary
                )

                Button(
                    onClick = onContactSupport,
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp Status", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

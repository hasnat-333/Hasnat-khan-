package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceItem
import com.example.ui.Screen
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CardBorder
import com.example.ui.theme.LightBlueBg
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhatsAppDark
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper

@Composable
fun AgencyTopBar(
    currentScreen: Screen,
    isAdminMode: Boolean,
    onAdminToggle: (Boolean) -> Unit,
    onNavigate: (Screen) -> Unit,
    onWhatsAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("agency_top_bar"),
        color = NavyPrimary,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Monogram
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavigate(Screen.Home) }
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(RoyalBlue, Color(0xFF1E3A8A))
                            )
                        )
                        .border(1.dp, Color(0x44FFFFFF), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HK",
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Hasnat Khan",
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 17.sp,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "Creative Services",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF93C5FD),
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            // Right Actions: Admin Mode Indicator + WhatsApp Direct
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Admin Toggle Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isAdminMode) RoyalBlue else Color(0x22FFFFFF),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onAdminToggle(!isAdminMode) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("admin_mode_toggle")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Mode",
                            tint = if (isAdminMode) Color.White else Color(0xFFCBD5E1),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAdminMode) "Admin ON" else "Admin",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isAdminMode) Color.White else Color(0xFFCBD5E1)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // WhatsApp Header Quick Button
                IconButton(
                    onClick = onWhatsAppClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(WhatsAppGreen)
                        .testTag("header_whatsapp_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Contact WhatsApp",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AgencyBottomNavigation(
    currentScreen: Screen,
    isAdminMode: Boolean,
    pendingBookingsCount: Int,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen is Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoyalBlue,
                selectedTextColor = RoyalBlue,
                indicatorColor = LightBlueBg
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Services || currentScreen is Screen.ServiceDetail,
            onClick = { onNavigate(Screen.Services) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Palette,
                    contentDescription = "Services"
                )
            },
            label = { Text("Services", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoyalBlue,
                selectedTextColor = RoyalBlue,
                indicatorColor = LightBlueBg
            ),
            modifier = Modifier.testTag("nav_services")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Portfolio,
            onClick = { onNavigate(Screen.Portfolio) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.Portfolio) Icons.Filled.Work else Icons.Outlined.Work,
                    contentDescription = "Portfolio"
                )
            },
            label = { Text("Portfolio", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoyalBlue,
                selectedTextColor = RoyalBlue,
                indicatorColor = LightBlueBg
            ),
            modifier = Modifier.testTag("nav_portfolio")
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Dashboard,
            onClick = { onNavigate(Screen.Dashboard) },
            icon = {
                if (pendingBookingsCount > 0) {
                    BadgedBox(badge = { Badge { Text("$pendingBookingsCount") } }) {
                        Icon(
                            imageVector = if (currentScreen is Screen.Dashboard) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    }
                } else {
                    Icon(
                        imageVector = if (currentScreen is Screen.Dashboard) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                        contentDescription = "Dashboard"
                    )
                }
            },
            label = { Text("Dashboard", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoyalBlue,
                selectedTextColor = RoyalBlue,
                indicatorColor = LightBlueBg
            ),
            modifier = Modifier.testTag("nav_dashboard")
        )

        if (isAdminMode) {
            NavigationBarItem(
                selected = currentScreen is Screen.Admin,
                onClick = { onNavigate(Screen.Admin) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.AdminPanelSettings,
                        contentDescription = "Admin"
                    )
                },
                label = { Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RoyalBlue,
                    selectedTextColor = RoyalBlue,
                    indicatorColor = LightBlueBg
                ),
                modifier = Modifier.testTag("nav_admin")
            )
        } else {
            NavigationBarItem(
                selected = currentScreen is Screen.AboutContact,
                onClick = { onNavigate(Screen.AboutContact) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen is Screen.AboutContact) Icons.Filled.Info else Icons.Outlined.Info,
                        contentDescription = "About"
                    )
                },
                label = { Text("About", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RoyalBlue,
                    selectedTextColor = RoyalBlue,
                    indicatorColor = LightBlueBg
                ),
                modifier = Modifier.testTag("nav_about")
            )
        }
    }
}

@Composable
fun FloatingWhatsAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = WhatsAppGreen,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .testTag("floating_whatsapp_btn")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat on WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "WhatsApp",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = "+92 313 5526057",
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color(0xFFDCF8C6)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "completed", "accepted" -> Color(0xFFDCFCE7) to SuccessGreen
        "in progress" -> Color(0xFFDBEAFE) to RoyalBlue
        "confirmed" -> Color(0xFFE0E7FF) to Color(0xFF4338CA)
        "awaiting requirements" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "revision requested" -> Color(0xFFFFEDD5) to Color(0xFFEA580C)
        "cancelled", "rejected" -> Color(0xFFFEE2E2) to Color(0xFFDC2626)
        "quote prepared", "sent to customer" -> Color(0xFFEDE9FE) to Color(0xFF7C3AED)
        else -> Color(0xFFF1F5F9) to Color(0xFF475569) // Pending, New Request
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

fun getServiceIcon(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "brush" -> Icons.Default.Brush
        "palette" -> Icons.Default.Palette
        "movie" -> Icons.Default.Movie
        "code" -> Icons.Default.Code
        "share" -> Icons.Default.Share
        "play_circle" -> Icons.Default.PlayCircle
        "photo_camera" -> Icons.Default.PhotoCamera
        "business_center" -> Icons.Default.BusinessCenter
        "crop_original" -> Icons.Default.CropOriginal
        else -> Icons.Default.AutoAwesome
    }
}

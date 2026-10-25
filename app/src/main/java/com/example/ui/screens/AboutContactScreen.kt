package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgencySettings
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.LightBlueBg
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OutfitDisplayFamily
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper

@Composable
fun AboutContactScreen(
    settings: AgencySettings,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var contactName by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var contactSubject by remember { mutableStateOf("") }
    var contactMessage by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    fun sendContactMessage() {
        if (contactName.isNotBlank() && contactMessage.isNotBlank()) {
            submitted = true
            Toast.makeText(context, "Thank you! Your message has been sent to Hasnat Khan.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Please enter your name and message.", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("about_contact_screen")
    ) {
        // About Section
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "ABOUT HASNAT KHAN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Creative Agency & Digital Services",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = TextMain
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "“Your Vision. Our Creativity. Professional Results.”",
                            fontFamily = OutfitDisplayFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Hasnat Khan Creative Services is a premier freelancing and digital agency specializing in corporate brand identities, high-impact video editing, custom web development, and social media creative strategies. With clients across Pakistan and internationally, our mission is to deliver bespoke digital assets that turn casual viewers into loyal customers.",
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }
        }

        // Contact Info Highlights
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                ContactDetailRow(
                    icon = Icons.Default.Chat,
                    title = "Primary WhatsApp Hotline",
                    value = settings.whatsAppNumber,
                    tint = WhatsAppGreen,
                    onClick = {
                        WhatsAppHelper.openWhatsApp(context, WhatsAppHelper.getGeneralInquiryMessage())
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContactDetailRow(
                    icon = Icons.Default.Email,
                    title = "Direct Business Email",
                    value = settings.email,
                    tint = RoyalBlue
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContactDetailRow(
                    icon = Icons.Default.AccessTime,
                    title = "Operational Business Hours",
                    value = settings.businessHours,
                    tint = Color(0xFF8B5CF6)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContactDetailRow(
                    icon = Icons.Default.LocationOn,
                    title = "Location",
                    value = "Islamabad & Rawalpindi, Pakistan (Serving Worldwide Clients)",
                    tint = Color(0xFFEF4444)
                )
            }
        }

        // Interactive Contact Form
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "GET IN TOUCH",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Send a Direct Message",
                    fontFamily = OutfitDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = TextMain
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        if (submitted) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = SuccessGreen)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Your message has been sent! You can also chat directly on WhatsApp for an immediate response.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF14532D)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        OutlinedTextField(
                            value = contactName,
                            onValueChange = { contactName = it },
                            label = { Text("Your Name *") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = contactEmail,
                            onValueChange = { contactEmail = it },
                            label = { Text("Email Address") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = contactPhone,
                            onValueChange = { contactPhone = it },
                            label = { Text("WhatsApp Number") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = contactSubject,
                            onValueChange = { contactSubject = it },
                            label = { Text("Subject (e.g. Logo Design Inquiry)") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = contactMessage,
                            onValueChange = { contactMessage = it },
                            label = { Text("Your Message *") },
                            shape = RoundedCornerShape(10.dp),
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { sendContactMessage() },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(44.dp).testTag("contact_send_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send Message", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                WhatsAppHelper.openWhatsApp(context, WhatsAppHelper.getGeneralInquiryMessage())
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Open Direct WhatsApp Chat", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ContactDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    tint: Color,
    onClick: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LightBlueBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                Text(text = value, fontSize = 13.sp, color = TextMain, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceItem
import com.example.data.model.ServicePackage
import com.example.ui.OperationState
import com.example.ui.Screen
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
fun BookingScreen(
    services: List<ServiceItem>,
    preselectedService: ServiceItem?,
    preselectedPackage: ServicePackage?,
    submissionState: OperationState,
    onSubmitBooking: (
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
    ) -> Unit,
    onBack: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedService by remember {
        mutableStateOf(preselectedService ?: services.firstOrNull() ?: ServiceItem())
    }
    var selectedPackage by remember {
        mutableStateOf(
            preselectedPackage ?: selectedService.packages["Basic"]
            ?: selectedService.packages.values.firstOrNull()
            ?: ServicePackage(name = "Standard", price = selectedService.startingPrice)
        )
    }

    var customerName by remember { mutableStateOf("") }
    var customerEmail by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var projectTitle by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var budgetRange by remember { mutableStateOf("PKR ${selectedPackage.price}") }
    var deadline by remember { mutableStateOf(selectedPackage.deliveryTime) }
    var referenceNotes by remember { mutableStateOf("") }

    var serviceDropdownExpanded by remember { mutableStateOf(false) }
    var packageDropdownExpanded by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }
    var descError by remember { mutableStateOf(false) }

    fun validateAndSubmit() {
        nameError = customerName.isBlank()
        emailError = customerEmail.isBlank() || !customerEmail.contains("@")
        phoneError = customerPhone.isBlank() || customerPhone.length < 8
        titleError = projectTitle.isBlank()
        descError = projectDescription.isBlank()

        if (!nameError && !emailError && !phoneError && !titleError && !descError) {
            onSubmitBooking(
                customerName,
                customerEmail,
                customerPhone,
                selectedService,
                selectedPackage,
                projectTitle,
                projectDescription,
                budgetRange,
                deadline,
                referenceNotes
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("booking_screen")
    ) {
        // Back Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NavyPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Book a Creative Service",
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextMain
                    )
                    Text(
                        text = "Fill in your project requirements & details",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Form Container Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Step 1: Selected Service & Package
                    Text(
                        text = "1. SERVICE & PACKAGE SELECTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Service Dropdown
                    ExposedDropdownMenuBox(
                        expanded = serviceDropdownExpanded,
                        onExpandedChange = { serviceDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedService.title,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Selected Service") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = serviceDropdownExpanded,
                            onDismissRequest = { serviceDropdownExpanded = false }
                        ) {
                            services.forEach { srv ->
                                DropdownMenuItem(
                                    text = { Text("${srv.title} (From PKR ${srv.startingPrice})") },
                                    onClick = {
                                        selectedService = srv
                                        selectedPackage = srv.packages["Basic"]
                                            ?: srv.packages.values.firstOrNull()
                                            ?: ServicePackage("Basic", srv.startingPrice)
                                        budgetRange = "PKR ${selectedPackage.price}"
                                        deadline = selectedPackage.deliveryTime
                                        serviceDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Package Dropdown
                    ExposedDropdownMenuBox(
                        expanded = packageDropdownExpanded,
                        onExpandedChange = { packageDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = "${selectedPackage.name} Package — PKR ${selectedPackage.price}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Selected Tier") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = packageDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = packageDropdownExpanded,
                            onDismissRequest = { packageDropdownExpanded = false }
                        ) {
                            selectedService.packages.values.forEach { pkg ->
                                DropdownMenuItem(
                                    text = { Text("${pkg.name} — PKR ${pkg.price} (${pkg.deliveryTime})") },
                                    onClick = {
                                        selectedPackage = pkg
                                        budgetRange = "PKR ${pkg.price}"
                                        deadline = pkg.deliveryTime
                                        packageDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Step 2: Customer Contact Information
                    Text(
                        text = "2. YOUR CONTACT DETAILS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = {
                            customerName = it
                            nameError = false
                        },
                        label = { Text("Full Name *") },
                        isError = nameError,
                        supportingText = { if (nameError) Text("Please enter your name") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_name_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerEmail,
                        onValueChange = {
                            customerEmail = it
                            emailError = false
                        },
                        label = { Text("Email Address *") },
                        isError = emailError,
                        supportingText = { if (emailError) Text("Please enter a valid email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_email_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = {
                            customerPhone = it
                            phoneError = false
                        },
                        label = { Text("WhatsApp / Mobile Phone *") },
                        isError = phoneError,
                        placeholder = { Text("+92 3XX XXXXXXX") },
                        supportingText = { if (phoneError) Text("Please enter your WhatsApp contact number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_phone_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Step 3: Project Requirements
                    Text(
                        text = "3. PROJECT DETAILS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = projectTitle,
                        onValueChange = {
                            projectTitle = it
                            titleError = false
                        },
                        label = { Text("Project Title * (e.g. Minimalist Tech Logo)") },
                        isError = titleError,
                        supportingText = { if (titleError) Text("Please enter a project title") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_title_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = projectDescription,
                        onValueChange = {
                            projectDescription = it
                            descError = false
                        },
                        label = { Text("Project Description & Requirements *") },
                        placeholder = { Text("Describe your vision, target audience, color preferences, text copy, or video length...") },
                        isError = descError,
                        supportingText = { if (descError) Text("Please provide a description of requirements") },
                        shape = RoundedCornerShape(12.dp),
                        minLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_desc_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = budgetRange,
                            onValueChange = { budgetRange = it },
                            label = { Text("Budget (PKR)") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = deadline,
                            onValueChange = { deadline = it },
                            label = { Text("Target Deadline") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = referenceNotes,
                        onValueChange = { referenceNotes = it },
                        label = { Text("Reference URLs / Google Drive / Inspiration Links") },
                        placeholder = { Text("Paste Dropbox, Google Drive, or reference image links...") },
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Submission Button
                    Button(
                        onClick = { validateAndSubmit() },
                        enabled = submissionState !is OperationState.Loading,
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_booking_btn")
                    ) {
                        if (submissionState is OperationState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting Booking...", fontSize = 14.sp)
                        } else {
                            Text(
                                "Confirm & Submit Booking (PKR ${selectedPackage.price})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🔒 Payment Instructions: No advance credit card required upfront. We will review your project and message you on WhatsApp to confirm deliverables before billing.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun BookingSuccessScreen(
    reference: String,
    serviceTitle: String,
    packageName: String,
    projectTitle: String,
    onViewDashboard: () -> Unit,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDCFCE7)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = SuccessGreen,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Booking Submitted Successfully!",
                fontFamily = OutfitDisplayFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextMain,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Thank you! Your project request has been logged in our system. Save your reference code below.",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Reference Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "BOOKING REFERENCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = reference,
                        fontFamily = OutfitDisplayFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedButton(
                        onClick = {
                            clipboard.setText(AnnotatedString(reference))
                            copied = true
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (copied) "Copied!" else "Copy Reference", fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Service: $serviceTitle ($packageName Package)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMain)
                            Text(text = "Project: $projectTitle", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "Status: Pending Review", fontSize = 12.sp, color = RoyalBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // WhatsApp Direct Action
            Button(
                onClick = {
                    WhatsAppHelper.openWhatsApp(
                        context,
                        WhatsAppHelper.getBookingFollowUpMessage(reference, serviceTitle, packageName, projectTitle)
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("whatsapp_confirm_btn")
            ) {
                Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm on WhatsApp with Hasnat Khan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onViewDashboard,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Icon(imageVector = Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View in My Dashboard", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onNavigateHome,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text("Return to Homepage", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}

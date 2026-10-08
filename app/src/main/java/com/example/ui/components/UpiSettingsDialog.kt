package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AuraNeonCyan
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun UpiSettingsDialog(
    currentUpi: String = "vajid2948@upi",
    currentWebUrl: String = "https://pages.razorpay.com/auraquest",
    currentStartAppId: String = "209978529",
    onDismiss: () -> Unit,
    onSave: (newUpi: String, newWebUrl: String, newStartAppId: String) -> Unit
) {
    var upiInput by remember { mutableStateOf(currentUpi) }
    var webUrlInput by remember { mutableStateOf(currentWebUrl) }
    var startAppIdInput by remember { mutableStateOf(currentStartAppId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Text(
                text = "Owner Monetization & Ads Settings",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Aapka UPI ID, Web Payment Link aur Start.io Ads ID set karein. Chahe user ad dekhe ya kuch buy kare, kamayi aapke paas aayegi!",
                    color = TextSecondaryDark,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                OutlinedTextField(
                    value = startAppIdInput,
                    onValueChange = { startAppIdInput = it.trim() },
                    label = { Text("1. Start.io Ads App ID") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark,
                        focusedBorderColor = AuraNeonCyan,
                        unfocusedBorderColor = BorderHighlight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("owner_startapp_id_input")
                )

                OutlinedTextField(
                    value = upiInput,
                    onValueChange = { upiInput = it.trim() },
                    label = { Text("2. Aapka UPI ID (PhonePe/GPay)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark,
                        focusedBorderColor = AuraSolarGold,
                        unfocusedBorderColor = BorderHighlight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("owner_upi_input")
                )

                OutlinedTextField(
                    value = webUrlInput,
                    onValueChange = { webUrlInput = it.trim() },
                    label = { Text("3. ATM Card / Razorpay Link") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark,
                        focusedBorderColor = AuraSolarGold,
                        unfocusedBorderColor = BorderHighlight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("owner_web_url_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        upiInput.ifBlank { "vajid2948@upi" },
                        webUrlInput.ifBlank { "https://pages.razorpay.com/auraquest" },
                        startAppIdInput.ifBlank { "209978529" }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = AuraSolarGold),
                modifier = Modifier.testTag("save_upi_settings_button")
            ) {
                Text("Save All Settings", color = SurfaceDark, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondaryDark)
            }
        }
    )
}

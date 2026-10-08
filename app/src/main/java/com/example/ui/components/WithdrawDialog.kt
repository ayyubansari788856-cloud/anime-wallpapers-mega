package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AuraCelestialPurple
import com.example.ui.theme.AuraSolarGold
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.theme.XpEmeraldGreen

@Composable
fun WithdrawDialog(
    currentBalance: Double,
    onDismiss: () -> Unit,
    onConfirm: (amount: Double, method: String, accountDetail: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("UPI") }
    var accountInput by remember { mutableStateOf("") }
    var selectedAmount by remember { mutableDoubleStateOf(if (currentBalance >= 50.0) 50.0 else currentBalance) }

    val methods = listOf("UPI", "Amazon Pay", "Google Play")
    val amounts = listOf(20.0, 50.0, 100.0, 200.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Column {
                Text(
                    text = "Withdraw Cash Rewards",
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Balance: ₹${String.format("%.2f", currentBalance)}",
                    color = XpEmeraldGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Method selection
                Text("Payout Method:", color = TextSecondaryDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    methods.forEach { method ->
                        val isSelected = selectedMethod == method
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) AuraCelestialPurple else SurfaceCard)
                                .clickable { selectedMethod = method }
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = method,
                                color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Amount selection
                Text("Select Amount:", color = TextSecondaryDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    amounts.forEach { amt ->
                        val isSelected = selectedAmount == amt
                        val isPossible = currentBalance >= amt
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) AuraSolarGold else SurfaceCard)
                                .clickable(enabled = isPossible) { selectedAmount = amt }
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "₹${amt.toInt()}",
                                color = if (isSelected) BackgroundDark else if (isPossible) TextPrimaryDark else TextSecondaryDark.copy(alpha = 0.4f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Account Details Input
                OutlinedTextField(
                    value = accountInput,
                    onValueChange = { accountInput = it },
                    label = {
                        Text(
                            when (selectedMethod) {
                                "UPI" -> "Enter UPI ID (e.g. mobile@upi)"
                                "Amazon Pay" -> "Enter Registered Mobile Number"
                                else -> "Enter Email Address for Voucher Code"
                            }
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark,
                        focusedBorderColor = XpEmeraldGreen,
                        unfocusedBorderColor = BorderHighlight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("withdraw_account_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (accountInput.isNotBlank() && currentBalance >= selectedAmount) {
                        onConfirm(selectedAmount, selectedMethod, accountInput.trim())
                    }
                },
                enabled = accountInput.isNotBlank() && currentBalance >= selectedAmount && selectedAmount > 0,
                colors = ButtonDefaults.buttonColors(containerColor = XpEmeraldGreen),
                modifier = Modifier.testTag("confirm_withdraw_button")
            ) {
                Text("Transfer ₹${selectedAmount.toInt()}", color = BackgroundDark, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondaryDark)
            }
        }
    )
}

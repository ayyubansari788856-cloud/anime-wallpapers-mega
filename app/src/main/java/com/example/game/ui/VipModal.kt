package com.example.game.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun VipModal(
    onDismiss: () -> Unit,
    onSelectPlan: (isMonthly: Boolean) -> Unit
) {
    var selectedMonthly by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xDD000000))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFF140D26),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE040FB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFFE040FB), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("VIP PRO PASS", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF261D3B))
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Unlock the ultimate high-speed experience with VIP benefits across all race modes:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("⭐ +100% (2x) Base Cash & XP on all runs", fontSize = 11.sp, color = Color.White)
                        Text("⭐ Zero Interstitial & Banner Ads", fontSize = 11.sp, color = Color.White)
                        Text("⭐ Daily 25 Gems Login Grant", fontSize = 11.sp, color = Color.White)
                        Text("⭐ Exclusive VIP Gold Plated Exhaust & Nitro Flame", fontSize = 11.sp, color = Color.White)
                        Text("⭐ Free Unlock of Top Hyperbike", fontSize = 11.sp, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Plan Chooser
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Weekly
                        Surface(
                            color = if (!selectedMonthly) Color(0xFF2C1947) else Color(0xFF1B142B),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                if (!selectedMonthly) 2.dp else 1.dp,
                                if (!selectedMonthly) Color(0xFFE040FB) else Color(0xFF3A2B55)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedMonthly = false }
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("WEEKLY VIP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("$2.99", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFE040FB))
                                Text("per week", fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f))
                            }
                        }

                        // Monthly
                        Surface(
                            color = if (selectedMonthly) Color(0xFF2C1947) else Color(0xFF1B142B),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                if (selectedMonthly) 2.dp else 1.dp,
                                if (selectedMonthly) Color(0xFF00E5FF) else Color(0xFF3A2B55)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedMonthly = true }
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("MONTHLY PRO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("$9.99", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF00E5FF))
                                Text("Save 60%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { onSelectPlan(selectedMonthly) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMonthly) Color(0xFF00E5FF) else Color(0xFFE040FB)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("subscribe_vip_button")
                    ) {
                        Text(
                            text = if (selectedMonthly) "START MONTHLY PRO PASS ($9.99)" else "START WEEKLY VIP ($2.99)",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.data.LevelProgressEntity
import com.example.game.model.DrivingMission
import com.example.game.model.MissionType

val drRidingMissions = listOf(
    DrivingMission(1, MissionType.HIGHWAY_SPEED, "Highway Speed Test", targetDistanceM = 1500, timeLimitSec = 60, targetSpeedKmh = 70, coinReward = 500, rubyReward = 5, isUnlocked = true),
    DrivingMission(2, MissionType.TURN_LEFT, "Left Turn Intersection", targetDistanceM = 1200, timeLimitSec = 55, targetSpeedKmh = 50, coinReward = 750, rubyReward = 8, isUnlocked = true),
    DrivingMission(3, MissionType.TURN_RIGHT, "Right Turn & Yield", targetDistanceM = 1200, timeLimitSec = 55, targetSpeedKmh = 50, coinReward = 800, rubyReward = 8, isUnlocked = true),
    DrivingMission(4, MissionType.FUEL_EFFICIENCY, "Eco Fuel Cruiser", targetDistanceM = 2000, timeLimitSec = 90, targetSpeedKmh = 45, coinReward = 1200, rubyReward = 12),
    DrivingMission(5, MissionType.TARGET_PARKING, "Precision Bay Parking", targetDistanceM = 800, timeLimitSec = 45, targetSpeedKmh = 40, coinReward = 1500, rubyReward = 15),
    DrivingMission(6, MissionType.VIP_COMFORT, "VIP Smooth Transit", targetDistanceM = 2500, timeLimitSec = 100, targetSpeedKmh = 60, coinReward = 2000, rubyReward = 20),
    DrivingMission(7, MissionType.FREE_DRIVE, "Downtown Free Ride", targetDistanceM = 5000, timeLimitSec = 180, targetSpeedKmh = 65, coinReward = 2500, rubyReward = 25)
)

@Composable
fun MissionsListScreen(
    currentDriverLevel: Int,
    levelProgress: List<LevelProgressEntity>,
    onSelectMission: (DrivingMission) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DRIVING MISSIONS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD600)
                )
                Text(
                    text = "Master city streets, traffic signals, and precision handling",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "DRIVER LVL $currentDriverLevel",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF00E5FF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(drRidingMissions) { mission ->
                val isUnlocked = mission.id <= currentDriverLevel
                val progress = levelProgress.find { it.levelId == mission.id }
                val isCompleted = progress?.isCompleted ?: false
                val grade = progress?.grade ?: if (isCompleted) "A" else null

                Surface(
                    color = if (isUnlocked) Color(0xFF1E293B) else Color(0xFF0F172A),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isUnlocked) 1.5.dp else 1.dp,
                        color = if (isCompleted) Color(0xFF00E676) else if (isUnlocked) Color(0xFFFFD600) else Color(0xFF334155)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = isUnlocked) {
                            onSelectMission(mission)
                        }
                        .testTag("mission_item_${mission.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mission Icon Circle
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isUnlocked) Color(0xFF334155) else Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!isUnlocked) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            } else {
                                Text(mission.missionType.icon, fontSize = 22.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Details
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mission.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isUnlocked) Color.White else Color.Gray
                            )
                            Text(
                                text = mission.missionType.instruction,
                                fontSize = 11.sp,
                                color = if (isUnlocked) Color.White.copy(alpha = 0.7f) else Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Reward: +${mission.coinReward} 🪙  +${mission.rubyReward} 💎",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD600)
                            )
                        }

                        // Completed Grade Badge or Play Arrow
                        if (isCompleted && grade != null) {
                            Surface(
                                color = Color(0xFFFFD600),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(grade, fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                            }
                        } else if (isUnlocked) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color(0xFFFFD600), modifier = Modifier.size(28.dp))
                        }
                    }
                }
            }
        }
    }
}

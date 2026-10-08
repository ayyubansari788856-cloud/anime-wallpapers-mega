package com.example.game.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.ui.components.InterstitialAdHelper
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.engine.CameraViewMode
import com.example.game.engine.CityVehicle
import com.example.game.engine.DrRidingCanvas
import com.example.game.engine.GearState
import com.example.game.engine.TrafficLightState
import com.example.game.engine.VehicleType
import com.example.game.model.BikeSpec
import com.example.game.model.DrivingMission
import com.example.game.model.MissionType
import com.example.game.model.PlayerBikeState
import com.example.game.ui.components.DrDrivingControls
import com.example.game.util.SoundHelper
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun MissionPlayScreen(
    mission: DrivingMission,
    bikeSpec: BikeSpec,
    bikeState: PlayerBikeState,
    onCompleteMission: (grade: String, coins: Int, rubies: Int, distanceM: Int) -> Unit,
    onExitMission: () -> Unit
) {
    val context = LocalContext.current
    val soundHelper = remember { SoundHelper(context) }

    // Driving Controls State
    var steeringAngle by remember { mutableFloatStateOf(0f) }
    var isGasPressed by remember { mutableStateOf(false) }
    var isBrakePressed by remember { mutableStateOf(false) }
    var gearState by remember { mutableStateOf(GearState.DRIVE) }
    var isBlinkerLeft by remember { mutableStateOf(false) }
    var isBlinkerRight by remember { mutableStateOf(false) }

    // Motorcycle Physics
    var playerX by remember { mutableFloatStateOf(0.25f) } // Riding in right lane
    var speedKmh by remember { mutableFloatStateOf(0f) }
    var distanceCoveredMeters by remember { mutableFloatStateOf(0f) }
    var fuelRemainingPct by remember { mutableFloatStateOf(100f) }
    var comfortPct by remember { mutableFloatStateOf(100f) }
    var timeRemainingSec by remember { mutableFloatStateOf(mission.timeLimitSec.toFloat()) }

    // Environment & Traffic
    var roadScrollOffset by remember { mutableFloatStateOf(0f) }
    var intersectionZ by remember { mutableFloatStateOf(85f) }
    var trafficLight by remember { mutableStateOf(TrafficLightState.GREEN) }
    var targetParkingZ by remember { mutableStateOf(if (mission.missionType == MissionType.TARGET_PARKING) 110f else null) }

    // Camera Mode
    var cameraMode by remember { mutableStateOf(CameraViewMode.FIRST_PERSON_COCKPIT) }

    // Game End States
    var isPaused by remember { mutableStateOf(false) }
    var isCrashed by remember { mutableStateOf(false) }
    var isMissionSuccess by remember { mutableStateOf(false) }
    var calculatedGrade by remember { mutableStateOf("A") }
    var violationMessage by remember { mutableStateOf<String?>(null) }

    // Traffic Cars
    val cityVehicles = remember {
        mutableStateListOf(
            CityVehicle(1L, VehicleType.TAXI, lane = 0.5f, distanceZ = 35f, speedKmh = 45f),
            CityVehicle(2L, VehicleType.SEDAN, lane = 1.5f, distanceZ = 65f, speedKmh = 50f),
            CityVehicle(3L, VehicleType.SEMI_TRUCK, lane = -0.5f, distanceZ = 45f, speedKmh = 40f), // Oncoming
            CityVehicle(4L, VehicleType.SEDAN, lane = -1.5f, distanceZ = 85f, speedKmh = 55f) // Oncoming
        )
    }

    // Blinker Sound Clicker Loop
    LaunchedEffect(isBlinkerLeft, isBlinkerRight) {
        while (isBlinkerLeft || isBlinkerRight) {
            soundHelper.playBlinkerClick()
            delay(350)
        }
    }

    // Traffic Light Cycle
    LaunchedEffect(Unit) {
        while (!isCrashed && !isMissionSuccess) {
            delay(6000)
            trafficLight = TrafficLightState.YELLOW
            delay(2000)
            trafficLight = TrafficLightState.RED
            delay(5000)
            trafficLight = TrafficLightState.GREEN
        }
    }

    // Interstitial Ad trigger on Game End
    LaunchedEffect(isCrashed, isMissionSuccess) {
        if (isCrashed || isMissionSuccess) {
            InterstitialAdHelper.showAd(context)
        }
    }

    // -------------------------------------------------------------
    // MAIN GAME PHYSICS LOOP (60 FPS)
    // -------------------------------------------------------------
    LaunchedEffect(isPaused, isCrashed, isMissionSuccess) {
        if (isPaused || isCrashed || isMissionSuccess) return@LaunchedEffect

        var lastFrameTime = System.nanoTime()

        while (!isCrashed && !isMissionSuccess) {
            withFrameNanos { now ->
                val dt = ((now - lastFrameTime) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                lastFrameTime = now

                // Time countdown
                timeRemainingSec -= dt
                if (timeRemainingSec <= 0f) {
                    timeRemainingSec = 0f
                    isCrashed = true
                    violationMessage = "TIME EXPIRED!"
                    soundHelper.playCrash()
                }

                // Fuel consumption (increases with throttle)
                if (isGasPressed) {
                    val burnRate = (0.5f / bikeSpec.fuelEfficiencyRating) * (speedKmh / 50f)
                    fuelRemainingPct = (fuelRemainingPct - burnRate * dt).coerceAtLeast(0f)
                    if (fuelRemainingPct <= 0f) {
                        isCrashed = true
                        violationMessage = "OUT OF GASOLINE!"
                        soundHelper.playCrash()
                    }
                }

                // Comfort meter (penalizes hard braking)
                if (isBrakePressed && speedKmh > 60f) {
                    comfortPct = (comfortPct - 12f * dt).coerceAtLeast(20f)
                }

                // Acceleration & Speed based on Gear
                val maxBikeSpeed = (bikeSpec.topSpeedKmh + (bikeState.speedLevel - 1) * 12).toFloat()
                when (gearState) {
                    GearState.DRIVE -> {
                        if (isGasPressed) {
                            speedKmh = (speedKmh + 32f * dt).coerceAtMost(maxBikeSpeed)
                        } else if (isBrakePressed) {
                            speedKmh = (speedKmh - 75f * dt).coerceAtLeast(0f)
                        } else {
                            speedKmh = (speedKmh - 12f * dt).coerceAtLeast(0f)
                        }
                    }
                    GearState.REVERSE -> {
                        if (isGasPressed) {
                            speedKmh = (speedKmh - 15f * dt).coerceIn(-18f, 0f)
                        } else if (isBrakePressed) {
                            speedKmh = (speedKmh + 35f * dt).coerceAtMost(0f)
                        } else {
                            speedKmh = (speedKmh + 10f * dt).coerceAtMost(0f)
                        }
                    }
                    GearState.NEUTRAL -> {
                        if (isBrakePressed) {
                            speedKmh = (speedKmh - 60f * dt).coerceAtLeast(0f)
                        } else {
                            speedKmh = (speedKmh - 8f * dt).coerceAtLeast(0f)
                        }
                    }
                }

                // Steering Lateral Movement
                val speedFactor = (abs(speedKmh) / 60f).coerceIn(0.15f, 1.0f)
                val deltaX = (steeringAngle / 60f) * speedFactor * 1.6f * dt
                playerX = (playerX + deltaX).coerceIn(-1.1f, 1.1f)

                // Scroll & Distance
                val deltaDist = (speedKmh * (1000f / 3600f)) * dt
                distanceCoveredMeters += deltaDist
                roadScrollOffset += speedKmh * dt * 0.18f

                // Intersection & Parking Bay Tracking
                intersectionZ -= deltaDist
                if (intersectionZ < -20f) {
                    intersectionZ = 150f
                }

                targetParkingZ?.let { pz ->
                    val newPz = pz - deltaDist
                    targetParkingZ = newPz

                    // Check for successful parking stop
                    if (mission.missionType == MissionType.TARGET_PARKING && newPz in -3f..5f && abs(speedKmh) < 3f && abs(playerX - 0.25f) < 0.2f) {
                        isMissionSuccess = true
                        calculatedGrade = if (comfortPct > 85f && timeRemainingSec > 15f) "S" else "A"
                        soundHelper.playMissionClear()
                    }
                }

                // Check Traffic Light Violation (Red Light)
                if (intersectionZ in -2f..4f && trafficLight == TrafficLightState.RED && speedKmh > 10f) {
                    violationMessage = "RED LIGHT VIOLATION! -50 COINS"
                    comfortPct = (comfortPct - 25f).coerceAtLeast(10f)
                }

                // Traffic Vehicle Simulation & Collision
                for (v in cityVehicles) {
                    val vMps = v.speedKmh * (1000f / 3600f)
                    val relSpeedMps = if (v.lane < 0f) (-vMps - (speedKmh * 1000f / 3600f)) else (vMps - (speedKmh * 1000f / 3600f))
                    v.distanceZ += relSpeedMps * dt

                    // Recycle cars
                    if (v.distanceZ < -15f) {
                        v.distanceZ = 120f + Random.nextFloat() * 40f
                        v.speedKmh = 40f + Random.nextFloat() * 20f
                    }

                    // Collision Detection with Traffic
                    if (v.distanceZ in -1.2f..2.5f) {
                        val lateralDist = abs(playerX - (v.lane * 0.40f))
                        if (lateralDist < 0.22f) {
                            isCrashed = true
                            violationMessage = "TRAFFIC CRASH COLLISION!"
                            soundHelper.playCrash()
                        }
                    }
                }

                // Mission Success Conditions
                if (mission.missionType == MissionType.HIGHWAY_SPEED && distanceCoveredMeters >= mission.targetDistanceM) {
                    isMissionSuccess = true
                    calculatedGrade = if (speedKmh > 75f && comfortPct > 80f) "S" else "A+"
                    soundHelper.playMissionClear()
                } else if ((mission.missionType == MissionType.TURN_LEFT || mission.missionType == MissionType.TURN_RIGHT) && distanceCoveredMeters >= mission.targetDistanceM) {
                    isMissionSuccess = true
                    val usedBlinker = if (mission.missionType == MissionType.TURN_LEFT) isBlinkerLeft else isBlinkerRight
                    calculatedGrade = if (usedBlinker && comfortPct > 70f) "A+" else "B"
                    soundHelper.playMissionClear()
                } else if (mission.missionType == MissionType.FUEL_EFFICIENCY && distanceCoveredMeters >= mission.targetDistanceM) {
                    isMissionSuccess = true
                    calculatedGrade = if (fuelRemainingPct > 60f) "S" else "A"
                    soundHelper.playMissionClear()
                } else if (mission.missionType == MissionType.VIP_COMFORT && distanceCoveredMeters >= mission.targetDistanceM) {
                    isMissionSuccess = true
                    calculatedGrade = if (comfortPct > 90f) "S" else "A"
                    soundHelper.playMissionClear()
                }
            }
        }
    }

    // -------------------------------------------------------------
    // USER INTERFACE & DR. DRIVING HUD
    // -------------------------------------------------------------
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18181B))
    ) {
        // 1. 3D City & Handlebar Canvas
        DrRidingCanvas(
            modifier = Modifier.fillMaxSize(),
            playerX = playerX,
            steeringAngleDegrees = steeringAngle,
            speedKmh = speedKmh,
            roadScrollOffset = roadScrollOffset,
            currentIntersectionZ = intersectionZ,
            trafficLight = trafficLight,
            cityVehicles = cityVehicles,
            bikeSpec = bikeSpec,
            bikeColorHex = bikeState.selectedColorHex,
            cameraMode = cameraMode,
            isBlinkingLeft = isBlinkerLeft,
            isBlinkingRight = isBlinkerRight,
            isBraking = isBrakePressed,
            targetParkingZ = targetParkingZ
        )

        // 2. TOP HUD: GPS INSTRUCTION, SPEEDO, FUEL & COMFORT
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            // GPS Mission Banner (Dr. Driving signature yellow/green banner)
            Surface(
                color = Color(0xFF18181B),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFD600)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(mission.missionType.icon, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = mission.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFD600)
                            )
                            Text(
                                text = mission.missionType.instruction,
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    // Countdown Timer
                    Surface(color = Color(0xFF27272A), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = "${timeRemainingSec.toInt()}s",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = if (timeRemainingSec < 15f) Color(0xFFFF1744) else Color(0xFF00E676),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sub-metrics: Speed readout, Fuel bar, Comfort bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Digital Speedometer (km/h)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${abs(speedKmh).toInt()}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "km/h",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }

                // Fuel & Comfort Gauges
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Fuel
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⛽", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        LinearProgressIndicator(
                            progress = { fuelRemainingPct / 100f },
                            modifier = Modifier.width(55.dp).height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = if (fuelRemainingPct > 25f) Color(0xFF00E676) else Color(0xFFFF1744),
                            trackColor = Color(0xFF3F3F46)
                        )
                    }

                    // Comfort
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🚸", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        LinearProgressIndicator(
                            progress = { comfortPct / 100f },
                            modifier = Modifier.width(55.dp).height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF00E5FF),
                            trackColor = Color(0xFF3F3F46)
                        )
                    }
                }

                // Camera Switch & Pause
                Row {
                    IconButton(
                        onClick = {
                            cameraMode = if (cameraMode == CameraViewMode.FIRST_PERSON_COCKPIT) {
                                CameraViewMode.THIRD_PERSON_CHASE
                            } else {
                                CameraViewMode.FIRST_PERSON_COCKPIT
                            }
                        },
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0x88000000))
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = { isPaused = !isPaused },
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0x88000000))
                    ) {
                        Icon(imageVector = Icons.Default.Pause, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Violation Warning Banner
            violationMessage?.let { msg ->
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(0xDDFF1744),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = msg,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // 3. DR. DRIVING CONTROLS (Steering wheel, pedals, gear shifter, blinkers, horn)
        DrDrivingControls(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            steeringAngle = steeringAngle,
            onSteeringChange = { steeringAngle = it },
            onSteeringRelease = {
                // Smooth spring auto-centering
                steeringAngle = 0f
            },
            isGasPressed = isGasPressed,
            onGasPressed = { isGasPressed = it },
            isBrakePressed = isBrakePressed,
            onBrakePressed = { isBrakePressed = it },
            gearState = gearState,
            onGearChange = {
                gearState = it
                soundHelper.playGearShift()
            },
            isBlinkerLeft = isBlinkerLeft,
            onToggleBlinkerLeft = {
                isBlinkerLeft = !isBlinkerLeft
                if (isBlinkerLeft) isBlinkerRight = false
            },
            isBlinkerRight = isBlinkerRight,
            onToggleBlinkerRight = {
                isBlinkerRight = !isBlinkerRight
                if (isBlinkerRight) isBlinkerLeft = false
            },
            onHonk = { soundHelper.playHorn() }
        )

        // 4. DR. DRIVING MISSION SUCCESS MODAL (GRADE S / A / B Stamp!)
        if (isMissionSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xEE090D16)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color(0xFFFAFAFA), // Classic Dr. Driving clean white card
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFFFFD600)),
                    modifier = Modifier.padding(24.dp).fillMaxWidth(0.90f)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "MISSION COMPLETE!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF18181B)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Big Dr. Driving Grade Stamp (S / A+ / B)
                        Surface(
                            color = Color(0xFFFFD600),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(76.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = calculatedGrade,
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Distance: ${distanceCoveredMeters.toInt()}m  •  Fuel Left: ${fuelRemainingPct.toInt()}%",
                            fontSize = 13.sp,
                            color = Color(0xFF52525B)
                        )
                        Text(
                            text = "Comfort Score: ${comfortPct.toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Rewards
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Surface(color = Color(0xFFFEF3C7), shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "+${mission.coinReward} 🪙 Gold",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Surface(color = Color(0xFFE0F2FE), shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "+${mission.rubyReward} 💎 Rubies",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0369A1),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                onCompleteMission(calculatedGrade, mission.coinReward, mission.rubyReward, distanceCoveredMeters.toInt())
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF18181B)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("next_mission_button")
                        ) {
                            Text("NEXT MISSION", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // 5. MISSION FAILED / CRASH MODAL
        if (isCrashed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xEE090D16)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color(0xFF18181B),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFF1744)),
                    modifier = Modifier.padding(24.dp).fillMaxWidth(0.90f)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "MISSION FAILED!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFF1744)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = violationMessage ?: "Bike crashed into traffic",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                isCrashed = false
                                speedKmh = 0f
                                fuelRemainingPct = 100f
                                comfortPct = 100f
                                timeRemainingSec = mission.timeLimitSec.toFloat()
                                intersectionZ = 85f
                                violationMessage = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("retry_mission_button")
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("RETRY MISSION", color = Color.Black, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = onExitMission,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27272A)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("BACK TO GARAGE", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

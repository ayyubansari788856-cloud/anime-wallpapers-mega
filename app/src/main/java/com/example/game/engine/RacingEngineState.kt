package com.example.game.engine

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

enum class CameraViewMode {
    THIRD_PERSON_CHASE,
    FIRST_PERSON_COCKPIT
}

enum class EnvironmentBiome(val title: String, val skyTop: Long, val skyBottom: Long, val roadColor: Long, val neonAccent: Long) {
    NEO_TOKYO("Neo Tokyo Night", 0xFF050510, 0xFF140D2B, 0xFF1A1A24, 0xFF00E5FF),
    SUNSET_CANYON("Sunset Canyon", 0xFF2A0845, 0xFFFF6B6B, 0xFF262121, 0xFFFFAB00),
    CYBER_GRID("Cyber Grid City", 0xFF020B14, 0xFF003366, 0xFF0F1A24, 0xFF00FF66),
    STORM_HIGHWAY("Storm Highway", 0xFF0A0F14, 0xFF1B2838, 0xFF181C20, 0xFF76FF03)
}

enum class VehicleType(
    val lengthMeters: Float,
    val widthMeters: Float,
    val baseSpeedKmh: Float,
    val color: Long,
    val isHeavyTruck: Boolean = false
) {
    SEDAN(4.2f, 1.8f, 120f, 0xFF3D5AFE),
    TAXI(4.2f, 1.8f, 110f, 0xFFFFD600),
    SUV(4.6f, 2.0f, 130f, 0xFFD50000),
    SPORTS_CAR(4.4f, 1.9f, 165f, 0xFFFF1744),
    SEMI_TRUCK(12.0f, 2.4f, 95f, 0xFF455A64, isHeavyTruck = true),
    POLICE(4.4f, 1.9f, 175f, 0xFF2979FF)
}

data class TrafficVehicle(
    val id: Long,
    val type: VehicleType,
    var lane: Float,          // -1.5, -0.5, 0.5, 1.5
    var distanceZ: Float,     // Distance ahead of player in meters (0 to 350)
    var speedKmh: Float,
    var targetLane: Float = lane,
    var laneChangeProgress: Float = 0f,
    var isPassed: Boolean = false
)

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var life: Float,
    var maxLife: Float,
    var color: Long,
    var size: Float
)

data class NearMissEvent(
    val bonusScore: Int,
    val comboMultiplier: Int,
    val timestamp: Long = System.currentTimeMillis()
)

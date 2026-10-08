package com.example.game.engine

enum class GearState {
    DRIVE,
    NEUTRAL,
    REVERSE
}

enum class TrafficLightState {
    GREEN,
    YELLOW,
    RED
}

data class CityVehicle(
    val id: Long,
    val type: VehicleType,
    var lane: Float,          // -1.5 (oncoming fast), -0.5 (oncoming slow), 0.5 (player left lane), 1.5 (player right lane)
    var distanceZ: Float,     // Distance ahead in meters
    var speedKmh: Float,
    var isBraking: Boolean = false,
    var blinkerLeft: Boolean = false,
    var blinkerRight: Boolean = false
)

data class Intersection(
    val distanceZ: Float,
    var lightState: TrafficLightState = TrafficLightState.GREEN,
    val hasCrosswalk: Boolean = true,
    val turnPrompt: String? = null // "TURN_LEFT", "TURN_RIGHT", null
)

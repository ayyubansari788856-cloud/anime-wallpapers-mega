package com.example.game.model

enum class BikeClass(val displayName: String) {
    SCOOTER("City Scooter 100cc"),
    COMMUTER("Classic 125cc"),
    STREET("Sport Naked 250cc"),
    SPORT("Twin Sport 400cc"),
    CRUISER("Highway V-Twin 750cc"),
    SUPERBIKE("Superbike 1000cc")
}

data class BikeSpec(
    val id: String,
    val name: String,
    val bikeClass: BikeClass,
    val topSpeedKmh: Int,
    val accelerationRating: Float,
    val handlingRating: Float,
    val fuelEfficiencyRating: Float,
    val priceCoins: Int,
    val priceGems: Int,
    val defaultColorHex: Long,
    val description: String
)

data class PlayerBikeState(
    val bikeId: String,
    val isUnlocked: Boolean = false,
    val speedLevel: Int = 1,
    val accelLevel: Int = 1,
    val handlingLevel: Int = 1,
    val fuelLevel: Int = 1,
    val selectedColorHex: Long = 0xFFE50914
)

enum class MissionType(val title: String, val icon: String, val instruction: String) {
    HIGHWAY_SPEED("Highway Speed", "⚡", "Maintain speed over 70 km/h without crashing"),
    TURN_LEFT("Turn Left", "◀", "Use left indicator and make a safe 90° left turn"),
    TURN_RIGHT("Turn Right", "▶", "Signal right, yield to traffic, and complete right turn"),
    FUEL_EFFICIENCY("Fuel Efficiency", "⛽", "Reach destination while consuming minimum fuel"),
    TARGET_PARKING("Precision Parking", "🅿️", "Navigate to the designated green bay and stop smoothly"),
    VIP_COMFORT("VIP Smooth Ride", "🚸", "Keep comfort level high by avoiding hard brakes and jerks"),
    FREE_DRIVE("Free City Ride", "🏙️", "Explore city avenues and master your riding skills")
}

data class DrivingMission(
    val id: Int,
    val missionType: MissionType,
    val title: String,
    val targetDistanceM: Int,
    val timeLimitSec: Int,
    val targetSpeedKmh: Int = 60,
    val coinReward: Int,
    val rubyReward: Int,
    val isUnlocked: Boolean = false,
    val bestGrade: String? = null
)

data class UserGameProfile(
    val coins: Int = 5000,
    val gems: Int = 50,
    val driverLevel: Int = 1,
    val driverScore: Int = 0,
    val totalMissionsCompleted: Int = 0,
    val activeBikeId: String = "koma_100",
    val isVipMember: Boolean = false,
    val hasAdFree: Boolean = false
)

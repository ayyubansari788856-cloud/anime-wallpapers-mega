package com.example.game.model

object BikeCatalog {
    val allBikes = listOf(
        BikeSpec(
            id = "koma_100",
            name = "Koma 100",
            bikeClass = BikeClass.SCOOTER,
            topSpeedKmh = 75,
            accelerationRating = 0.50f,
            handlingRating = 0.90f,
            fuelEfficiencyRating = 0.95f,
            priceCoins = 0,
            priceGems = 0,
            defaultColorHex = 0xFFE50914, // Iconic Dr. Driving Red
            description = "The classic starter scooter. Highly maneuverable, light throttle response, and superior fuel economy for city traffic."
        ),
        BikeSpec(
            id = "chavit_125",
            name = "Chavit 125",
            bikeClass = BikeClass.COMMUTER,
            topSpeedKmh = 95,
            accelerationRating = 0.65f,
            handlingRating = 0.85f,
            fuelEfficiencyRating = 0.88f,
            priceCoins = 5000,
            priceGems = 15,
            defaultColorHex = 0xFFFAFAFA, // Pearl White
            description = "Urban commuter bike with smooth linear power delivery and stable front fork suspension for intersection turns."
        ),
        BikeSpec(
            id = "bari_250",
            name = "Bari 250",
            bikeClass = BikeClass.STREET,
            topSpeedKmh = 135,
            accelerationRating = 0.78f,
            handlingRating = 0.82f,
            fuelEfficiencyRating = 0.75f,
            priceCoins = 18000,
            priceGems = 40,
            defaultColorHex = 0xFFFFD600, // Vibrant Yellow
            description = "Sport naked roadster with punchy mid-range torque, aggressive front disc brakes, and sharp lane-weaving agility."
        ),
        BikeSpec(
            id = "mirage_400",
            name = "Mirage 400",
            bikeClass = BikeClass.SPORT,
            topSpeedKmh = 175,
            accelerationRating = 0.88f,
            handlingRating = 0.80f,
            fuelEfficiencyRating = 0.65f,
            priceCoins = 45000,
            priceGems = 90,
            defaultColorHex = 0xFF1976D2, // Royal Blue
            description = "Parallel twin sport motorcycle with aerodynamic windscreen and excellent stability for high-speed highway missions."
        ),
        BikeSpec(
            id = "pegasus_750",
            name = "Pegasus 750",
            bikeClass = BikeClass.CRUISER,
            topSpeedKmh = 160,
            accelerationRating = 0.82f,
            handlingRating = 0.70f,
            fuelEfficiencyRating = 0.70f,
            priceCoins = 85000,
            priceGems = 150,
            defaultColorHex = 0xFF212121, // Matte Black
            description = "Heavy V-Twin highway cruiser with low center of gravity, deep exhaust note, and supreme comfort rating."
        ),
        BikeSpec(
            id = "venom_1000",
            name = "Venom 1000",
            bikeClass = BikeClass.SUPERBIKE,
            topSpeedKmh = 230,
            accelerationRating = 0.98f,
            handlingRating = 0.92f,
            fuelEfficiencyRating = 0.50f,
            priceCoins = 180000,
            priceGems = 300,
            defaultColorHex = 0xFFFF1744, // Crimson Red & White
            description = "The flagship hyper sportbike. Incomparable acceleration and top speed for elite Dr. Riding drivers."
        )
    )

    fun getBikeById(id: String): BikeSpec {
        return allBikes.find { it.id == id } ?: allBikes.first()
    }
}

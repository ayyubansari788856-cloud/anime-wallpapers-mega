package com.example.wallpaper.model

data class AnimeWallpaper(
    val id: String,
    val title: String,
    val animeTitle: String,
    val characterName: String,
    val category: String,
    val imageUrl: String,
    val resolution: String = "4K UHD (2160x3840)",
    val views: Int = 1240,
    val downloads: Int = 340,
    val likes: Int = 890,
    val isFavorite: Boolean = false,
    val tags: List<String> = listOf("Anime", "Aesthetic", "4K")
)

enum class WallpaperCategory(val displayName: String, val icon: String) {
    ALL("All (1,500+)", "🌟"),
    DUKE_BIKES("KTM Duke & Bikes", "🏍️"),
    SUPERCARS("Supercars 4K", "🏎️"),
    ANIME("Anime 4K", "🎌"),
    ANIMALS("Wild Animals", "🦁"),
    FRUITS("Fruits & Aesthetic", "🍓"),
    NARUTO("Naruto Shippuden", "🍥"),
    DEMON_SLAYER("Demon Slayer", "🗡️"),
    JUJUTSU_KAISEN("Jujutsu Kaisen", "👁️"),
    SOLO_LEVELING("Solo Leveling", "⚔️"),
    DRAGON_BALL("Dragon Ball Z", "🐉"),
    AMOLED_DARK("AMOLED & Neon", "🌌")
}

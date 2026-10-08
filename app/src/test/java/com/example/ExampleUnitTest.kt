package com.example

import com.example.data.local.MicroTask
import com.example.data.local.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun userProfile_initialValuesCorrect() {
    val profile = UserProfile(
      name = "Aura Traveler",
      currentStreak = 5,
      bestStreak = 14,
      auraCoins = 320,
      totalXp = 680,
      level = 3,
      streakFreezes = 2,
      walletCashBalance = 45.0,
      availableScratchCards = 1
    )

    assertEquals("Aura Traveler", profile.name)
    assertEquals(5, profile.currentStreak)
    assertEquals(14, profile.bestStreak)
    assertEquals(320, profile.auraCoins)
    assertEquals(45.0, profile.walletCashBalance, 0.001)
    assertEquals(1, profile.availableScratchCards)
    assertFalse(profile.sunSparkEarnedToday)
    assertFalse(profile.moonSparkEarnedToday)
  }

  @Test
  fun microTask_creationAndRewards() {
    val morningTask = MicroTask(
      title = "Hydrate & Awaken",
      subtitle = "Drink 1 glass of water",
      category = "DAWN",
      durationSeconds = 90,
      taskType = "WATER",
      xpReward = 40,
      coinReward = 15
    )

    assertEquals("DAWN", morningTask.category)
    assertEquals(90, morningTask.durationSeconds)
    assertEquals(40, morningTask.xpReward)
    assertEquals(15, morningTask.coinReward)
    assertFalse(morningTask.isCompleted)
  }
}

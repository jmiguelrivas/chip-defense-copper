package com.rama.chipdefense_copper

import android.content.res.Resources

class PurseOfCoins(
    val gameMechanics: GameMechanics,
    private val levelMode: GameMechanics.LevelMode = GameMechanics.LevelMode.BASIC
)
/** Auxiliary object that holds the current amount of coins for a level mode
 *
 */
{
    // DO NOT change the names of the variables!
    data class Contents(
        /** total coins gathered in the game mode */
        var totalCoins: Int = 0,
        /** coins spent on heroes */
        var spentCoins: Int = 0,
        /** coins got from level rewards */
        var rewardCoins: Int = 0,
        /** coins caught running in the stages */
        var runningCoins: Int = 0,
        /** coins used to purchase lost lives */
        var coinsSpentOnPurchases: Int = 0,
        /** coins got as a one-time gift */
        var coinsGotAsGift: Int = 0
    )

    enum class ExpenditureType { HEROES, LIVES }

    companion object {
        fun coinsAsString(number: Int, resources: Resources): String {
            when (number) {
                0 -> return resources.getString(R.string.coins_none)
                1 -> return resources.getString(R.string.coins_singular)
                else -> return resources.getString(R.string.coins_plural).format(number)
            }
        }
    }

    /** whether this purse has already been used. Must be migrated otherwise */
    var initialized: Boolean = false

    var contents = Contents()

    fun addReward(amount: Int) {
        contents.totalCoins += amount
        contents.rewardCoins += amount
    }

    fun addGift(amount: Int): Int
            /** add a specific amount as a one-time gift.
             * @return the number of coins actually got. Will be 0 if the gift had been granted before. */
    {
        val actual = amount - contents.coinsGotAsGift
        if (actual > 0) {
            contents.totalCoins += actual
            contents.coinsGotAsGift = amount
            return actual
        } else
            return 0
    }

    fun spend(amount: Int, spendFor: ExpenditureType = ExpenditureType.HEROES) {
        when (spendFor) {
            ExpenditureType.HEROES -> contents.spentCoins += amount
            ExpenditureType.LIVES -> contents.coinsSpentOnPurchases += amount
        }
    }

    fun availableCoins(): Int {
        val value = contents.totalCoins - contents.spentCoins - contents.coinsSpentOnPurchases
        return if (value >= 0) value else 0
    }

    fun canAfford(price: Int): Boolean {
        return availableCoins() >= price
    }

    fun calculateInitialContents() {
        val summaries = when (levelMode) {
            GameMechanics.LevelMode.BASIC ->
                gameMechanics.summaryPerNormalLevel.values

            GameMechanics.LevelMode.TURBO ->
                gameMechanics.summaryPerTurboLevel.values

            GameMechanics.LevelMode.ENDLESS ->
                gameMechanics.summaryPerEndlessLevel.values
        }

        val rewardCoins = summaries.sumOf { it.coinsGot }

        contents.rewardCoins = rewardCoins
        contents.runningCoins = 0 // legacy, or compute per-mode if needed
        contents.spentCoins = 0
        contents.coinsSpentOnPurchases = 0
        contents.totalCoins = rewardCoins

    }

}
@file:Suppress("DEPRECATION")

package com.rama.chipdefense_copper.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.rama.chipdefense_copper.BaseFullscreenActivity
import com.rama.chipdefense_copper.GameMechanics
import com.rama.chipdefense_copper.Persistency
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.Settings
import com.rama.chipdefense_copper.Stage

class WelcomeActivity : BaseFullscreenActivity() {
    private var info: PackageInfo? = null
    private var settings = Settings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        applySystemInsets(findViewById<View>(R.id.root))

        info = packageManager.getPackageInfo(
                this.packageName,
                PackageManager.GET_ACTIVITIES
        )

        // migrate preferences
        val prefsLegacy = getSharedPreferences(Persistency.filename_legacy, MODE_PRIVATE)
        val prefsSettings = getSharedPreferences(Persistency.filename_settings, MODE_PRIVATE)
        settings.migrateSettings(prefsLegacy, prefsSettings)
    }

    private var gameState: String? = null
    private var nextLevelToPlay = Stage.Identifier()
    private var maxLevel = Stage.Identifier()
    private var turboSeriesAvailable = false
    private var endlessSeriesAvailable = false

    private fun determineLevels(prefs: SharedPreferences) {
        maxLevel.series = prefs.getInt("MAXSERIES", 1)
        maxLevel.number = prefs.getInt("MAXSTAGE", 0)
        nextLevelToPlay.series = prefs.getInt("LASTSERIES", 1)
        nextLevelToPlay.number = prefs.getInt("LASTSTAGE", 0)
        turboSeriesAvailable = prefs.getBoolean("TURBO_AVAILABLE", false)
        endlessSeriesAvailable = prefs.getBoolean("ENDLESS_AVAILABLE", false)
    }

    @Suppress("UNUSED_PARAMETER")
    fun showMaxLevelInfo(v: View) {
        /** displays the max level reached so far as graphical display */
        val seriesName = when (maxLevel.series) {
            GameMechanics.SERIES_NORMAL -> getString(R.string.name_series_1)
            GameMechanics.SERIES_TURBO -> getString(R.string.name_series_2)
            GameMechanics.SERIES_ENDLESS -> getString(R.string.name_series_3)
            else -> "???"  // shouldn't happen
        }
        val textToDisplay = getString(R.string.stage_reached).format(seriesName, maxLevel.number)
        Toast.makeText(this, textToDisplay, Toast.LENGTH_LONG).show()

    }

    private fun migrateLevelInfo(oldPrefs: SharedPreferences, newPrefs: SharedPreferences)
            /** gets the level info out of the "old" prefs file and puts it into the "new" one.
             * The keys are deleted from oldPrefs.
             * This function is used for upgrade to version 1.44.
             */
    {
        determineLevels(newPrefs)
        if (maxLevel.series == 1 && maxLevel.number == 0) {
            // no level info, try to use old values
            determineLevels(oldPrefs)
            newPrefs.edit().apply {
                putInt("MAXSERIES", maxLevel.series)
                putInt("MAXSTAGE", maxLevel.number)
                putInt("LASTSERIES", nextLevelToPlay.series)
                putInt("LASTSTAGE", nextLevelToPlay.number)
                putBoolean("TURBO_AVAILABLE", turboSeriesAvailable)
                putBoolean("ENDLESS_AVAILABLE", turboSeriesAvailable)
                apply()
            }
            oldPrefs.edit().apply {
                remove("MAXSERIES")
                remove("MAXSTAGE")
                remove("LASTSERIES")
                remove("LASTSTAGE")
                remove("TURBO_AVAILABLE")
                remove("ENDLESS_AVAILABLE")
                remove("STATUS")  // has also been migrated
                apply()
            }
        }
    }

    private fun setupButtons() {
        val isTurboAvailable = intent.getBooleanExtra("TURBO_AVAILABLE", false)
        val isEndlessAvailable = intent.getBooleanExtra("ENDLESS_AVAILABLE", false)

        val prefsState = getSharedPreferences(Persistency.filename_state, MODE_PRIVATE)
        val prefsLegacy = getSharedPreferences(Persistency.filename_legacy, MODE_PRIVATE)
        gameState = prefsState.getString("STATUS", "")
        determineLevels(prefsState)

        if (maxLevel.series == 1 && maxLevel.number == 0)
            migrateLevelInfo(prefsLegacy, prefsState)

        val buttonResume = findViewById<Button>(R.id.continueGameButton)

        buttonResume.text = when {
            // Level 0 but turbo mode available → play level 1 in turbo
            maxLevel.number == 0 && (isTurboAvailable || isEndlessAvailable) ->
                getString(R.string.play_level_x)
                    .format(Stage.numberToString(nextLevelToPlay.number, settings.showLevelsInHex))

            // Level 0 normal → start game
            maxLevel.number == 0 ->
                getString(R.string.button_start_game)

            // Play specific level
            gameState == "running" || gameState == "complete" ->
                getString(R.string.play_level_x)
                    .format(Stage.numberToString(nextLevelToPlay.number, settings.showLevelsInHex))

            // Unknown state → disable button
            else -> {
                buttonResume.isEnabled = false
                ""
            }
        }
    }


    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences(Persistency.filename_settings, MODE_PRIVATE)
        settings.loadFromFile(prefs)
        setupButtons()
    }

    fun resumeGame(@Suppress("UNUSED_PARAMETER") v: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("ACTIVATE_LOGGING", settings.activateLogging)
        when {
            maxLevel.number == 0 -> {
                // start new game
                intent.putExtra("RESET_PROGRESS", true)
                intent.putExtra("START_ON_STAGE", 1)
                intent.putExtra("START_ON_SERIES", 0)
                intent.putExtra("CONTINUE_GAME", false)
                startActivity(intent)
            }

            gameState == "running" -> {
                intent.putExtra("RESUME_GAME", true)
                startActivity(intent)
            }

            else -> {
                intent.putExtra("START_ON_STAGE", nextLevelToPlay.number)
                intent.putExtra("START_ON_SERIES", nextLevelToPlay.series)
                intent.putExtra("CONTINUE_GAME", false)
                startActivity(intent)
            }
        }
    }

    fun startLevelSelection(@Suppress("UNUSED_PARAMETER") v: View) {
        val intent = Intent(this, LevelSelectActivity::class.java)
        intent.putExtra("TURBO_AVAILABLE", turboSeriesAvailable)
        intent.putExtra("ENDLESS_AVAILABLE", endlessSeriesAvailable)
        intent.putExtra("NEXT_SERIES", nextLevelToPlay.series)
        startActivity(intent)
    }

    fun displaySettingsDialog(@Suppress("UNUSED_PARAMETER") v: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("MAXSERIES", maxLevel.series)
        startActivity(intent)
        setupButtons()
    }

    fun displayAboutDialog(@Suppress("UNUSED_PARAMETER") v: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    fun exitActivity(v: View) {
        finishAffinity()
    }
}
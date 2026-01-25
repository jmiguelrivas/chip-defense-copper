package com.rama.chipdefense_copper.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ContextThemeWrapper
import com.rama.chipdefense_copper.GameMechanics
import com.rama.chipdefense_copper.Persistency
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.Settings
import com.rama.chipdefense_copper.Stage
import com.google.android.material.tabs.TabLayout
import com.rama.chipdefense_copper.BaseFullscreenActivity
import com.rama.chipdefense_copper.utils.dp

@Suppress("DEPRECATION")
class LevelSelectActivity : BaseFullscreenActivity() {

    private var levels: HashMap<Int, Stage.Summary> = HashMap()
    private var selectedLevelView: Button? = null
    private var selectedLevel: Int = 0
    private var selectedSeries: Int = 0
    private var isTurboAvailable = false
    private var isEndlessAvailable = false
    private val settings = Settings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        isTurboAvailable = intent.getBooleanExtra("TURBO_AVAILABLE", false)
        isEndlessAvailable = intent.getBooleanExtra("ENDLESS_AVAILABLE", false)
        setContentView(R.layout.activity_level)
        applySystemInsets(findViewById<View>(R.id.root))
        setupSelector()
    }

    fun dismiss(@Suppress("UNUSED_PARAMETER") v: View) {
        finish()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        isTurboAvailable = intent.getBooleanExtra("TURBO_AVAILABLE", false)
        isEndlessAvailable = intent.getBooleanExtra("ENDLESS_AVAILABLE", false)
        setupSelector()
    }

    private fun setupSelector() {
        val prefs = getSharedPreferences(Persistency.filename_settings, MODE_PRIVATE)
        settings.loadFromFile(prefs)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        if (GameMechanics.makeAllLevelsAvailable) {
            isTurboAvailable = true
            isEndlessAvailable = true
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                prepareStageSelector(tab.position + 1)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        val currentSeries = intent.getIntExtra("NEXT_SERIES", GameMechanics.SERIES_NORMAL)
        prepareStageSelector(currentSeries)

        val tab = tabLayout.getTabAt(currentSeries - 1)
        tab?.select()
    }

    private fun nextLevelPossible(level: Int, series: Int): Boolean {
        return ((series == GameMechanics.SERIES_ENDLESS) || (level < GameMechanics.maxLevelAvailable))
    }

    private fun prepareStageSelector(series: Int) {
        val listView = findViewById<LinearLayout>(R.id.levelList)
        listView.removeAllViews()
        selectedSeries = series

        when (series) {
            GameMechanics.SERIES_NORMAL -> {
                levels = Persistency(this).loadStageSummaries(GameMechanics.SERIES_NORMAL)
                populateStageList(
                        listView, levels, GameMechanics.SERIES_NORMAL,
                )
            }

            GameMechanics.SERIES_TURBO -> {
                if (isTurboAvailable) {
                    levels = Persistency(this).loadStageSummaries(GameMechanics.SERIES_TURBO)
                    populateStageList(
                            listView, levels, GameMechanics.SERIES_NORMAL,
                    )
                } else {
                    val textView = TextView(ContextThemeWrapper(this, R.style.Content))
                    textView.text = getString(R.string.message_series_unavailable)
                    textView.setPadding(dp(16), dp(16), dp(16), dp(16))
                    listView.addView(textView)
                }
            }

            GameMechanics.SERIES_ENDLESS -> {
                if (isEndlessAvailable) {
                    levels = Persistency(this).loadStageSummaries(GameMechanics.SERIES_ENDLESS)
                    populateStageList(
                            listView, levels, GameMechanics.SERIES_ENDLESS,
                    )
                } else {
                    val textView = TextView(ContextThemeWrapper(this, R.style.Content))
                    textView.text = getString(R.string.message_endless_unavailable)
                    textView.setPadding(dp(16), dp(16), dp(16), dp(16))
                    listView.addView(textView)
                }
            }
        }
    }

    private fun populateStageList(
        listView: LinearLayout,
        stageSummary: HashMap<Int, Stage.Summary>,
        series: Int
    ) {
        if (stageSummary.isEmpty())
            stageSummary[1] = Stage.Summary()

        val highestLevelInList = ArrayList(stageSummary.keys).last()
        if (nextLevelPossible(highestLevelInList, series) && (stageSummary[highestLevelInList]?.won == true))
            stageSummary[highestLevelInList + 1] = Stage.Summary()

        for ((level, summary) in stageSummary.entries) {
            val levelEntryView = Button(ContextThemeWrapper(this, R.style.Content))

            val levelNumber = Stage.numberToString(level, settings.showLevelsInHex)
            var textString = " " + getString(R.string.level_entry).format(levelNumber)

            val coinsMaxAvailable = when {
                summary.coinsMaxAvailable > 0 -> summary.coinsMaxAvailable
                else -> summary.coinsAvailable + summary.coinsGot
            }

            if (coinsMaxAvailable > 0) {
                val formatString =
                    if (summary.coinsGot == 1)
                        resources.getString(R.string.coins_got)
                    else
                        resources.getString(R.string.coins_got_plural)

                textString =
                    textString.plus(" :: " + formatString.format(summary.coinsGot, coinsMaxAvailable))
            }

            levelEntryView.text = textString
            levelEntryView.setBackgroundColor(resources.getColor(R.color.background_tertiary_color))
            levelEntryView.gravity = Gravity.START
            levelEntryView.setPadding(dp(16), dp(22), dp(16), dp(22))

            when {
                (summary.won == true && (summary.coinsGot < summary.coinsMaxAvailable))
                    -> levelEntryView.setTextColor(resources.getColor(R.color.foreground_inactive_color))

                summary.won == true -> levelEntryView.setTextColor(resources.getColor(R.color.foreground_color))
                else -> levelEntryView.setTextColor(resources.getColor(R.color.foreground_color))
            }

            levelEntryView.isClickable = true
            levelEntryView.setOnClickListener { onLevelSelect(levelEntryView, level) }
            listView.addView(levelEntryView)

            if (!nextLevelPossible(level, series))
                break
        }
    }

    private fun onLevelSelect(v: View, level: Int) {
        selectedLevel = level

        selectedLevelView?.setBackgroundColor(resources.getColor(R.color.background_tertiary_color))
        selectedLevelView = v as Button
        selectedLevelView?.setBackgroundColor(resources.getColor(R.color.stage_selection_background_color))
    }

    fun startGame(@Suppress("UNUSED_PARAMETER") v: View) {
        if (selectedLevel == 0) return

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("START_ON_STAGE", selectedLevel)
        intent.putExtra("START_ON_SERIES", selectedSeries)
        intent.putExtra("CONTINUE_GAME", false)
        startActivity(intent)
        finish()
    }
}

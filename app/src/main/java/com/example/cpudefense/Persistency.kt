package com.example.cpudefense

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cpudefense.activities.GameActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.File

class Persistency(private val activity: Activity)
{
    @Suppress("ConstPropertyName")
    companion object
    {
        // DO NOT change the file names,
        // this would break the saved games.
        const val filename_legacy      = "prefs.xml"
        const val filename_settings    = "settings"
        const val filename_structure   = "structure"
        const val filename_thumbnails  = "thumbnails"
        const val filename_saves       = "saves"
        const val filename_state       = "state"

        // Export / Import
        const val EXPORT_FILENAME = "cpudefense_backup.json"
    }

    // Gson instance reused everywhere
    private val gson = Gson()

    // helper to access SharedPreferences safely
    private fun prefs(name: String): SharedPreferences =
        activity.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)

    // define preferences files
    /** file that holds all settings and preferences.
     * For historical reasons, there are some more data in this file, such as:
     *
     */
    private val prefsLegacy: SharedPreferences = prefs(filename_legacy)

    /** file for the user's preferences and settings */
    @Suppress("unused")
    private val prefsSettings: SharedPreferences = prefs(filename_settings)

    /** file that holds the structure of levels without any attackers. Used in ENDLESS series. */
    private val prefsStructure: SharedPreferences = prefs(filename_structure)

    /** file for the small thumbnails that are displayed in the level selector */
    private val prefsThumbnails: SharedPreferences = prefs(filename_thumbnails)

    /** file for the overall game progress, such as heroes or coins in the purse */
    private val prefsSaves: SharedPreferences = prefs(filename_saves)

    /** file for the state within the current level, such as attacker and chip positions, number of waves, etc. */
    private val prefsState: SharedPreferences = prefs(filename_state)

    /* ====================================================
     * EXPORT / IMPORT SUPPORT
     * ==================================================== */

    private val allPrefFiles = listOf(
            filename_legacy,
            filename_settings,
            filename_structure,
            filename_thumbnails,
            filename_saves,
            filename_state
    )

    /**
     * Export ALL SharedPreferences into a single JSON file.
     * This does NOT alter any existing game state.
     */
    fun exportAllDataToFile(): String {
        return try {
            val exportData = HashMap<String, Map<String, Any?>>()

            for (file in allPrefFiles) {
                val prefsMap = prefs(file).all

                // Convert raw map into a clean JSON map
                val cleanedMap = prefsMap.mapValues { (_, value) ->
                    parseJsonIfNeeded(value)
                }

                exportData[file] = cleanedMap
            }

            val json = gson.toJson(exportData)

            val file = File(
                    activity.getExternalFilesDir(null),
                    EXPORT_FILENAME
            )

            file.writeText(json)

            toast("Export completed:\n${file.absolutePath}")

            json
        } catch (e: Exception) {
            toast("Export failed: ${e.message}")
            ""
        }
    }

    private fun parseJsonIfNeeded(value: Any?): Any? {
        return if (value is String && looksLikeJson(value)) {
            try {
                gson.fromJson(value, Any::class.java)
            } catch (e: Exception) {
                value
            }
        } else {
            value
        }
    }

    private fun looksLikeJson(value: String): Boolean {
        val trimmed = value.trim()
        return (trimmed.startsWith("{") && trimmed.endsWith("}")) ||
                (trimmed.startsWith("[") && trimmed.endsWith("]"))
    }

    fun exportAllDataToUri(uri: android.net.Uri): Boolean {
        return try {
            val folder = androidx.documentfile.provider.DocumentFile.fromTreeUri(activity, uri)
                ?: return false

            val file = folder.createFile("application/json", "cpudefense_backup.json")
                ?: return false

            val outputStream = activity.contentResolver.openOutputStream(file.uri)
                ?: return false

            val allData = exportAllDataToFile()

            if (allData.isEmpty()) return false

            outputStream.use { out ->
                out.write(allData.toByteArray())
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    /**
     * Import ALL SharedPreferences from a previously exported JSON file.
     * Existing preferences are cleared BEFORE restore.
     */
    fun importAllDataFromUri(uri: android.net.Uri): Boolean {
        return try {
            val json = activity.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
                ?: return false

            val type = object : TypeToken<Map<String, Map<String, Any?>>>() {}.type
            val imported: Map<String, Map<String, Any?>> = gson.fromJson(json, type)

            for ((fileName, entries) in imported) {
                val editor = prefs(fileName).edit()
                editor.clear()

                for ((key, value) in entries) {
                    when (value) {
                        is String  -> editor.putString(key, value)
                        is Boolean -> editor.putBoolean(key, value)
                        is Int     -> editor.putInt(key, value)
                        is Long    -> editor.putLong(key, value)
                        is Float   -> editor.putFloat(key, value)
                        else       -> editor.putString(key, gson.toJson(value))
                    }
                }
                editor.apply()
            }

            toast("Import completed. Restart the game.")
            true
        } catch (e: Exception) {
            toast("Import failed: ${e.message}")
            false
        }
    }


    private fun toast(msg: String)
    {
        activity.runOnUiThread {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }
    }

    data class SerializableStateData (
        val general: GameMechanics.StateData,
        val stage: Stage.Data?
    )

    // designators of the level data in the prefs
    private val seriesKey = hashMapOf(
            GameMechanics.SERIES_NORMAL to "levels",
            GameMechanics.SERIES_TURBO to "levels_series2",
            GameMechanics.SERIES_ENDLESS to "levels_endless"
    )

    data class SerializableLevelSummary (
        val level: HashMap<Int, Stage.Summary> = HashMap()
    )

    data class SerializableLevelData (
        val level: HashMap<Int, Stage.Data> = HashMap()
    )

    data class SerializableHeroData (
        val upgrades: MutableList<Hero.Data> = mutableListOf()
    )

    data class SerializableHeroDataPerMode (
        val basic: MutableList<Hero.Data> = mutableListOf(),
        val endless: MutableList<Hero.Data> = mutableListOf()
    )

    data class SerializablePurseContents (
        var basic: PurseOfCoins.Contents,
        var endless: PurseOfCoins.Contents,
    )

    data class SerializableHolidays (
        val period: HashMap<Int, Hero.Holiday> = HashMap(),
    )

    fun saveCurrentLevelState(gameMechanics: GameMechanics)
            /** saves the state of the current level, i.e. the layout of the board, the level of chips,
             * position of attackers, etc., and also their waves.
             */
    {
        val editor = prefsSaves.edit()
        val json = gson.toJson(gameMechanics.currentlyActiveStage?.provideData())
        editor.putString("currentstage", json)
        editor.apply()
    }

    fun saveGeneralState(gameMechanics: GameMechanics)
            /** saves the state of the current level (but not the level layout).
             * This includes current lives, speed control settings, game phase.
             */
    {
        val editor = prefsState.edit()
        val json = gson.toJson(gameMechanics.state)
        editor.putString("GENERAL", json)
        editor.apply()
    }

    fun saveCoins(gameMechanics: GameMechanics)
    {
        val emptyContents = PurseOfCoins.Contents()
        val purseData = SerializablePurseContents(basic = emptyContents, endless = emptyContents)
        gameMechanics.purseOfCoins[GameMechanics.LevelMode.BASIC]?.contents?.let { purse -> purseData.basic = purse }
        gameMechanics.purseOfCoins[GameMechanics.LevelMode.ENDLESS]?.contents?.let { purse -> purseData.endless = purse }
        prefsSaves.edit().putString("coins", gson.toJson(purseData)).apply()
    }

    private fun saveCompleteState(gameMechanics: GameMechanics?)
            /** saves all data that is needed to continue a game later.
             * This includes levels completed and coins got,
             * but also the state of the currently running game.
             */
    {
        val editor = prefsLegacy.edit()
        gameMechanics?.let {
            // save global data:
            var json = gson.toJson(it.global)
            editor.putString("global", json)

            // save current game state:
            val stateData = SerializableStateData(
                    general = it.state,
                    stage = it.currentlyActiveStage?.provideData()
            )
            json = gson.toJson(stateData)
            editor.putString("state", json)

            // save upgrades got so far:
            saveHeroes(it)

            // save level data:
            saveAllStageSummaries(it)
            editor.apply()

            // save coins in purse:
            val emptyContents = PurseOfCoins.Contents()
            val purseData = SerializablePurseContents(basic = emptyContents, endless = emptyContents)
            gameMechanics.purseOfCoins[GameMechanics.LevelMode.BASIC]?.contents?.let { purse -> purseData.basic = purse }
            gameMechanics.purseOfCoins[GameMechanics.LevelMode.ENDLESS]?.contents?.let { purse -> purseData.endless = purse }
            prefsSaves.edit().putString("coins", gson.toJson(purseData)).commit()
        }
    }

    fun saveStageSummaries(gameMechanics: GameMechanics, series: Int)
            /** saves all summaries of the levels that belong to the given series
             */
    {
        val editor = prefsSaves.edit()
        val data = when (series)
        {
            GameMechanics.SERIES_NORMAL -> SerializableLevelSummary(gameMechanics.summaryPerNormalLevel)
            GameMechanics.SERIES_TURBO -> SerializableLevelSummary(gameMechanics.summaryPerTurboLevel)
            GameMechanics.SERIES_ENDLESS -> SerializableLevelSummary(gameMechanics.summaryPerEndlessLevel)
            else -> null
        }
        data?.let {
            val json = gson.toJson(it)
            editor.putString(seriesKey[series], json)
            editor.apply()
        }
    }

    private fun saveAllStageSummaries(gameMechanics: GameMechanics?)
    {
        gameMechanics?.let {
            saveStageSummaries(it, GameMechanics.SERIES_NORMAL)
            saveStageSummaries(it, GameMechanics.SERIES_TURBO)
            saveStageSummaries(it, GameMechanics.SERIES_ENDLESS)
        }
    }

    fun saveThumbnailOfLevel(gameActivity: GameActivity, stage: Stage)
    {
        val editor = prefsThumbnails.edit()
        val levelIdent = stage.data.ident
        if (levelIdent.number != 0) {
            val outputStream = ByteArrayOutputStream()
            val snapshot: Bitmap? = when (levelIdent.series)
            {
                GameMechanics.SERIES_ENDLESS -> gameActivity.levelThumbnailEndless[levelIdent.number]
                else -> gameActivity.levelThumbnail[levelIdent.number]
            }
            snapshot?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val encodedImage: String =
                Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            val key: String = when (levelIdent.series)
            {
                GameMechanics.SERIES_ENDLESS -> "thumbnail_%d_endless".format(levelIdent.number)
                else -> "thumbnail_%d".format(levelIdent.number)
            }
            editor.putString(key, encodedImage)
            editor.apply()
        }
    }

    fun saveHeroes(gameMechanics: GameMechanics)
    {
        // remove deprecated hero saves (prior to 1.34)
        var editor = prefsLegacy.edit()
        editor.remove("upgrades")
        editor.apply()

        editor = prefsSaves.edit()
        val heroData = SerializableHeroDataPerMode()
        gameMechanics.heroesByMode[GameMechanics.LevelMode.BASIC]?.values?.forEach { hero -> heroData.basic.add(hero.data) }
        gameMechanics.heroesByMode[GameMechanics.LevelMode.ENDLESS]?.values?.forEach { hero -> heroData.endless.add(hero.data) }
        editor.putString("heroes", gson.toJson(heroData))
        editor.apply()
    }

    fun saveHolidays(gameMechanics: GameMechanics)
    {
        val editor = prefsSaves.edit()
        val data = SerializableHolidays(gameMechanics.holidays)
        val json = gson.toJson(data)
        editor.putString("holidays", json)
        editor.apply()
    }

    fun loadState(gameMechanics: GameMechanics?)
    {
        gameMechanics?.let {
            // get level data
            loadAllStageSummaries(it)

            // get global data
            loadGlobalData(it)

            // get upgrades
            loadAllHeroes(it)

            // get state of running game
            loadGeneralState(it)

            val json = prefsLegacy.getString("state", "none")
            if (json != "none") {
                val data: SerializableStateData =
                    gson.fromJson(json, SerializableStateData::class.java)
                it.state = data.general
                it.stageData = data.stage
                (activity as GameActivity).setGameSpeed(it.state.speed)  // restore game speed mode
            }

            // load contents of purses
            loadCoins(gameMechanics)
        }
    }

    fun loadGlobalData(gameMechanics: GameMechanics)
            /** retrieve some global game data, such as total number of coins.
            Saving is done in saveState().
            DEPRECATED.
             */
    {
        val json = prefsLegacy.getString("global", "none")
        if (json == "none")
            gameMechanics.global = GameMechanics.GlobalData()
        else
            gameMechanics.global = gson.fromJson(json, GameMechanics.GlobalData::class.java)
    }

    fun loadCurrentLevelState(gameMechanics: GameMechanics)
            /** retrieves the state of the current level, i.e. the layout of the board, the level of chips,
             * position of attackers, etc.
             */
    {
        val key = "currentstage"
        val json = prefsSaves.getString(key, "none")
        if (json != "none") {
            gameMechanics.stageData =
                gson.fromJson(json, Stage.Data::class.java)
        }
        prefsLegacy.edit().let { it.remove(key); it.apply() }
    }

    fun loadGeneralState(gameMechanics: GameMechanics)
            /** loads the state of the current level (but not the level layout).
             * This includes current lives, speed control settings, game phase.
             */
    {
        val key = "GENERAL"
        val json = prefsState.getString(key, "none")
        if (json != "none") {
            gameMechanics.state =
                gson.fromJson(json, GameMechanics.StateData::class.java)
        }
        prefsLegacy.edit().let { it.remove(key); it.apply() }
    }

    fun loadStageSummaries(series: Int): HashMap<Int, Stage.Summary>
            /** loads the summaries for the given series (1, 2, ...).
             * @param series one of GameMechanics.SERIES_NORMAL, _TURBO, _ENDLESS
             * @return the set of all level summaries, or null if none can be found (or the series doesn't exist)
             */
    {
        try {
            val key = seriesKey[series]
            var json = prefsSaves.getString(key, "none")
            if (json == "none")
            {
                // Migration hack: If the data is not in saves.xml, get it from the legacy file and then delete it there.
                json = prefsLegacy.getString(seriesKey[series], "none")
                // prefsLegacy.edit().let { it.remove(key); it.apply() } // TODO: activate this again in later versions
            }
            val data: SerializableLevelSummary =
                gson.fromJson(json, SerializableLevelSummary::class.java)
            return data.level
        }
        catch (e: Exception)
        {
            return hashMapOf()
        }
    }

    fun loadAllStageSummaries(gameMechanics: GameMechanics)
    {
        gameMechanics.summaryPerNormalLevel = loadStageSummaries(GameMechanics.SERIES_NORMAL)
        gameMechanics.summaryPerTurboLevel = loadStageSummaries(GameMechanics.SERIES_TURBO)
        gameMechanics.summaryPerEndlessLevel = loadStageSummaries(GameMechanics.SERIES_ENDLESS)
    }

    fun loadThumbnailOfLevel(level: Int, series: Int): Bitmap?
    {
        val key: String = when(series)
        {
            GameMechanics.SERIES_ENDLESS -> "thumbnail_%d_endless".format(level)
            else -> "thumbnail_%d".format(level)
        }
        var encodedString = prefsLegacy.getString(key, "")
        if (encodedString != "")
        {
            // migrate into new thumbnails file
            val editorOfNewFile = prefsThumbnails.edit()
            editorOfNewFile.putString(key, encodedString)
            editorOfNewFile.apply()
            val editorOfOldFile = prefsLegacy.edit()
            editorOfOldFile.remove(key)
            editorOfOldFile.apply()
        }
        else
            encodedString = prefsThumbnails.getString(key, "")

        // reconstruct bitmap from string saved in preferences
        var snapshot: Bitmap?
        try {
            val decodedBytes: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            snapshot = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
        catch(e: Exception)
        {
            // unable to get a level snapshot, for whatever reason
            snapshot = null
        }
        return snapshot
    }

    fun loadAllHeroes(gameMechanics: GameMechanics)
    {
        gameMechanics.heroes = loadHeroes(gameMechanics, null)  // legacy, now deprecated
        gameMechanics.heroesByMode[GameMechanics.LevelMode.BASIC] =
            loadHeroes(gameMechanics, GameMechanics.LevelMode.BASIC)
        gameMechanics.heroesByMode[GameMechanics.LevelMode.ENDLESS] =
            loadHeroes(gameMechanics, GameMechanics.LevelMode.ENDLESS)
    }

    private fun loadHeroes(gameMechanics: GameMechanics, mode: GameMechanics.LevelMode?): HashMap<Hero.Type, Hero>
            /** gets the heroes from the appropriate save file.
             * @param mode The level series mode (normal or endless). If 'null', get the data from
             * the "old" heroes save file (now deprecated).
             */
    {
        val heroMap = HashMap<Hero.Type, Hero>()
        val file = if (mode == null) prefsLegacy else prefsSaves
        val key = if (mode == null) "upgrades" else "heroes"
        val json = file.getString(key, "none")
        if (json == "none")
            return heroMap
        try {
            val listOfHeroData: MutableList<Hero.Data> = when (mode) {
                null -> gson.fromJson(json, SerializableHeroData::class.java).upgrades
                GameMechanics.LevelMode.BASIC ->
                    gson.fromJson(json, SerializableHeroDataPerMode::class.java).basic
                GameMechanics.LevelMode.ENDLESS ->
                    gson.fromJson(json, SerializableHeroDataPerMode::class.java).endless
            }
            for (heroData in listOfHeroData) {
                try {
                    heroMap[heroData.type] =
                        Hero.createFromData(activity as GameActivity, heroData)
                }
                catch (ex: NullPointerException) {
                    /* may happen if a previously existing hero type is definitely removed from the game */
                }
            }
            loadHolidays(gameMechanics)
        }
        catch (ex: Exception) {
            // save file has not the expected structure
            activity.runOnUiThread {
                Toast.makeText(activity, "Save file corrupted.", Toast.LENGTH_LONG).show()
            }
        }
        return heroMap
    }

    fun loadCoins(gameMechanics: GameMechanics)
    {
        // get number of coins
        val json = prefsSaves.getString("coins", "none")
        try {
            if (json != "none") {
                val data: SerializablePurseContents =
                    gson.fromJson(json, SerializablePurseContents::class.java)
                gameMechanics.purseOfCoins[GameMechanics.LevelMode.BASIC]?.let { purse ->
                    purse.contents = data.basic; purse.initialized = true
                }
                gameMechanics.purseOfCoins[GameMechanics.LevelMode.ENDLESS]?.let { purse ->
                    purse.contents = data.endless; purse.initialized = true
                }
            }
        }
        catch (ex: Exception) {
            // save file has not the expected structure
            activity.runOnUiThread {
                Toast.makeText(activity, "Save file (coins info) corrupted.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadHolidays(gameMechanics: GameMechanics)
    {
        val json = prefsSaves.getString("holidays", "none")
        if (json != "none" && !GameMechanics.resetHeroHolidays) {
            val data: SerializableHolidays =
                gson.fromJson(json, SerializableHolidays::class.java)
            gameMechanics.holidays = data.period
        }
    }

    fun saveLevelStructure(series: Int, data: HashMap<Int, Stage.Data>)
            /** saves the structure data for all 'endless' levels to structure.xml.
             */
    {
        val editor = prefsStructure.edit()
        val levelData = SerializableLevelData(data)
        val json = gson.toJson(levelData)
        editor.putString("series_%d".format(series), json)
        editor.apply()
    }

    fun loadLevelStructure(series: Int): HashMap<Int, Stage.Data>
    {
        try {
            val json = prefsStructure.getString("series_%d".format(series), "none")
            if (json == "none")
                return hashMapOf()
            val data: SerializableLevelData =
                gson.fromJson(json, SerializableLevelData::class.java)
            return data.level
        }
        catch (e: Exception)
        {
            return hashMapOf()
        }
    }
}
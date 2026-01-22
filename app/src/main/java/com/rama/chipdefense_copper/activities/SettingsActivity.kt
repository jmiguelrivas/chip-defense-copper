package com.rama.chipdefense_copper.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.rama.chipdefense_copper.GameMechanics
import com.rama.chipdefense_copper.Persistency
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher


class SettingsActivity : AppCompatActivity()
{
    var settings = Settings()
    private var isEndlessAvailable = false

    private lateinit var exportLauncher: ActivityResultLauncher<String>
    private lateinit var importLauncher: ActivityResultLauncher<Array<String>>

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun restartApp() {
        val launchIntent = packageManager
            .getLaunchIntentForPackage(packageName)
            ?: return

        launchIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        startActivity(launchIntent)

        // Ensure the current process is killed
        finishAffinity()
        Runtime.getRuntime().exit(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)  // method of AppCompatActivity
        if (intent.getIntExtra("MAXSERIES", 1) >= GameMechanics.SERIES_ENDLESS)
            isEndlessAvailable = true
        setContentView(R.layout.activity_settings)
        loadPrefs()

        // --------- NEW: Wire Import/Export Buttons ----------
        findViewById<View>(R.id.exportData)?.setOnClickListener {
            exportData()
        }

        findViewById<View>(R.id.importData)?.setOnClickListener {
            importData()
        }

        // EXPORT launcher
        exportLauncher =
            registerForActivityResult(
                    ActivityResultContracts.CreateDocument("application/json")
            ) { uri ->
                if (uri != null) {
                    val persistency = Persistency(this)
                    val success = persistency.exportAllDataToUri(uri)

                    if (success) {
                        toast("Export successful!")
                    } else {
                        toast("Export failed.")
                    }
                } else {
                    toast("Export canceled.")
                }
            }


        // IMPORT launcher
        importLauncher = registerForActivityResult(
                ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri != null) {

                contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val persistency = Persistency(this)
                val success = persistency.importAllDataFromUri(uri)

                if (success) {
                    toast("Import completed. Restarting…")
                    restartApp()
                } else {
                    toast("Import failed.")
                }
            } else {
                toast("No file selected.")
            }
        }
    }

    private fun loadPrefs()
    {
        val prefs = getSharedPreferences(Persistency.filename_settings, MODE_PRIVATE)
        settings.loadFromFile(prefs)
        findViewById<SwitchCompat>(R.id.switch_disable_purchase_dialog)?.isChecked = settings.configDisablePurchaseDialog
        findViewById<SwitchCompat>(R.id.switch_disable_background)?.isChecked = settings.configDisableBackground
        findViewById<SwitchCompat>(R.id.switch_show_atts_in_range)?.isChecked = settings.configShowAttackersInRange
        findViewById<SwitchCompat>(R.id.switch_use_large_buttons)?.isChecked = settings.configUseLargeButtons
        findViewById<SwitchCompat>(R.id.switch_show_framerate)?.isChecked = settings.showFrameRate
        findViewById<SwitchCompat>(R.id.switch_fast_fast_forward)?.isChecked = settings.fastFastForward
        findViewById<SwitchCompat>(R.id.switch_keep_levels)?.isChecked = settings.keepLevels
        findViewById<SwitchCompat>(R.id.switch_use_hex)?.isChecked = settings.showLevelsInHex
        findViewById<SwitchCompat>(R.id.switch_activate_log)?.let {
            it.isChecked = settings.activateLogging
            if (GameMechanics.enableLogging) {
                it.visibility = VISIBLE
                it.isEnabled = true
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun savePrefs(v: View)
    {
        settings.configDisablePurchaseDialog = findViewById<SwitchCompat>(R.id.switch_disable_purchase_dialog)?.isChecked ?: false
        settings.configDisableBackground = findViewById<SwitchCompat>(R.id.switch_disable_background)?.isChecked ?: false
        settings.configShowAttackersInRange = findViewById<SwitchCompat>(R.id.switch_show_atts_in_range)?.isChecked ?: false
        settings.configUseLargeButtons = findViewById<SwitchCompat>(R.id.switch_use_large_buttons)?.isChecked ?: false
        settings.showFrameRate = findViewById<SwitchCompat>(R.id.switch_show_framerate)?.isChecked ?: false
        settings.fastFastForward = findViewById<SwitchCompat>(R.id.switch_fast_fast_forward)?.isChecked ?: false
        settings.keepLevels = findViewById<SwitchCompat>(R.id.switch_keep_levels)?.isChecked ?: true
        settings.showLevelsInHex = findViewById<SwitchCompat>(R.id.switch_use_hex)?.isChecked ?: false
        settings.activateLogging = findViewById<SwitchCompat>(R.id.switch_activate_log)?.isChecked ?: false
        val prefs = getSharedPreferences(Persistency.filename_settings, MODE_PRIVATE)
        settings.saveToFile(prefs)
    }

    fun dismiss(@Suppress("UNUSED_PARAMETER") v: View)
    {
        finish()
    }

    fun startNewGame(@Suppress("UNUSED_PARAMETER") v: View)
    {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_dialog_reset_progress)
        dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.findViewById<TextView>(R.id.question).text = resources.getText(R.string.query_restart_game)
        dialog.findViewById<TextView>(R.id.button1)?.let{
            it.text = resources.getText(R.string.choice_1)
            it.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("RESET_PROGRESS", true)
                intent.putExtra("CONTINUE_GAME", false)
                startActivity(intent)
                dialog.dismiss()
                dismiss(v)
            }
        }
        dialog.findViewById<TextView>(R.id.button2)?.let{
            it.text = resources.getText(R.string.choice_2)
            if (isEndlessAvailable) {
                it.setOnClickListener {
                    val intent = Intent(this, GameActivity::class.java)
                    intent.putExtra("RESET_ENDLESS", true)
                    intent.putExtra("CONTINUE_GAME", false)
                    startActivity(intent)
                    dialog.dismiss()
                    dismiss(v)
                }
            }
            else
            {
                it.setTextColor(Color.BLACK)
            }
        }
        dialog.findViewById<TextView>(R.id.button3)?.let{
            it.text = resources.getText(R.string.choice_3)
            it.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    // ----------------------------
    // NEW: Import / Export
    // ----------------------------

    private fun exportData() {
        exportLauncher.launch("cpudefense_backup.json")
    }

    private fun importData() {
        importLauncher.launch(arrayOf("application/json"))
    }
}

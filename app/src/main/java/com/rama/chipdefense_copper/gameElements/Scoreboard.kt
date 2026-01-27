@file:Suppress("DEPRECATION", "SpellCheckingInspection")

package com.rama.chipdefense_copper.gameElements

import android.graphics.*
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.rama.chipdefense_copper.*
import com.rama.chipdefense_copper.networkmap.Viewport
import com.rama.chipdefense_copper.utils.*
import androidx.core.graphics.createBitmap

class ScoreBoard(val gameView: GameView) : GameElement() {
    private var resources = gameView.resources

    private var area = Rect()
    private var menuButton = MenuButtonDisplay()
    private var stageDisplay = StageDisplay()
    private var information = Information()
    private var waves = Waves()
    private var lives = Lives()
    private var coins = Coins()
    private var temperature = Temperature()
    private var debugStatusLine: DebugStatusLine? = null
    private var displayOutputSize: Int = 120
    private val horizontalPaddingPx = 48

    /** height of the (virtual) line between the display title and the actual display */
    private var divider: Int = 0

    /** the amount in pixels that separates the header text from the divider */
    private var dividerMargin: Int = 8
    private val scoreboardBorderWidth = 4.0f
    fun GameView.createGameDisplayPaint(
        colorParam: Int
    ): Paint = Paint().apply {

        color = ContextCompat.getColor(
                context,
                colorParam
        )

        typeface = ResourcesCompat.getFont(
                context,
                R.font.jersey25_regular
        )

        letterSpacing = 0.05f

        textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                14f,
                resources.displayMetrics
        )

        isAntiAlias = true
    }

    fun drawGameDisplay(
        canvas: Canvas,
        gameView: GameView,
        x: Int,
        y: Int,
        title: String,
        value: String
    ) {
        val density = gameView.resources.displayMetrics.density
        val padding = (8 * density).toInt()
        val titleGap = (4 * density).toInt()

        // Outer square
        val outerRect = Rect(
                x,
                y,
                x + displayOutputSize,
                y + displayOutputSize
        )

        // Background
        val bgPaint = Paint().apply {
            color = ContextCompat.getColor(
                    gameView.context,
                    R.color.dashboard_display_background_color
            )
            style = Paint.Style.FILL
        }

        canvas.drawRect(outerRect, bgPaint)

        // Optional border
//        val borderPaint = Paint().apply {
//            color = ContextCompat.getColor(
//                    gameView.context,
//                    R.color.dashboard_divider_color
//            )
//            style = Paint.Style.STROKE
//            strokeWidth = 3f
//        }
//        canvas.drawRect(outerRect, borderPaint)

        // Title paint (same style, smaller)
        val titlePaint = gameView.createGameDisplayPaint(
                colorParam = R.color.foreground_color
        ).apply {
            textAlign = Paint.Align.CENTER
        }

        // Value paint (main output)
        val valuePaint = gameView.createGameDisplayPaint(
                colorParam = R.color.dashboard_display_foregorund_color
        ).apply {
            textAlign = Paint.Align.CENTER
        }

        // Title baseline
        val titleY = y + padding + titlePaint.textSize

        canvas.drawText(
                title,
                outerRect.centerX().toFloat(),
                titleY,
                titlePaint
        )

        // Value rect (below title)
        val valueRect = Rect(
                outerRect.left + padding,
                (titleY + titleGap).toInt(),
                outerRect.right - padding,
                outerRect.bottom - padding
        )

        valueRect.displayTextCenteredInRect(
                canvas,
                value,
                valuePaint
        )
    }

    fun setSize(area: Rect)
            /** sets the size of the score board and determines the dimensions of all components.
             * @param area The rectangle that the score board shall occupy
             */
    {
        val itemSpacingPx = 16
        var displayCount =
            listOf(
                    menuButton,
                    stageDisplay,
                    waves,
                    information,
                    coins,
                    lives,
                    temperature
            ).size

        val usableWidth =
            area.width() - (horizontalPaddingPx * 2)

        val totalDisplaysWidth =
            displayCount * displayOutputSize +
                    (displayCount - 1) * itemSpacingPx

        var cursorX =
            area.left +
                    horizontalPaddingPx +
                    (usableWidth - totalDisplaysWidth) / 2


        this.area = Rect(area)
        divider = this.area.height() * 32 / 100
        if (gameView.gameActivity.settings.showFrameRate) {
            debugStatusLine = DebugStatusLine()
            debugStatusLine?.setSize(area, divider)
        }

        val top = area.top
        val bottom = area.bottom

        menuButton.setSize(
                Rect(cursorX, top, cursorX + displayOutputSize, bottom),
                divider
        )
        cursorX += displayOutputSize + itemSpacingPx

        stageDisplay.setSize(
                Rect(cursorX, top, cursorX + displayOutputSize, bottom),
                divider
        )
        cursorX += displayOutputSize + itemSpacingPx

        waves.setSize(
                Rect(cursorX, top, cursorX + displayOutputSize, bottom),
                divider
        )
        cursorX += displayOutputSize + itemSpacingPx


        information.setSize(
                Rect(cursorX, top, cursorX + displayOutputSize, bottom),
                divider
        )
        cursorX += displayOutputSize + itemSpacingPx

        coins.setSize(
                Rect(cursorX, top, cursorX + displayOutputSize, bottom),
                divider
        )
        cursorX += displayOutputSize + itemSpacingPx

        lives.setSize(
                Rect(cursorX, top, cursorX + displayOutputSize, bottom),
                divider
        )
        cursorX += displayOutputSize + itemSpacingPx

        temperature.setSize(
                Rect(cursorX, top, cursorX + displayOutputSize, bottom),
                divider
        )

    }

    fun informationToString(number: Int): String {
        if (number < 512 && number > -512)
            return "%d bit".format(number)
        val bytes: Int = number / 8
        if (bytes < 800 && bytes > -800)
            return "%d B".format(bytes)
        val kiB: Float = bytes.toFloat() / 1024.0f
        if (kiB < 800 && kiB > -800)
            return "%.1f KiB".format(kiB)
        val mibiBytes: Float = kiB / 1024.0f
        if (mibiBytes < 800 && mibiBytes > -800)
            return "%.1f MiB".format(mibiBytes)
        val gibiBytes: Float = mibiBytes / 1024.0f
        return "%.1f GiB".format(gibiBytes)
    }

    override fun update() {
    }

    override fun display(canvas: Canvas, viewport: Viewport) {
        val currentStage = gameView.gameMechanics.currentStageIdent
        val paint = Paint()
        paint.color = ContextCompat.getColor(gameView.context, R.color.dashboard_color)

        paint.style = Paint.Style.FILL
        canvas.drawRect(area, paint)
        paint.color = ContextCompat.getColor(gameView.context, R.color.dashboard_divider_color)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = scoreboardBorderWidth
        // canvas.drawRect(area, paint)
        canvas.drawLine(area.left.toFloat(), area.top.toFloat(), area.right.toFloat(), area.top.toFloat(), paint)

        menuButton.display(canvas)
        stageDisplay.display(canvas)
        waves.display(canvas)
        if (currentStage.series > 1 || currentStage.number > 2)
            information.display(canvas)
        lives.display(canvas)
        coins.display(canvas)
        temperature.display(canvas)
        debugStatusLine?.display(canvas)
    }

    fun displayHeader(canvas: Canvas, area: Rect, text: String)
            /**
             * Display text in 'header' text size
             *
             * @param canvas Where to paint on
             * @param area The rectangle where the header text should be placed
             * @param text The actual string to be displayed
             */
    {
        val rect = Rect(area)
        rect.bottom = divider

        val paint = gameView.createGameDisplayPaint(
                colorParam = R.color.fps_debug
        ).apply {
            textAlign = Paint.Align.LEFT
        }
        rect.displayTextLeftAlignedInRect(canvas, text, paint, baseline = divider - dividerMargin)
    }

    fun recreateBitmap()
            /**
             * Recreate all parts of the score board. Called when resuming the game.
             */
    {
        if (area.width() > 0 && area.height() > 0) {
            information.recreateBitmap()
            waves.recreateBitmap()
            lives.recreateBitmap()
            coins.recreateBitmap()
            temperature.recreateBitmap()
        }
    }

//    fun onTouchDown(x: Float, y: Float): Boolean {
//        if (menuButton.contains(x, y)) {
//            gameView.gameActivity.showReturnDialog()
//            return true
//        }
//        return false
//    }

    inner class MenuButtonDisplay {
        private var area = Rect()
        private var divider = 0
        private lateinit var bitmap: Bitmap
        private val paint = Paint()

        fun setSize(area: Rect, divider: Int): Rect {
            val left = area.left
            val top = area.top + (area.height() - displayOutputSize) / 2

            this.area = Rect(
                    left,
                    top,
                    left + displayOutputSize,
                    top + displayOutputSize
            )

            this.divider = divider
            bitmap = createBitmap(this.area.width(), this.area.height())
            recreateBitmap()
            return Rect(this.area.right, area.top, area.right, area.bottom)
        }

        fun display(canvas: Canvas) {
            canvas.drawBitmap(bitmap, null, area, paint)
        }

        fun recreateBitmap() {
            bitmap = createBitmap(area.width(), area.height())
            val canvas = Canvas(bitmap)

            // background
            canvas.drawRect(
                    Rect(0, 0, area.width(), area.height()),
                    Paint().apply {
                        color = ContextCompat.getColor(
                                gameView.context,
                                R.color.button_color
                        )
                        style = Paint.Style.FILL
                    }
            )

            // icon
            val drawable = ContextCompat.getDrawable(
                    gameView.context,
                    R.drawable.icon_bars
            ) ?: return

            val iconSize = (displayOutputSize * 0.5f).toInt()
            val left = (area.width() - iconSize) / 2
            val top = (area.height() - iconSize) / 2

            drawable.setBounds(
                    left,
                    top,
                    left + iconSize,
                    top + iconSize
            )
            drawable.draw(canvas)
        }

        fun contains(x: Float, y: Float): Boolean {
            return area.contains(x.toInt(), y.toInt())
        }
    }


    inner class StageDisplay {
        private var area = Rect()
        private var divider = 0

        private var lastValue = ""
        lateinit var bitmap: Bitmap
        private val paint = Paint()

        fun setSize(area: Rect, divider: Int): Rect {
            this.area = Rect(area.left, area.top, area.left + displayOutputSize, area.bottom)
            bitmap = createBitmap(this.area.width(), this.area.height())
            this.divider = divider
            return Rect(this.area.right, area.top, area.right, area.bottom)
        }

        fun display(canvas: Canvas) {
            val stage = gameView.gameMechanics.currentStageIdent
            val newValue = "S${stage.series}-${stage.number}"

            if (newValue != lastValue) {
                lastValue = newValue
                recreateBitmap()
            }
            canvas.drawBitmap(bitmap, null, area, paint)
        }

        fun recreateBitmap() {
            bitmap = createBitmap(area.width(), area.height())
            val canvas = Canvas(bitmap)

            drawGameDisplay(
                    canvas = canvas,
                    gameView = gameView,
                    x = (area.width() - displayOutputSize) / 2,
                    y = (area.height() - displayOutputSize) / 2,
                    title = resources.getString(R.string.scoreboard_stage),
                    value = lastValue
            )
        }
    }


    inner class Information
    /** display of current amount of information ('cash') */
    {
        private var area = Rect()
        private var divider = 0

        private var lastValue = -1   // used to detect value changes
        lateinit var bitmap: Bitmap
        val paint = Paint()

        fun setSize(area: Rect, divider: Int): Rect
                /** sets the area that is taken up by the information count.
                 * @param area The whole area of the score board
                 * @divider height of the line between header and contents
                 * @return The rectangle that remains (original area minus occupied area)
                 */
        {
            this.area = Rect(area.left, area.top, area.left + displayOutputSize, area.bottom)
            bitmap = createBitmap(this.area.width(), this.area.height())
            this.divider = divider
            return Rect(this.area.right, area.top, area.right, area.bottom)
        }

        fun display(canvas: Canvas) {
            val state = gameView.gameMechanics.state
            if (state.cash != lastValue) {
                /* only render the display if value has changed, otherwise re-use bitmap */
                lastValue = state.cash
                recreateBitmap()
            }
            canvas.drawBitmap(bitmap, null, area, paint)
        }

        fun recreateBitmap() {
            bitmap = createBitmap(area.width(), area.height())
            val canvas = Canvas(bitmap)

            val currentStage = gameView.gameMechanics.currentStageIdent

            val value = if (currentStage.series > 1 || currentStage.number > 2) {
                informationToString(gameView.gameMechanics.state.cash)
            } else {
                "--"
            }

            drawGameDisplay(
                    canvas = canvas,
                    gameView = gameView,
                    x = (area.width() - displayOutputSize) / 2,
                    y = (area.height() - displayOutputSize) / 2,
                    title = resources.getString(R.string.scoreboard_inf),
                    value = value
            )
        }

    }

    inner class Waves {
        private var area = Rect()
        private var divider = 0

        private var lastValue = -1   // used to detect value changes
        lateinit var bitmap: Bitmap
        val paint = Paint()

        fun setSize(area: Rect, divider: Int): Rect {
            this.area = Rect(area.left, area.top, area.left + displayOutputSize, area.bottom)
            bitmap = createBitmap(this.area.width(), this.area.height())
            this.divider = divider
            return Rect(this.area.right, area.top, area.right, area.bottom)
        }

        fun display(canvas: Canvas) {
            val stage: Stage? = gameView.gameMechanics.currentlyActiveStage
            if (stage?.data?.wavesCount != lastValue) {
                /* only render the display if value has changed, otherwise re-use bitmap */
                lastValue = stage?.data?.wavesCount ?: -1
                recreateBitmap()
            }
            canvas.drawBitmap(bitmap, null, area, paint)
        }

        fun recreateBitmap() {
            bitmap = createBitmap(area.width(), area.height())
            val canvas = Canvas(bitmap)

            val stage = gameView.gameMechanics.currentlyActiveStage ?: return

            val value = "${stage.data.wavesCount} / ${stage.data.maxWaves}"

            drawGameDisplay(
                    canvas = canvas,
                    gameView = gameView,
                    x = (area.width() - displayOutputSize) / 2,
                    y = (area.height() - displayOutputSize) / 2,
                    title = resources.getString(R.string.scoreboard_waves),
                    value = value
            )
        }

    }

    inner class Lives {
        private var area = Rect()
        private var divider = 0

        private var lastValue = -1   // used to detect value changes
        lateinit var bitmap: Bitmap
        private val paint = Paint()
        private var preferredSizeLedX = 0

        fun setSize(area: Rect, divider: Int): Rect {
            this.area = Rect(area.left, area.top, area.left + displayOutputSize, area.bottom)
            bitmap = createBitmap(this.area.width(), this.area.height())
            this.divider = divider
            return Rect(this.area.right, area.top, area.right, area.bottom)
        }

        fun display(canvas: Canvas) {
            val state = gameView.gameMechanics.state
            if (state.lives != lastValue) {
                /* only render the display if value has changed, otherwise re-use bitmap */
                lastValue = state.lives
                recreateBitmap()
            }
            canvas.drawBitmap(bitmap, null, area, paint)
        }

        fun recreateBitmap() {
            val state = gameView.gameMechanics.state
            bitmap = createBitmap(area.width(), area.height())
            val canvas = Canvas(bitmap)
            val value = "${state.lives} / ${state.currentMaxLives}"

            drawGameDisplay(
                    canvas = canvas,
                    gameView = gameView,
                    x = (area.width() - displayOutputSize) / 2,
                    y = (area.height() - displayOutputSize) / 2,
                    title = resources.getString(R.string.scoreboard_status),
                    value = value
            )
        }
    }

    inner class Coins {
        private var area = Rect()
        private var divider = 0
        private var coins: Int = 0

        private var lastValue = -1   // used to detect value changes
        lateinit var bitmap: Bitmap
        private val paint = Paint()

        fun setSize(area: Rect, divider: Int): Rect {
            this.area = Rect(area.left, area.top, area.left + displayOutputSize, area.bottom)
            bitmap = createBitmap(this.area.width(), this.area.height())
            this.divider = divider
            return Rect(this.area.right, area.top, area.right, area.bottom)
        }

        fun display(canvas: Canvas) {
            val stage: Stage? = gameView.gameMechanics.currentlyActiveStage
            val state: GameMechanics.StateData = gameView.gameMechanics.state
            if (stage?.summary?.coinsMaxAvailable == 0)
                return  // levels where you can't get coins
            coins = state.coinsInLevel + state.coinsExtra
            if (coins < 0)
                return  // something went wrong, shouldn't happen
            if (coins != lastValue) {
                /* only render the display if value has changed, otherwise re-use bitmap */
                lastValue = coins
                recreateBitmap()
            }
            canvas.drawBitmap(bitmap, null, area, paint)
        }

        fun recreateBitmap() {
            bitmap = createBitmap(area.width(), area.height())
            val canvas = Canvas(bitmap)
            val state = gameView.gameMechanics.state
            val coinsValue = (state.coinsInLevel + state.coinsExtra).toString()

            drawGameDisplay(
                    canvas = canvas,
                    gameView = gameView,
                    x = (area.width() - displayOutputSize) / 2,
                    y = (area.height() - displayOutputSize) / 2,
                    title = resources.getString(R.string.scoreboard_coins),
                    value = coinsValue
            )
        }
    }

    inner class Temperature {
        private var area = Rect()
        private var divider = 0

        private var lastValue = -1
        private var temperature: Int = GameMechanics.baseTemperature

        lateinit var bitmap: Bitmap
        private val paint = Paint()

        private fun isTemperatureAvailable(): Boolean {
            val state = gameView.gameMechanics.state
            return state.heat > 0
        }

        fun setSize(area: Rect, divider: Int): Rect {
            this.area = Rect(area.left, area.top, area.left + displayOutputSize, area.bottom)
            this.divider = divider
            bitmap = createBitmap(area.width(), area.height())
            return Rect(this.area.right, area.top, area.right, area.bottom)
        }

        fun display(canvas: Canvas) {
            val state = gameView.gameMechanics.state

            val newValue =
                if (state.heat > 0) {
                    (state.heat / GameMechanics.heatPerDegree +
                            GameMechanics.baseTemperature).toInt()
                } else {
                    Int.MIN_VALUE
                }

            if (newValue != lastValue) {
                lastValue = newValue
                recreateBitmap()
            }

            canvas.drawBitmap(bitmap, null, area, paint)
        }

        fun recreateBitmap() {
            bitmap = createBitmap(area.width(), area.height())
            val canvas = Canvas(bitmap)

            val valueText: String
            val valuePaint = gameView.createGameDisplayPaint(
                    colorParam = R.color.dashboard_display_foregorund_color
            )

            if (lastValue == Int.MIN_VALUE) {
                valueText = "--"
                valuePaint.alpha = 120
            } else {
                valueText = "$lastValue°"
                if (lastValue >= GameMechanics.temperatureLimit)
                    valuePaint.color = ContextCompat.getColor(gameView.context, R.color.led_red)
                else if (lastValue >= GameMechanics.temperatureWarnThreshold)
                    valuePaint.color = ContextCompat.getColor(gameView.context, R.color.led_turbo)
            }

            drawGameDisplay(
                    canvas = canvas,
                    gameView = gameView,
                    x = (area.width() - displayOutputSize) / 2,
                    y = (area.height() - displayOutputSize) / 2,
                    title = resources.getString(R.string.scoreboard_temp),
                    value = valueText
            )
        }
    }

    inner class DebugStatusLine
    /** this is an additional text displayed at every tick.
     * It is meant to hold additional debug info, e. g. the current frame rate
     */
    {
        private var area = Rect()
        private var divider: Int = 0
        private val paint = Paint()
        private var bitmap: Bitmap? = null
        private var lastValue = 0.0

        fun setSize(area: Rect, divider: Int) {
            this.divider = divider
            this.area = Rect(area.left, 0, area.right, divider)
            paint.color = Color.YELLOW
            paint.style = Paint.Style.FILL
        }

        fun display(canvas: Canvas) {
            @Suppress("SimplifyBooleanWithConstants")
            if (gameView.gameMechanics.timeBetweenFrames != lastValue || true)
                recreateBitmap()
            bitmap?.let { canvas.drawBitmap(it, null, area, paint) }
        }

        private fun recreateBitmap() {
            if (area.width() > 0 && area.height() > 0)
                bitmap = createBitmap(area.width(), area.height())
            val fps = if (gameView.gameMechanics.timeBetweenFrames > 0f) {
                1000f / gameView.gameMechanics.timeBetweenFrames
            } else {
                0f
            }
            val textToDisplay = "%.1f FPS".format(fps)

            bitmap?.let {
                val canvas = Canvas(it)
                val rect = Rect(50, 0, 0, 0)

                displayHeader(canvas, rect, textToDisplay)
            }
            lastValue = gameView.gameMechanics.timeBetweenFrames
        }
    }
}
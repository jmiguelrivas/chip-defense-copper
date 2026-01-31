@file:Suppress("DEPRECATION")

package com.rama.chipdefense_copper.gameElements

import android.content.res.Resources
import android.graphics.*
import android.text.TextPaint
import com.rama.chipdefense_copper.GameView
import com.rama.chipdefense_copper.Hero
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.networkmap.Viewport
import com.rama.chipdefense_copper.utils.displayTextCenteredInRect
import com.rama.chipdefense_copper.utils.setCenter
import com.rama.chipdefense_copper.utils.setTopLeft
import com.rama.chipdefense_copper.utils.textStyleContent

class HeroCard(val gameView: GameView, val hero: Hero) : GameElement()
/** graphical representation of a hero or a heroine */
{
    val type = hero.data.type
    val resources: Resources = gameView.resources

    /** rectangle with the size of the card, positioned at (0|0) */
    var cardArea = Rect()

    /** rectangle at the actual position on the screen */
    var cardAreaOnScreen = Rect(cardArea)

    /** the area where the hero photo goes */
    private var portraitArea = Rect()
    private var portraitAreaOnScreen = Rect(portraitArea)
    private var myBitmap: Bitmap? = null
    private var effectBitmap = BitmapFactory.decodeResource(resources, R.drawable.glow)
    private var shortDescRect = Rect(cardAreaOnScreen)

    /** state used for various graphical effects */
    enum class GraphicalState { NORMAL, TRANSIENT_LEVEL_0, TRANSIENT }

    private var graphicalState = GraphicalState.NORMAL
    private var transition = 0.0f
    var heroOpacity = 0f

    /** the little boxes that show the current level */
    private var levelIndicator = mutableListOf<Rect>()
    private var indicatorSize = portraitArea.width() / 10

    /** additional flags */
    private var showNextUpdate = true
    private var monochrome = false

    /* different paint objects */
    private var paintRect = Paint()
    private var paintInactive = Paint()
    private var paintIndicator = Paint()
    private var paintText = Paint()
    private val paintHero = Paint()

    var isVisible: Boolean = false

    var inactiveColor = resources.getColor(R.color.foreground_inactive_color)
    private var monochromeColor = inactiveColor
    var activeColor: Int = if (monochrome) monochromeColor
    else when (type) {
        Hero.Type.INCREASE_CHIP_SUB_SPEED -> resources.getColor(R.color.upgrade_active_chip_sub)
        Hero.Type.INCREASE_CHIP_SUB_RANGE -> resources.getColor(R.color.upgrade_active_chip_sub)
        Hero.Type.DOUBLE_HIT_SUB -> resources.getColor(R.color.upgrade_active_chip_sub)
        Hero.Type.INCREASE_CHIP_SHR_SPEED -> resources.getColor(R.color.upgrade_active_chip_shr)
        Hero.Type.INCREASE_CHIP_SHR_RANGE -> resources.getColor(R.color.upgrade_active_chip_shr)
        Hero.Type.DOUBLE_HIT_SHR -> resources.getColor(R.color.upgrade_active_chip_shr)
        Hero.Type.INCREASE_CHIP_MEM_SPEED -> resources.getColor(R.color.upgrade_active_chip_mem)
        Hero.Type.INCREASE_CHIP_MEM_RANGE -> resources.getColor(R.color.upgrade_active_chip_mem)
        Hero.Type.ENABLE_MEM_UPGRADE -> resources.getColor(R.color.upgrade_active_chip_mem)
        Hero.Type.REDUCE_HEAT -> resources.getColor(R.color.upgrade_active_chip_clk)
        Hero.Type.INCREASE_CHIP_RES_STRENGTH -> resources.getColor(R.color.upgrade_active_chip_res)
        Hero.Type.INCREASE_CHIP_RES_DURATION -> resources.getColor(R.color.upgrade_active_chip_res)
        Hero.Type.CONVERT_HEAT -> resources.getColor(R.color.upgrade_active_chip_res)
        Hero.Type.DECREASE_ATT_FREQ -> resources.getColor(R.color.upgrade_active_general)
        Hero.Type.DECREASE_ATT_SPEED -> resources.getColor(R.color.upgrade_active_general)
        Hero.Type.DECREASE_ATT_STRENGTH -> resources.getColor(R.color.upgrade_active_general)
        Hero.Type.DECREASE_COIN_STRENGTH -> resources.getColor(R.color.upgrade_active_general)
        Hero.Type.ADDITIONAL_LIVES -> resources.getColor(R.color.upgrade_active_meta)
        Hero.Type.INCREASE_MAX_HERO_LEVEL -> resources.getColor(R.color.upgrade_active_meta)
        Hero.Type.LIMIT_UNWANTED_CHIPS -> resources.getColor(R.color.upgrade_active_meta)
        Hero.Type.CREATE_ADDITIONAL_CHIPS -> resources.getColor(R.color.upgrade_active_meta)
        Hero.Type.INCREASE_STARTING_CASH -> resources.getColor(R.color.upgrade_active_eco)
        Hero.Type.GAIN_CASH -> resources.getColor(R.color.upgrade_active_eco)
        Hero.Type.GAIN_CASH_ON_KILL -> resources.getColor(R.color.upgrade_active_eco)
        Hero.Type.INCREASE_REFUND -> resources.getColor(R.color.upgrade_active_eco)
        Hero.Type.DECREASE_UPGRADE_COST -> resources.getColor(R.color.upgrade_active_eco)
        Hero.Type.DECREASE_REMOVAL_COST -> resources.getColor(R.color.upgrade_active_eco)
    }

    init {
        paintRect.style = Paint.Style.STROKE
        paintInactive = Paint(paintRect)
        paintInactive.color = inactiveColor
        paintText = TextPaint(textStyleContent(gameView.context))
        shortDescRect.top =
            shortDescRect.bottom - (50 * resources.displayMetrics.scaledDensity).toInt()
        heroOpacity = when (hero.data.level) {
            0 -> 0f
            else -> 1f
        }
    }

    override fun update() {
    }

    override fun display(canvas: Canvas, viewport: Viewport) {
        when (hero.data.level) {
            0 -> {
                paintRect.color = inactiveColor
                paintRect.strokeWidth = 2f
            }

            else -> {
                paintRect.color = activeColor
                paintRect.strokeWidth = 2f + hero.data.level / 2
            }
        }
        paintRect.strokeWidth *= resources.displayMetrics.scaledDensity
        myBitmap?.let { canvas.drawBitmap(it, null, cardAreaOnScreen, paintRect) }

        // display hero picture
        // (this is put here because of fading)
        // display hero picture
        if (hero.isOnLeave) {
            portraitAreaOnScreen.displayTextCenteredInRect(
                    canvas,
                    "On leave",
                    paintText
            )
        } else {
            if (hero.data.level == 0) {
                // Hero not unlocked → draw empty chip
                canvas.drawBitmap(
                        gameView.emptyChip,
                        null,
                        portraitAreaOnScreen,
                        null
                )
            } else {
                // Hero unlocked → draw portrait
                paintHero.alpha = (255f * heroOpacity).toInt()
                hero.person.picture?.let {
                    canvas.drawBitmap(it, null, portraitAreaOnScreen, paintHero)
                }
            }
        }
    }

    fun appearInstant() {
        heroOpacity = 1f
        isVisible = true
        graphicalState = GraphicalState.NORMAL
    }

    fun displayHighlightFrame(canvas: Canvas) {
        with(paintInactive)
        {
            val originalThickness = strokeWidth
            val originalAlpha = alpha
            alpha = 60
            strokeWidth = originalThickness + 12 * resources.displayMetrics.scaledDensity
            canvas.drawRect(cardAreaOnScreen, this)
            alpha = 60
            strokeWidth = originalThickness + 6 * resources.displayMetrics.scaledDensity
            canvas.drawRect(cardAreaOnScreen, this)
            // restore original values
            strokeWidth = originalThickness
            alpha = originalAlpha
        }
        // }
    }

    private fun displayLine(canvas: Canvas, x0: Int, y0: Int, x1: Int, y1: Int)
            /** draws a fraction of the line between x0,y0 and x1,y1 */
    {
        val x: Float = x0 * (1 - transition) + x1 * transition
        val y = y0 * (1 - transition) + y1 * transition
        canvas.drawLine(x0.toFloat(), y0.toFloat(), x, y, paintRect)
        // draw the glow effect
        val effectRect = Rect(0, 0, effectBitmap.width, effectBitmap.height)
        effectRect.setCenter(x.toInt(), y.toInt())
        canvas.drawBitmap(effectBitmap, null, effectRect, paintText)
    }

    fun putAt(left: Int, top: Int) {
        cardAreaOnScreen = Rect(cardArea)
        cardAreaOnScreen.setTopLeft(left, top)

        portraitAreaOnScreen = Rect(portraitArea)

        val padding = (GameView.globalPadding * gameView.scaleFactor).toInt()

        portraitAreaOnScreen.setTopLeft(
                cardAreaOnScreen.left + padding,
                cardAreaOnScreen.top + padding
        )

        paintText.textSize = GameView.heroCardTextSize * gameView.textScaleFactor
        indicatorSize = portraitArea.width() / 10
    }

    fun create(showNextUpdate: Boolean = true, monochrome: Boolean = false) {
        cardArea =
            Rect(0, 0, (GameView.cardWidth * gameView.scaleFactor).toInt(), (GameView.cardHeight * gameView.scaleFactor).toInt())
        portraitArea =
            Rect(0, 0, (GameView.cardPictureSize * gameView.scaleFactor).toInt(), (GameView.cardPictureSize * gameView.scaleFactor).toInt())
        paintText.textSize = GameView.heroCardTextSize * gameView.textScaleFactor
        indicatorSize = portraitArea.width() / 10
        this.showNextUpdate = showNextUpdate
        this.monochrome = monochrome
        createBitmap()
    }

    private fun addLevelDecoration(canvas: Canvas) {
        paintIndicator.color = if (hero.data.level == 0) inactiveColor else activeColor
        var verticalIndicatorSize = indicatorSize  // squeeze when max level is greater than 8
        if (hero.getMaxUpgradeLevel() > 8)
            verticalIndicatorSize = portraitArea.height() / (5 + hero.getMaxUpgradeLevel())
        for (i in 1..hero.getMaxUpgradeLevel()) {
            val rect = Rect(0, 0, indicatorSize, verticalIndicatorSize)
            rect.setTopLeft(0, (2 * i - 1) * verticalIndicatorSize)
            levelIndicator.add(rect)
            paintIndicator.style =
                if (i <= hero.data.level) Paint.Style.FILL else Paint.Style.STROKE
            canvas.drawRect(rect, paintIndicator)
        }
        return
    }

    fun createBitmap() {
        val bitmap =
            Bitmap.createBitmap(cardArea.width(), cardArea.height(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val density = resources.displayMetrics.scaledDensity
        val spacing = 6f * density
        val bottomPadding = 10f * density

        val cardWidth = bitmap.width
        val cardHeight = bitmap.height

        val paintActive = Paint(paintText)
        val paintInactive = Paint(paintText).apply {
            color = resources.getColor(R.color.foreground_inactive_color)
        }

        // --- TEXT CONTENT
        val counterText = "${hero.data.level} / ${hero.getMaxUpgradeLevel()}"
        val activeText = hero.strengthDesc
        val upgradeText = hero.upgradeDesc

        // --- MEASURE WIDTHS
        val counterWidth = paintActive.measureText(counterText)
        val activeWidth = paintActive.measureText(activeText)
        val upgradeWidth =
            if (showNextUpdate) paintInactive.measureText(upgradeText) else 0f

        val rowWidth =
            if (showNextUpdate) activeWidth + spacing + upgradeWidth
            else activeWidth

        // --- BASELINES
        val bottomBaseline = cardHeight - bottomPadding
        val fm = paintActive.fontMetrics
        val counterBaseline = bottomBaseline + fm.top - spacing

        // --- CENTER X POSITIONS
        val counterX = (cardWidth - counterWidth) / 2f
        val rowStartX = (cardWidth - rowWidth) / 2f

        // --- DRAW COUNTER (TOP)
        canvas.drawText(counterText, counterX, counterBaseline, paintActive)

        // --- DRAW ACTIVE TEXT
        canvas.drawText(activeText, rowStartX, bottomBaseline, paintActive)

        // --- DRAW INACTIVE UPGRADE TEXT
        if (showNextUpdate) {
            val upgradeX = rowStartX + activeWidth + spacing
            canvas.drawText(upgradeText, upgradeX, bottomBaseline, paintInactive)
        }

        myBitmap = bitmap
    }


    fun upgradeAnimation() {
        appearInstant()
        graphicalState = GraphicalState.TRANSIENT
    }

    fun downgradeAnimation() {
        if (hero.data.level == 0) {
            heroOpacity = 0f
            isVisible = false
            graphicalState = GraphicalState.TRANSIENT_LEVEL_0
        } else {
            appearInstant()
            graphicalState = GraphicalState.TRANSIENT
        }
    }
}
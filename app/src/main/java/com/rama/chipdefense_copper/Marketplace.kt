package com.rama.chipdefense_copper

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import com.rama.chipdefense_copper.effects.Fadable
import com.rama.chipdefense_copper.effects.Fader
import com.rama.chipdefense_copper.effects.Flippable
import com.rama.chipdefense_copper.effects.Flipper
import com.rama.chipdefense_copper.gameElements.Button
import com.rama.chipdefense_copper.gameElements.GameElement
import com.rama.chipdefense_copper.networkmap.Viewport
import com.rama.chipdefense_copper.utils.setCenter
import com.rama.chipdefense_copper.utils.textStyleContent

class Marketplace(val gameView: GameView) : GameElement() {
    private val resources: Resources = gameView.resources
    private val gameMechanics = gameView.gameMechanics
    private var buttonFinish: Button? = null
    private var buttonRefund: Button? = null
    private var buttonRefundAll: Button? = null
    private var buttonPurchase: Button? = null
    private var buttonWikipedia: Button? = null
    private var showRefundOneButton: Boolean = false
    private var myArea = Rect()

    /** area used for cards, without header */
    private var cardsArea = Rect()
    private var bioPanel = Rect()
    private var biographyArea = Rect()
    private var clearPaint = Paint()
    private var paint = Paint()

    /** used for scrolling */
    private var cardViewOffset = 0f

    private var upgrades = mutableListOf<Hero>()
    private var purse = gameMechanics.currentPurse()
    private var selected: Hero? = null
    private var coins = mutableListOf<Coin>()
    private var coinSize = (32 * gameView.scaleFactor).toInt()

    private val headerHeight: Int
        get() = (coinSize * 1.2f).toInt()


    /** maximum number of coins that are displayed separately */

    private var currentWiki: Hero? = null

    var nextGameLevel = Stage.Identifier()

    init {
        clearPaint.color = resources.getColor(R.color.background_tertiary_color)
        clearPaint.style = Paint.Style.FILL
    }

    fun setSize(area: Rect) {
        coinSize = 80

        // set main area
        myArea = Rect(
                area.left,
                area.top + GameView.notchSize + headerHeight,
                area.right,
                area.bottom
        )

        // cards area at top
        cardsArea = Rect(
                GameView.globalPadding,
                myArea.top + GameView.globalPadding,
                myArea.right - GameView.globalPadding,
                (myArea.top + GameView.cardHeight + 90).toInt()
        )

        // create buttons first
        createButton()  // this will also call layoutButtons()

        // bio panel area is between cards and buttons
        bioPanel = Rect(
                myArea.left + GameView.globalPadding,
                cardsArea.bottom + GameView.globalPadding * 3,
                myArea.right - GameView.globalPadding,
                myArea.bottom - (buttonFinish?.area?.height()
                    ?: 0) - 16 /* optional bottom margin */
        )

        // biography area inside right panel, ending above the purchase button
        biographyArea = Rect(bioPanel).apply {
            bottom = buttonPurchase?.area?.top ?: bioPanel.bottom
        }
    }

    fun fillMarket(level: Stage.Identifier) {
        nextGameLevel = level
        val newUpgrades = mutableListOf<Hero>()
        val heroes = gameMechanics.currentHeroes(level)
        purse = gameMechanics.currentPurse()
        for (type in Hero.Type.entries) {
            /* if upgrade already exists (because it has been bought earlier),
            get it from the game data. Otherwise, create an empty card.
            Only add upgrades that are allowed (available) at present.
             */
            var hero: Hero? = heroes[type]
            if (hero == null)
                hero = Hero.createFromData(gameView.gameActivity, Hero.Data(type))
            if (hero.isAvailable(level) || hero.data.level > 0) {
                hero.createBiography(biographyArea)
                newUpgrades.add(hero)
            }
            hero.setDesc()
            hero.card.create(showNextUpdate = true)
            hero.isOnLeave = hero.isOnLeave(level)
        }
        arrangeCards(newUpgrades, cardViewOffset)
        upgrades = newUpgrades
        if (level.mode() == GameMechanics.LevelMode.ENDLESS) // grant a gift at the beginning of 'Endless'
        {
            val gift = purse.addGift(GameMechanics.defaultGiftCoins)
            if (gift > 0) {
                Toast.makeText(
                        gameView.gameActivity, resources.getString(R.string.coins_received_as_gift)
                    .format(gift), Toast.LENGTH_SHORT
                )
                    .show()
                Persistency(gameView.gameActivity).saveCoins(gameMechanics)
            }
        }
        coins = MutableList(purse.availableCoins()) { Coin(gameMechanics, coinSize) }
    }

    private fun arrangeCards(heroes: MutableList<Hero>, dX: Float = 0f) {
        val space = 20
        var posX = cardsArea.left + space + dX.toInt()
        val posY = cardsArea.top + space

        for (hero in heroes) {
            hero.card.putAt(posX, posY)
            posX += hero.card.cardArea.width() + space
        }
    }

    override fun update() {
    }

    private fun createButton() {
        buttonFinish = Button(gameView, resources.getString(R.string.button_playlevel))
        buttonRefund = Button(gameView, resources.getString(R.string.button_refund_one))
        buttonRefundAll = Button(gameView, resources.getString(R.string.button_refund_all))
        buttonPurchase = Button(gameView, purchaseButtonText(null))
        buttonWikipedia = Button(gameView, resources.getString(R.string.button_wiki))

        layoutButtons()
    }

    private fun layoutButtons() {
        var currentBottom = myArea.bottom - GameView.globalPadding

        // List the buttons in order from bottom to top
        val buttonsStack = mutableListOf<Button>()

        buttonFinish?.let { buttonsStack.add(it) }

        buttonRefundAll?.let { buttonsStack.add(it) }

        buttonPurchase?.let { buttonsStack.add(it) }

        if (showRefundOneButton) {
            buttonRefund?.let { buttonsStack.add(it) }
        }

        buttonWikipedia?.let { buttonsStack.add(it) }

        // Position buttons
        for (button in buttonsStack) {
            val height = button.area.height()
            val rect = Rect(
                    myArea.left + GameView.globalPadding,
                    currentBottom - height,
                    myArea.right - GameView.globalPadding,
                    currentBottom
            )
            button.area.set(rect)
            button.touchableArea.set(rect)
            currentBottom = rect.top - GameView.globalPadding
        }
    }

    fun onDown(event: MotionEvent): Boolean {
        if (buttonWikipedia?.area?.contains(event.x.toInt(), event.y.toInt()) == true) {
            wikipedia()
        }
        if (buttonFinish?.area?.contains(event.x.toInt(), event.y.toInt()) == true) {
            selected = null
            makeButtonText(null)
            gameView.gameActivity.startNextStage(nextGameLevel)
            return true
        }
        if (buttonRefund?.area?.contains(event.x.toInt(), event.y.toInt()) == true) {
            selected?.let {
                refundOne(it)
                return true
            }
        }
        if (buttonRefundAll?.area?.contains(event.x.toInt(), event.y.toInt()) == true) {
            val dialog = Dialog(gameView.gameActivity)
            dialog.setContentView(R.layout.layout_dialog_heroes)
            dialog.window?.setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(true)
            dialog.findViewById<android.widget.TextView>(R.id.question).text =
                resources.getText(R.string.query_reset)
            val button1 = dialog.findViewById<android.widget.Button>(R.id.button1)
            val button2 = dialog.findViewById<android.widget.Button>(R.id.button2)
            button2?.text = resources.getText(R.string.yes)
            button1?.text = resources.getText(R.string.no)
            button2?.setOnClickListener { dialog.dismiss(); refundAll() }
            button1?.setOnClickListener { dialog.dismiss() }
            dialog.show()
            return true
        }
        if (buttonPurchase?.area?.contains(event.x.toInt(), event.y.toInt()) == true) {
            selected?.let {
                if (heroIsOnLeave(it)) return true
                val price = it.getPrice(it.data.level)
                if (purse.canAfford(price) && it.data.level < it.getMaxUpgradeLevel()) {
                    purse.spend(price)
                    it.data.coinsSpent += price
                    (1 until price).run { Fader(gameView, coins.last(), Fader.Type.DISAPPEAR) }
                    it.doUpgrade()
                    gameMechanics.currentHeroes(nextGameLevel)[it.data.type] = it
                    save()
                    fillMarket(nextGameLevel)
                    makeButtonText(it)
                }
            }
            return true
        }
        for (coin in coins) {
            if (coin.myArea.contains(event.x.toInt(), event.y.toInt())) {
                if (!coin.isCurrentlyFlipping)
                    Flipper(gameView, coin, Flipper.Type.HORIZONTAL)
                return true
            }
        }
        for (hero in upgrades)
            if (hero.card.cardAreaOnScreen.contains(event.x.toInt(), event.y.toInt()))
                hero.let {
                    selected = it
                    it.biography?.viewOffset = 0f
                    makeButtonText(it)
                    return true
                }
        if (cardsArea.contains(event.x.toInt(), event.y.toInt())) {
            selected = null
            makeButtonText(null)
        }
        return false
    }

    private fun wikipedia()
            /** opens the system's default browser and points it to the hero's wikipedia article */
    {
        if (currentWiki != selected) {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, selected?.person?.url?.toUri())
            try {
                gameView.gameActivity.startActivity(browserIntent)
                currentWiki = selected
            } catch (_: Exception) {
            }  // come here if no external app can handle the request
        }
    }

    private fun refundAll()
            /** resets all heroes to level 0 that meet certain criteria,
             * e.g. that are not on leave.
             * Refunds the coins spent on the hero.
             */
    {
        for (card in upgrades.filter { it.data.level > 0 && !it.isOnLeave }) {
            val refund =
                if (card.data.coinsSpent > 0) card.data.coinsSpent else 0  // was: 4
            purse.spend(-refund)
            card.resetUpgrade()
        }
        save()
        fillMarket(nextGameLevel)
        makeButtonText(null)
    }

    private fun refundOne(hero: Hero) {
        with(hero)
        {
            if (data.type == Hero.Type.INCREASE_MAX_HERO_LEVEL) // heroes that cannot be fired
            {
                val res = resources
                val text = res.getString(R.string.message_cannot_fire)
                    .format(res.getString(R.string.button_refund_all))
                Toast.makeText(gameView.gameActivity, text, Toast.LENGTH_SHORT).show()
                return
            }
            if (heroIsOnLeave(hero)) return
            when (data.level) {
                0 -> return  // should not happen
                1 -> { // sell hero completely
                    val refund = 1
                    data.coinsSpent = 0
                    purse.spend(-refund)
                    doDowngrade()
                }

                else -> {
                    val refund = data.level - 1
                    data.coinsSpent -= refund
                    purse.spend(-refund)
                    doDowngrade()
                }
            }

        }
        save()
        fillMarket(nextGameLevel)
        makeButtonText(hero)
    }

    private fun save() {
        Persistency(gameView.gameActivity).saveHeroes(gameMechanics)
        Persistency(gameView.gameActivity).saveCoins(gameMechanics)
    }


    @Suppress("UNUSED_PARAMETER")
    fun onScroll(event1: MotionEvent?, event2: MotionEvent?, dX: Float, dY: Float): Boolean {
        val scrollFactor = 1.4f  // higher values make scrolling faster
        if (dY == 0f)
            return false  // only vertical movements are considered here
        event1?.let {
            val posX = it.x.toInt()
            val posY = it.y.toInt()
            when {
                cardsArea.contains(posX, posY) -> {
                    cardViewOffset -= dX * scrollFactor
                    if (cardViewOffset > 0f) cardViewOffset = 0f
                    arrangeCards(upgrades, cardViewOffset)
                }

                bioPanel.contains(posX, posY) -> {
                    selected?.biography?.scroll(dY)
                }

                else -> {}
            }
        }
        return true
    }

    private fun heroIsOnLeave(hero: Hero): Boolean {
        if (hero.isOnLeave) {
            val res = resources
            val text = res.getString(R.string.message_is_on_leave).format(hero.person.name)
            Toast.makeText(gameView.gameActivity, text, Toast.LENGTH_SHORT).show()
            return true
        } else
            return false
    }

    override fun display(canvas: Canvas, viewport: Viewport) {
        if (gameMechanics.state.phase != GameMechanics.GamePhase.MARKETPLACE)
            return
        if (myArea.width() == 0 || myArea.height() == 0) {
            val width = gameView.gameActivity.gameView.width
            val height = gameView.gameActivity.gameView.height
            if (width > 0 && height > 0) {
                setSize(Rect(0, 0, width, height))
                fillMarket(nextGameLevel)
            } else
                return
        }
        canvas.drawColor(resources.getColor(R.color.background_tertiary_color))
        // draw cards
        selected?.card?.displayHighlightFrame(canvas)
        for (hero in upgrades)
            hero.card.display(canvas, viewport)

        displayAvailableCoins(canvas, viewport, Rect(0, GameView.notchSize, myArea.right, cardsArea.top))

        // draw buttons
        buttonFinish?.display(canvas)
        if (showRefundOneButton) buttonRefund?.display(canvas)
        buttonRefundAll?.display(canvas)
        selected?.let {
            buttonPurchase?.display(canvas)
            if (showRefundOneButton) buttonWikipedia?.display(canvas)
        }

        // draw biography
        selected?.biography?.display(canvas)
    }

    private fun displayAvailableCoins(
        canvas: Canvas,
        viewport: Viewport,
        coinsArea: Rect
    ) {
        displayCompactCoins(canvas, viewport, coinsArea)
    }

    private fun displayCompactCoins(
        canvas: Canvas,
        viewport: Viewport,
        area: Rect
    ) {
        val coin = coins.first()
        val centerY = area.centerY()
        val startX = area.left + GameView.globalPadding

        coin.setCenter(startX + coinSize / 2, centerY)
        coin.display(canvas, viewport)

        val text = "× ${coins.size}"

        val textPaint = TextPaint(
                textStyleContent(gameView.context)
        ).apply {
            textSize *= gameView.textScaleFactor
        }

        val textX = coin.myArea.right + GameView.globalPadding
        val textY = centerY + textPaint.textSize / 3

        canvas.drawText(text, textX.toFloat(), textY, textPaint)
    }

    private fun makeButtonText(card: Hero?) {
        buttonPurchase?.text = purchaseButtonText(card)
        showRefundOneButton = displayRefundOneButton(card)
        layoutButtons()

        // relayout buttons and recalc dependent areas
        setSize(Rect(0, 0, gameView.width, gameView.height))
    }

    private fun purchaseButtonText(card: Hero?): String {
        val hero = card ?: selected
        hero?.let {
            val level = it.data.level
            val price = it.getPrice(level)
            return if (level <= 1)
                resources.getString(R.string.button_purchase)
            else
                resources.getString(R.string.button_purchase_plural).format(price)
        }
        return resources.getString(R.string.button_purchase)
    }

    private fun displayRefundOneButton(card: Hero?): Boolean {
        if (card == null) return false
        return card.data.level > 0
    }

    inner class Coin(val gameMechanics: GameMechanics, size: Int) : GameElement(), Fadable,
        Flippable
    /** graphical representation of a crypto coin */
    {
        val paint = Paint()
        val myArea = Rect(0, 0, size, size)
        private var myBitmap: Bitmap = createBitmap(size, size)
        private val myCanvas = Canvas(myBitmap)
        var isCurrentlyFlipping = false

        init {
            paint.alpha = 255
            myCanvas.drawBitmap(gameView.currentCoinBitmap(nextGameLevel), null, Rect(0, 0, size, size), paint)
        }

        override fun update() {
        }

        override fun display(canvas: Canvas, viewport: Viewport) {
            canvas.drawBitmap(myBitmap, null, myArea, paint)
        }

        override fun fadeDone(type: Fader.Type) {
        }

        override fun setOpacity(opacity: Float) {
            paint.alpha = (opacity * 255f).toInt()
        }

        override fun setBitmap(bitmap: Bitmap) {
            myBitmap = bitmap
        }

        override fun provideBitmap(): Bitmap {
            return myBitmap
        }

        override fun flipStart() {
            isCurrentlyFlipping = true
        }

        override fun flipDone() {
            isCurrentlyFlipping = false
        }

        fun setCenter(x: Int, y: Int) {
            myArea.setCenter(x, y)
        }


    }
}


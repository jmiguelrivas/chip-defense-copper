package com.rama.chipdefense_copper.data

import android.content.res.Resources
import android.graphics.Bitmap
import com.rama.chipdefense_copper.GameMode
import com.rama.chipdefense_copper.Hero
import com.rama.chipdefense_copper.R
import com.rama.chipdefense_copper.activities.GameActivity
import com.rama.chipdefense_copper.utils.createHero

object HeroesCatalog {

    data class HeroInfo(
        val key: String,
        val name: String,
        val fullName: String,
        val effect: String,
        val vitae: String,
        val picture: Bitmap,
        val isAvailableIn: (GameMode) -> Boolean
    )

    private val basicHeroes = mapOf(
            Hero.Type.INCREASE_CHIP_SUB_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "turing",
                        name = "Turing",
                        fullName = "Alan Turing",
                        effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SUB"),
                        vitae = res.getString(R.string.turing),
                        picture = game.createHero("Alan Turing", res.getString(R.string.shortdesc_SUB), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_CHIP_SHR_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "lovelace",
                        name = "Lovelace",
                        fullName = "Ada Lovelace",
                        effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SHR"),
                        vitae = res.getString(R.string.lovelace),
                        picture = game.createHero("Ada Lovelace", res.getString(R.string.shortdesc_SHR), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_CHIP_MEM_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "bertrand_russell",
                        name = "bertrand_russell",
                        fullName = "Bertrand Russell",
                        effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("MEM"),
                        vitae = res.getString(R.string.bertrand_russell),
                        picture = game.createHero("Bertrand Russell", res.getString(R.string.shortdesc_MEM), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.REDUCE_HEAT to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "chappe",
                        name = "Chappe",
                        fullName = "Claude Chappe",
                        effect = res.getString(R.string.HERO_EFFECT_HEAT),
                        vitae = res.getString(R.string.chappe),
                        picture = game.createHero("Claude Chappe", res.getString(R.string.shortdesc_heat), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_STARTING_CASH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "hollerith",
                        name = "Hollerith",
                        fullName = "Herman Hollerith",
                        effect = res.getString(R.string.HERO_EFFECT_STARTINFO),
                        vitae = res.getString(R.string.hollerith),
                        picture = game.createHero("Herman Hollerith", res.getString(R.string.shortdesc_startinfo), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DECREASE_UPGRADE_COST to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "nikola_tesla",
                        name = "nikola_tesla",
                        fullName = "Nikola Tesla",
                        effect = res.getString(R.string.HERO_EFFECT_UPGRADECOST),
                        vitae = res.getString(R.string.nikola_tesla),
                        picture = game.createHero("Nikola Tesla", res.getString(R.string.shortdesc_upgrade), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.ADDITIONAL_LIVES to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "zuse",
                        name = "Zuse",
                        fullName = "Konrad Zuse",
                        effect = res.getString(R.string.HERO_EFFECT_LIVES),
                        vitae = res.getString(R.string.zuse),
                        picture = game.createHero("Konrad Zuse", res.getString(R.string.shortdesc_lives), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.LIMIT_UNWANTED_CHIPS to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "kilby",
                        name = "Kilby",
                        fullName = "Jack Kilby",
                        effect = res.getString(R.string.HERO_EFFECT_LIMITUNWANTED),
                        vitae = res.getString(R.string.kilby),
                        picture = game.createHero("Jack Kilby", res.getString(R.string.shortdesc_limit_unwanted), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.CREATE_ADDITIONAL_CHIPS to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "neumann",
                        name = "Neumann",
                        fullName = "John von Neumann",
                        effect = res.getString(R.string.HERO_CREATE_CHIPS),
                        vitae = res.getString(R.string.neumann),
                        picture = game.createHero("John von Neumann", res.getString(R.string.shortdesc_create_wanted), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.ENABLE_MEM_UPGRADE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "leibniz",
                        name = "Leibniz",
                        fullName = "Gottfried Wilhelm Leibniz",
                        effect = res.getString(R.string.HERO_EFFECT_ENABLEMEM),
                        vitae = res.getString(R.string.leibniz),
                        picture = game.createHero("Gottfried Wilhelm Leibniz", res.getString(R.string.shortdesc_MEM), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DECREASE_ATT_FREQ to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "al_khwarizmi",
                        name = "al_khwarizmi",
                        fullName = "Al-Khwarizmi",
                        effect = res.getString(R.string.HERO_EFFECT_FREQUENCY),
                        vitae = res.getString(R.string.al_khwarizmi),
                        picture = game.createHero("Al-Khwarizmi", res.getString(R.string.shortdesc_frequency), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DECREASE_COIN_STRENGTH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "gottlob_frege",
                        name = "gottlob_frege",
                        fullName = "Gottlob Frege",
                        effect = res.getString(R.string.HERO_EFFECT_COINSTRENGTH),
                        vitae = res.getString(R.string.gottlob_frege),
                        picture = game.createHero("Gottlob Frege", res.getString(R.string.shortdesc_coin_strength), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.GAIN_CASH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "franke",
                        name = "Franke",
                        fullName = "Herbert W. Franke",
                        effect = res.getString(R.string.HERO_EFFECT_INFOOVERTIME),
                        vitae = res.getString(R.string.franke),
                        picture = game.createHero("Herbert W. Franke", res.getString(R.string.shortdesc_info_gain), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.GAIN_CASH_ON_KILL to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "mandelbrot",
                        name = "Mandelbrot",
                        fullName = "Benoît B. Mandelbrot",
                        effect = res.getString(R.string.HERO_EFFECT_GAININFO),
                        vitae = res.getString(R.string.mandelbrot),
                        picture = game.createHero("Benoît B. Mandelbrot", res.getString(R.string.shortdesc_info_on_kill), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DECREASE_REMOVAL_COST to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "hamilton",
                        name = "Hamilton",
                        fullName = "Margaret Hamilton",
                        effect = res.getString(R.string.HERO_EFFECT_DECREASEREMOVAL),
                        vitae = res.getString(R.string.hamilton),
                        picture = game.createHero("Margaret Hamilton", res.getString(R.string.shortdesc_reduce_removal), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DECREASE_ATT_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "vaughan",
                        name = "Vaughan",
                        fullName = "Dorothy Vaughan",
                        effect = res.getString(R.string.HERO_EFFECT_ATTSPEED),
                        vitae = res.getString(R.string.vaughan),
                        picture = game.createHero("Dorothy Vaughan", res.getString(R.string.shortdesc_att_speed), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DECREASE_ATT_STRENGTH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "euclid",
                        name = "Euclid",
                        fullName = "Euclid",
                        effect = res.getString(R.string.HERO_EFFECT_ATTSTRENGTH),
                        vitae = res.getString(R.string.euclid),
                        picture = game.createHero("Euclid", res.getString(R.string.shortdesc_att_strength), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_REFUND to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "tramiel",
                        name = "Tramiel",
                        fullName = "Jack Tramiel",
                        effect = res.getString(R.string.HERO_EFFECT_REFUNDPRICE),
                        vitae = res.getString(R.string.tramiel),
                        picture = game.createHero("Jack Tramiel", res.getString(R.string.shortdesc_refund), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_CHIP_SUB_RANGE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "wiener",
                        name = "Wiener",
                        fullName = "Norbert Wiener",
                        effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SUB"),
                        vitae = res.getString(R.string.wiener),
                        picture = game.createHero(
                                "Norbert Wiener", res.getString(R.string.shortdesc_range)
                            .format("SUB"), R.style.Chip_Gold
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_CHIP_SHR_RANGE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "pascal",
                        name = "Pascal",
                        fullName = "Blaise Pascal",
                        effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SHR"),
                        vitae = res.getString(R.string.pascal),
                        picture = game.createHero(
                                "Blaise Pascal", res.getString(R.string.shortdesc_range)
                            .format("SHR"), R.style.Chip_Gold
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_CHIP_MEM_RANGE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "hopper",
                        name = "Hopper",
                        fullName = "Grace Hopper",
                        effect = res.getString(R.string.HERO_EFFECT_RANGE).format("MEM"),
                        vitae = res.getString(R.string.hopper),
                        picture = game.createHero(
                                "Grace Hopper", res.getString(R.string.shortdesc_range)
                            .format("MEM"), R.style.Chip_Gold
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_MAX_HERO_LEVEL to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "charles_babbage",
                        name = "charles_babbage",
                        fullName = "Charles Babbage",
                        effect = res.getString(R.string.HERO_EFFECT_MAXHEROUPGRADE),
                        vitae = res.getString(R.string.charles_babbage),
                        picture = game.createHero("Charles Babbage", res.getString(R.string.shortdesc_max_hero_upgrade), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_CHIP_RES_STRENGTH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "ohm",
                        name = "Ohm",
                        fullName = "Georg Ohm",
                        effect = res.getString(R.string.HERO_EFFECT_RES_STRENGTH),
                        vitae = res.getString(R.string.ohm),
                        picture = game.createHero("Georg Ohm", res.getString(R.string.shortdesc_RES), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.INCREASE_CHIP_RES_DURATION to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "volta",
                        name = "Volta",
                        fullName = "Alessandro Volta",
                        effect = res.getString(R.string.HERO_EFFECT_RES_DURATION),
                        vitae = res.getString(R.string.volta),
                        picture = game.createHero("Alessandro Volta", res.getString(R.string.shortdesc_duration), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.CONVERT_HEAT to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "shannon",
                        name = "Shannon",
                        fullName = "Claude Shannon",
                        effect = res.getString(R.string.HERO_EFFECT_CONVERT_HEAT),
                        vitae = res.getString(R.string.shannon),
                        picture = game.createHero("Claude Shannon", res.getString(R.string.shortdesc_heat_conversion), R.style.Chip_Gold),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DOUBLE_HIT_SUB to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "boole",
                        name = "Boole",
                        fullName = "George Boole",
                        effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SUB"),
                        vitae = res.getString(R.string.boole),
                        picture = game.createHero(
                                "George Boole", res.getString(R.string.shortdesc_double_chance)
                            .format("SUB"), R.style.Chip_Gold
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },

            Hero.Type.DOUBLE_HIT_SHR to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "conway",
                        name = "Conway",
                        fullName = "John Horton Conway",
                        effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SHR"),
                        vitae = res.getString(R.string.conway),
                        picture = game.createHero(
                                "John Horton Conway", res.getString(R.string.shortdesc_double_chance)
                            .format("SHR"), R.style.Chip_Gold
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Basic }
                )
            },
    )

    private val endlessHeroes = mapOf(
            Hero.Type.INCREASE_CHIP_SUB_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "torvalds",
                        name = "torvalds",
                        fullName = "Linus Torvalds",
                        effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SUB"),
                        vitae = res.getString(R.string.torvalds),
                        picture = game.createHero("Linus Torvalds", res.getString(R.string.shortdesc_SUB), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_CHIP_SHR_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "ward_cunningham",
                        name = "ward_cunningham",
                        fullName = "Ward Cunningham",
                        effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SHR"),
                        vitae = res.getString(R.string.ward_cunningham),
                        picture = game.createHero("Ward Cunningham", res.getString(R.string.shortdesc_SHR), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_CHIP_MEM_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "knuth",
                        name = "Knuth",
                        fullName = "Donald E. Knuth",
                        effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("MEM"),
                        vitae = res.getString(R.string.knuth),
                        picture = game.createHero("Donald E. Knuth", res.getString(R.string.shortdesc_MEM), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.REDUCE_HEAT to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "phil_zimmermann",
                        name = "phil_zimmermann",
                        fullName = "Phil Zimmermann",
                        effect = res.getString(R.string.HERO_EFFECT_HEAT),
                        vitae = res.getString(R.string.phil_zimmermann),
                        picture = game.createHero("Phil Zimmermann", res.getString(R.string.shortdesc_heat), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_STARTING_CASH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "larry_wall",
                        name = "larry_wall",
                        fullName = "Larry Wall",
                        effect = res.getString(R.string.HERO_EFFECT_STARTINFO),
                        vitae = res.getString(R.string.larry_wall),
                        picture = game.createHero("Larry Wall", res.getString(R.string.shortdesc_startinfo), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DECREASE_UPGRADE_COST to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "osborne",
                        name = "Osborne",
                        fullName = "Adam Osborne",
                        effect = res.getString(R.string.HERO_EFFECT_UPGRADECOST),
                        vitae = res.getString(R.string.osborne),
                        picture = game.createHero("Adam Osborne", res.getString(R.string.shortdesc_upgrade), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.ADDITIONAL_LIVES to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "berners_lee",
                        name = "berners_lee",
                        fullName = "Tim Berners-Lee",
                        effect = res.getString(R.string.HERO_EFFECT_LIVES),
                        vitae = res.getString(R.string.berners_lee),
                        picture = game.createHero("Tim Berners-Lee", res.getString(R.string.shortdesc_lives), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.LIMIT_UNWANTED_CHIPS to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "guido_van_rossum",
                        name = "guido_van_rossum",
                        fullName = "Guido van Rossum",
                        effect = res.getString(R.string.HERO_EFFECT_LIMITUNWANTED),
                        vitae = res.getString(R.string.guido_van_rossum),
                        picture = game.createHero("Guido van Rossum", res.getString(R.string.shortdesc_limit_unwanted), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.CREATE_ADDITIONAL_CHIPS to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "mitchell_baker",
                        name = "mitchell_baker",
                        fullName = "Mitchell Baker",
                        effect = res.getString(R.string.HERO_CREATE_CHIPS),
                        vitae = res.getString(R.string.mitchell_baker),
                        picture = game.createHero("John von Neumann", res.getString(R.string.shortdesc_create_wanted), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.ENABLE_MEM_UPGRADE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "eric_s_raymond",
                        name = "eric_s_raymond",
                        fullName = "Eric S. Raymond",
                        effect = res.getString(R.string.HERO_EFFECT_ENABLEMEM),
                        vitae = res.getString(R.string.eric_s_raymond),
                        picture = game.createHero("Eric S. Raymond", res.getString(R.string.shortdesc_MEM), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DECREASE_ATT_FREQ to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "stallman",
                        name = "Stallman",
                        fullName = "Richard Stallman",
                        effect = res.getString(R.string.HERO_EFFECT_FREQUENCY),
                        vitae = res.getString(R.string.stallman),
                        picture = game.createHero("Richard Stallman", res.getString(R.string.shortdesc_frequency), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DECREASE_COIN_STRENGTH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "diffie",
                        name = "Diffie",
                        fullName = "Whitfield Diffie",
                        effect = res.getString(R.string.HERO_EFFECT_COINSTRENGTH),
                        vitae = res.getString(R.string.diffie),
                        picture = game.createHero("Whitfield Diffie", res.getString(R.string.shortdesc_coin_strength), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.GAIN_CASH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "bruce_perens",
                        name = "bruce_perens",
                        fullName = "Bruce Perens",
                        effect = res.getString(R.string.HERO_EFFECT_INFOOVERTIME),
                        vitae = res.getString(R.string.bruce_perens),
                        picture = game.createHero("Bruce Perens", res.getString(R.string.shortdesc_info_gain), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.GAIN_CASH_ON_KILL to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "chris_lilley",
                        name = "chris_lilley",
                        fullName = "Chris Lilley",
                        effect = res.getString(R.string.HERO_EFFECT_GAININFO),
                        vitae = res.getString(R.string.chris_lilley),
                        picture = game.createHero("Chris Lilley", res.getString(R.string.shortdesc_info_on_kill), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DECREASE_REMOVAL_COST to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "brendan_eich",
                        name = "brendan_eich",
                        fullName = "Brendan Eich",
                        effect = res.getString(R.string.HERO_EFFECT_DECREASEREMOVAL),
                        vitae = res.getString(R.string.brendan_eich),
                        picture = game.createHero("Brendan Eich", res.getString(R.string.shortdesc_reduce_removal), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DECREASE_ATT_SPEED to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "chris_dibona",
                        name = "chris_dibona",
                        fullName = "Chris DiBona",
                        effect = res.getString(R.string.HERO_EFFECT_ATTSPEED),
                        vitae = res.getString(R.string.chris_dibona),
                        picture = game.createHero("Chris DiBona", res.getString(R.string.shortdesc_att_speed), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DECREASE_ATT_STRENGTH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "schneier",
                        name = "Schneier",
                        fullName = "Bruce Schneier",
                        effect = res.getString(R.string.HERO_EFFECT_ATTSTRENGTH),
                        vitae = res.getString(R.string.schneier),
                        picture = game.createHero("Bruce Schneier", res.getString(R.string.shortdesc_att_strength), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_REFUND to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "bert_bos",
                        name = "bert_bos",
                        fullName = "Bert Bos",
                        effect = res.getString(R.string.HERO_EFFECT_REFUNDPRICE),
                        vitae = res.getString(R.string.bert_bos),
                        picture = game.createHero("Bert Bos", res.getString(R.string.shortdesc_refund), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_CHIP_SUB_RANGE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "yukihiro_matsumoto",
                        name = "yukihiro_matsumoto",
                        fullName = "Yukihiro Matsumoto",
                        effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SUB"),
                        vitae = res.getString(R.string.yukihiro_matsumoto),
                        picture = game.createHero(
                                "Yukihiro Matsumoto", res.getString(R.string.shortdesc_range)
                            .format("SUB"), R.style.Chip_Red
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_CHIP_SHR_RANGE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "hakon_wium_lie",
                        name = "hakon_wium_lie",
                        fullName = "Håkon Wium Lie",
                        effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SHR"),
                        vitae = res.getString(R.string.hakon_wium_lie),
                        picture = game.createHero(
                                "Håkon Wium Lie", res.getString(R.string.shortdesc_range)
                            .format("SHR"), R.style.Chip_Red
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_CHIP_MEM_RANGE to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "ciaran_gultnieks",
                        name = "ciaran_gultnieks",
                        fullName = "Ciaran Gultnieks",
                        effect = res.getString(R.string.HERO_EFFECT_RANGE).format("MEM"),
                        vitae = res.getString(R.string.ciaran_gultnieks),
                        picture = game.createHero(
                                "Ciaran Gultnieks", res.getString(R.string.shortdesc_range)
                            .format("MEM"), R.style.Chip_Red
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_MAX_HERO_LEVEL to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "meier",
                        name = "Meier",
                        fullName = "Sid Meier",
                        effect = res.getString(R.string.HERO_EFFECT_MAXHEROUPGRADE),
                        vitae = res.getString(R.string.meier),
                        picture = game.createHero("Sid Meier", res.getString(R.string.shortdesc_max_hero_upgrade), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_CHIP_RES_STRENGTH to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "miguel_de_icaza",
                        name = "miguel_de_icaza",
                        fullName = "Miguel de Icaza",
                        effect = res.getString(R.string.HERO_EFFECT_RES_STRENGTH),
                        vitae = res.getString(R.string.miguel_de_icaza),
                        picture = game.createHero("Miguel de Icaza", res.getString(R.string.shortdesc_RES), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.INCREASE_CHIP_RES_DURATION to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "brian_behlendorf",
                        name = "brian_behlendorf",
                        fullName = "Brian Behlendorf",
                        effect = res.getString(R.string.HERO_EFFECT_RES_DURATION),
                        vitae = res.getString(R.string.brian_behlendorf),
                        picture = game.createHero("Brian Behlendorf", res.getString(R.string.shortdesc_duration), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.CONVERT_HEAT to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "federico_mena_quintero",
                        name = "federico_mena_quintero",
                        fullName = "Federico Mena Quintero",
                        effect = res.getString(R.string.HERO_EFFECT_CONVERT_HEAT),
                        vitae = res.getString(R.string.federico_mena_quintero),
                        picture = game.createHero("Federico Mena Quintero", res.getString(R.string.shortdesc_heat_conversion), R.style.Chip_Red),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DOUBLE_HIT_SUB to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "luis_von_ahn",
                        name = "luis_von_ahn",
                        fullName = "Luis von Ahn",
                        effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SUB"),
                        vitae = res.getString(R.string.luis_von_ahn),
                        picture = game.createHero(
                                "Luis von Ahn", res.getString(R.string.shortdesc_double_chance)
                            .format("SUB"), R.style.Chip_Red
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },

            Hero.Type.DOUBLE_HIT_SHR to { game: GameActivity, res: Resources ->
                HeroInfo(
                        key = "gabriela_salas_cabrera",
                        name = "gabriela_salas_cabrera",
                        fullName = "Gabriela Salas Cabrera",
                        effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SHR"),
                        vitae = res.getString(R.string.gabriela_salas_cabrera),
                        picture = game.createHero(
                                "Gabriela Salas Cabrera", res.getString(R.string.shortdesc_double_chance)
                            .format("SHR"), R.style.Chip_Red
                        ),
                        isAvailableIn = { mode -> mode == GameMode.Endless }
                )
            },
    )

    fun get(type: Hero.Type, game: GameActivity, res: Resources, mode: GameMode): HeroInfo {
        val pools = listOf(basicHeroes, endlessHeroes)

        for (pool in pools) {
            pool[type]?.let { heroProvider ->
                val heroInfo = heroProvider(game, res)
                if (heroInfo.isAvailableIn(mode)) return heroInfo
            }
        }

        throw IllegalArgumentException("Hero type $type not found for mode $mode")
    }

    fun getAllForMode(mode: GameMode, game: GameActivity, res: Resources): List<HeroInfo> {
        val list = mutableListOf<HeroInfo>()
        basicHeroes.values.forEach { if (it(game, res).isAvailableIn(mode)) list.add(it(game, res)) }
        endlessHeroes.values.forEach { if (it(game, res).isAvailableIn(mode)) list.add(it(game, res)) }
        return list
    }


}
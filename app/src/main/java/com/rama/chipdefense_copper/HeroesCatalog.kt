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

    fun get(
        type: Hero.Type,
        game: GameActivity,
        res: Resources
    ): HeroInfo {
        return when (type) {
            Hero.Type.INCREASE_CHIP_SUB_SPEED -> HeroInfo(
                    key = "turing",
                    name = "Turing",
                    fullName = "Alan Turing",
                    effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SUB"),
                    vitae = res.getString(R.string.turing),
                    picture = game.createHero("Alan Turing", res.getString(R.string.shortdesc_SUB), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_CHIP_SHR_SPEED -> HeroInfo(
                    key = "lovelace",
                    name = "Lovelace",
                    fullName = "Ada Lovelace",
                    effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SHR"),
                    vitae = res.getString(R.string.lovelace),
                    picture = game.createHero("Ada Lovelace", res.getString(R.string.shortdesc_SHR), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_CHIP_MEM_SPEED -> HeroInfo(
                    key = "knuth",
                    name = "Knuth",
                    fullName = "Donald E. Knuth",
                    effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("MEM"),
                    vitae = res.getString(R.string.knuth),
                    picture = game.createHero("Donald E. Knuth", res.getString(R.string.shortdesc_MEM), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.REDUCE_HEAT -> HeroInfo(
                    key = "chappe",
                    name = "Chappe",
                    fullName = "Claude Chappe",
                    effect = res.getString(R.string.HERO_EFFECT_HEAT),
                    vitae = res.getString(R.string.chappe),
                    picture = game.createHero("Claude Chappe", res.getString(R.string.shortdesc_heat), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_STARTING_CASH -> HeroInfo(
                    key = "hollerith",
                    name = "Hollerith",
                    fullName = "Herman Hollerith",
                    effect = res.getString(R.string.HERO_EFFECT_STARTINFO),
                    vitae = res.getString(R.string.hollerith),
                    picture = game.createHero("Herman Hollerith", res.getString(R.string.shortdesc_startinfo), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DECREASE_UPGRADE_COST -> HeroInfo(
                    key = "osborne",
                    name = "Osborne",
                    fullName = "Adam Osborne",
                    effect = res.getString(R.string.HERO_EFFECT_UPGRADECOST),
                    vitae = res.getString(R.string.osborne),
                    picture = game.createHero("Adam Osborne", res.getString(R.string.shortdesc_upgrade), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.ADDITIONAL_LIVES -> HeroInfo(
                    key = "zuse",
                    name = "Zuse",
                    fullName = "Konrad Zuse",
                    effect = res.getString(R.string.HERO_EFFECT_LIVES),
                    vitae = res.getString(R.string.zuse),
                    picture = game.createHero("Konrad Zuse", res.getString(R.string.shortdesc_lives), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.LIMIT_UNWANTED_CHIPS -> HeroInfo(
                    key = "kilby",
                    name = "Kilby",
                    fullName = "Jack Kilby",
                    effect = res.getString(R.string.HERO_EFFECT_LIMITUNWANTED),
                    vitae = res.getString(R.string.kilby),
                    picture = game.createHero("Jack Kilby", res.getString(R.string.shortdesc_limit_unwanted), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.CREATE_ADDITIONAL_CHIPS -> HeroInfo(
                    key = "neumann",
                    name = "Neumann",
                    fullName = "John von Neumann",
                    effect = res.getString(R.string.HERO_CREATE_CHIPS),
                    vitae = res.getString(R.string.neumann),
                    picture = game.createHero("John von Neumann", res.getString(R.string.shortdesc_create_wanted), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.ENABLE_MEM_UPGRADE -> HeroInfo(
                    key = "leibniz",
                    name = "Leibniz",
                    fullName = "Gottfried Wilhelm Leibniz",
                    effect = res.getString(R.string.HERO_EFFECT_ENABLEMEM),
                    vitae = res.getString(R.string.leibniz),
                    picture = game.createHero("Gottfried Wilhelm Leibniz", res.getString(R.string.shortdesc_MEM), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DECREASE_ATT_FREQ -> HeroInfo(
                    key = "stallman",
                    name = "Stallman",
                    fullName = "Richard Stallman",
                    effect = res.getString(R.string.HERO_EFFECT_FREQUENCY),
                    vitae = res.getString(R.string.stallman),
                    picture = game.createHero("Richard Stallman", res.getString(R.string.shortdesc_frequency), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DECREASE_COIN_STRENGTH -> HeroInfo(
                    key = "diffie",
                    name = "Diffie",
                    fullName = "Whit Diffie",
                    effect = res.getString(R.string.HERO_EFFECT_COINSTRENGTH),
                    vitae = res.getString(R.string.diffie),
                    picture = game.createHero("Whit Diffie", res.getString(R.string.shortdesc_coin_strength), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.GAIN_CASH -> HeroInfo(
                    key = "franke",
                    name = "Franke",
                    fullName = "Herbert W. Franke",
                    effect = res.getString(R.string.HERO_EFFECT_INFOOVERTIME),
                    vitae = res.getString(R.string.franke),
                    picture = game.createHero("Herbert W. Franke", res.getString(R.string.shortdesc_info_gain), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.GAIN_CASH_ON_KILL -> HeroInfo(
                    key = "mandelbrot",
                    name = "Mandelbrot",
                    fullName = "Benoît B. Mandelbrot",
                    effect = res.getString(R.string.HERO_EFFECT_GAININFO),
                    vitae = res.getString(R.string.mandelbrot),
                    picture = game.createHero("Benoît B. Mandelbrot", res.getString(R.string.shortdesc_info_on_kill), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DECREASE_REMOVAL_COST -> HeroInfo(
                    key = "hamilton",
                    name = "Hamilton",
                    fullName = "Margaret Hamilton",
                    effect = res.getString(R.string.HERO_EFFECT_DECREASEREMOVAL),
                    vitae = res.getString(R.string.hamilton),
                    picture = game.createHero("Margaret Hamilton", res.getString(R.string.shortdesc_reduce_removal), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DECREASE_ATT_SPEED -> HeroInfo(
                    key = "vaughan",
                    name = "Vaughan",
                    fullName = "Dorothy Vaughan",
                    effect = res.getString(R.string.HERO_EFFECT_ATTSPEED),
                    vitae = res.getString(R.string.vaughan),
                    picture = game.createHero("Dorothy Vaughan", res.getString(R.string.shortdesc_att_speed), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DECREASE_ATT_STRENGTH -> HeroInfo(
                    key = "schneier",
                    name = "Schneier",
                    fullName = "Bruce Schneier",
                    effect = res.getString(R.string.HERO_EFFECT_ATTSTRENGTH),
                    vitae = res.getString(R.string.schneier),
                    picture = game.createHero("Bruce Schneier", res.getString(R.string.shortdesc_att_strength), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_REFUND -> HeroInfo(
                    key = "tramiel",
                    name = "Tramiel",
                    fullName = "Jack Tramiel",
                    effect = res.getString(R.string.HERO_EFFECT_REFUNDPRICE),
                    vitae = res.getString(R.string.tramiel),
                    picture = game.createHero("Jack Tramiel", res.getString(R.string.shortdesc_refund), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_CHIP_SUB_RANGE -> HeroInfo(
                    key = "wiener",
                    name = "Wiener",
                    fullName = "Norbert Wiener",
                    effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SUB"),
                    vitae = res.getString(R.string.wiener),
                    picture = game.createHero(
                            "Norbert Wiener", res.getString(R.string.shortdesc_range)
                        .format("SUB"), R.style.Chip_Gold
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_CHIP_SHR_RANGE -> HeroInfo(
                    key = "pascal",
                    name = "Pascal",
                    fullName = "Blaise Pascal",
                    effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SHR"),
                    vitae = res.getString(R.string.pascal),
                    picture = game.createHero(
                            "Blaise Pascal", res.getString(R.string.shortdesc_range)
                        .format("SHR"), R.style.Chip_Gold
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_CHIP_MEM_RANGE -> HeroInfo(
                    key = "hopper",
                    name = "Hopper",
                    fullName = "Grace Hopper",
                    effect = res.getString(R.string.HERO_EFFECT_RANGE).format("MEM"),
                    vitae = res.getString(R.string.hopper),
                    picture = game.createHero(
                            "Grace Hopper", res.getString(R.string.shortdesc_range)
                        .format("MEM"), R.style.Chip_Gold
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_MAX_HERO_LEVEL -> HeroInfo(
                    key = "meier",
                    name = "Meier",
                    fullName = "Sid Meier",
                    effect = res.getString(R.string.HERO_EFFECT_MAXHEROUPGRADE),
                    vitae = res.getString(R.string.meier),
                    picture = game.createHero("Sid Meier", res.getString(R.string.shortdesc_max_hero_upgrade), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_CHIP_RES_STRENGTH -> HeroInfo(
                    key = "ohm",
                    name = "Ohm",
                    fullName = "Georg Ohm",
                    effect = res.getString(R.string.HERO_EFFECT_RES_STRENGTH),
                    vitae = res.getString(R.string.ohm),
                    picture = game.createHero("Georg Ohm", res.getString(R.string.shortdesc_RES), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.INCREASE_CHIP_RES_DURATION -> HeroInfo(
                    key = "volta",
                    name = "Volta",
                    fullName = "Alessandro Volta",
                    effect = res.getString(R.string.HERO_EFFECT_RES_DURATION),
                    vitae = res.getString(R.string.volta),
                    picture = game.createHero("Alessandro Volta", res.getString(R.string.shortdesc_duration), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.CONVERT_HEAT -> HeroInfo(
                    key = "shannon",
                    name = "Shannon",
                    fullName = "Claude Shannon",
                    effect = res.getString(R.string.HERO_EFFECT_CONVERT_HEAT),
                    vitae = res.getString(R.string.shannon),
                    picture = game.createHero("Claude Shannon", res.getString(R.string.shortdesc_heat_conversion), R.style.Chip_Gold),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DOUBLE_HIT_SUB -> HeroInfo(
                    key = "boole",
                    name = "Boole",
                    fullName = "George Boole",
                    effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SUB"),
                    vitae = res.getString(R.string.boole),
                    picture = game.createHero(
                            "George Boole", res.getString(R.string.shortdesc_double_chance)
                        .format("SUB"), R.style.Chip_Gold
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            Hero.Type.DOUBLE_HIT_SHR -> HeroInfo(
                    key = "conway",
                    name = "Conway",
                    fullName = "John Horton Conway",
                    effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SHR"),
                    vitae = res.getString(R.string.conway),
                    picture = game.createHero(
                            "John Horton Conway", res.getString(R.string.shortdesc_double_chance)
                        .format("SHR"), R.style.Chip_Gold
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Basic }
            )

            // ENDLESS / TURBO MODE

            Hero.Type.INCREASE_CHIP_SUB_SPEED -> HeroInfo(
                    key = "turing",
                    name = "Turing",
                    fullName = "Alan Turing",
                    effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SUB"),
                    vitae = res.getString(R.string.turing),
                    picture = game.createHero("Alan Turing", res.getString(R.string.shortdesc_SUB), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_CHIP_SHR_SPEED -> HeroInfo(
                    key = "lovelace",
                    name = "Lovelace",
                    fullName = "Ada Lovelace",
                    effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("SHR"),
                    vitae = res.getString(R.string.lovelace),
                    picture = game.createHero("Ada Lovelace", res.getString(R.string.shortdesc_SHR), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_CHIP_MEM_SPEED -> HeroInfo(
                    key = "knuth",
                    name = "Knuth",
                    fullName = "Donald E. Knuth",
                    effect = res.getString(R.string.HERO_EFFECT_CHIPSPEED).format("MEM"),
                    vitae = res.getString(R.string.knuth),
                    picture = game.createHero("Donald E. Knuth", res.getString(R.string.shortdesc_MEM), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.REDUCE_HEAT -> HeroInfo(
                    key = "chappe",
                    name = "Chappe",
                    fullName = "Claude Chappe",
                    effect = res.getString(R.string.HERO_EFFECT_HEAT),
                    vitae = res.getString(R.string.chappe),
                    picture = game.createHero("Claude Chappe", res.getString(R.string.shortdesc_heat), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_STARTING_CASH -> HeroInfo(
                    key = "hollerith",
                    name = "Hollerith",
                    fullName = "Herman Hollerith",
                    effect = res.getString(R.string.HERO_EFFECT_STARTINFO),
                    vitae = res.getString(R.string.hollerith),
                    picture = game.createHero("Herman Hollerith", res.getString(R.string.shortdesc_startinfo), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DECREASE_UPGRADE_COST -> HeroInfo(
                    key = "osborne",
                    name = "Osborne",
                    fullName = "Adam Osborne",
                    effect = res.getString(R.string.HERO_EFFECT_UPGRADECOST),
                    vitae = res.getString(R.string.osborne),
                    picture = game.createHero("Adam Osborne", res.getString(R.string.shortdesc_upgrade), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.ADDITIONAL_LIVES -> HeroInfo(
                    key = "zuse",
                    name = "Zuse",
                    fullName = "Konrad Zuse",
                    effect = res.getString(R.string.HERO_EFFECT_LIVES),
                    vitae = res.getString(R.string.zuse),
                    picture = game.createHero("Konrad Zuse", res.getString(R.string.shortdesc_lives), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.LIMIT_UNWANTED_CHIPS -> HeroInfo(
                    key = "kilby",
                    name = "Kilby",
                    fullName = "Jack Kilby",
                    effect = res.getString(R.string.HERO_EFFECT_LIMITUNWANTED),
                    vitae = res.getString(R.string.kilby),
                    picture = game.createHero("Jack Kilby", res.getString(R.string.shortdesc_limit_unwanted), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.CREATE_ADDITIONAL_CHIPS -> HeroInfo(
                    key = "neumann",
                    name = "Neumann",
                    fullName = "John von Neumann",
                    effect = res.getString(R.string.HERO_CREATE_CHIPS),
                    vitae = res.getString(R.string.neumann),
                    picture = game.createHero("John von Neumann", res.getString(R.string.shortdesc_create_wanted), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.ENABLE_MEM_UPGRADE -> HeroInfo(
                    key = "leibniz",
                    name = "Leibniz",
                    fullName = "Gottfried Wilhelm Leibniz",
                    effect = res.getString(R.string.HERO_EFFECT_ENABLEMEM),
                    vitae = res.getString(R.string.leibniz),
                    picture = game.createHero("Gottfried Wilhelm Leibniz", res.getString(R.string.shortdesc_MEM), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DECREASE_ATT_FREQ -> HeroInfo(
                    key = "stallman",
                    name = "Stallman",
                    fullName = "Richard Stallman",
                    effect = res.getString(R.string.HERO_EFFECT_FREQUENCY),
                    vitae = res.getString(R.string.stallman),
                    picture = game.createHero("Richard Stallman", res.getString(R.string.shortdesc_frequency), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DECREASE_COIN_STRENGTH -> HeroInfo(
                    key = "diffie",
                    name = "Diffie",
                    fullName = "Whit Diffie",
                    effect = res.getString(R.string.HERO_EFFECT_COINSTRENGTH),
                    vitae = res.getString(R.string.diffie),
                    picture = game.createHero("Whit Diffie", res.getString(R.string.shortdesc_coin_strength), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.GAIN_CASH -> HeroInfo(
                    key = "franke",
                    name = "Franke",
                    fullName = "Herbert W. Franke",
                    effect = res.getString(R.string.HERO_EFFECT_INFOOVERTIME),
                    vitae = res.getString(R.string.franke),
                    picture = game.createHero("Herbert W. Franke", res.getString(R.string.shortdesc_info_gain), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.GAIN_CASH_ON_KILL -> HeroInfo(
                    key = "mandelbrot",
                    name = "Mandelbrot",
                    fullName = "Benoît B. Mandelbrot",
                    effect = res.getString(R.string.HERO_EFFECT_GAININFO),
                    vitae = res.getString(R.string.mandelbrot),
                    picture = game.createHero("Benoît B. Mandelbrot", res.getString(R.string.shortdesc_info_on_kill), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DECREASE_REMOVAL_COST -> HeroInfo(
                    key = "hamilton",
                    name = "Hamilton",
                    fullName = "Margaret Hamilton",
                    effect = res.getString(R.string.HERO_EFFECT_DECREASEREMOVAL),
                    vitae = res.getString(R.string.hamilton),
                    picture = game.createHero("Margaret Hamilton", res.getString(R.string.shortdesc_reduce_removal), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DECREASE_ATT_SPEED -> HeroInfo(
                    key = "vaughan",
                    name = "Vaughan",
                    fullName = "Dorothy Vaughan",
                    effect = res.getString(R.string.HERO_EFFECT_ATTSPEED),
                    vitae = res.getString(R.string.vaughan),
                    picture = game.createHero("Dorothy Vaughan", res.getString(R.string.shortdesc_att_speed), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DECREASE_ATT_STRENGTH -> HeroInfo(
                    key = "schneier",
                    name = "Schneier",
                    fullName = "Bruce Schneier",
                    effect = res.getString(R.string.HERO_EFFECT_ATTSTRENGTH),
                    vitae = res.getString(R.string.schneier),
                    picture = game.createHero("Bruce Schneier", res.getString(R.string.shortdesc_att_strength), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_REFUND -> HeroInfo(
                    key = "tramiel",
                    name = "Tramiel",
                    fullName = "Jack Tramiel",
                    effect = res.getString(R.string.HERO_EFFECT_REFUNDPRICE),
                    vitae = res.getString(R.string.tramiel),
                    picture = game.createHero("Jack Tramiel", res.getString(R.string.shortdesc_refund), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_CHIP_SUB_RANGE -> HeroInfo(
                    key = "wiener",
                    name = "Wiener",
                    fullName = "Norbert Wiener",
                    effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SUB"),
                    vitae = res.getString(R.string.wiener),
                    picture = game.createHero(
                            "Norbert Wiener", res.getString(R.string.shortdesc_range)
                        .format("SUB"), R.style.Chip_Red
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_CHIP_SHR_RANGE -> HeroInfo(
                    key = "pascal",
                    name = "Pascal",
                    fullName = "Blaise Pascal",
                    effect = res.getString(R.string.HERO_EFFECT_RANGE).format("SHR"),
                    vitae = res.getString(R.string.pascal),
                    picture = game.createHero(
                            "Blaise Pascal", res.getString(R.string.shortdesc_range)
                        .format("SHR"), R.style.Chip_Red
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_CHIP_MEM_RANGE -> HeroInfo(
                    key = "hopper",
                    name = "Hopper",
                    fullName = "Grace Hopper",
                    effect = res.getString(R.string.HERO_EFFECT_RANGE).format("MEM"),
                    vitae = res.getString(R.string.hopper),
                    picture = game.createHero(
                            "Grace Hopper", res.getString(R.string.shortdesc_range)
                        .format("MEM"), R.style.Chip_Red
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_MAX_HERO_LEVEL -> HeroInfo(
                    key = "meier",
                    name = "Meier",
                    fullName = "Sid Meier",
                    effect = res.getString(R.string.HERO_EFFECT_MAXHEROUPGRADE),
                    vitae = res.getString(R.string.meier),
                    picture = game.createHero("Sid Meier", res.getString(R.string.shortdesc_max_hero_upgrade), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_CHIP_RES_STRENGTH -> HeroInfo(
                    key = "ohm",
                    name = "Ohm",
                    fullName = "Georg Ohm",
                    effect = res.getString(R.string.HERO_EFFECT_RES_STRENGTH),
                    vitae = res.getString(R.string.ohm),
                    picture = game.createHero("Georg Ohm", res.getString(R.string.shortdesc_RES), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.INCREASE_CHIP_RES_DURATION -> HeroInfo(
                    key = "volta",
                    name = "Volta",
                    fullName = "Alessandro Volta",
                    effect = res.getString(R.string.HERO_EFFECT_RES_DURATION),
                    vitae = res.getString(R.string.volta),
                    picture = game.createHero("Alessandro Volta", res.getString(R.string.shortdesc_duration), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.CONVERT_HEAT -> HeroInfo(
                    key = "shannon",
                    name = "Shannon",
                    fullName = "Claude Shannon",
                    effect = res.getString(R.string.HERO_EFFECT_CONVERT_HEAT),
                    vitae = res.getString(R.string.shannon),
                    picture = game.createHero("Claude Shannon", res.getString(R.string.shortdesc_heat_conversion), R.style.Chip_Red),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DOUBLE_HIT_SUB -> HeroInfo(
                    key = "boole",
                    name = "Boole",
                    fullName = "George Boole",
                    effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SUB"),
                    vitae = res.getString(R.string.boole),
                    picture = game.createHero(
                            "George Boole", res.getString(R.string.shortdesc_double_chance)
                        .format("SUB"), R.style.Chip_Red
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )

            Hero.Type.DOUBLE_HIT_SHR -> HeroInfo(
                    key = "conway",
                    name = "Conway",
                    fullName = "John Horton Conway",
                    effect = res.getString(R.string.HERO_EFFECT_CHANCE_DOUBLE).format("SHR"),
                    vitae = res.getString(R.string.conway),
                    picture = game.createHero(
                            "John Horton Conway", res.getString(R.string.shortdesc_double_chance)
                        .format("SHR"), R.style.Chip_Red
                    ),
                    isAvailableIn = { mode -> mode is GameMode.Endless }
            )
        }
    }
}
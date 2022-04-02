package me.yoursole.ctf.datafiles

import me.yoursole.ctf.CTF
import me.yoursole.ctf.datafiles.items.HermesBoots
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.scoreboard.DisplaySlot
import java.util.*

class GameLoop {
    companion object {
        var taskId = -1
    }

    init {
        taskId = Bukkit.getScheduler()
            .scheduleSyncRepeatingTask(CTF.instance, {
                if (GameData.droppingPos != null && GameData.dropLoc != GameData.droppingPos) {
                    GameData.droppingPos!!.block.type = Material.AIR
                    GameData.droppingPos!!.add(0.0, -1.0, 0.0)
                    GameData.droppingPos!!.block.type = Material.END_GATEWAY
                }
                if (GameData.it != null) {
                    GameData.scores.compute(GameData.it!!.uniqueId) { _: UUID?, score: Int? -> (score ?: 0) + 1 }
                    GameData.it!!.addPotionEffect(GameData.slowness)
                    GameData.it!!.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 40.0
                    if (GameData.it!!.isInWater) {
                        GameData.it!!.remainingAir -= 2
                    }
                }
                for (player in Bukkit.getOnlinePlayers()) {
                    if (GameData.it == null) {
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 20.0
                        player.addPotionEffect(GameData.saturation)
                    } else {
                        if (player.uniqueId !== GameData.it?.uniqueId) {
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 20.0
                            player.addPotionEffect(GameData.haste)
                            if (GameData.it?.isInsideVehicle == true) {
                                player.addPotionEffect(GameData.dolphin)
                            }
                        }
                    }
                    player.addPotionEffect(GameData.nightVision)
                    var canFly = player.gameMode != GameMode.SURVIVAL && player.gameMode != GameMode.ADVENTURE
                    for (armor in player.inventory.armorContents!!) {
                        if (armor == null) continue
                        if (Utils.compareBreakable(armor, HermesBoots.item)) {
                            canFly = true
                        }
                    }
                    player.allowFlight = canFly
                    if (!canFly) player.isFlying = false
                }
                if (GameData.scores.isNotEmpty()) {
                    val sb = Bukkit.getScoreboardManager()
                    val s = sb.mainScoreboard
                    var o = s.getObjective(DisplaySlot.SIDEBAR)
                    if (o == null) {
                        o = s.registerNewObjective("Score", "dummy", "Scores")
                        o.displaySlot = DisplaySlot.SIDEBAR
                    }
                    for ((key, value) in GameData.scores) {
                        if (value > 0) {
                            val p = Bukkit.getPlayer(key) ?: continue
                            val sc = o.getScore(p.displayName)
                            sc.score = value
                        }
                    }
                }
            }, 0L, 20L)
    }
}
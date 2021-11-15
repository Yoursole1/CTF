package me.yoursole.ctf.datafiles

import me.yoursole.ctf.CTF
import me.yoursole.ctf.datafiles.items.HermesBoots
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.scoreboard.DisplaySlot
import java.lang.Math.toRadians
import java.util.*
import kotlin.math.*

class GameLoop {
    companion object {
        var taskId = -1
        private val arrows = arrayOf(
            "←", "↑", "→", "↓", "↖", "↗", "↘", "↙"
        )
    }

    init {
        taskId = Bukkit.getScheduler()
            .scheduleSyncRepeatingTask(CTF.instance, {
                if (GameData.it != null) {
                    GameData.scores.compute(GameData.it!!.uniqueId) { _: UUID?, score: Int? -> (score ?: 0) + 1 }
                    GameData.it!!.addPotionEffect(GameData.slowness)
                    GameData.it!!.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 40.0
                }
                for (player in Bukkit.getOnlinePlayers()) {
                    if (GameData.it == null) {
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 20.0
                        player.addPotionEffect(GameData.saturation)
                    } else {
                        if (player.uniqueId !== GameData.it!!.uniqueId) {
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = 20.0
                            player.addPotionEffect(GameData.haste)
                            if (GameData.it!!.isInsideVehicle) {
                                player.addPotionEffect(GameData.dolphin)
                            }
                        }
                    }
                    player.addPotionEffect(GameData.nightVision)
                    var canFly = player.gameMode != GameMode.SURVIVAL && player.gameMode != GameMode.ADVENTURE
                    for (armor in player.inventory.armorContents) {
                        if (armor == null) continue
                        if (Utils.compareBreakable(armor, HermesBoots.item)) {
                            canFly = true
                        }
                    }
                    player.allowFlight = canFly
                    if (!canFly) player.isFlying = false
                }
                if (GameData.timerMs != -1L) {
                    val diff = (GameData.timerMs - System.currentTimeMillis()) / 1000L
                    val min = (diff / 60).toInt()
                    val sec = (diff % 60).toInt()
                    val flagHolder = if (GameData.it == null) "Nobody" else GameData.it!!.displayName
                    val timeString = if (min > 0) min.toString() + "m " + sec + "s" else sec.toString() + "s"
                    for (player in Bukkit.getOnlinePlayers()) {
                        var arrow = "|"
                        if (GameData.it != null && GameData.it !== player) {
                            var itLoc = GameData.itLoc
                            if (GameData.netherHunters.contains(player) && GameData.inNether) {
                                itLoc = (Bukkit.getPlayer(GameData.it!!.uniqueId))!!.location
                            } else if (GameData.netherHunters.contains(player) && !GameData.inNether) {
                                itLoc = GameData.netherBackupLocs[player]
                            }
                            val a = itLoc!!.x - player.location.x
                            val c = sqrt(a * a + (itLoc.z - player.location.z).pow(2.0))
                            var angleBetween = asin(a / c) * 180 / 3.14
                            if (player.location.z < itLoc.z) {
                                angleBetween = 180 - abs(angleBetween)
                                if (player.location.x > itLoc.x) {
                                    angleBetween *= -1.0
                                }
                            }
                            val angle = toRadians(player.location.yaw.toDouble())
                            val x = cos(angle)
                            var angleFacing = 180 - acos(x) * 180 / 3.14
                            if (player.location.direction.x < 0) {
                                angleFacing *= -1.0
                            }
                            /*
                        if(angleFacing<-90&&angleBetween<-90){

                        }
                        */
                            var combined = angleFacing - angleBetween
                            if (abs(combined) > 180) {
                                combined = if (combined > 0) {
                                    -1 * (360 - combined)
                                } else {
                                    360 - abs(combined)
                                }
                            }
                            if (combined in -22.5..22.5) {
                                //forward
                                arrow = arrows[1]
                            } else if (combined in 22.5..67.5) {
                                //diag left forward
                                arrow = arrows[4]
                            } else if (combined in 67.5..112.5) {
                                //left
                                arrow = arrows[0]
                            } else if (combined in 112.5..157.5) {
                                //diag left backword
                                arrow = arrows[7]
                            } else if (combined in 157.0..180.0 || combined in -180.0..-157.5) {
                                //backwards
                                arrow = arrows[3]
                            } else if (combined in -157.5..-112.5) {
                                // backward right
                                arrow = arrows[6]
                            } else if (combined in -112.5..-67.5) {
                                // right
                                arrow = arrows[2]
                            } else if (combined in -67.5..-22.5) {
                                // forward right
                                arrow = arrows[5]
                            }
                        }
                        arrow = if (GameData.inNether) {
                            "§4${arrow} (NETHER)"
                        } else {
                            "§1 (OVERWORLD)"
                        }
                        player.sendActionBar("§a$flagHolder has the flag! §f§l$arrow §b$timeString")
                        GameData.arrow = arrow
                    }
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
                            val p = Bukkit.getPlayer(key)
                            val sc = o.getScore(p!!.displayName)
                            sc.score = value
                        }
                    }
                }
            }, 0L, 20L)
    }
}
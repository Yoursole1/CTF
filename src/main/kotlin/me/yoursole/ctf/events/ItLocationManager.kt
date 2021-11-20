package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerPortalEvent
import java.lang.Math.toRadians
import kotlin.math.*

object ItLocationManager : Listener {
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (!GameData.gameRunning) return
        val diff = (GameData.timerMs - System.currentTimeMillis()) / 1000L
        val min = (diff / 60).toInt()
        val sec = (diff % 60).toInt()
        val flagHolder = if (GameData.it == null) "Nobody" else GameData.it!!.displayName
        val timeString = if (min > 0) min.toString() + "m " + sec + "s" else sec.toString() + "s"
        if (e.player.uniqueId == GameData.it!!.uniqueId && e.player.world.getBlockAt(e.player.location).type == Material.NETHER_PORTAL) {
            GameData.backUpLoc = GameData.itLoc
            return
        }
        if (GameData.gameRunning && e.player.uniqueId == GameData.it!!.uniqueId && !GameData.inNether) {
            GameData.itLoc = e.player.location
            return
        }
        if (e.player.uniqueId != GameData.it!!.uniqueId && e.player.world.getBlockAt(e.player.location).type == Material.NETHER_PORTAL) {
            GameData.netherBackupLocs[e.player] = e.player.location
            return
        }
        val player = e.player
        var arrow = "|"
        if (GameData.it != null && GameData.it !== player) {
            var itLoc: Location? = GameData.itLoc!!.clone()
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
            "§1${arrow} (OVERWORLD)"
        }
        if (GameData.arrow == arrow) return
        player.sendActionBar("§a$flagHolder has the flag! §f§l$arrow §b$timeString")
    }

    @EventHandler
    fun onPlayerChangeDim(e: PlayerPortalEvent) {
        if (!GameData.gameRunning) {
            return
        }
        if (e.player.world.environment == World.Environment.NETHER) {
            if (e.player.uniqueId === GameData.it!!.uniqueId) {
                GameData.inNether = false
            } else {
                GameData.netherHunters.remove(e.player)
            }
            //player exits nether (ik its backwards)
            e.to = GameData.gameSpawnPoint!!
        } else if (e.player.world.environment == World.Environment.NORMAL) {
            //player enters nether
            if (e.player.uniqueId === GameData.it!!.uniqueId) {
                GameData.itLoc = GameData.backUpLoc
                GameData.inNether = true
            } else {
                GameData.netherHunters.add(e.player)
            }
            e.to = GameData.netherMainPoint!!
        }
    }

    private val arrows = arrayOf(
        "←", "↑", "→", "↓", "↖", "↗", "↘", "↙"
    )

    /**
     * May use later idk
     **/
    private val Player.inNether: Boolean
        get() = if (uniqueId === GameData.it!!.uniqueId) {
            GameData.inNether
        } else {
            GameData.netherHunters.contains(this)
        }
}
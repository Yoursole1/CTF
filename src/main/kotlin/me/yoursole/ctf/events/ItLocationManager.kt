package me.yoursole.ctf.events

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.getArrowFor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerPortalEvent

object ItLocationManager : Listener {
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (GameData.it == null) return
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
            for (p: Player in Bukkit.getOnlinePlayers()) {
                var ar = "|"
                if (GameData.it != null && GameData.it !== p) {
                    var itLoc: Location? = GameData.itLoc!!.clone()
                    if (GameData.netherHunters.contains(p) && GameData.inNether) {
                        itLoc = (Bukkit.getPlayer(GameData.it!!.uniqueId))!!.location
                    } else if (GameData.netherHunters.contains(p) && !GameData.inNether) {
                        itLoc = GameData.netherBackupLocs[p]
                    }
                    ar = p.getArrowFor(itLoc!!).char.toString()
                }
                ar = if (GameData.inNether) {
                    "§4${ar} (NETHER)"
                } else {
                    "§1${ar} (OVERWORLD)"
                }
                if (GameData.arrow == ar) return
                p.sendActionBar("§a$flagHolder has the flag! §f§l$ar §b$timeString")
            }
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
            arrow = player.getArrowFor(itLoc!!).char.toString()
        }
        arrow = if (GameData.inNether) {
            "§4${arrow} (NETHER)"
        } else {
            "§1${arrow} (OVERWORLD)"
        }
        if (GameData.arrow == arrow) return
        player.sendActionBar("§a$flagHolder has the flag! §f§l$arrow §b$timeString")


    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerChangeDim(e: PlayerPortalEvent) {
        if (e.player.world.environment == World.Environment.NETHER) {
            e.to = GameData.gameSpawnPoint
        } else if (e.player.world.environment == World.Environment.NORMAL) {
            e.to = GameData.netherMainPoint
        }
        if (GameData.it == null) return
        if (e.player.world.environment == World.Environment.NETHER) {
            if (e.player.uniqueId === GameData.it!!.uniqueId) {
                GameData.inNether = false
            } else {
                GameData.netherHunters.remove(e.player)
            }
            //player exits nether (ik its backwards)

        } else if (e.player.world.environment == World.Environment.NORMAL) {
            //player enters nether
            if (e.player.uniqueId === GameData.it!!.uniqueId) {
                GameData.itLoc = GameData.backUpLoc
                GameData.inNether = true
            } else {
                GameData.netherHunters.add(e.player)
            }


        }
    }
}
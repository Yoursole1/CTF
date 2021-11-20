package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils
import me.yoursole.ctf.datafiles.Utils.doTelekinesis
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.util.Vector
import kotlin.math.roundToInt

object TunnelersPickaxe : Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.isCancelled) return
        val player = event.player
        val block = event.block
        val face = player.eyeLocation.direction
        if (Utils.compareBreakable(item, player.inventory.itemInMainHand)) {
            val distance = if (!GameData.gameRunning) 3 else if (player.uniqueId === GameData.it!!.uniqueId) 2 else 5
            for (i in 1 until distance) {
                val candidate = player.world.getBlockAt(
                    block.location.add(
                        Vector(
                            face.x.roundToInt() * i,
                            face.y.roundToInt() * i,
                            face.z.roundToInt() * i
                        )
                    )
                )
                if (breakable.contains(candidate.type)) {
                    candidate.type = Material.AIR
                    for (drop in candidate.getDrops(player.inventory.itemInMainHand)) player.doTelekinesis(drop)
                }
            }
        }
    }

    val item by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.GOLDEN_PICKAXE)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§6Tunneler's Pickaxe")
                lore = listOf(
                    "§6Item Ability: Tunnel",
                    "§fDigs 5 blocks forward in the direction you are looking.",
                    "§cCan break: Cobblestone, Stone, Gravel, Deepslate, Netherrack"
                )
                isUnbreakable = true
                addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
            }
            val key = NamespacedKey.fromString("ctf:tunnelerspickaxe")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape("IGI", " S ", " S ")
            recipe.setIngredient('G', Material.GOLD_BLOCK)
            recipe.setIngredient('I', Material.GOLD_INGOT)
            recipe.setIngredient('S', Material.STICK)
            recipe.group = "ctf:tunnelerspickaxe"
            Bukkit.addRecipe(recipe)
            setCTFId("tunnelerspickaxe")
            GameData.recipeKeys["tunnelerspickaxe"] = key
        }
    }
    private val breakable = setOf(
        Material.COBBLESTONE,
        Material.DEEPSLATE,
        Material.DRIPSTONE_BLOCK,
        Material.NETHERRACK,
        Material.STONE,
        Material.ANDESITE,
        Material.DIORITE,
        Material.GRANITE,
        Material.GRAVEL,
        Material.SANDSTONE,
        Material.RED_SANDSTONE,
        Material.DIRT,
        Material.GRASS_BLOCK,
        Material.COARSE_DIRT,
    )
}
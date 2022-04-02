package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils
import me.yoursole.ctf.datafiles.Utils.getCTFId
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import java.util.*

object ThorsAxe : Listener {
    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (event.damager is Player && event.entity is LivingEntity && event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val damager = event.damager as Player
            val target = event.entity as LivingEntity
            if (damager.inventory.itemInMainHand.getCTFId() == "thorsaxe") {
                val h = hitMap.compute(damager.uniqueId) { _: UUID?, hits: Int? -> (hits ?: 0) + 1 }!!
                if (h == 4) {
                    hitMap[damager.uniqueId] = 0
                    target.health = target.health - 1
                    target.world.playSound(target.location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f)
                    target.world.strikeLightningEffect(target.location)
                }
            }
        }
    }

    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.STONE_AXE)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§bThor's Axe")
                lore = listOf(
                    "§6Item Ability: Smite",
                    "§fHarness the power of Thor!",
                    "§fEvery 3 hits, your next hit will gain smite.",
                    "§fSmite strikes the target with lightning, dealing 1 true damage.",
                    "§fTrue damage ignores all protection.",
                )
                addEnchant(Enchantment.DURABILITY, 2, false)
            }
            setCTFId("thorsaxe")
            val key = NamespacedKey.fromString("ctf:thorsaxe")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape("OI ", "IB ", " B ")
            recipe.setIngredient('O', Material.OBSIDIAN)
            recipe.setIngredient('I', Material.IRON_BLOCK)
            recipe.setIngredient('B', Material.BONE)
            recipe.group = "ctf:thorsaxe"
            Bukkit.addRecipe(recipe)
            GameData.recipeKeys["thorsaxe"] = key
        }
    }
    val hitMap = HashMap<UUID, Int>()
}
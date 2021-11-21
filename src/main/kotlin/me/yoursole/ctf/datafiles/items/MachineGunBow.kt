package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.CTF
import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.getCTFAttributes
import me.yoursole.ctf.datafiles.Utils.getCTFId
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.Damageable
import java.util.*

object MachineGunBow : Listener {

    private var ticks = 0
    private val shotInTick = hashMapOf<UUID, Int>()
    private const val regenPerSecond = 18
    private const val damagePerUse = 20

    init {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CTF.instance, {
            ticks++
            shotInTick.clear()
            if (ticks % 20 == 0) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.inventory.forEach {
                        if (it != null && it.itemMeta is Damageable && it.getCTFId() == "machinegunbow") {
                            it.editMeta(Damageable::class.java) {
                                it.damage -= regenPerSecond
                            }
                        }
                    }
                }
                ticks = 0
            }
        }, 0L, 1L)
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.item?.itemMeta is Damageable && event.item?.getCTFId() == "machinegunbow") {
            event.isCancelled = true
            val shot = shotInTick.getOrPut(event.player.uniqueId) { 0 }
            if (shot < 2 && (event.item?.itemMeta as Damageable).damage < Material.BOW.maxDurability + damagePerUse) {
                shotInTick.compute(event.player.uniqueId) { _, i -> (i ?: 0).inc() }
                event.player.launchProjectile(Arrow::class.java).apply {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                    damage /= 6
                }
                event.item?.editMeta(Damageable::class.java) {
                    it.damage += damagePerUse
                }
            }
        }
    }


    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.BOW)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§bMachine Gun Bow")
                lore = listOf(
                    "§6Item Ability: Firepower",
                    "§fHolding right click fires x arrows per second",
                    "§fEach arrow decreases the durability by x.",
                    "§fRegain x durability every second.",
                    "§fArrows deal x times less damage."
                )
                addEnchant(Enchantment.ARROW_INFINITE, 1, false)
                addEnchant(Enchantment.DURABILITY, 10, true)
            }
            setCTFId("machinegunbow")
            val key = NamespacedKey.fromString("ctf:machinegunbow")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape("ADA", "ABA", "AAA")
            recipe.setIngredient('A', Material.ARROW)
            recipe.setIngredient('D', Material.DIAMOND)
            recipe.setIngredient('B', Material.BOW)
            recipe.group = "ctf:machinegunbow"
            Bukkit.addRecipe(recipe)
            GameData.recipeKeys["machinegunbow"] = key
        }
    }
}
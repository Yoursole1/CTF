package me.yoursole.ctf.datafiles.items

import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.LeatherArmorMeta
import java.util.*

object HermesBoots : Listener {
    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.LEATHER_BOOTS)).apply {
            itemMeta = (itemMeta as LeatherArmorMeta).apply {
                setDisplayName("§7Hermes' Boots")
                lore = listOf(
                    "§6Item Ability: Messenger",
                    "§fAllows you to double jump!",
                    "§fDouble Jumping damages the boots and the wearer by 1"
                )
                setColor(Color.fromRGB(0xb3acab))
                addItemFlags(ItemFlag.HIDE_DYE)
                addEnchant(Enchantment.DURABILITY, 10, true)
                addAttributeModifier(
                    Attribute.GENERIC_MOVEMENT_SPEED,
                    AttributeModifier(
                        UUID.randomUUID(),
                        "Hermes' Speed",
                        0.3,
                        AttributeModifier.Operation.ADD_SCALAR,
                        EquipmentSlot.FEET
                    )
                )
                addAttributeModifier(
                    Attribute.GENERIC_ARMOR,
                    AttributeModifier(
                        UUID.randomUUID(),
                        "Hermes' Armor",
                        1.25,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.FEET
                    )
                )
            }
            setCTFId("hermes")
            val key = NamespacedKey.fromString("ctf:hermes")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape("   ", "F F", "FDF")
            recipe.setIngredient('F', Material.FEATHER)
            recipe.setIngredient('D', Material.DIAMOND)
            recipe.group = "ctf:hermes"
            Bukkit.addRecipe(recipe)
            GameData.recipeKeys["hermes"] = key
        }
    }

    @EventHandler
    fun onAttemptFly(event: PlayerToggleFlightEvent) {
        if (event.isFlying) {
            val player = event.player
            val boots = event.player.inventory.armorContents?.get(0)
            if (Utils.compareBreakable(boots, item)) {
                player.damage(1.0)
                val meta = boots?.itemMeta as Damageable
                meta.damage += 1
                if (meta.damage > boots.type.maxDurability) {
                    player.inventory.boots = null
                }
                boots.itemMeta = meta
                player.velocity = player.location.direction.multiply(1.25).setY(0.81)
                event.isCancelled = true
                player.allowFlight = false
                player.isFlying = false
            }
        }
    }
}
package me.yoursole.ctf.datafiles.items

import io.papermc.paper.event.player.PlayerArmSwingEvent
import me.yoursole.ctf.datafiles.GameData
import me.yoursole.ctf.datafiles.Utils.getCTFId
import me.yoursole.ctf.datafiles.Utils.setCTFId
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftSnowball
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

object WebShooter : Listener {
    @EventHandler
    fun onInteract(event: PlayerArmSwingEvent) {
        val player = event.player
        val item = player.equipment.getItem(event.hand)
        if (item.getCTFId() == "webshooter") {
            val snowball = player.launchProjectile(Snowball::class.java) as CraftSnowball
            snowball.customName = "WebShooter"
            snowball.addPassenger(snowball.world.spawnFallingBlock(snowball.location, Material.COBWEB.createBlockData())
                .also { it.isInvulnerable = true })
            snowball.isInvulnerable = true
        }
    }

    @EventHandler
    fun onProjectileLand(event: ProjectileHitEvent) {
        if (event.entity.customName == "WebShooter") {
            val snowball = event.entity as Snowball
            snowball.passengers.forEach {
                it.remove()
            }
            val block = event.hitBlock?.location ?: return
            val player = snowball.shooter as Player
            player.location.y += 0.5
            val loc = player.location.clone()
            loc.y *= 1.07
            val delta = block.subtract(player.location)
            val velocity = delta.toVector().normalize().multiply(4)
            velocity.y *= 2
            player.velocity = velocity
        }
    }

    val item: CraftItemStack by lazy {
        CraftItemStack.asCraftCopy(ItemStack(Material.PLAYER_HEAD)).apply {
            itemMeta = itemMeta.apply {
                setDisplayName("§fWeb Shooter")
                lore = listOf(
                    "§6Item Ability: Hang Tight!",
                    "§fRight click to shoot a web,",
                    "§fpulling you in that direction!",
                )
                addEnchant(Enchantment.DURABILITY, 10, true)
            }
            setCTFId("webshooter")
            val key = NamespacedKey.fromString("ctf:webshooter")!!
            val recipe = ShapedRecipe(key, this)
            recipe.shape("WWW", "WWW", "WWW")
            recipe.setIngredient('W', Material.COBWEB)
            recipe.group = "ctf:webshooter"
            Bukkit.addRecipe(recipe)
            GameData.recipeKeys["webshooter"] = key
        }
    }
}
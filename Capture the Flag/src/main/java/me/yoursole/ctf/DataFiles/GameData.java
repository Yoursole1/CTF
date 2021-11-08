package me.yoursole.ctf.DataFiles;

import me.yoursole.ctf.DataFiles.Items.*;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameData {
    public static Player it;
    public static HashMap<UUID,Integer> scores = new HashMap<>();
    public static long timerMs = -1;

    public static final HashMap<String, ItemStack> customItems = new HashMap<>();
    public static final HashMap<String, NamespacedKey> recipeKeys = new HashMap<>();

    public static final PotionEffect haste = PotionEffectType.FAST_DIGGING.createEffect(60, 2);
    public static final PotionEffect saturation = PotionEffectType.SATURATION.createEffect(60, 0);
    public static final PotionEffect dolphin = PotionEffectType.DOLPHINS_GRACE.createEffect(60, 1);
    public static final PotionEffect nightVision = PotionEffectType.NIGHT_VISION.createEffect(200, 68);

    public static final PotionEffect slowness = PotionEffectType.SLOW.createEffect(60, 1);
    public static final PotionEffect regen = PotionEffectType.REGENERATION.createEffect(60, 1);


    public static Location itLoc;
    public static boolean inNether = false;
    public static Location backUpLoc;
    public static ArrayList<Player> netherHunters = new ArrayList<>();
    public static HashMap<Player, Location> netherBackupLocs = new HashMap<>();
    public static String arrow = "|";

    static {
        try {
            customItems.put("flag", Flag.flag);
            customItems.put("hermes", HermesBoots.item);
            customItems.put("tunnelerspickaxe", TunnelersPickaxe.item);
            customItems.put("goblinssword", GoblinsSword.item);
            customItems.put("thorsaxe", ThorsAxe.item);
            customItems.put("cheapapple", CheapApple.item);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

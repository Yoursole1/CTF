package me.yoursole.ctf.DataFiles;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Utils {
    public static boolean compareBreakable(ItemStack first, ItemStack second) {
        boolean similar = false;

        if(first == null || second == null){
            return similar;
        }

        boolean sameTypeId = (first.getType() == second.getType());
        boolean sameHasItemMeta = (first.hasItemMeta() == second.hasItemMeta());
        boolean sameEnchantments = (first.getEnchantments().equals(second.getEnchantments()));
        boolean sameItemMeta = true;

        if(sameHasItemMeta) {
            ItemMeta m1 = first.getItemMeta();
            ItemMeta m2 = second.getItemMeta();
            if (m1 instanceof Damageable ^ m2 instanceof Damageable) return false;
            if (m1 instanceof Damageable && m2 instanceof Damageable) {
                ((Damageable)m1).setDamage(0);
                ((Damageable)m2).setDamage(0);
                sameItemMeta = Bukkit.getItemFactory().equals(m1, m2);
            }
        }

        if(sameTypeId && sameHasItemMeta && sameEnchantments && sameItemMeta){
            similar = true;
        }

        return similar;
    }

    public static void doTelekinesis(Player player, ItemStack... stack) {
        Map<Integer, ItemStack> items = player.getInventory().addItem(stack);
        if (!items.isEmpty()) {
            for (ItemStack item : items.values()) {
                Item drop = player.getWorld().dropItemNaturally(player.getLocation(), item);
                drop.setPickupDelay(5);
                drop.setOwner(player.getUniqueId());
                drop.setThrower(player.getUniqueId());
            }
        }
    }

    public static int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }


    public static void generateNewWorlds(){
        String overworldName = GameData.overworldName;
        String netherName = GameData.netherName;

        File f = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + overworldName);
        deleteWorld(f);
        f = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + netherName);
        deleteWorld(f);
        //ensures the worlds that are generated are deleted


        WorldCreator wc = new WorldCreator(overworldName);
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        wc.generatorSettings("2;0;1;");
        wc.seed(getRandom(1,1000000));
        GameData.world=wc.createWorld();
        //alternative Bukkit.createWorld(wc)

        wc = new WorldCreator(netherName);
        wc.environment(World.Environment.NETHER);
        wc.type(WorldType.NORMAL);
        wc.generatorSettings("2;0;1;");
        wc.seed(getRandom(1,1000000));
        GameData.world_nether=wc.createWorld();

        //worlds should be generated
    }

    public static void deleteWorlds(){

        String overworldName = GameData.overworldName;
        String netherName = GameData.netherName;

        Bukkit.unloadWorld(overworldName,false);
        Bukkit.unloadWorld(netherName,false);

        File f = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + overworldName);
        deleteWorld(f);
        f = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + netherName);
        deleteWorld(f);

    }

    public static void deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        path.delete();
    }

    /*
    Only teleports the player if they are not in the world at all
    Does not have an effect if the player is already in the world
     */
    public static void sendPlayer(Player player, World world){
        if(player.getLocation().getWorld()!=world){
            player.teleport(world.getSpawnLocation());
        }
    }

    public static void setWorldSpawn(World world, Location location){
        world.setSpawnLocation(location);
    }

    /**
     * @param world
     * @param type
     * @return
     */
    public static Location getStructure(World world, StructureType type){
        Location loc = world.locateNearestStructure(world.getSpawnLocation(),type,10000,false);

        return loc;
    }
}

package me.yoursole.ctf;

import me.yoursole.ctf.Commands.*;
import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.GameLoop;
import me.yoursole.ctf.DataFiles.Items.GoblinsSword;
import me.yoursole.ctf.DataFiles.Items.HermesBoots;
import me.yoursole.ctf.DataFiles.Items.ThorsAxe;
import me.yoursole.ctf.DataFiles.Items.TunnelersPickaxe;
import me.yoursole.ctf.DataFiles.Utils;
import me.yoursole.ctf.Events.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CTF extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MoveItemEvent(), this);
        getServer().getPluginManager().registerEvents(new DropItemEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new GetCompass(), this);
        getServer().getPluginManager().registerEvents(new PlaceFlagEvent(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);
        getServer().getPluginManager().registerEvents(new Leave(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new HermesBoots(), this);
        getServer().getPluginManager().registerEvents(new TunnelersPickaxe(), this);
        getServer().getPluginManager().registerEvents(new GoblinsSword(), this);
        getServer().getPluginManager().registerEvents(new ThorsAxe(), this);
        getServer().getPluginManager().registerEvents(new BuiltLimit(), this);
        getServer().getPluginManager().registerEvents(new ItDamagePlayer(), this);
        getServer().getPluginManager().registerEvents(new CraftEvent(), this);
        getServer().getPluginManager().registerEvents(new ItLocationManager(), this);
        getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
        Objects.requireNonNull(this.getCommand("start")).setExecutor(new Start());
        Objects.requireNonNull(this.getCommand("stop")).setExecutor(new Stop());
        Objects.requireNonNull(this.getCommand("scores")).setExecutor(new Scores());
        Objects.requireNonNull(this.getCommand("timer")).setExecutor(new Timer());
        Objects.requireNonNull(this.getCommand("togglechat")).setExecutor(new ToggleChat());
        Objects.requireNonNull(this.getCommand("customitem")).setExecutor(new CustomItem());
        Objects.requireNonNull(this.getCommand("top")).setExecutor(new Top());

        new GameLoop();
    }

    @Override
    public void onDisable() {
        for (NamespacedKey key : GameData.recipeKeys.values()) {
            Bukkit.removeRecipe(key);
        }
        Bukkit.getScheduler().cancelTask(GameLoop.taskId);

        Utils.deleteWorlds();

    }
}

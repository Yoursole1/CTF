package me.yoursole.ctf.Events;

import me.yoursole.ctf.Commands.ToggleChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onOpChat(AsyncPlayerChatEvent e){
        if (e.getPlayer().isOp()) {
            e.setMessage(ChatColor.YELLOW + e.getMessage());
        } else if (ToggleChat.chatMuted) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Chat is currently muted!");
        }
    }
}

package net.daechler.spawning;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawning extends JavaPlugin implements CommandExecutor, Listener {

    @Override
    public void onEnable() {
        // Display an enable message
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getName() + " has been enabled!");
        this.saveDefaultConfig();
        this.getCommand("setspawn").setExecutor(this);
        this.getCommand("spawn").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Display a disable message
        getServer().getConsoleSender().sendMessage(ChatColor.RED + getName() + " has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            setSpawn(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Spawn set!");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("spawn")) {
            player.teleport(getSpawn());
            player.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
            return true;
        }

        return false;
    }

    public void setSpawn(Location location) {
        FileConfiguration config = this.getConfig();
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        this.saveConfig();
    }

    public Location getSpawn() {
        FileConfiguration config = this.getConfig();
        return new Location(
                Bukkit.getWorld(config.getString("spawn.world")),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.yaw"),
                (float) config.getDouble("spawn.pitch")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (getConfig().getBoolean("teleportOnJoin")) {
            event.getPlayer().teleport(getSpawn());
        }
    }
}

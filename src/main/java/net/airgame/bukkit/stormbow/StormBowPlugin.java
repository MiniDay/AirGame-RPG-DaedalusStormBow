package net.airgame.bukkit.stormbow;

import net.airgame.bukkit.api.annotation.CommandScan;
import net.airgame.bukkit.stormbow.listener.MainListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@CommandScan("net.airgame.bukkit.stormbow.command")
public final class StormBowPlugin extends JavaPlugin {
    private static StormBowPlugin instance;

    public static StormBowPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new MainListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package com.i0dev.Reclaim;

import com.i0dev.Reclaim.commands.CmdReclaim;
import com.i0dev.Reclaim.config.GeneralConfig;
import com.i0dev.Reclaim.config.MessageConfig;
import com.i0dev.Reclaim.config.StorageConfig;
import com.i0dev.Reclaim.templates.AbstractCommand;
import com.i0dev.Reclaim.templates.AbstractConfiguration;
import com.i0dev.Reclaim.templates.AbstractListener;
import com.i0dev.Reclaim.templates.AbstractManager;
import com.i0dev.Reclaim.utility.ConfigUtil;
import com.i0dev.Reclaim.utility.MsgUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Heart extends JavaPlugin {

    List<AbstractManager> managers = new ArrayList<>();
    List<AbstractConfiguration> configs = new ArrayList<>();

    public static boolean usingPapi;
    public static boolean usingMCoreFactions;

    @Override
    public void onEnable() {

        usingPapi = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        Plugin factions = getServer().getPluginManager().getPlugin("Factions");
        usingMCoreFactions = factions != null && factions.getDescription().getVersion().startsWith("2.");

        managers.addAll(Arrays.asList(
                new CmdReclaim(this, "Reclaim")
        ));

        configs.addAll(Arrays.asList(
                new StorageConfig(this, getDataFolder() + "/Storage.json"),
                new GeneralConfig(this, getDataFolder() + "/General.json"),
                new MessageConfig(this, getDataFolder() + "/Messages.json")
        ));

        reload();
        registerManagers();

        System.out.println("\u001B[32m" + this.getDescription().getName() + " by: " + this.getDescription().getAuthors().get(0) + " has been enabled.");
    }

    public void reload() {
        // old ~ new
        ArrayList<MsgUtil.Pair<AbstractConfiguration, AbstractConfiguration>> toReplace = new ArrayList<>();
        configs.forEach(abstractConfiguration -> toReplace.add(new MsgUtil.Pair<>(abstractConfiguration, ConfigUtil.load(abstractConfiguration, this))));
        toReplace.forEach(pairs -> {
            configs.remove(pairs.getKey());
            configs.add(pairs.getValue());
        });
    }


    @Override
    public void onDisable() {
        configs.clear();
        managers.forEach(AbstractManager::deinitialize);
        HandlerList.unregisterAll(this);
        managers.clear();
        Bukkit.getScheduler().cancelTasks(this);
        System.out.println("\u001B[31m" + this.getDescription().getName() + " by: " + this.getDescription().getAuthors().get(0) + " has been disabled.");
    }

    public <T> T getManager(Class<T> clazz) {
        return (T) managers.stream().filter(manager -> manager.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public <T> T getConfig(Class<T> clazz) {
        return (T) configs.stream().filter(config -> config.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public void registerManagers() {
        managers.forEach(abstractManager -> {
            if (abstractManager.isLoaded()) abstractManager.deinitialize();
            if (abstractManager instanceof AbstractListener)
                getServer().getPluginManager().registerEvents((AbstractListener) abstractManager, this);
            else if (abstractManager instanceof AbstractCommand) {
                getCommand(((AbstractCommand) abstractManager).getCommand()).setExecutor(((AbstractCommand) abstractManager));
                getCommand(((AbstractCommand) abstractManager).getCommand()).setTabCompleter(((AbstractCommand) abstractManager));
            }
            abstractManager.initialize();
            abstractManager.setLoaded(true);
        });
    }

    public GeneralConfig cnf() {
        return getConfig(GeneralConfig.class);
    }

    public MessageConfig msg() {
        return getConfig(MessageConfig.class);
    }

    public StorageConfig storage() {
        return getConfig(StorageConfig.class);
    }

}

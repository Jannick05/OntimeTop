package dk.nydt;

import dk.nydt.commands.Ontime;
import dk.nydt.commands.OntimeTop;
import dk.nydt.events.InventoryListener;
import dk.nydt.events.JoinListener;
import dk.nydt.events.QuitListener;
import dk.nydt.tasks.TimeUpdate;
import dk.nydt.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {
    public static Main instance;
    private static PluginManager pluginManager;
    private boolean access = false;
    public static Config ontime, config, license;
    public static FileConfiguration ontimeYML, configYML, licenseYML;
    @Override
    public void onEnable() {
        pluginManager = getServer().getPluginManager();
        instance = this;

        //license yml
        if (!(new File(getDataFolder(), "license.yml")).exists())
            saveResource("license.yml", false);

        license = new Config(this, null, "license.yml");
        licenseYML = license.getConfig();

        String license = licenseYML.getString("License");
        if(!new AdvancedLicense(license, "https://license.cutekat.dk/verify.php", this).debug().register()) return;
        access = true;

        //register commands
        getCommand("Ontime").setExecutor(new Ontime());
        getCommand("OntimeTop").setExecutor(new OntimeTop());

        //register events
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        //ontime yml
        if (!(new File(getDataFolder(), "ontime.yml")).exists())
            saveResource("ontime.yml", false);

        //config yml
        if (!(new File(getDataFolder(), "config.yml")).exists())
            saveResource("config.yml", false);

        config = new Config(this, null, "config.yml");
        configYML = config.getConfig();

        ontime = new Config(this, null, "ontime.yml");
        ontimeYML = ontime.getConfig();
        //updates on time every 10 minutes
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TimeUpdate(), 0L, 12000L);


    }

    @Override
    public void onDisable() {
        if (access) {
            config.saveConfig();
            ontime.saveConfig();
            license.saveConfig();
        }
        license.saveConfig();
    }

    public static Main getInstance(){
        return instance;
    }
}

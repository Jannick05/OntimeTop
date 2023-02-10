package dk.nydt.events;

import dk.nydt.Main;
import dk.nydt.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static dk.nydt.Main.ontime;


public class JoinListener implements Listener {

    Main plugin;
    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    TimeUtils TimeUtils = new TimeUtils();

    @EventHandler
    public void onPlayer(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (!TimeUtils.hasAccount(p)) {
            TimeUtils.createAccount(p);
        } else {
            TimeUtils.loadAccount(p);
        }
        ontime.saveConfig();
    }
}

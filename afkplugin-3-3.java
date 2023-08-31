package asahi2986github.com.afkplugin3;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AFKPlugin extends JavaPlugin {

    private Set<UUID> afkPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("AFKPlugin has been enabled.");
        getCommand("afk").setExecutor(new AFKCommand());
        getCommand("unafk").setExecutor(new UnAFKCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("AFKPlugin has been disabled.");
    }

    private class AFKCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!afkPlayers.contains(player.getUniqueId())) {
                    afkPlayers.add(player.getUniqueId());
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, false, false));
                    player.sendMessage("You are now AFK.");
                }
            }
            return true;
        }
    }

    private class UnAFKCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (afkPlayers.contains(player.getUniqueId())) {
                    afkPlayers.remove(player.getUniqueId());
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    player.sendMessage("You are no longer AFK.");
                    Bukkit.getScheduler().runTaskLater(AFKPlugin.this, () -> {
                        if (!afkPlayers.contains(player.getUniqueId())) {
                            player.sendMessage("You can now use /afk again.");
                        }
                    }, 20 * 60 * 15); //timer-15m
                }
            }
            return true;
        }
    }
}

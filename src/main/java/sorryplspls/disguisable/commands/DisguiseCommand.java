package sorryplspls.disguisable.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sorryplspls.disguisable.Disguisable;
import sorryplspls.disguisable.entitytypes.ENTITY;
import sorryplspls.disguisable.entitytypes.PLAYER;
import sorryplspls.disguisable.exceptions.SkinFetchException;
import sorryplspls.disguisable.messages.MessageService;
import java.util.*;

public class DisguiseCommand implements CommandExecutor, TabCompleter {

    public static final Map<Player, Entity> disguised = new HashMap<>();
    //private final TexturesService texturesService;
    private final PLAYER playerDisguiser;

    public DisguiseCommand(PLAYER playerDisguiser) {
        this.playerDisguiser = playerDisguiser;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission(Disguisable.getDisguisable().getConfig().getString("Permission", "disguisable.*"))) {
            MessageService.send(player, "&cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            MessageService.send(player, "&cUsage: /disguise <TYPE|OFF>]");
            return false;
        }

        EntityType type;

        if (args[0].toUpperCase(Locale.ROOT).equals("OFF")) {
            if (disguised.containsKey(player)) {
                disguised.get(player).remove();
                disguised.remove(player);
                player.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
                MessageService.send(player, "&cYou are no longer disguised.");
            } else {
                MessageService.send(player, "&cYou are not disguised.");
            }
            return true;
        }

        if (args[0].toUpperCase(Locale.ROOT).equals("PLAYER")) {
            if (args.length < 2) {
                MessageService.send(player, "&cYou must specify the name of the player to disguise as.");
                return false;
            }

            if (Bukkit.getPlayerExact(args[1]) == null) {
                MessageService.send(player, "&cFailed to find player with the name: &f" + args[1]);
                return false;
            }

            if (disguised.containsKey(player)) {
                disguised.get(player).remove();
                disguised.remove(player);
                player.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
            }

            try {
                if(args[1].equalsIgnoreCase(player.getName())){
                    playerDisguiser.DISGUISE_PLAYER(player, player.getName());
                    MessageService.send(player, "&7You are now back to your default skin!");
                    return true;
                }
                playerDisguiser.DISGUISE_PLAYER(player, args[1]);
                MessageService.send(player, "&7You are now disguised as player &2" + args[1] + "&7!");
            } catch (SkinFetchException e) {
                MessageService.send(player, "&cFailed to disguise: " + e.getMessage());
            }
            return true;
        }

        try {
            type = EntityType.valueOf(args[0].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            MessageService.send(player, "&cPlease use a valid entity type.");
            return false;
        }

        if (disguised.containsKey(player)) {
            disguised.get(player).remove();
            disguised.remove(player);
            player.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
            MessageService.send(player, "&cYour previous disguise has been removed.");
        }

        ENTITY.DISGUISE_ENTITY(player, type);
        MessageService.send(player, "&7You are now disguised as a &2" + type.name() + "&7!");
        MessageService.send(player, "&7To disable your disguise, write &f/disguise OFF!");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        Set<String> blocked = new HashSet<>(Arrays.asList("BLOCK_DISPLAY", "BREEZE", "AREA_EFFECT_CLOUD", "ARMOR_STAND", "EGG", "THROWN_EXP_BOTTLE", "ENDER_CRYSTAL", "GLOW_ITEM_FRAME", "INTERACTION", "ITEM_DISPLAY", "MINECART_COMMAND", "MINECART_FURNACE", "MINECART_MOB_SPAWNER", "MINECART_HOPPER", "EXPERIENCE_ORB", "TEXT_DISPLAY", "ITEM_FRAME", "LLAMA_SPIT", "SPECTRAL_ARROW"));

        if (args.length == 1) {
            String partial = args[0].toUpperCase(Locale.ROOT);
            tabs.add("PLAYER");
            for (EntityType entityType : EntityType.values()) {
                if (entityType.isSpawnable() &&
                        !blocked.contains(entityType.name()) &&
                        entityType.name().startsWith(partial)) {
                    tabs.add(entityType.name());
                }
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("PLAYER")) {
            for (Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
                tabs.add(onlinePlayer.getName());
            }
        }
        return tabs;
    }
}

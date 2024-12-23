package sorryplspls.disguisable.messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import sorryplspls.disguisable.config.ConfigService;

public class MessageService {

    public static void send(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigService.getPrefix() + " " + message));
    }

}

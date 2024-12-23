package sorryplspls.disguisable;

import org.bukkit.plugin.java.JavaPlugin;
import sorryplspls.disguisable.commands.DisguiseCommand;
import sorryplspls.disguisable.entitytypes.*;
import sorryplspls.disguisable.properties.TexturesService;
import sorryplspls.disguisable.requests.HttpRequesterService;

public final class Disguisable extends JavaPlugin {

    private static Disguisable disguisable;

    public static Disguisable getDisguisable() {
        return disguisable;
    }

    void setDisguisable(Disguisable disguisable) {
        Disguisable.disguisable = disguisable;
    }

    @Override
    public void onEnable() {
        setDisguisable(this);
        saveDefaultConfig();

        //ConfigService configService = new ConfigService();
        HttpRequesterService httpRequesterService = new HttpRequesterService();
        TexturesService texturesService = new TexturesService(httpRequesterService);

        PLAYER player = new PLAYER(texturesService);
        getCommand("disguise").setExecutor(new DisguiseCommand(player));
        getCommand("disguise").setTabCompleter(new DisguiseCommand(player));
    }
}

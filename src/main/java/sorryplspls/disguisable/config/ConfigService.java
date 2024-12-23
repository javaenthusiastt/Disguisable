package sorryplspls.disguisable.config;

import sorryplspls.disguisable.Disguisable;

public class ConfigService {

    public static String getLanguage(){
        return Disguisable.getDisguisable().getConfig().getString("Language", "EN");
    }

    public static String getPrefix(){
        return Disguisable.getDisguisable().getConfig().getString("Prefix", "&6&lDisguise &8|");
    }

    public static String getHttpRequest(){
        return Disguisable.getDisguisable().getConfig().getString("HTTP_REQUEST", "https://api.mojang.com/users/profiles/minecraft/");
    }

    public static String getHttpAnswer(){
        return Disguisable.getDisguisable().getConfig().getString("HTTP_ANSWER", "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false");
    }

    public static void resetConfig(){
        Disguisable.getDisguisable().reloadConfig();
    }
}

package sorryplspls.disguisable.entitytypes;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.entity.Player;
import sorryplspls.disguisable.exceptions.SkinFetchException;
import sorryplspls.disguisable.properties.TexturesService;
import sorryplspls.disguisable.messages.MessageService;

import java.util.Collection;

public class PLAYER {

    private final TexturesService texturesService;

    public PLAYER(TexturesService texturesService) {
        this.texturesService = texturesService;
    }

    public void DISGUISE_PLAYER(Player player, String name) throws SkinFetchException {
        Collection<ProfileProperty> ARG_TEXTURES = texturesService.skinTextures(name);
        PlayerProfile playerProfile = player.getPlayerProfile();
        playerProfile.setProperties(ARG_TEXTURES);
        player.setPlayerProfile(playerProfile);
    }
}

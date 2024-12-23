package sorryplspls.disguisable.entitytypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sorryplspls.disguisable.messages.MessageService;

import static sorryplspls.disguisable.commands.DisguiseCommand.disguised;

public class ENTITY {

    public static void DISGUISE_ENTITY(Player player, EntityType entityType) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

        Entity entity_DISG = player.getWorld().spawnEntity(player.getLocation(), entityType);
        configure(entity_DISG);
        disguised.put(player, entity_DISG);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!disguised.containsKey(player) || !player.isOnline()) {
                    entity_DISG.remove();
                    this.cancel();
                    return;
                }
                entity_DISG.teleport(player.getLocation());
                entity_DISG.setFireTicks(0);
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Disguisable"), 1L, 1L);
    }

    private static void configure(Entity entity) {
        entity.setSilent(true);
        entity.setInvulnerable(true);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setCollidable(false);
            if (livingEntity instanceof Mob mob) {
                mob.setAI(false);
            }
        }
    }
}

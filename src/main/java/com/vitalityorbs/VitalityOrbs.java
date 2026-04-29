package com.vitalityorbs;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.util.Random;

public class VitalityOrbs extends JavaPlugin implements Listener {

    private NamespacedKey vitalityOrbKey;
    private final Random dropRandomizer = new Random();
    private final LegacyComponentSerializer colorCoder = LegacyComponentSerializer.legacyAmpersand();

    @Override
    public void onEnable() {
        this.vitalityOrbKey = new NamespacedKey(this, "vitality_orb");
        
        try {
            saveDefaultConfig();
            getServer().getPluginManager().registerEvents(this, this);
            getLogger().info("VitalityOrbs plugin started up. Version: " + getDescription().getVersion());
        } catch (Exception e) {
            getLogger().severe("Something went wrong during enable! " + e.getMessage());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onMobDeathEvent(EntityDeathEvent e) {
        LivingEntity victim = e.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;
        
        double currentChance = getConfig().getDouble("drop-chance", 0.05);
        
        if (dropRandomizer.nextDouble() > currentChance) {
            return;
        }

        try {
            ItemStack orbStack = new ItemStack(Material.NETHER_STAR);
            ItemMeta orbMeta = orbStack.getItemMeta();
            
            String configName = getConfig().getString("orb.display-name", "&6&lVitality Orb");
            Component finalName = colorCoder.deserialize(configName);

            if (orbMeta != null) {
                orbMeta.displayName(finalName);
                orbMeta.getPersistentDataContainer().set(vitalityOrbKey, PersistentDataType.BYTE, (byte) 1);
                orbStack.setItemMeta(orbMeta);
            }

            org.bukkit.Location spawnLoc = victim.getLocation().add(0, 0.5, 0);
            Item orbEntity = spawnLoc.getWorld().dropItem(spawnLoc, orbStack);
            
            orbEntity.setCanPlayerPickup(true);
            orbEntity.setCanMobPickup(false);
            orbEntity.setCustomNameVisible(true);
            orbEntity.customName(finalName);
            orbEntity.setGlowing(true);

            int lifeTimeTicks = getConfig().getInt("orb.despawn-timer", 30) * 20;
            
            getServer().getScheduler().runTaskTimer(this, task -> {
                if (!orbEntity.isValid() || orbEntity.isDead() || orbEntity.getTicksLived() >= lifeTimeTicks) {
                    if (orbEntity.isValid()) {
                        orbEntity.remove();
                    }
                    task.cancel();
                    return;
                }
                
                orbEntity.getWorld().spawnParticle(Particle.HEART, orbEntity.getLocation().clone().add(0, 0.2, 0), 1, 0.1, 0.1, 0.1, 0);
                orbEntity.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, orbEntity.getLocation().clone().add(0, 0.2, 0), 1, 0.2, 0.2, 0.2, 0.05);
                
            }, 0, 5);
            
        } catch (Exception err) {
            getLogger().warning("Failed to spawn an orb at " + victim.getLocation().toString());
            err.printStackTrace();
        }
    }

    @EventHandler
    public void onOrbPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        Item pickedItem = event.getItem();
        ItemStack stack = pickedItem.getItemStack();
        if (stack == null || stack.getItemMeta() == null) return;

        if (stack.getItemMeta().getPersistentDataContainer().has(vitalityOrbKey, PersistentDataType.BYTE)) {
            event.setCancelled(true);
            pickedItem.remove();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.2f);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 2f);
            
            double heal = 2.0;
            double hp = player.getHealth();
            double max = player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
            player.setHealth(hp + heal > max ? max : hp + heal);
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
            player.getWorld().spawnParticle(Particle.HEART, player.getLocation().clone().add(0, 1, 0), 5, 0.3, 0.3, 0.3, 0.1);

            if (getConfig().getBoolean("messages.enabled")) {
                String raw = getConfig().getString("messages.pickup", "&aYou picked up a &6&l{orb}&a!");
                String name = getConfig().getString("orb.display-name", "Vitality Orb");
                StringBuilder clean = new StringBuilder();
                boolean skip = false;
                for (char c : name.toCharArray()) {
                    if (skip) {
                        skip = false;
                        continue;
                    }
                    if (c == '&') skip = true;
                    else clean.append(c);
                }
                player.sendMessage(colorCoder.deserialize(raw.replace("{orb}", clean.toString())));
            }
        }
    }
}

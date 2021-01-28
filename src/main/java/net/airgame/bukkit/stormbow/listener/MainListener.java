package net.airgame.bukkit.stormbow.listener;

import net.airgame.bukkit.stormbow.StormBowPlugin;
import net.airgame.bukkit.stormbow.constant.NameSpacedKeys;
import net.airgame.bukkit.stormbow.persistent.BooleanDataType;
import net.airgame.bukkit.stormbow.util.BowUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.List;

public class MainListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource shooter = projectile.getShooter();
        if (!(shooter instanceof LivingEntity)) {
            return;
        }
        LivingEntity entity = (LivingEntity) shooter;
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) {
            return;
        }
        float speed = (float) projectile.getVelocity().length();
        ItemStack item = equipment.getItemInMainHand();
        if (isStormBow(item)) {
            if (launchStormBow(entity, speed)) {
                projectile.remove();
            }
            return;
        }
        if (item.getType() == Material.BOW) {
            return;
        }
        if (isStormBow(equipment.getItemInOffHand())) {
            if (launchStormBow(entity, speed)) {
                projectile.remove();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        Entity hitEntity = event.getHitEntity();
        Boolean flag = entity.getPersistentDataContainer().getOrDefault(NameSpacedKeys.IS_STORM_ARROW.key, BooleanDataType.INSTANCE, false);
        if (!flag) {
            return;
        }
        if (hitEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) hitEntity;
            livingEntity.setNoDamageTicks(1);
            return;
        }
        Bukkit.getScheduler().runTaskLater(StormBowPlugin.getInstance(), entity::remove, 60);
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Skeleton)) {
            return;
        }
        if (Math.random() > 0.01) {
            return;
        }
        List<ItemStack> drops = event.getDrops();
        drops.clear();
        ItemStack stack = new ItemStack(Material.BOW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("§6§l代达罗斯风暴弓");
        stack.setItemMeta(meta);
        drops.add(stack);
    }

    private boolean isStormBow(ItemStack stack) {
        if (stack.getAmount() < 1) {
            return false;
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return false;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        Boolean flag = container.get(NameSpacedKeys.IS_STORM_BOW.key, BooleanDataType.INSTANCE);
        if (flag == null) {
            return false;
        }
        return flag;
    }

    private boolean launchStormBow(LivingEntity entity, float speed) {
        Location eyeLocation = BowUtils.getEyeTargetLocation(entity, 0.1, 32);

        Location spawnLocation = eyeLocation.clone();
        spawnLocation.setY(eyeLocation.getY() + 10);

        while (spawnLocation.getBlock().getType().isSolid()) {
            if (spawnLocation.getY() < eyeLocation.getY() + 3) {
                return false;
            }
            spawnLocation.setY(spawnLocation.getY() - 1);
        }

        World world = entity.getWorld();
        for (int i = 0; i < 3 + Math.random() * 5; i++) {
            Bukkit.getScheduler().runTaskLater(StormBowPlugin.getInstance(), () -> {
                Arrow arrow = world.spawnArrow(
                        spawnLocation,
                        new Vector(
                                -0.1 + Math.random() * 0.2,
                                -speed * 0.3,
                                -0.1 + Math.random() * 0.2
                        ),
                        speed,
                        0
                );
                arrow.setShooter(entity);
                arrow.setKnockbackStrength(0);
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                arrow.getPersistentDataContainer().set(NameSpacedKeys.IS_STORM_ARROW.key, BooleanDataType.INSTANCE, true);
            }, 1 + i);
        }
        return true;
    }

}

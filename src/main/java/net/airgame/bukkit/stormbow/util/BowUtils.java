package net.airgame.bukkit.stormbow.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class BowUtils {

    public static Location getEyeTargetLocation(LivingEntity entity, double scanSize, int lengthLimit) {
        Location location = entity.getEyeLocation();
        World world = entity.getWorld();

        double yaw = Math.toRadians(location.getYaw());
        double pitch = Math.toRadians(location.getPitch());

        double y = -Math.sin(pitch) * scanSize;

        double vScan = Math.cos(pitch) * scanSize;

        double x = -Math.sin(yaw) * vScan;
        double z = Math.cos(yaw) * vScan;

        int size = (int) (lengthLimit / scanSize);
        Location target = location.clone();

        for (int i = 0; i < size; i++) {
            target.add(x, y, z);
            if (target.getBlock().getType().isSolid()) {
                return target;
            }
            List<LivingEntity> livingEntities = world.getLivingEntities();
            livingEntities.remove(entity);
            for (LivingEntity livingEntity : livingEntities) {
                if (livingEntity.getBoundingBox().contains(target.getX(), target.getY(), target.getZ())) {
                    return target;
                }
            }
        }
        return target;
    }
}

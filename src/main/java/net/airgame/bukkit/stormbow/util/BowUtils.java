package net.airgame.bukkit.stormbow.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;

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
                return target.add(-x, -y, -z);
            }
            List<LivingEntity> livingEntities = world.getLivingEntities();
            livingEntities.remove(entity);
            for (LivingEntity livingEntity : livingEntities) {
                BoundingBox boundingBox = livingEntity.getBoundingBox();
                if (boundingBox.contains(target.getX(), target.getY(), target.getZ())) {
                    return target.add(x, y, z);
                }
            }
        }
        return target;
    }
}

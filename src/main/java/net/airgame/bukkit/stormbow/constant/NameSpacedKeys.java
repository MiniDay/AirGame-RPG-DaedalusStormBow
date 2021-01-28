package net.airgame.bukkit.stormbow.constant;

import net.airgame.bukkit.stormbow.StormBowPlugin;
import org.bukkit.NamespacedKey;

public enum NameSpacedKeys {
    IS_STORM_BOW(),
    IS_STORM_ARROW();
    public final NamespacedKey key;

    NameSpacedKeys() {
        this.key = new NamespacedKey(StormBowPlugin.getInstance(), this.name());
    }
}

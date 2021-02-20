package net.airgame.bukkit.stormbow.command;

import net.airgame.bukkit.api.annotation.Command;
import net.airgame.bukkit.api.annotation.CommandExecutor;
import net.airgame.bukkit.api.annotation.Sender;
import net.airgame.bukkit.stormbow.constant.NameSpacedKeys;
import net.airgame.bukkit.stormbow.persistent.BooleanDataType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

@CommandExecutor(
        name = "stormBow",
        permission = "stormBow.get"
)
public class SetBowCommand {

    @Command
    @SuppressWarnings("ConstantConditions")
    public void get(@Sender Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack stack = new ItemStack(Material.BOW);
        ItemMeta meta = stack.getItemMeta();
        meta.getPersistentDataContainer().set(NameSpacedKeys.IS_STORM_BOW.key, BooleanDataType.INSTANCE, true);
        stack.setItemMeta(meta);
        inventory.addItem(stack);
        player.sendMessage("代达罗斯风暴弓构建完成.");
    }
}

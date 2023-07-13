package coffee.weneed.bukkit.shuklerception.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import coffee.weneed.bukkit.shuklerception.Shulkerception;

public class UseItem implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event) {
		
		// Shulker Box Right Click
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			if (!player.getGameMode().equals(GameMode.SURVIVAL))
				return;
			if (!player.hasPermission("Shulkerception.use"))
				return;

			ItemStack item = player.getInventory().getItemInMainHand();
			if (item.getType() == Material.AIR)
				return;
			if (item.getAmount() > 1)
				return;
			// test for Shulker Boxes that are renamed
			if (!Shulkerception.useNamedBoxes || !Shulkerception.useFormattedNamedBoxes) {
				ItemStack i = new ItemStack(item.getType());
				String defaultName = i.getItemMeta().getDisplayName();

				if (!Shulkerception.useNamedBoxes && !item.getItemMeta().getDisplayName().equals(defaultName))
					return;
				if (!Shulkerception.useFormattedNamedBoxes && !item.getItemMeta().getDisplayName().equals(ChatColor.stripColor(item.getItemMeta().getDisplayName())))
					return;

			}

			if (Shulkerception.supportedMaterials.contains(item.getType()) && (event.getAction() == Action.RIGHT_CLICK_AIR && player.isSneaking())) {
				event.setCancelled(true);
				player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 1, 1);
				//TODO getHeldItemSlot() should be getRawSlot
				player.openInventory(Shulkerception.createShulkerBoxInventory(player.getInventory().getHeldItemSlot(), player.getInventory().getItemInMainHand()).getInventory());
			}

		}

	}
}

package coffee.weneed.bukkit.shuklerception.events;

import coffee.weneed.bukkit.shuklerception.NestedInventory;
import coffee.weneed.bukkit.shuklerception.Shulkerception;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class InventoryListener implements Listener {
	//play shulker box close sound on Inventory close
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCloseInventory(InventoryCloseEvent event) {
		String title = event.getView().getTitle();
		if (!title.contains(Shulkerception.IDENTIFIER))
			return;

		Player player = (Player) event.getPlayer();
		((NestedInventory) event.getInventory().getHolder()).save();
		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1, 1);

		closeInventoryAnim(event.getInventory().getHolder());
		//make sure there is no loop from opening the new inventory
	}

	public void closeInventoryAnim(InventoryHolder inventory) {

		if (!(inventory instanceof NestedInventory) || ((NestedInventory) inventory).getNestedMaster() == null) {
        }/*
		BlockState state = ((NestedInventory) inventory).getNestedMaster().getInventory().getLocation().getWorld().getBlockAt(inventory.getInventory().getLocation()).getState();
		if ((!(state instanceof Chest) && !(state instanceof DoubleChest))){
			return;
		}
		if (state instanceof Chest) {
			Chest chest = (Chest) state;
			chest.close();
		} else if (state instanceof DoubleChest) {
			Chest chest = (Chest) ((DoubleChest) state).getLeftSide();
			chest.close();
			chest = (Chest) ((DoubleChest) state).getRightSide();
			chest.close();
		}*/
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onOpenInventory(InventoryOpenEvent event) {
		String title = event.getView().getTitle();
		if (!title.contains(Shulkerception.IDENTIFIER)) {
        }


	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		// ensure the Inventory is a Shulker Box Backpack Inventory
		if (!event.getWhoClicked().getGameMode().equals(GameMode.SURVIVAL))
			return;
		if (event.getWhoClicked().hasPermission("Shulkerception.use") && event.getCursor().getType().isAir()) {
			if (event.getCurrentItem() != null && event.getCurrentItem().getAmount() == 1 && Shulkerception.supportedMaterials.contains(event.getCurrentItem().getType()) && event.getClick().equals(ClickType.RIGHT)) {
				InventoryType.SlotType s = event.getSlotType();
				if (s.equals(InventoryType.SlotType.ARMOR) || s.equals(InventoryType.SlotType.CRAFTING) || s.equals(InventoryType.SlotType.RESULT)) {
					return;
				}
				if ((event.getInventory().getType().equals(InventoryType.CHEST) || event.getInventory().getHolder() instanceof NestedInventory || event.getInventory().getHolder() instanceof HumanEntity) &&
						((event.getInventory().getHolder() instanceof NestedInventory && event.getRawSlot() <= 27) || (s.equals(InventoryType.SlotType.CONTAINER) || s.equals(InventoryType.SlotType.QUICKBAR))
								|| (event.getRawSlot() <= 27 && ((NestedInventory) event.getInventory().getHolder()).getParent() != null))) {





					if (event.getInventory().getHolder() instanceof NestedInventory && ((NestedInventory) event.getInventory().getHolder()).checkTree(event.getCurrentItem(), event.getSlot())) {
						event.setCancelled(true);
						return;
					}
					if (event.getInventory().getLocation() == null && !(event.getInventory().getHolder() instanceof HumanEntity) && !(event.getInventory().getHolder() instanceof NestedInventory)) {
						//ignore if the inventory has no location, fixes borke
						return;
					}
					event.setCancelled(true);
					InventoryClickEvent finalEvent = event;
					Shulkerception.plugin.getServer().getScheduler().runTask(Shulkerception.plugin, () ->
					{

						Player player = (Player) finalEvent.getWhoClicked();
						player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 1, 1);
						finalEvent.getWhoClicked().openInventory(Shulkerception.createShulkerBoxInventory(finalEvent.getSlot(), finalEvent.getCurrentItem(), finalEvent.getInventory().getHolder()).getInventory());
					});
					return;
				} else if (event.getInventory().getType().equals(InventoryType.ENDER_CHEST) && event.getRawSlot() <= 27) {
					/*event.setCancelled(true);
					InventoryClickEvent finalEvent1 = event;
					Shulkerception.plugin.getServer().getScheduler().runTask(Shulkerception.plugin, () ->
						{
						Player player = (Player) finalEvent1.getWhoClicked();
						player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 1, 1);
						finalEvent1.getWhoClicked().openInventory(Shulkerception.createShulkerBoxInventory(finalEvent1.getSlot(), finalEvent1.getCurrentItem(), finalEvent1.getInventory().getHolder()).getInventory());
					});*/
					return;
				}
			}
		}
		if (!event.getView().getTitle().contains(Shulkerception.IDENTIFIER)) {
			return;
		}
		
		
		if (event.getCurrentItem() == null && event.getCursor().getType().isAir()) {
			if ((((NestedInventory) event.getInventory().getHolder()).getMaster() == null && ((NestedInventory) event.getInventory().getHolder()).getParent() == null) || (((NestedInventory) event.getInventory().getHolder()).getParent() == null && ((NestedInventory) event.getInventory().getHolder()).getMaster() == null) || !event.getWhoClicked().hasPermission("Shulkerception.use") || !event.getClick().equals(ClickType.SHIFT_RIGHT)) {
				return;
			}
			event.setCancelled(true);
			InventoryClickEvent finalEvent2 = event;
			Shulkerception.plugin.getServer().getScheduler().runTask(Shulkerception.plugin, () ->
			{
				finalEvent2.getWhoClicked().openInventory(((NestedInventory) finalEvent2.getInventory().getHolder()).getParent() == null ? ((NestedInventory) finalEvent2.getInventory().getHolder()).getMaster().getInventory() : ((NestedInventory) finalEvent2.getInventory().getHolder()).getParent().getInventory());
				closeInventoryAnim(finalEvent2.getInventory().getHolder());
			});
			return;
		} else if (event.getInventory().getHolder() instanceof NestedInventory && ((NestedInventory) event.getInventory().getHolder()).checkTree(event.getCurrentItem(), event.getSlot())){
				event.setCancelled(true);
			return;
		}

		if (event.getInventory().getHolder() instanceof NestedInventory && ((NestedInventory) event.getInventory().getHolder()).checkTree(event.getCurrentItem(), event.getSlot()) && event.getAction().name().toLowerCase().contains("DROP".toLowerCase())){
			event.setCancelled(true);
			return;
		}

		if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getInventory().getHolder() instanceof NestedInventory){
			if (NBTEditor.contains(event.getCurrentItem(), "BPIsBackPack") && NBTEditor.getBoolean(event.getCurrentItem(), "BPIsBackPack")){
				event.setCancelled(true);
			} else if (NBTEditor.contains(event.getCurrentItem(), "PublicBukkitValues", "slimefun:slimefun_item") && NBTEditor.getString(event.getCurrentItem(), "PublicBukkitValues", "slimefun:slimefun_item").toLowerCase().contains("backpack")){
				event.setCancelled(true);
			}
		}
		NestedInventory inv = (NestedInventory) event.getInventory().getHolder();
		if (inv == null){
			return;
		}
		if (event.getCurrentItem() != null && Shulkerception.supportedMaterials.contains(event.getCurrentItem().getType()) &&
				(!Shulkerception.nesting ||
						// prevent putting box inside itself (tests this by testing equal-ness for shulker boxes in hotbar
						(inv.checkTree(event.getCurrentItem(), event.getSlot()) && event.getRawSlot() >= 54))) {
			event.setCancelled(true);
			return;
		}

		ItemStack shulkerBox = ((NestedInventory) event.getInventory().getHolder()).getShulker();
		// prevent duplication exploits on laggy servers by closing Inventory if no shulker box in hand on Inventory click
		if (shulkerBox == null) {
			return;
			//event.setCancelled(true);
			//event.getWhoClicked().closeInventory();
		}

		// prevent nesting too far
		if (event.getCurrentItem() != null && Shulkerception.supportedMaterials.contains(event.getCurrentItem().getType())) {
			if (event.getRawSlot() > 34) {
				if (Shulkerception.nesting && Shulkerception.nestingDepth > -1 && Shulkerception.getNestingDepth(event.getCurrentItem(), 1) >= Shulkerception.nestingDepth) {
					event.setCancelled(true);
					return;
				}
			}
		}

		BlockStateMeta im = (BlockStateMeta) shulkerBox.getItemMeta();
		ShulkerBox shulker = (ShulkerBox) im.getBlockState();

		// set all contents minus most recent item
		shulker.getInventory().setContents(event.getInventory().getContents());

		// set most recent item
		// if (event.getAction() == InventoryAction.DROP_ALL_SLOT)
		//shulker.getInventory().setItem(event.getSlot(), event.getCurrentItem());

		im.setBlockState(shulker);
		shulkerBox.setItemMeta(im);
	}

}

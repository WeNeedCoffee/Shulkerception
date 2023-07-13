package coffee.weneed.bukkit.shuklerception;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

public class NestedInventory implements InventoryHolder {
	private ItemStack shulker;
	private NestedInventory parent;
	private Inventory inventory;
	private boolean closed = false;
	private InventoryHolder master;
	private int slot = -1;
	public void setSlot(int slot){
		this.slot = slot;
	}
	public int getSlot() {
		return this.slot;
	}
	private void setMaster(InventoryHolder master) {
		this.master = master;
	}

	public InventoryHolder getNestedMaster() {
		if (getMaster() == null){
			if (getParent() == null) {
				return null; //why?
			} else {
				return getParent().getNestedMaster();
			}
		} else {
			return getMaster();
		}
	}

	public InventoryHolder getMaster() {
		return master;
	}
	public NestedInventory(ItemStack shulker, int slot) {
		this(null, shulker, slot);
	}
	public NestedInventory(InventoryHolder master, ItemStack shulker, int slot) {
		this(null, shulker, slot);
		setMaster(master);
	}
	public NestedInventory(NestedInventory parent, ItemStack shulker, int slot) {
		setShulker(shulker);
		setParent(parent);
		setSlot(slot);
		BlockStateMeta im = (BlockStateMeta) shulker.getItemMeta();
		ShulkerBox shulkerstate = (ShulkerBox) im.getBlockState();
		String name = shulker.getItemMeta().getDisplayName();
		if (name.equals("")) {
			name = "Shulker Box";
		}
		Inventory inv = Bukkit.createInventory(this, 27, name + Shulkerception.IDENTIFIER);
		setInventory(inv);
		getInventory().setContents(shulkerstate.getInventory().getContents());
	}

	public ItemStack getShulker() {
		return shulker;
	}

	public void setShulker(ItemStack shulker) {
		this.shulker = shulker;
	}

	public void save() {
		BlockStateMeta im = (BlockStateMeta) getShulker().getItemMeta();
		ShulkerBox shulker = (ShulkerBox) im.getBlockState();

		//set all contents minus most recent item
		shulker.getInventory().setContents(getInventory().getContents());
		im.setBlockState(shulker);
		getShulker().setItemMeta(im);
		
		if (getParent() != null) {
			getParent().save();
		}
	}

	public boolean checkTree(ItemStack shulker, int slot) {
		System.out.print(slot + " " + getSlot());
		if (getShulker().equals(shulker) && slot == getSlot()) {
			return true;
		} else if (getParent() != null) {
			return getParent().checkTree(shulker, slot);
		}
		return false;
	}
	public NestedInventory getParent() {
		return parent;
	}

	public void setParent(NestedInventory parent) {
		this.parent = parent;
	}

	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inv) {
		this.inventory = inv;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
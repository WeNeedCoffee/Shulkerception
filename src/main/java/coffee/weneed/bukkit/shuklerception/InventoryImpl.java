package coffee.weneed.bukkit.shuklerception;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventoryImpl implements Inventory {
	private Inventory inventory;

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public InventoryImpl(Inventory inv) {
		setInventory(inv);
	}

	@Override
	public int getSize() {
		return getInventory().getSize();
	}

	@Override
	public int getMaxStackSize() {
		return getInventory().getMaxStackSize();
	}

	@Override
	public void setMaxStackSize(int size) {
		getInventory().setMaxStackSize(size);
	}

	@Override
	public @Nullable ItemStack getItem(int index) {
		return getInventory().getItem(index);
	}

	@Override
	public void setItem(int index, @Nullable ItemStack item) {
		getInventory().setItem(index, item);
	}

	@Override
	public @NotNull HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... items) throws IllegalArgumentException {
		return getInventory().addItem(items);
	}

	@Override
	public @NotNull HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... items) throws IllegalArgumentException {
		return getInventory().removeItem(items);
	}

	@Override
	public @NotNull HashMap<Integer, ItemStack> removeItemAnySlot(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
		return getInventory().removeItemAnySlot(itemStacks);
	}

	@Override
	public @NotNull ItemStack[] getContents() {
		return getInventory().getContents();
	}

	@Override
	public void setContents(@NotNull ItemStack[] items) throws IllegalArgumentException {
		getInventory().setContents(items);
	}

	@Override
	public @NotNull ItemStack[] getStorageContents() {
		return getInventory().getStorageContents();
	}

	@Override
	public void setStorageContents(@NotNull ItemStack[] items) throws IllegalArgumentException {
		getInventory().setStorageContents(items);
	}

	@Override
	public boolean contains(@NotNull Material material) throws IllegalArgumentException {
		return getInventory().contains(material);
	}

	@Override
	public boolean contains(@Nullable ItemStack item) {
		return getInventory().contains(item);
	}

	@Override
	public boolean contains(@NotNull Material material, int amount) throws IllegalArgumentException {
		return getInventory().contains(material, amount);
	}

	@Override
	public boolean contains(@Nullable ItemStack item, int amount) {
		return getInventory().contains(item, amount);
	}

	@Override
	public boolean containsAtLeast(@Nullable ItemStack item, int amount) {
		return getInventory().containsAtLeast(item, amount);
	}

	@Override
	public @NotNull HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
		return getInventory().all(material);
	}

	@Override
	public @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item) {
		return getInventory().all(item);
	}

	@Override
	public int first(@NotNull Material material) throws IllegalArgumentException {
		return getInventory().first(material);
	}

	@Override
	public int first(@NotNull ItemStack item) {
		return getInventory().first(item);
	}

	@Override
	public int firstEmpty() {
		return getInventory().firstEmpty();
	}

	@Override
	public void remove(@NotNull Material material) throws IllegalArgumentException {
		getInventory().remove(material);
	}

	@Override
	public void remove(@NotNull ItemStack item) {
		getInventory().remove(item);
	}

	@Override
	public void clear(int index) {
		getInventory().clear(index);
	}

	@Override
	public void clear() {
		getInventory().clear();
	}

	@Override
	public int close() {
		return getInventory().close();
	}

	@Override
	public @NotNull List<HumanEntity> getViewers() {
		return getInventory().getViewers();
	}

	@Override
	public @NotNull InventoryType getType() {
		return getInventory().getType();
	}

	@Override
	public @Nullable InventoryHolder getHolder() {
		return getInventory().getHolder();
	}

	@Override
	public @Nullable InventoryHolder getHolder(boolean b) {
		return getInventory().getHolder(b);
	}

	@Override
	public @NotNull ListIterator<ItemStack> iterator() {
		return getInventory().iterator();
	}

	@Override
	public @NotNull ListIterator<ItemStack> iterator(int index) {
		return getInventory().iterator(index);
	}

	@Override
	public @Nullable Location getLocation() {
		return getInventory().getLocation();
	}

	@Override
	public boolean isEmpty() {
		return getInventory().isEmpty();
	}

}
package coffee.weneed.bukkit.shuklerception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.io.ByteStreams;
import coffee.weneed.bukkit.shuklerception.commands.reload;
import coffee.weneed.bukkit.shuklerception.events.InventoryListener;
import coffee.weneed.bukkit.shuklerception.events.UseItem;

public class Shulkerception extends JavaPlugin {

	public static final String IDENTIFIER = ChatColor.BLUE + "+" + ChatColor.RESET;
	public static final List<Material> supportedMaterials = Arrays.asList(Material.SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX);
	//Settings
	public static boolean nesting = false;

	public static boolean placed = false;
	public static int nestingDepth = -1;

	public static Shulkerception plugin;

	public static boolean useNamedBoxes = true;
	public static boolean useFormattedNamedBoxes = true;
	public static NestedInventory createShulkerBoxInventory(int slot, ItemStack shulkerBoxItemStack, InventoryHolder parent) {

		if (shulkerBoxItemStack.getItemMeta() instanceof BlockStateMeta) {
			BlockStateMeta im = (BlockStateMeta) shulkerBoxItemStack.getItemMeta();
			if (im.getBlockState() instanceof ShulkerBox) {
				if (parent instanceof  NestedInventory) {
					return new NestedInventory((NestedInventory) parent, shulkerBoxItemStack, slot);
				} else if (parent instanceof HumanEntity) {
					return new NestedInventory(shulkerBoxItemStack, slot);
				} else {
					return new NestedInventory(parent, shulkerBoxItemStack, slot);
				}
			}
		}
		//should never happen, because function is only called for shulker boxes
		return null;
	}
	
	public static NestedInventory createShulkerBoxInventory(int slot, ItemStack shulkerBoxItemStack) {
		return createShulkerBoxInventory(slot, shulkerBoxItemStack, null);
	}
	
	public static int getNestingDepth(ItemStack shulkerBox, int currentDepth) {

		//make arraylist of all shulker boxes in current box
		BlockStateMeta im = (BlockStateMeta) shulkerBox.getItemMeta();
		ShulkerBox shulker = (ShulkerBox) im.getBlockState();
		ItemStack[] items = shulker.getInventory().getContents();

		ArrayList<ItemStack> boxes = new ArrayList<>();
		for (ItemStack item : items) {
			if (item == null) {
				continue;
			}
			if (supportedMaterials.contains(item.getType())) {
				boxes.add(item);
			}
		}

		//determine maximum depth of boxes contained in current box
		int maxDepth = currentDepth;
		for (ItemStack box : boxes) {
			int nextDepth = getNestingDepth(box, currentDepth + 1);
			if (nextDepth > maxDepth) {
				maxDepth = nextDepth;
			}
		}

		return maxDepth;
	}

	// config and IO
	private PluginDescriptionFile descFile = getDescription();
	private PluginManager pm = getServer().getPluginManager();
	private FileConfiguration config;

	private Logger logger = getLogger();

	//load config settings
	public void loadConfiguration() {
		config = getConfig();
		placed = config.getBoolean("placed");
		nesting = config.getBoolean("nesting");
		nestingDepth = config.getInt("nesting-depth");
		useNamedBoxes = config.getBoolean("use-renamed-boxes");
		useFormattedNamedBoxes = config.getBoolean("use-formatted-renamed-boxes");
	}

	//Loads file from JAR with comments
	private File loadResource(Plugin plugin, String resource) {
		File folder = plugin.getDataFolder();
		if (!folder.exists()) {
			folder.mkdir();
		}
		File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = plugin.getResource(resource); OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceFile;
	}

	@Override
	public void onDisable() {
		//posts exit message in chat
		logger.info(descFile.getName() + " V: " + descFile.getVersion() + " has been disabled");
	}

	@Override
	public void onEnable() {
		plugin = this;
		//load config.yml (generate one if not there)
		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			loadResource(getPlugin(Shulkerception.class), "config.yml");
		}

		loadConfiguration();

		getCommand("scReload").setExecutor(new reload(this));
		registerEvents();

		//posts confirmation in chat
		logger.info(descFile.getName() + " V: " + descFile.getVersion() + " has been enabled");
	}

	public void registerEvents() {
		pm.registerEvents(new UseItem(), this);
		pm.registerEvents(new InventoryListener(), this);
	}

}

package me.bullterrier292;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Torch;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.CommandExecute;

public class Commands extends CommandExecute implements Listener, CommandExecutor {
	int task1;
	int task2;
	int task3;
	int task4;
	int taskGoThoughWholeMap;

	int autoChecker1;
	int autoChecker2;
	int counter;
	// -288 86 112
	// + +

	private String cornerCordXZplus = "-287, 86, 112";
	private String dirChar = "ES";
	public static int howManyChunksTogo = 100;
	int chunkNumber = 0;

	public String cmd1 = "rw";
	public String cmd2 = "rwfixdchunks";
	public String cmd3 = "rwfixsurface";
	public String cmd4 = "rwautodchunks";
	public String cmd5 = "rwautosurface";
	public String cmd6 = "rwtest";
	public String cmd7 = "rwstop";

	// =======================================================================================
	// =======================================================================================
	// =======================================================================================
	public static int chunkpointsHasbeenUpdated = 0;

	public static ArrayList<String> chunkpointsXZ = tellingChunkPointXZ(-288, 112, howManyChunksTogo);

	public static ArrayList<Integer> chunkFirstPointX = new ArrayList();
	public static ArrayList<Integer> chunkFirstPointZ = new ArrayList();

	public static ArrayList<Integer> chunkSecondPointX = new ArrayList();
	public static ArrayList<Integer> chunkSecondPointZ = new ArrayList();

	public static ArrayList<String> wholeChunkExpired = new ArrayList();
	public static ArrayList<String> surfaceChunksExpired = new ArrayList();

	// =======================================================================================
	// =======================================================================================
	// =======================================================================================
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);
		chunkCalculatorStarted();

		/*
		 * Location loc = player.getLocation(); Chunk c = loc.getChunk();
		 * System.out.println(c.getBlock(0, 0, 0).getX());
		 * System.out.println(c.getBlock(0, 0, 0).getZ());
		 */

		System.out.println(args.length);
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase(cmd1)) {
				sender.sendMessage("World Restore is working");
				// goingThroughChunks("world", -288, 0, 112, 256, 10);

				counter = howManyChunksTogo * howManyChunksTogo;

				int howManyChunksOnce = 10;
				int howManySecondDelay = 2;

				counter = counter / howManyChunksOnce + 1;

				sender.sendMessage("starting");

				taskGoThoughWholeMap = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {

					@Override
					public void run() {

						for (int i = 0; i < howManyChunksOnce; i++) {
							String chunkKey = String.valueOf(chunkNumber);
							fixSpecificChunk(chunkKey, 0);
							System.out.println("=");
							chunkNumber++;
						}

						System.out.println("Going in chunk: " + chunkNumber);

						if (counter <= 0 || chunkNumber >= (howManyChunksTogo * howManyChunksTogo)) {

							Bukkit.getScheduler().cancelTask(taskGoThoughWholeMap);
							chunkNumber = 0;
						}

						counter--;

					}

				}, 0, 20 * howManySecondDelay);

				// System.out.println("ended");

				return true;

			} else if (cmd.getName().equalsIgnoreCase(cmd2)) {
				counter = 0;
				int howManyChunksOnce = 4;
				int howManySecondDelay = 5;
				int y = 0;
				Bukkit.getScheduler().cancelTask(task1);
				if (p.getConfig().contains("Main.wholeChunkIds")) {
					for (String key : p.getConfig().getConfigurationSection("Main.wholeChunkIds").getKeys(false)) {
						counter++;
					}

					counter = counter / howManyChunksOnce + 1;

					sender.sendMessage("starting");
					task1 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
						@Override
						public void run() {

							if (counter <= 0) {
								Bukkit.getScheduler().cancelTask(task1);
							}
							fixDetectedChunksToDefault(y, howManyChunksOnce);
							counter--;

						}

					}, 0, 20 * howManySecondDelay);

					// System.out.println("ended");
				}

				return true;
			} else if (cmd.getName().equalsIgnoreCase(cmd3)) {
				counter = 0;
				int howManyChunksOnce = 4;
				int howManySecondDelay = 5;
				int y = 54;
				Bukkit.getScheduler().cancelTask(task2);
				if (p.getConfig().contains("Main.surfaceChunkIds")) {
					for (String key : p.getConfig().getConfigurationSection("Main.surfaceChunkIds").getKeys(false)) {
						counter++;
					}

					counter = counter / howManyChunksOnce + 1;

					sender.sendMessage("starting");
					task2 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
						@Override
						public void run() {

							if (counter <= 0) {
								Bukkit.getScheduler().cancelTask(task2);
							}
							fixDetectedChunksToDefault(y, howManyChunksOnce);
							counter--;
							System.out.println(counter);

						}

					}, 0, 20 * howManySecondDelay);

					// System.out.println("ended");
				}

				return true;
			} else if (cmd.getName().equalsIgnoreCase(cmd4)) {
				System.out.println(cmd5 + "asd");
				if (args.length > 0) {
					System.out.println("sisal");

					System.out.println("starting");
					if (isInteger(args[0])) {
						sender.sendMessage(ChatColor.GREEN + "Automatic whole Chunks has been enabled");
						autochecker(0, Integer.parseInt(args[0]));
						autoUpdater(0);
					} else {
						sender.sendMessage(ChatColor.GREEN + "whole chunk updates have been canceled");
						Bukkit.getScheduler().cancelTask(task3);
						Bukkit.getScheduler().cancelTask(autoChecker1);
					}

				} else {
					sender.sendMessage(ChatColor.RED + "Give more arguments");
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase(cmd5)) {
				System.out.println(cmd5 + "asd");
				if (args.length > 0) {
					System.out.println("sisal");

					System.out.println("starting");
					if (isInteger(args[0])) {
						sender.sendMessage(ChatColor.GREEN + "Automatic surface Chunks has been enabled");
						autochecker(1, Integer.parseInt(args[0]));
						autoUpdater(1);
					} else {
						sender.sendMessage(ChatColor.RED + "Surface chunk updates have been canceled");
						Bukkit.getScheduler().cancelTask(task4);
						Bukkit.getScheduler().cancelTask(autoChecker2);
					}

				} else {
					sender.sendMessage(ChatColor.RED + "Give more arguments");
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase(cmd6)) {
				System.out.println("testingWG");
				testingWG();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase(cmd7)) {
				Bukkit.getScheduler().cancelTask(taskGoThoughWholeMap);
				sender.sendMessage(ChatColor.RED + "Restore whole world has been canceled!");
				return true;

			}

		} else {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
		}

		return false;

	}

	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public void autoUpdater(int sectionValue) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);

		int howManySecondDelay = 10;

		if (sectionValue == 0) {

			howManySecondDelay = 4;

			task3 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
				@Override
				public void run() {
					if (wholeChunkExpired.size() > 0) {
						String chunkKey = wholeChunkExpired.get(0);
						fixSpecificChunk(chunkKey, sectionValue);
						wholeChunkExpired.remove(0);
					}
				}

			}, 0, 20 * howManySecondDelay);

		} else if (sectionValue == 1) {

			howManySecondDelay = 2;

			task4 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
				@Override
				public void run() {
					if (surfaceChunksExpired.size() > 0) {
						String chunkKey = surfaceChunksExpired.get(0);
						fixSpecificChunk(chunkKey, sectionValue);
						surfaceChunksExpired.remove(0);
					}
				}

			}, 0, 20 * howManySecondDelay);
		}

	}

	public void autochecker(int sectionValue, int timeInMs) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);
		int howManySecondDelay = 30;
		if (sectionValue == 0) {
			autoChecker1 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
				@Override
				public void run() {
					wholeChunkExpired = checkChunkTimes(sectionValue, timeInMs);

				}

			}, 0, 20 * howManySecondDelay);
		} else if (sectionValue == 1) {
			autoChecker2 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
				@Override
				public void run() {
					surfaceChunksExpired = checkChunkTimes(sectionValue, timeInMs);
				}

			}, 0, 20 * howManySecondDelay);
		}
	}

	public static void chunkCalculatorStarted() {
		if (chunkpointsHasbeenUpdated == 0) {
			cuttingChunkList();
			chunkpointsHasbeenUpdated = 1;
		}
	}

	public void fixDetectedChunksToDefault(int y, int howManyChunksOnce) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);
		int chunkHeight = 256;
		int chunksTogo = 1;
		int count = 0;
		String section = "Main.wholeChunkIds";

		if (y != 0) {
			section = "Main.surfaceChunkIds";
			chunkHeight = 131;
		}
		if (p.getConfig().contains(section)) {

			for (String key : p.getConfig().getConfigurationSection(section).getKeys(false)) {
				ConfigurationSection chunkInfo = p.getConfig().getConfigurationSection(section);

				String valuesInfo = chunkInfo.getString(key);

				String[] valueParts = valuesInfo.split(":");

				// double placedTime = Double.parseDouble(valueParts[1]);

				String[] chunkCordinateParts = valueParts[0].split(",");
				int x = Integer.parseInt(chunkCordinateParts[0]);
				int z = Integer.parseInt(chunkCordinateParts[1]);

				goingThroughChunks("world", x, y, z, chunkHeight, chunksTogo);

				p.getConfig().set(section + "." + key, null);
				count++;

				if (count >= howManyChunksOnce) {
					break;
				}
			}

			p.saveConfig();
		}

	}

	public void fixSpecificChunk(String chunkKey, int sectionValue) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);
		String section = "Main.wholeChunkIds";
		int chunkHeight = 256;
		int y = 0;

		if (sectionValue == 0) {
			// who chunk
			section = "Main.wholeChunkIds";
			chunkHeight = 256;
			y = 0;
		} else if (sectionValue == 1) {
			// surface 55-130
			section = "Main.surfaceChunkIds";
			chunkHeight = 130;
			y = 55;
		}
		if (p.getConfig().contains(section + "." + chunkKey)) {
			ConfigurationSection chunkInfo = p.getConfig().getConfigurationSection(section);

			String valuesInfo = chunkInfo.getString(chunkKey);

			String[] valueParts = valuesInfo.split(":");

			String[] chunkCordinateParts = valueParts[0].split(",");
			int x = Integer.parseInt(chunkCordinateParts[0]);
			int z = Integer.parseInt(chunkCordinateParts[1]);

			goingThroughChunks("world", x, y, z, chunkHeight, 1);

			p.getConfig().set(section + "." + chunkKey, null);
			p.saveConfig();
		} else {
			System.out.println(chunkFirstPointX.get(Integer.parseInt(chunkKey)));
			System.out.println(chunkFirstPointZ.get(Integer.parseInt(chunkKey)));
			goingThroughChunks("world", chunkFirstPointX.get(Integer.parseInt(chunkKey)), y,
					chunkFirstPointZ.get(Integer.parseInt(chunkKey)), chunkHeight, 1);

		}
	}

	public static ArrayList checkChunkTimes(int sectionValue, double timeWhenExpireMs) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);
		String section = "Main.wholeChunkIds";
		ArrayList<String> chunkIds = new ArrayList();
		if (sectionValue == 0) {
			// who chunk
			section = "Main.wholeChunkIds";

		} else if (sectionValue == 1) {
			// surface 55-130
			section = "Main.surfaceChunkIds";

		}

		for (String key : p.getConfig().getConfigurationSection(section).getKeys(false)) {
			ConfigurationSection chunkInfo = p.getConfig().getConfigurationSection(section);

			String valuesInfo = chunkInfo.getString(key);

			String[] valueParts = valuesInfo.split(":");

			Calendar cal = Calendar.getInstance();
			Long timeNow = cal.getTimeInMillis();

			double placedTime = Double.parseDouble(valueParts[1]);

			double timeDif = timeNow - placedTime;
			System.out.println(timeDif);
			if (timeDif >= timeWhenExpireMs) {

				chunkIds.add(key);

			}

		}
		return chunkIds;
	}

	public void testingWG() {
		Bukkit.getServer().getWorld("world").getBlockAt(-277, 86, 152).setType(Material.CHEST);
		Block block = Bukkit.getServer().getWorld("world").getBlockAt(-277, 86, 152);
		Chest chest = (Chest) block.getState();
		ItemStack[] chestInv = chest.getInventory().getContents(); // inventory before
		Bukkit.getServer().getWorld("world").getBlockAt(-275, 86, 152).setType(Material.CHEST);

		Block block2 = Bukkit.getServer().getWorld("world").getBlockAt(-275, 86, 152);
		Chest toChest = (Chest) block2.getState();
		System.out.println(chestInv.length);

		ItemStack[] toChestInv = new ItemStack[chestInv.length]; // chest content

		try {
			for (int i = 0; i < toChestInv.length; i++) {
				toChestInv[i] = chestInv[i];
			}
		} catch (Exception e) {
			System.out.println("asd");
		}
		try {
			for (int i = 0; i < chestInv.length; i++) {
				if (toChestInv[i] != null) {
					// toChest.getInventory().addItem(toChestInv[i]);
					toChest.getInventory().setItem(i, toChestInv[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("asd2");
		}
		Block blocktest = Bukkit.getServer().getWorld("world").getBlockAt(-276, 86, 150);
		System.out.println("material data: " + blocktest.getData());
		/*
		 * System.out.println("asdasd"); Block blocktest2 =
		 * Bukkit.getServer().getWorld("world").getBlockAt(-275, 86, 150);
		 * 
		 * blocktest2.setTypeIdAndData(50, blocktest.getData(), false); /*
		 * 
		 * Block block = Bukkit.getServer().getWorld("world").getBlockAt(-277, 86, 152);
		 * System.out.println(block);
		 * 
		 * Chest chest = (Chest) block.getState(); Inventory inv = chest.getInventory();
		 * System.out.println(inv.getContents()[1]);
		 * System.out.println(inv.getContents().length);
		 * System.out.println(inv.getItem(1));
		 * 
		 * Bukkit.getServer().getWorld("world").getBlockAt(-275, 86,
		 * 152).setType(Material.CHEST); Block block2 =
		 * Bukkit.getServer().getWorld("world").getBlockAt(-275, 86, 152); Chest chest2
		 * = (Chest) block2.getState(); Inventory inv2 = chest.getInventory();
		 * System.out.println("hello");
		 * 
		 * //inv2.setContents(inv.getContents());
		 * 
		 * for (int i = 0; i < inv.getContents().length; i++) {
		 * 
		 * if (inv.getItem(i) != null) { ItemStack item = inv.getItem(i);
		 * System.out.println(item); inv.addItem(item); } System.out.println(i);
		 * 
		 * } chest2.update(); /* Player p = (Player) s; Location loc =
		 * p.getTargetBlock((HashSet<Byte>) null, 0).getLocation(); Location loc2 =
		 * p.getLocation(); Block b = p.getWorld().getBlockAt(loc); Block b2 =
		 * p.getWorld().getBlockAt(p.getLocation()); b2.setTypeIdAndData(b.getTypeId(),
		 * b.getData(), true); CraftWorld cw = (CraftWorld) p.getWorld(); TileEntity
		 * tileEntity = cw.getTileEntityAt(loc.getBlockX(), loc.getBlockY(),
		 * loc.getBlockZ()); TileEntity tileEntity2 =
		 * cw.getTileEntityAt(loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
		 * NBTTagCompound ntc = new NBTTagCompound(); NBTTagCompound ntc2 = new
		 * NBTTagCompound(); tileEntity.b(ntc); ntc2 = (NBTTagCompound) ntc.clone();
		 * ntc2.setInt("x", loc2.getBlockX()); ntc2.setInt("y", loc2.getBlockY());
		 * ntc2.setInt("z", loc2.getBlockZ()); tileEntity2.a(ntc2);
		 * tileEntity2.update();
		 */
	}

	public void goingThroughChunks(String worldname, int x, int y, int z, int chunkHeight, int chunksTogo) {
		List compBlocks = Arrays.asList(53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180, 203, // stairs
				64, 71, 96, 167, 193, 194, 195, 196, 324, 330, 427, 428, 429, 430, 431, // doors
				34, 33, 29, // pistons
				62, 61, 343, // furnaces
				84, 137, 210, 211, 422, // commandblocks
				144// mobheads
		);
		List commonBlocks = Arrays.asList(1, 2, 3, 4, 5, 6);
		List torchData = Arrays.asList(1, 3, 4, 2);
		ArrayList<Block> torchBlocks = new ArrayList<>();
		ArrayList<Block> signBlocks = new ArrayList<>();
		List materialSign = Arrays.asList(Material.WALL_SIGN, Material.SIGN);
		if (signBlocks.size() > 0) {
			System.out.println("HELOOOOOOOOOOOOOOOOOOOOOOOOOOO!================");
		}
		WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		String default_world = "default_world";

		int dirMultX = 1;
		int dirMultZ = 1;

		int dirX = 1 * dirMultX;
		int dirZ = 1 * dirMultZ;

		int chunkSizex = 16;
		int chunkSizez = 16;

		int default_x = x;
		int default_y = y;
		int default_z = z;

		int chunkSizeX = x + (chunkSizex * dirX);
		int chunkSizeZ = z + (chunkSizez * dirZ);

		// going thorugh chunks by z
		for (int chunknumber1 = 0; chunknumber1 < chunksTogo; chunknumber1++) {

			// going through chunks by x
			for (int chunknumber2 = 0; chunknumber2 < chunksTogo; chunknumber2++) {

				// going through x axel
				for (; x < chunkSizeX; x++) {

					// going thorugh z axel
					for (; z < chunkSizeZ; z++) {

						// going through y axel
						for (; y < chunkHeight; y++) {
							// Bukkit.getServer().getWorld(worldname).getBlockAt(x, y,
							// z).setType(Material.STONE);

							Material worldBlock = Bukkit.getServer().getWorld(worldname).getBlockAt(x, y, z).getType();
							Material default_worldBlock = Bukkit.getServer().getWorld(default_world).getBlockAt(x, y, z)
									.getType();
							Block b = Bukkit.getServer().getWorld(worldname).getBlockAt(x, y, z);
							Block b2 = Bukkit.getServer().getWorld(default_world).getBlockAt(x, y, z);
							// here

							if (worldBlock != default_worldBlock || default_worldBlock == Material.CHEST) {

								boolean canPlace = true;
								for (ProtectedRegion r : WGBukkit.getRegionManager(Bukkit.getWorld(worldname))
										.getApplicableRegions(b.getLocation())) {

									if (r.getOwners().size() > 0) {
										canPlace = false;
									}
								}
								if (canPlace) {

									/*
									 * Bukkit.getServer().getWorld(worldname).getBlockAt(x, y, z)
									 * .setType(default_worldBlock);
									 */
									// here
									String str = "";

									if (default_worldBlock == Material.CHEST) {
										System.out.println("hello");
										str = avaivableOreDirection(x, y, z, Material.CHEST, default_world);
										if (str.length() > 0) {
											Location loc = rightCord(str.charAt(0), worldname, x, y, z);
											Block block1 = Bukkit.getServer().getWorld(worldname).getBlockAt(loc);
											if (block1.getType() != Material.CHEST) {
												block1.setType(Material.CHEST);
											}
										}

									}
									System.out.println("B2 material:" + b2.getType());
									if (b2.getTypeId() > 16) {
										if (b2.getTypeId() == Material.TORCH.getId()
												|| b2.getTypeId() == Material.REDSTONE_TORCH_ON.getId()
												|| b2.getTypeId() == Material.REDSTONE_TORCH_OFF.getId()) {
											torchBlocks.add(b2);

											//

										} else if (materialSign.contains(b2.getType())) {
											System.out.println("sign added");
											signBlocks.add(b2);

										} else {

											Location point1 = new Location(Bukkit.getServer().getWorld(worldname), x, y,
													z);
											copyBlockToLocation(b2, point1);
										}

									} else {
										Bukkit.getServer().getWorld(worldname).getBlockAt(x, y, z)
												.setType(default_worldBlock);
									}

								}

							}
						}
						y = default_y;

					}
					y = default_y;
					z = default_z;

				}
				chunkSizeX = x + (chunkSizex * dirX);

			}

			// one chunk has row has been done => going next

			x = default_x;
			default_z += 16;
			z = default_z;
			y = default_y;

			chunkSizeX = x + (chunkSizex * dirX);
			chunkSizeZ = z + (chunkSizez * dirZ);

		}
		System.out.println(torchBlocks);
		System.out.println(signBlocks);
		if (!torchBlocks.isEmpty()) {
			for (int i = 0; i < torchBlocks.size(); i++) {
				Block db = torchBlocks.get(i);
				Block overworldBlock = Bukkit.getServer().getWorld(worldname).getBlockAt(db.getX(),
						db.getY(), db.getZ());
				overworldBlock.setTypeIdAndData(db.getTypeId(), db.getData(), false);
			}
		}
		
		if (!signBlocks.isEmpty()) {
			for (int i = 0; i < signBlocks.size(); i++) {
				Block db = signBlocks.get(i);
				Location p = new Location(Bukkit.getServer().getWorld(worldname), db.getX(), db.getY(), db.getZ());
				copyBlockToLocation(db, p);
				System.out.println("ADDD");
			}

		}

	}

	public static ArrayList<String> tellingChunkPointXZ(int x, int z, int chunksTogo) {

		ArrayList<String> chunkpointsXZ = new ArrayList();

		int default_x = x;
		int default_z = z;

		String str;

		for (int zAxel = 0; zAxel < chunksTogo; zAxel++) {

			for (int xAxel = 0; xAxel < chunksTogo; xAxel++) {
				str = String.valueOf(x) + "," + String.valueOf(z);

				str += ":";

				x += 15;
				z += 15;

				str += String.valueOf(x) + "," + String.valueOf(z);
				chunkpointsXZ.add(str);
				str = "";

				x += 1;
				z = default_z;

			}

			x = default_x;
			default_z += 16;
			z = default_z;

		}
		return chunkpointsXZ;
	}

	public static void cuttingChunkList() {
		for (int i = 0; i <= chunkpointsXZ.size() - 1; i++) {

			String chunkString = chunkpointsXZ.get(i);
			String[] chunkParts = chunkString.split(":");

			String chunkFirstPoint = chunkParts[0];
			String chunkSecondPoint = chunkParts[1];

			String[] chunkFirstPointParts = chunkFirstPoint.split(",");
			String[] chunkSecondPointParts = chunkSecondPoint.split(",");

			int firstX = Integer.parseInt(chunkFirstPointParts[0]);
			int firstZ = Integer.parseInt(chunkFirstPointParts[1]);

			chunkFirstPointX.add(firstX);
			chunkFirstPointZ.add(firstZ);

			int secondX = Integer.parseInt(chunkSecondPointParts[0]);
			int secondZ = Integer.parseInt(chunkSecondPointParts[1]);

			chunkSecondPointX.add(secondX);
			chunkSecondPointZ.add(secondZ);
		}
	}

	public static void copyBlockToLocation(Block sourceBlock, Location location) {
		Block targetBlock = location.getBlock();
		//
		targetBlock.setType(sourceBlock.getType());
		BlockState sourceState = sourceBlock.getState();
		BlockState targetState = targetBlock.getState();
		targetState.setType(sourceState.getType());
		targetState.setData(sourceState.getData());
		// TODO Can we not just copy the state over itself, without copying its
		// individual members?
		if (sourceState instanceof InventoryHolder) {
			InventoryHolder sourceHolder = (InventoryHolder) sourceState;
			InventoryHolder targetHolder = (InventoryHolder) targetState;
			targetHolder.getInventory().setContents(sourceHolder.getInventory().getContents());
		}
		//
		if (sourceState instanceof Banner) {
			Banner sourceBanner = (Banner) sourceState;
			Banner targetBanner = (Banner) targetState;
			targetBanner.setBaseColor(sourceBanner.getBaseColor());
			targetBanner.setPatterns(sourceBanner.getPatterns());
		} else if (sourceState instanceof BrewingStand) {
			BrewingStand sourceStand = (BrewingStand) sourceState;
			BrewingStand targetStand = (BrewingStand) targetState;
			targetStand.setBrewingTime(sourceStand.getBrewingTime());
		} else if (sourceState instanceof Chest) {
			Chest sourceChest = (Chest) sourceState;
			Chest targetChest = (Chest) targetState;
			// targetChest.getBlockInventory().setContents(sourceChest.getBlockInventory().getContents());

			ItemStack[] chestInv = sourceChest.getInventory().getContents(); // inventory before

			ItemStack[] toChestInv = new ItemStack[chestInv.length]; // chest content
			try {
				for (int i = 0; i < toChestInv.length; i++) {
					toChestInv[i] = chestInv[i];
				}
			} catch (Exception e) {
				System.out.println("asd1");
			}

			try {
				for (int i = 0; i < chestInv.length; i++) {
					if (toChestInv[i] != null) {
						targetChest.getInventory().addItem(toChestInv[i]);
					}
				}
			} catch (Exception e) {
				System.out.println("asd2");
			}

		} else if (sourceState instanceof CommandBlock) {
			CommandBlock sourceCommandBlock = (CommandBlock) sourceState;
			CommandBlock targetCommandBlock = (CommandBlock) targetState;
			targetCommandBlock.setName(sourceCommandBlock.getName());
			targetCommandBlock.setCommand(sourceCommandBlock.getCommand());
		} else if (sourceState instanceof CreatureSpawner) {
			CreatureSpawner sourceSpawner = (CreatureSpawner) sourceState;
			CreatureSpawner targetSpawner = (CreatureSpawner) targetState;
			targetSpawner.setSpawnedType(sourceSpawner.getSpawnedType());
			targetSpawner.setDelay(sourceSpawner.getDelay());
		} else if (sourceState instanceof Furnace) {
			Furnace sourceFurnace = (Furnace) sourceState;
			Furnace targetFurnace = (Furnace) targetState;
			targetFurnace.setBurnTime(sourceFurnace.getBurnTime());
			targetFurnace.setCookTime(sourceFurnace.getCookTime());
		} else if (sourceState instanceof Jukebox) {
			Jukebox sourceJukebox = (Jukebox) sourceState;
			Jukebox targetJukebox = (Jukebox) targetState;
			targetJukebox.setPlaying(sourceJukebox.getPlaying());
		} else if (sourceState instanceof NoteBlock) {
			NoteBlock sourceNoteBlock = (NoteBlock) sourceState;
			NoteBlock targetNoteBlock = (NoteBlock) targetState;
			targetNoteBlock.setNote(sourceNoteBlock.getNote());
		} else if (sourceState instanceof Sign) {
			Sign sourceSign = (Sign) sourceState;
			Sign targetSign = (Sign) targetState;
			String[] lines = sourceSign.getLines();
			for (int i = 0; i < lines.length; i++) {
				targetSign.setLine(i, lines[i]);
			}
		} else if (sourceState instanceof Skull) {
			Skull sourceSkull = (Skull) sourceState;
			Skull targetSkull = (Skull) targetState;
			targetSkull.setOwner(sourceSkull.getOwner());
			targetSkull.setRotation(sourceSkull.getRotation());
			targetSkull.setSkullType(sourceSkull.getSkullType());
		} else if (sourceState instanceof Torch) {
			Torch sourceTorch = (Torch) sourceState;
			Torch targetTorch = (Torch) targetState;

		}
		//
		targetState.update();
	}

	public static String avaivableOreDirection(int x, int y, int z, Material detectedBlock, String worldName) {
		// Returns string with directions where is stone in one cordinate for instance
		// str = "NSWEAB"

		String str = "";

		int i = 0;
		switch (i) {

		case 0:
			// North
			Material northBlock = Bukkit.getServer().getWorld(worldName).getBlockAt(x, y, z - 1).getType();
			if (northBlock == detectedBlock) {
				str += "N";
			}
		case 1:
			Material southBlock = Bukkit.getServer().getWorld(worldName).getBlockAt(x, y, z + 1).getType();
			if (southBlock == detectedBlock) {
				str += "S";
			}
		case 2:
			Material eastBlock = Bukkit.getServer().getWorld(worldName).getBlockAt(x + 1, y, z).getType();
			if (eastBlock == detectedBlock) {
				str += "E";
			}
		case 3:
			Material westBlock = Bukkit.getServer().getWorld(worldName).getBlockAt(x - 1, y, z).getType();
			if (westBlock == detectedBlock) {
				str += "W";
			}
		case 4:
			Material aboveBlock = Bukkit.getServer().getWorld(worldName).getBlockAt(x, y + 1, z).getType();
			if (aboveBlock == detectedBlock) {
				str += "A";
			}
		case 5:
			Material belowBlock = Bukkit.getServer().getWorld(worldName).getBlockAt(x, y - 1, z).getType();
			if (belowBlock == detectedBlock) {
				str += "B";
			}

		}

		return str;

	}

	public Location rightCord(char dirChar, String worldname, int x, int y, int z) {

		if (dirChar == 'N') {
			z--;
		} else if (dirChar == 'S') {
			z++;
		} else if (dirChar == 'E') {
			x++;
		} else if (dirChar == 'W') {
			x--;
		}
		Location loc = new Location(Bukkit.getServer().getWorld(worldname), x, y, z);
		return loc;
	}

	public void addTorchSupportBlock(int data, Block b, Block defaultB) {
		//if something is against something till will add block to it! not needed in this setup
		int x = b.getX();
		int y = b.getY();
		int z = b.getZ();

		switch (data) {

		case 4:
			z++;
			break;
		case 1:
			x--;
			break;
		case 3:
			z--;
			break;
		case 2:
			x++;
			break;
		}
		Location loc = new Location(b.getWorld(), x, y, z);
		System.out.println(loc);
		copyBlockToLocation(defaultB, loc);
	}
}

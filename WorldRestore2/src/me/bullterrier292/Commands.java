package me.bullterrier292;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
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
	int goingChunks;

	int autoChecker1;
	int autoChecker2;

	int counter1;
	int counter2;
	int counter3;
	int counter4;
	int counterChunk;
	// -288 86 112
	// + +

	int chunkNumber = 0;

	public String cmd1 = "rw";
	public String cmd2 = "rwfixdchunks";
	public String cmd3 = "rwfixsurface";
	public String cmd4 = "rwautodchunks";
	public String cmd5 = "rwautosurface";
	public String cmd6 = "rwtest";
	public String cmd7 = "rwstop";
	public String cmd8 = "wrset";

	// =======================================================================================
	// =======================================================================================
	// =======================================================================================

	public static ArrayList<String> wholeChunkExpired = new ArrayList();
	public static ArrayList<String> surfaceChunksExpired = new ArrayList();

	// =======================================================================================
	// =======================================================================================
	// =======================================================================================
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);

		if (cmd.getName().equalsIgnoreCase(cmd1)) {
			sender.sendMessage("World Restore is working");
			String section = "ChunkPoints.";
			System.out.println(section);
			System.out.println(args[0]);
			if (args.length >0 ) {
				if (p.getConfig().contains(section)) {

					for (String key : p.getConfig().getConfigurationSection(section).getKeys(false)) {
						ConfigurationSection info = p.getConfig().getConfigurationSection(section);

						if (key.equalsIgnoreCase(args[0])) {

							String valuesInfo = info.getString(key);

							String[] valueParts = valuesInfo.split(":");

							String[] chunkCordinateParts = valueParts[0].split(",");
							int x = Integer.parseInt(chunkCordinateParts[0]);
							int z = Integer.parseInt(chunkCordinateParts[1]);

							Chunk c = ((Player) sender).getWorld().getChunkAt(x, z);
							int y = Integer.parseInt(valueParts[1]);
							String DirecionStr = valueParts[2];
							int howManyChunksOnce = Integer.parseInt(valueParts[3]);
							int howManySecondDelay = Integer.parseInt(valueParts[4]);
							int howManyChunks = Integer.parseInt(valueParts[5]);

							goingToNextChunk(c, y, DirecionStr, howManyChunksOnce, howManySecondDelay, howManyChunks);

						}
					}

				}
			} else {
				sender.sendMessage(ChatColor.RED + "/rw name");
			}
			/*
			 * Chunk c = ((Player) sender).getLocation().getChunk(); int howManyChunksOnce =
			 * 2; int howManySecondDelay = 5; int howManyChunks = 3;
			 * 
			 * goingToNextChunk(c, 0, "ES", howManyChunksOnce, howManySecondDelay,
			 * howManyChunks);
			 */
			return true;
		} else if (cmd.getName().equalsIgnoreCase(cmd2)) {
			counter2 = 0;
			int howManyChunksOnce = 4;
			int howManySecondDelay = 5;
			int y = 0;
			Bukkit.getScheduler().cancelTask(task1);

			String sectionName1 = "Main.wholeChunkIds";

			if (p.getConfig().contains(sectionName1)) {

				for (String key : p.getConfig().getConfigurationSection(sectionName1).getKeys(false)) {
					counter2++;
				}

				counter2 = counter2 / howManyChunksOnce + 1;

				sender.sendMessage("starting");
				task1 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
					@Override
					public void run() {

						if (counter2 <= 0) {
							Bukkit.getScheduler().cancelTask(task1);
						}
						fixDetectedChunksToDefault(y, howManyChunksOnce);
						counter2--;

					}

				}, 0, 20 * howManySecondDelay);

			}

			return true;
		} else if (cmd.getName().equalsIgnoreCase(cmd3)) {
			counter3 = 0;
			int howManyChunksOnce = 4;
			int howManySecondDelay = 5;
			int y = 54;
			Bukkit.getScheduler().cancelTask(task2);
			if (p.getConfig().contains("Main.surfaceChunkIds")) {
				for (String key : p.getConfig().getConfigurationSection("Main.surfaceChunkIds").getKeys(false)) {
					counter3++;
				}

				counter3 = counter3 / howManyChunksOnce + 1;

				sender.sendMessage("starting");
				task2 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
					@Override
					public void run() {

						if (counter3 <= 0) {
							Bukkit.getScheduler().cancelTask(task2);
						}
						fixDetectedChunksToDefault(y, howManyChunksOnce);
						counter3--;
						System.out.println(counter3);

					}

				}, 0, 20 * howManySecondDelay);

			}

			return true;
		} else if (cmd.getName().equalsIgnoreCase(cmd4)) {

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
			if (args.length == 5) {
				String chunkName;
				Chunk c = ((Player) sender).getLocation().getChunk();
				int chunkIdPart1 = c.getX();
				int chunkIdPart2 = c.getZ();
				String chunkid = String.valueOf(chunkIdPart1) + ',' + String.valueOf(chunkIdPart2);
				int y = 0;
				String DirecionStr;
				int howManyChunksOnce;
				int howManySecondDelay;
				int howManyChunks;

				p.getConfig().set("ChunkPoints." + args[0],
						chunkid + ":" + y + ":" + args[1] + ":" + args[2] + ":" + args[3] + ":" + args[4]);
				p.saveConfig();
				sender.sendMessage(ChatColor.GREEN
						+ "ChunkPoint has been added to list. Now you can restore that point by /rw name");
			} else {
				sender.sendMessage(ChatColor.RED
						+ "/wrset Name Direction(for example ES) HowManyChunksOnce HowManySecondsDelayBetweenUpdates HowManyChunks(MultiliedItself)");
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase(cmd7)) {
			Bukkit.getScheduler().cancelTask(counterChunk);
			sender.sendMessage(ChatColor.RED + "Restore whole world has been canceled!");
			return true;

		} else if (cmd.getName().equalsIgnoreCase(cmd8)) {
			if (args.length == 5) {

				Chunk c = ((Player) sender).getLocation().getChunk();
				int chunkIdPart1 = c.getX();
				int chunkIdPart2 = c.getZ();
				String chunkid = String.valueOf(chunkIdPart1) + ',' + String.valueOf(chunkIdPart2);
				int y = 0;

				p.getConfig().set("ChunkPoints." + args[0],
						chunkid + ":" + y + ":" + args[1] + ":" + args[2] + ":" + args[3] + ":" + args[4]);
				p.saveConfig();
				sender.sendMessage(ChatColor.GREEN
						+ "ChunkPoint has been added to list. Now you can restore that point by /rw name");
			} else {
				sender.sendMessage(ChatColor.RED
						+ "/wrset Name Direction(for example ES) HowManyChunksOnce HowManySecondsDelayBetweenUpdates HowManyChunks(MultiliedItself)");
			}
			return true;
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

				String[] chunkCordinateParts = valueParts[0].split(",");
				int x = Integer.parseInt(chunkCordinateParts[0]);
				int z = Integer.parseInt(chunkCordinateParts[1]);

				goingThroughChunks("world", x, y, z, chunkHeight, chunksTogo, true);

				count++;

				if (count >= howManyChunksOnce) {
					break;
				}
			}

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

			goingThroughChunks("world", x, y, z, chunkHeight, 1, true);

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

	int chunkX;
	int chunkZ;

	int chunkSwitcher1;
	int chunkSwitcher2;
	int lastChunkX;
	int lastChunkZ;

	public boolean firstISlowerThan(int x, int y) {
		if (x <= y) {
			return true;
		}
		return false;
	}

	public void goingToNextChunk(Chunk c, int y, String DirecionStr, int howManyChunksOnce, int howManySecondDelay,
			int howManyChunks) {
		Plugin plugin = WorldRestore.getPlugin(WorldRestore.class);

		DirecionStr = "ES";
		String worldname = "world";

		chunkX = c.getX();
		chunkZ = c.getZ();
		counterChunk = 1 + (howManyChunks * howManyChunks) / howManyChunksOnce;

		Bukkit.getScheduler().cancelTask(goingChunks);

		int defaultChunkX = chunkX;
		int defaultChunkZ = chunkZ;

		// effected by directionES
		int dirMultiplierX = 1;
		int dirMultiplierZ = 1;

		lastChunkX = chunkX;
		lastChunkX += (howManyChunks * dirMultiplierX);
		lastChunkZ = chunkZ;
		lastChunkZ += +(howManyChunks * dirMultiplierZ);

		boolean firstIslowerX = firstISlowerThan(chunkX, lastChunkX);
		boolean firstIslowerZ = firstISlowerThan(chunkZ, lastChunkZ);

		goingChunks = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {

				for (int i = 0; i < howManyChunksOnce; i++) {

					Chunk chunk = Bukkit.getServer().getWorld(worldname).getChunkAt(chunkX, chunkZ);

					int x = chunk.getBlock(0, 0, 0).getX();
					int z = chunk.getBlock(0, 0, 0).getZ();

					goingThroughChunks(worldname, x, y, z, 256, 1, true);

					if (firstIslowerX) {
						if (chunkX >= lastChunkX) {
							System.out.println(counterChunk);
							System.out.println("Chunkid" + chunkX + "," + chunkZ);
							chunkZ += (1 * dirMultiplierZ);
							chunkX = defaultChunkX;

						} else {
							chunkX += (1 * dirMultiplierX);
						}

					} else {
						if (lastChunkX >= chunkX) {
							chunkZ += (1 * dirMultiplierZ);
							chunkX = defaultChunkZ;

						} else {
							chunkX += (1 * dirMultiplierX);
						}
					}

				}

				if (counterChunk <= 0) {
					Bukkit.getScheduler().cancelTask(goingChunks);
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Chunks has been fixed");
				}

				counterChunk--;

			}

		}, 0, 20 * howManySecondDelay);

	}

	public void addChunksNotFixedConfig(Chunk c) {
		Plugin plugin = WorldRestore.getPlugin(WorldRestore.class);
		int x = c.getBlock(0, 0, 0).getX();
		int z = c.getBlock(0, 0, 0).getZ();
		int chunkIdPart1 = c.getX();
		int chunkIdPart2 = c.getZ();
		String chunkid = ' ' + String.valueOf(chunkIdPart1) + ',' + String.valueOf(chunkIdPart2) + ' ';
		plugin.getConfig().set("Main." + "notFixed." + chunkid, x + "," + z);
		plugin.saveConfig();

	}

	public boolean checkIfchunkHasNoPlayers(Chunk c) {
		Plugin p = WorldRestore.getPlugin(WorldRestore.class);
		String section = "Main.playerQuitLocations";
		int chunk_x = c.getX();
		int chunk_z = c.getZ();
		
		for (Player player : p.getServer().getOnlinePlayers()) {
			Chunk playerChunk = player.getLocation().getChunk();
			if (chunk_x == playerChunk.getX() && chunk_z == playerChunk.getZ()) {

				addChunksNotFixedConfig(c);
				return false;

			}
		}

		if (p.getConfig().contains(section)) {

			for (String key : p.getConfig().getConfigurationSection(section).getKeys(false)) {
				ConfigurationSection quitInfo = p.getConfig().getConfigurationSection(section);

				String valuesInfo = quitInfo.getString(key);

				String[] valueParts = valuesInfo.split(":");

				// double placedTime = Double.parseDouble(valueParts[1]);

				String[] chunkCordinateParts = valueParts[0].split(",");
				int x = Integer.parseInt(chunkCordinateParts[0]);
				int z = Integer.parseInt(chunkCordinateParts[1]);

				if (chunk_x == x && chunk_z == z) {

					addChunksNotFixedConfig(c);
					return false;

				}
			}

			p.saveConfig();
		}
		return true;
	}

	public void goingThroughChunks(String worldname, int x, int y, int z, int chunkHeight, int chunksTogo,
			boolean checkPlayers) {
		Plugin plugin = WorldRestore.getPlugin(WorldRestore.class);

		ArrayList<Block> torchBlocks = new ArrayList<>();
		ArrayList<Block> signBlocks = new ArrayList<>();
		List materialSign = Arrays.asList(Material.WALL_SIGN, Material.SIGN);

		String default_world = "default_world";
		Location chunkloc = new Location(Bukkit.getServer().getWorld(worldname), x, y, z);
		Chunk c = Bukkit.getServer().getWorld(worldname).getChunkAt(chunkloc);

		boolean canGo;
		if (checkPlayers) {

			if (checkIfchunkHasNoPlayers(c)) {

				canGo = true;
			} else {
				System.out.println("player at" + c.getX() + "," + c.getZ());
				canGo = false;
			}
		} else {
			canGo = true;
		}

		int chunkSizex = 16;
		int chunkSizez = 16;

		int default_x = x;
		int default_y = y;
		int default_z = z;

		int chunkSizeX = x + chunkSizex;
		int chunkSizeZ = z + chunkSizez;

		// going thorugh chunks by z
		for (int chunknumber1 = 0; chunknumber1 < chunksTogo; chunknumber1++) {

			// going through chunks by x
			for (int chunknumber2 = 0; chunknumber2 < chunksTogo; chunknumber2++) {
				if (canGo) {
					// going through x axel
					for (; x < chunkSizeX; x++) {

						// going thorugh z axel
						for (; z < chunkSizeZ; z++) {

							// going through y axel
							for (; y < chunkHeight; y++) {
								// Bukkit.getServer().getWorld(worldname).getBlockAt(x, y,
								// z).setType(Material.STONE);

								Material worldBlock = Bukkit.getServer().getWorld(worldname).getBlockAt(x, y, z)
										.getType();
								Material default_worldBlock = Bukkit.getServer().getWorld(default_world)
										.getBlockAt(x, y, z).getType();
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

											str = avaivableOreDirection(x, y, z, Material.CHEST, default_world);
											if (str.length() > 0) {
												Location loc = rightCord(str.charAt(0), worldname, x, y, z);
												Block block1 = Bukkit.getServer().getWorld(worldname).getBlockAt(loc);
												if (block1.getType() != Material.CHEST) {
													block1.setType(Material.CHEST);
												}
											}

										}

										if (b2.getTypeId() > 16) {
											if (b2.getTypeId() == Material.TORCH.getId()
													|| b2.getTypeId() == Material.REDSTONE_TORCH_ON.getId()
													|| b2.getTypeId() == Material.REDSTONE_TORCH_OFF.getId()) {
												torchBlocks.add(b2);

												//

											} else if (materialSign.contains(b2.getType())) {

												signBlocks.add(b2);

											} else {

												Location point1 = new Location(Bukkit.getServer().getWorld(worldname),
														x, y, z);
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
					chunkSizeX = x;

				}

				// one chunk has row has been done => going next

				x = default_x;
				default_z += 16;
				z = default_z;
				y = default_y;

				chunkSizeX = x;
				chunkSizeZ = z;
			}
		}

		if (!torchBlocks.isEmpty()) {
			for (int i = 0; i < torchBlocks.size(); i++) {
				Block db = torchBlocks.get(i);
				Block overworldBlock = Bukkit.getServer().getWorld(worldname).getBlockAt(db.getX(), db.getY(),
						db.getZ());
				overworldBlock.setTypeIdAndData(db.getTypeId(), db.getData(), false);
			}
		}

		if (!signBlocks.isEmpty()) {
			for (int i = 0; i < signBlocks.size(); i++) {
				Block db = signBlocks.get(i);
				Location p = new Location(Bukkit.getServer().getWorld(worldname), db.getX(), db.getY(), db.getZ());
				copyBlockToLocation(db, p);

			}

		}

		int chunkIdPart1 = c.getX();
		int chunkIdPart2 = c.getZ();
		String chunkid = ' ' + String.valueOf(chunkIdPart1) + ',' + String.valueOf(chunkIdPart2) + ' ';
		String section;
		if (y <= 0 && chunkHeight > 130) {
			section = "Main.wholeChunkIds." + chunkid;
			plugin.getConfig().set(section, null);

			section = "Main.surfaceChunkIds." + chunkid;
			plugin.getConfig().set(section, null);
			plugin.saveConfig();

		} else {
			section = "Main.surfaceChunkIds." + chunkid;
			plugin.getConfig().set(section, null);
			plugin.saveConfig();
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

}

package me.bullterrier292;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.CheckedInputStream;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class EventsClass implements Listener {

	public static Plugin plugin = WorldRestore.getPlugin(WorldRestore.class);

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		//Commands.chunkCalculatorStarted();

		Location loc = event.getBlock().getLocation();

		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		chunkLogger(x, y, z);

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		//Commands.chunkCalculatorStarted();

		Location loc = event.getBlock().getLocation();

		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		chunkLogger(x, y, z);

	}

	public static void chunkLogger(int x, int y, int z) {
		
		ArrayList<Integer> firstPointX = Commands.chunkFirstPointX;
		ArrayList<Integer> firstPointZ = Commands.chunkFirstPointZ;

		ArrayList<Integer> secondPointX = Commands.chunkSecondPointX;
		ArrayList<Integer> secondPointZ = Commands.chunkSecondPointZ;

		for (int i = 0; i <= firstPointX.size() - 1; i++) {

			int fpX = firstPointX.get(i);
			int fpZ = firstPointZ.get(i);

			int spX = secondPointX.get(i);
			int spZ = secondPointZ.get(i);

			if (checkInArea(x, z, fpX, fpZ, spX, spZ)) {

				int chunkId = i;
				Calendar cal = Calendar.getInstance();
				Long timeNow = cal.getTimeInMillis();
				
				if (y < 55 || y > 130) {
					System.out.println("y");
					plugin.getConfig().set("Main." + "wholeChunkIds." + chunkId, fpX + "," + fpZ + ":" + timeNow);
					plugin.saveConfig();

					break;
				} else {
					System.out.println("surf");
					plugin.getConfig().set("Main." + "surfaceChunkIds." + chunkId, fpX + "," + fpZ + ":" + timeNow);
					plugin.saveConfig();
					break;
				}
			}

		}

	}

	public static List switcher(int a, int b) {
		// a gonna be lower than b

		List myList = new ArrayList();

		int xx;

		if (a > b) {
			xx = a;
			a = b;
			b = xx;
		}

		myList.add(a);
		myList.add(b);

		return myList;
	}

	public static boolean between(int i, int min, int max) {
		if (i >= min && i <= max)
			return true;
		else
			return false;
	}

	public static boolean checkInArea(int x, int z, int x1, int z1, int x2, int z2) {

		List xxx = switcher(x1, x2);

		List zzz = switcher(z1, z2);

		if (between(x, (int) xxx.get(0), (int) xxx.get(1)) && between(z, (int) zzz.get(0), (int) zzz.get(1))) {
			return true;
		}
		return false;

	}

}

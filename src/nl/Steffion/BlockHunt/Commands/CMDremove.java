package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.W;
import nl.Steffion.BlockHunt.Managers.CommandC;
import nl.Steffion.BlockHunt.Managers.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PlayerM;
import nl.Steffion.BlockHunt.Managers.PlayerM.PermsC;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDremove extends DefaultCMD {

	@Override
	public boolean exectue(Player player, Command cmd, String label,
			String[] args) {
		if (PlayerM.hasPerm(player, PermsC.remove, true)) {
			if (player != null) {
				if (args.length <= 1) {
					MessageM.sendFMessage(player,
							ConfigC.error_notEnoughArguments, true, "syntax-"
									+ CommandC.REMOVE.usage);
				} else {
					for (Arena arena : W.arenaList) {
						if (args[1].equalsIgnoreCase(arena.arenaName)) {
							MessageM.sendFMessage(player,
									ConfigC.normal_removeRemovedArena, true,
									"name-" + args[1]);
							W.arenas.getFile().set(args[1], null);
							for (String sign : W.signs.getFile().getKeys(false)) {
								if (W.signs.getFile().get(sign + ".arenaName")
										.toString().equalsIgnoreCase(args[1])) {
									LocationSerializable signLoc = new LocationSerializable(
											(Location) W.signs.getFile().get(
													sign + ".location"));
									signLoc.getBlock().setType(Material.AIR);
									signLoc.getWorld().playEffect(signLoc,
											Effect.MOBSPAWNER_FLAMES, 0);
									signLoc.getWorld().playSound(signLoc,
											Sound.ENDERDRAGON_WINGS, 1, 1);
									W.signs.getFile().set(sign, null);
								}
							}

							W.arenas.save();
							W.signs.load();
							for (Arena arena2 : W.arenaList) {
								ArenaHandler.stopArena(arena2);
							}
							ArenaHandler.loadArenas();
							return true;
						}
					}

					MessageM.sendFMessage(player, ConfigC.error_noArena, true,
							"name-" + args[1]);
				}
			} else {
				MessageM.sendFMessage(player, ConfigC.error_onlyIngame, true);
			}
		}
		return true;
	}
}
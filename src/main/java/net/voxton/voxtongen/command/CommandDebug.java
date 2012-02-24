package net.voxton.voxtongen.command;

import net.voxton.voxtongen.VoxtonGen;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handler for the /debug command.
 *
 * @author HaxtorMoogle
 */
public class CommandDebug implements CommandExecutor {
//    private final CityWorld plugin;
    public CommandDebug(VoxtonGen plugin) {
//        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender instanceof Player) {
//            Player player = (Player) sender;
//            plugin.setDebugging(player, !plugin.isDebugging(player));
            return true;
        } else {
            return false;
        }
    }

}

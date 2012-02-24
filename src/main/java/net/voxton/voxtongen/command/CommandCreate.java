package net.voxton.voxtongen.command;

import net.voxton.voxtongen.VoxtonGen;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCreate implements CommandExecutor {
    private final VoxtonGen plugin;

    public CommandCreate(VoxtonGen plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("cityworld.command")) {
                player.sendMessage("Loading/creating world... This might take a moment...");
                player.teleport(plugin.getCityWorld().getSpawnLocation());
                return true;
            } else {
                sender.sendMessage("You do not have permission to use this command");
                return false;
            }
        } else {
            sender.sendMessage("This command is only usable by a player");
            return false;
        }
    }

}

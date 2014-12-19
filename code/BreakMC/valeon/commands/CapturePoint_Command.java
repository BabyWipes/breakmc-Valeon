package code.BreakMC.valeon.commands;

import code.BreakMC.valeon.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;

public class CapturePoint_Command implements CommandExecutor
{
    CapturePointManager cpm;
    
    public CapturePoint_Command() {
        super();
        this.cpm = Valeon.getInstance().getCapturePointManager();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("breakmc.cp")) {
            p.sendMessage("§fUnknown command. Type \"/help\" for help.");
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("capturepoint")) {
            if (args.length == 0) {
                p.sendMessage("§b§m-----------§r§b[ §5CapturePoint §b]§m-----------");
                p.sendMessage("§d/cp §7- Shows this page.");
                p.sendMessage("§d/cp info §7- Shows developer debug information.");
                p.sendMessage("§b§m-----------------------------------");
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
                    p.sendMessage("§b§m-----------§r§b[ §5Info §b]§m-----------");
                    p.sendMessage("§dRunning§b: ");
                    p.sendMessage("§dCapture Time§b: " + this.cpm.getCaptureTime());
                    p.sendMessage("§dHolders§b: " + this.cpm.getHolders());
                    p.sendMessage("§b§m----------------------------");
                    return true;
                }
                if (!args[0].equalsIgnoreCase("test")) {
                    return false;
                }
                final Random r = new Random();
                if (r.nextInt(100) >= 0 && r.nextInt(100) <= 24) {
                    Bukkit.broadcastMessage("RED");
                    return true;
                }
                if (r.nextInt(100) >= 24 && r.nextInt(100) <= 49) {
                    Bukkit.broadcastMessage("BLUE");
                    return true;
                }
                if (r.nextInt(100) >= 49 && r.nextInt(100) <= 74) {
                    Bukkit.broadcastMessage("GREEN");
                    return true;
                }
                if (r.nextInt(100) >= 74 && r.nextInt(100) <= 99) {
                    Bukkit.broadcastMessage("YELLOW");
                    return true;
                }
                Bukkit.broadcastMessage("RED");
                return true;
            }
        }
        return false;
    }
}

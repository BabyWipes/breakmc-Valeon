package code.BreakMC.valeon;

import org.bukkit.plugin.java.*;
import code.BreakMC.valeon.commands.*;
import org.bukkit.command.*;
import org.bukkit.*;
import code.BreakMC.valeon.events.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;

public class Valeon extends JavaPlugin
{
    public static Valeon instance;
    public CapturePointManager cpm;
    public boolean isRunning;
    
    public Valeon() {
        super();
        this.isRunning = false;
    }
    
    public void onEnable() {
        Valeon.instance = this;
        this.cpm = new CapturePointManager();
        this.isRunning = true;
        this.getCommand("capturepoint").setExecutor((CommandExecutor)new CapturePoint_Command());
        Bukkit.getPluginManager().registerEvents((Listener)new CaptureEvents(), (Plugin)this);
    }
    
    public static Valeon getInstance() {
        return Valeon.instance;
    }
    
    public CapturePointManager getCapturePointManager() {
        return this.cpm;
    }
}

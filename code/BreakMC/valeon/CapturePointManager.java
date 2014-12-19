package code.BreakMC.valeon;

import org.bukkit.entity.*;
import org.bukkit.*;
import code.BreakMC.valeon.utils.*;
import org.bukkit.potion.*;
import org.hcsoups.hardcore.teams.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;
import java.util.*;

public class CapturePointManager
{
    Valeon instance;
    public int captureTID;
    public int fireworkTID;
    public int captureTime;
    public boolean isCaptured;
    public ArrayList<UUID> cappers;
    public ArrayList<UUID> holders;
    public Location spawnc1;
    public Location spawnc2;
    public Location redc1;
    public Location redc2;
    
    public CapturePointManager() {
        super();
        this.instance = Valeon.getInstance();
        this.captureTID = 0;
        this.fireworkTID = 1;
        this.captureTime = 11;
        this.isCaptured = false;
        this.cappers = new ArrayList<UUID>();
        this.holders = new ArrayList<UUID>();
        this.spawnc1 = new Location(Bukkit.getWorld("world"), -59.5, 256.0, -59.5);
        this.spawnc2 = new Location(Bukkit.getWorld("world"), 60.5, 0.0, 60.5);
        this.redc1 = new Location(Bukkit.getWorld("world"), -497.5, 60.0, -497.5);
        this.redc2 = new Location(Bukkit.getWorld("world"), -501.5, 69.0, -501.5);
    }
    
    public void startCapture(final Player p) {
        Bukkit.getScheduler().cancelTask(this.captureTID);
        this.cappers.add(p.getUniqueId());
        if (TeamManager.getInstance().isOnTeam(p.getName())) {
            final Team t = TeamManager.getInstance().getPlayerTeam(p);
            Bukkit.broadcastMessage("§3Team §b" + t.getName() + "§3's §b" + p.getName() + " §3has begun untying the flag!");
        }
        else {
            Bukkit.broadcastMessage("§b" + p.getName() + " §3has begun untying the flag!");
        }
        this.captureTID = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                if (CapturePointManager.this.captureTime > 0) {
                    final CapturePointManager this$0 = CapturePointManager.this;
                    --this$0.captureTime;
                }
                if (CapturePointManager.this.captureTime <= 10 && CapturePointManager.this.captureTime >= 1) {
                    p.sendMessage("§3Untying §3[§b" + CapturePointManager.convertSecondsToMinutes(CapturePointManager.this.captureTime) + "§3]");
                }
                if (CapturePointManager.this.captureTime == 0) {
                    CapturePointManager.this.isCaptured = true;
                    Bukkit.getScheduler().cancelTask(CapturePointManager.this.captureTID);
                    CapturePointManager.this.cappers.clear();
                    CapturePointManager.this.holders.add(p.getUniqueId());
                    if (TeamManager.getInstance().isOnTeam(p.getName())) {
                        final Team t = TeamManager.getInstance().getPlayerTeam(p);
                        Bukkit.broadcastMessage("§3Team §b" + t.getName() + "§3's §b" + p.getName() + " §3has untyed the flag!\n§b§lStop them before they reach spawn!");
                    }
                    else {
                        Bukkit.broadcastMessage("§b" + p.getName() + " §3has untyed the flag!\n§b§lStop them before they reach spawn!");
                    }
                    final ItemStack flag = ItemUtill.createItem(Material.WOOL, (short)14, 1, "§cRed Flag");
                    p.getInventory().setHelmet(flag);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 0));
                }
            }
        }, 0L, 20L);
    }
    
    public void startFireworks(final Player p) {
        Bukkit.getScheduler().cancelTask(this.fireworkTID);
        this.fireworkTID = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                Player[] onlinePlayers;
                for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
                    final Player all = onlinePlayers[i];
                    CapturePointManager.this.hasFlag(all);
                }
            }
        }, 0L, 200L);
    }
    
    public void stopCapture(final Player p) {
        Bukkit.getScheduler().cancelTask(this.captureTID);
        this.cappers.remove(p.getUniqueId());
        p.sendMessage("§bFailed to untie the flag!");
        this.captureTime = 11;
    }
    
    public Boolean isCapturing(final Player p) {
        if (this.cappers.contains(p.getUniqueId())) {
            return true;
        }
        return false;
    }
    
    public Boolean hasFlag(final Player p) {
        if (this.holders.contains(p.getUniqueId())) {
            return true;
        }
        return false;
    }
    
    public List<UUID> getHolders() {
        return this.holders;
    }
    
    public List<UUID> getCappers() {
        return this.cappers;
    }
    
    public int getCaptureTID() {
        return this.captureTID;
    }
    
    public int getCaptureTime() {
        return this.captureTime;
    }
    
    public Boolean isCaptured() {
        return this.isCaptured;
    }
    
    public boolean isInside(final Location loc, final Location l1, final Location l2) {
        final int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        final int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        final int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        final int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        final int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        final int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        return loc.getX() >= x1 && loc.getX() <= x2 && loc.getY() >= y1 && loc.getY() <= y2 && loc.getZ() >= z1 && loc.getZ() <= z2;
    }
    
    public static String convertSecondsToMinutes(final int time) {
        final int minutes = time / 60;
        final int seconds = time % 60;
        final String disMinu = new StringBuilder().append(minutes).toString();
        final String disSec = String.valueOf((seconds < 10) ? "0" : "") + seconds;
        final String formattedTime = String.valueOf(disMinu) + ":" + disSec;
        return formattedTime;
    }
}

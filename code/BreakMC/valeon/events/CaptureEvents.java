package code.BreakMC.valeon.events;

import code.BreakMC.valeon.*;
import org.bukkit.inventory.*;
import org.hcsoups.hardcore.teams.*;
import java.util.*;
import org.bukkit.event.*;
import java.text.*;
import org.bukkit.*;
import code.BreakMC.valeon.utils.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.event.player.*;

public class CaptureEvents implements Listener
{
    Valeon instance;
    CapturePointManager cpm;
    HashMap<UUID, Integer> hits;
    
    public CaptureEvents() {
        super();
        this.instance = Valeon.getInstance();
        this.cpm = Valeon.getInstance().getCapturePointManager();
        this.hits = new HashMap<UUID, Integer>();
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (this.instance.isRunning) {
            if (this.cpm.hasFlag(p) && this.cpm.isInside(p.getLocation(), this.cpm.spawnc1, this.cpm.spawnc2)) {
                this.cpm.getHolders().remove(p.getUniqueId());
                if (TeamManager.getInstance().isOnTeam(p.getName())) {
                    final Team t = TeamManager.getInstance().getPlayerTeam(p);
                    Bukkit.broadcastMessage("§3§lTeam §b§l" + t.getName() + "§3§l's §b§l" + p.getName() + " §3§lhas captured the §c§lRed §3§lflag!\n§b§lCongratulations!");
                }
                else {
                    Bukkit.broadcastMessage("§b§l" + p.getName() + " §3§lhas captured the §c§lRed §3§lflag!\n§b§lCongratulations!");
                }
                e.getPlayer().getInventory().setHelmet((ItemStack)null);
                e.getPlayer().updateInventory();
                this.cpm.getHolders().remove(e.getPlayer().getUniqueId());
                for (final PotionEffect pe : e.getPlayer().getActivePotionEffects()) {
                    e.getPlayer().removePotionEffect(pe.getType());
                }
            }
            if (this.cpm.isCaptured()) {
                return;
            }
            if (this.cpm.getCappers().size() == 0) {
                if (!this.cpm.getCappers().contains(p.getUniqueId()) && this.cpm.isInside(p.getLocation(), this.cpm.redc1, this.cpm.redc2)) {
                    if (Cooldowns.getCooldown(p, "CapCooldown") > 0L) {
                        return;
                    }
                    this.cpm.startCapture(p);
                }
            }
            else if (this.cpm.getCappers().contains(p.getUniqueId()) && !this.cpm.isInside(p.getLocation(), this.cpm.redc1, this.cpm.redc2)) {
                this.cpm.stopCapture(p);
                Player[] onlinePlayers;
                for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
                    final Player all = onlinePlayers[i];
                    Cooldowns.setCooldown(all, "CapCooldown", 5000L);
                }
            }
        }
    }
    
    @EventHandler
    public void onDamageByEntity(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player v = (Player)e.getEntity();
            if (e.getDamager() instanceof Player) {
                final Player d = (Player)e.getDamager();
                if (this.cpm.isCapturing(v)) {
                    this.cpm.stopCapture(v);
                    Player[] onlinePlayers;
                    for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
                        final Player all = onlinePlayers[i];
                        Cooldowns.setCooldown(all, "CapCooldown", 5000L);
                    }
                }
                else if (this.cpm.hasFlag(v)) {
                    if (this.hits.containsKey(v.getUniqueId())) {
                        if (this.hits.get(v.getUniqueId()) <= 3) {
                            this.hits.put(v.getUniqueId(), this.hits.get(v.getUniqueId()) + 1);
                        }
                        else {
                            this.hits.remove(v.getUniqueId());
                            final String x = new DecimalFormat("######.").format(v.getLocation().getX());
                            final String y = new DecimalFormat("######.").format(v.getLocation().getY());
                            final String z = new DecimalFormat("######.").format(v.getLocation().getZ());
                            Bukkit.broadcastMessage("§3The flag has been dropped at X: §b" + x.replace(".", "") + " §3Y: §b" + y.replace(".", "") + " §3Z:§b " + z.replace(".", "") + "§3!");
                            final ItemStack flagis = ItemUtill.createItem(Material.WOOL, (short)14, 1, "§cRed Flag");
                            final Item flage = v.getWorld().dropItem(v.getLocation(), flagis);
                            flage.setPickupDelay(300);
                            v.getInventory().setHelmet((ItemStack)null);
                            v.updateInventory();
                            this.cpm.getHolders().remove(v.getUniqueId());
                            for (final PotionEffect pe : v.getActivePotionEffects()) {
                                v.removePotionEffect(pe.getType());
                            }
                        }
                    }
                    else {
                        this.hits.put(v.getUniqueId(), 1);
                    }
                }
                else if (this.cpm.hasFlag(d)) {
                    e.setCancelled(true);
                    d.sendMessage("§cYou cannot attack while carrying the flag.");
                }
            }
            if (e.getDamager() instanceof Arrow) {
                if (this.cpm.isCapturing(v)) {
                    this.cpm.stopCapture(v);
                    Player[] onlinePlayers2;
                    for (int length2 = (onlinePlayers2 = Bukkit.getOnlinePlayers()).length, j = 0; j < length2; ++j) {
                        final Player all2 = onlinePlayers2[j];
                        Cooldowns.setCooldown(all2, "CapCooldown", 5000L);
                    }
                }
                else if (this.cpm.hasFlag(v)) {
                    final String x2 = new DecimalFormat("######.").format(v.getLocation().getX());
                    final String y2 = new DecimalFormat("######.").format(v.getLocation().getY());
                    final String z2 = new DecimalFormat("######.").format(v.getLocation().getZ());
                    Bukkit.broadcastMessage("§3The flag has been dropped at X: §b" + x2.replace(".", "") + " §3Y: §b" + y2.replace(".", "") + " §3Z:§b " + z2.replace(".", "") + "§3!");
                    final ItemStack flagis2 = ItemUtill.createItem(Material.WOOL, (short)14, 1, "§cRed Flag");
                    final Item flage2 = v.getWorld().dropItem(v.getLocation(), flagis2);
                    flage2.setPickupDelay(300);
                    v.getInventory().setHelmet((ItemStack)null);
                    v.updateInventory();
                    this.cpm.getHolders().remove(v.getUniqueId());
                    for (final PotionEffect pe2 : v.getActivePotionEffects()) {
                        v.removePotionEffect(pe2.getType());
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        if (this.cpm.hasFlag(e.getEntity())) {
            final String x = new DecimalFormat("######.").format(e.getEntity().getLocation().getX());
            final String y = new DecimalFormat("######.").format(e.getEntity().getLocation().getY());
            final String z = new DecimalFormat("######.").format(e.getEntity().getLocation().getZ());
            Bukkit.broadcastMessage("§3The flag has been dropped at X: §b" + x.replace(".", "") + " §3Y: §b" + y.replace(".", "") + " §3Z:§b " + z.replace(".", "") + "§3!");
            final ItemStack flagis = ItemUtill.createItem(Material.WOOL, (short)14, 1, "§cRed Flag");
            final Item flage = e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), flagis);
            flage.setPickupDelay(300);
            e.getEntity().getInventory().setHelmet((ItemStack)null);
            e.getEntity().updateInventory();
            this.cpm.getHolders().remove(e.getEntity().getUniqueId());
            for (final PotionEffect pe : e.getEntity().getActivePotionEffects()) {
                e.getEntity().removePotionEffect(pe.getType());
            }
        }
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        if (this.cpm.hasFlag(e.getPlayer())) {
            final String x = new DecimalFormat("######.").format(e.getPlayer().getLocation().getX());
            final String y = new DecimalFormat("######.").format(e.getPlayer().getLocation().getY());
            final String z = new DecimalFormat("######.").format(e.getPlayer().getLocation().getZ());
            Bukkit.broadcastMessage("§3The flag has been dropped at X: §b" + x.replace(".", "") + " §3Y: §b" + y.replace(".", "") + " §3Z:§b " + z.replace(".", "") + "§3!");
            final ItemStack flagis = ItemUtill.createItem(Material.WOOL, (short)14, 1, "§cRed Flag");
            final Item flage = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), flagis);
            flage.setPickupDelay(300);
            e.getPlayer().getInventory().setHelmet((ItemStack)null);
            e.getPlayer().updateInventory();
            this.cpm.getHolders().remove(e.getPlayer().getUniqueId());
            for (final PotionEffect pe : e.getPlayer().getActivePotionEffects()) {
                e.getPlayer().removePotionEffect(pe.getType());
            }
        }
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (!this.cpm.hasFlag(p)) {
            return;
        }
        if (e.getSlot() == 39) {
            e.setCancelled(true);
            p.updateInventory();
        }
    }
    
    @EventHandler
    public void onPickup(final PlayerPickupItemEvent e) {
        final Player p = e.getPlayer();
        if (e.getItem() != null && e.getItem().getItemStack().getType() == Material.WOOL && e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().contains("Flag")) {
            if (TeamManager.getInstance().isOnTeam(p.getName())) {
                final Team t = TeamManager.getInstance().getPlayerTeam(p);
                Bukkit.broadcastMessage("§3The flag has been picked up by team §b" + t.getName() + "§3's §b" + p.getName() + "§3!");
            }
            else {
                Bukkit.broadcastMessage("§3The flag has been picked up by §b" + p.getName() + "§3!");
            }
            p.getInventory().setHelmet(e.getItem().getItemStack());
            e.getItem().getItemStack().setType(Material.AIR);
            e.getItem().remove();
            p.updateInventory();
            this.cpm.getHolders().add(p.getUniqueId());
            for (final PotionEffect pe : p.getActivePotionEffects()) {
                p.removePotionEffect(pe.getType());
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 0));
        }
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if (this.cpm.hasFlag(e.getPlayer()) && e.getItem() != null) {
            if (e.getItem().getType() == Material.ENDER_PEARL) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cYou cannot use ender pearls.");
                e.getPlayer().updateInventory();
            }
            else if (e.getItem().getType() == Material.POTION) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cYou cannot use potions.");
                e.getPlayer().updateInventory();
            }
            else if (e.getItem().getType() == Material.BOW) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cYou cannot use bows.");
                e.getPlayer().updateInventory();
            }
        }
    }
}

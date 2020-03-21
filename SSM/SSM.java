package SSM;

import SSM.Kits.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class SSM extends JavaPlugin implements Listener {

    public static HashMap<UUID, Kit> playerKit = new HashMap<UUID, Kit>();
    public static Kit[] allKits;

    public static void main(String[] args) {
        // for testing junk
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        allKits = new Kit[]{
            new KitCreeper(this),
            new KitIronGolem(this),
            new KitSkeleton(this),
            new KitSlime(this),
            new KitSpider(this),
            new KitWitch(this),
            new KitShulker(this)
        };
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kit")) {
            if (!(sender instanceof Player)) {
                return true;
            }
            Player player = (Player) sender;
            if (args.length == 1) {
                for (Kit check : allKits) {
                    if (check.name.equalsIgnoreCase(args[0])) {
                        Kit kit = null;
                        try {
                            kit = check.getClass().getDeclaredConstructor(Plugin.class).newInstance(this);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        if (kit == null) {
                            return true;
                        }
                        kit.equipKit(player);
                        playerKit.put(player.getUniqueId(), kit);
                        return true;
                    }
                }
            }
            String finalMessage = "Kit Choices: ";
            for (Kit kit : allKits) {
                finalMessage += kit.getName() + " ";
            }
            player.sendMessage(finalMessage);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Block blockIn = e.getTo().getBlock();
        Block blockOn = e.getFrom().getBlock().getRelative(BlockFace.DOWN);
        if (blockOn.getType() == Material.GOLD_BLOCK) {
            Double x = player.getLocation().getDirection().getX() * 1.2;
            Double z = player.getLocation().getDirection().getZ() * 1.2;
            player.setVelocity(new Vector(x, 1.2, z));
        }
        if (blockOn.getType() == Material.IRON_BLOCK){
            Location loc = player.getLocation();
            Vector dir = loc.getDirection();
            dir.normalize();
            dir.multiply(10); //5 blocks a way
            loc.add(dir);
            player.teleport(loc);
        }
        if (blockIn.isLiquid()) {
            player.setHealth(0.0);
        }
    }

    @EventHandler
    public void stopHealthRegen(EntityRegainHealthEvent e) {
        if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void stopHungerLoss(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String name = player.getDisplayName();
        e.setQuitMessage(ChatColor.YELLOW + name + " has fucking rage quit, what a fucking bitch LOL");
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity NPC = e.getRightClicked();
        if (NPC == null) {
            return;
        }
        if (NPC.getCustomName().equalsIgnoreCase("Alchemist")) {
            int potion = (int) (Math.random() * 10) + 1;
            switch (potion) {
                case 1:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2));
                    break;
                case 2:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 2));
                    break;
                case 3:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 2));
                    break;
                case 4:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 2));
                    break;
                case 5:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 2));
                    break;
                case 6:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 2));
                    break;
                case 7:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 2));
                    break;
                case 8:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 2));
                    break;
                case 9:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 20));
                    break;
                case 10:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 20));
                    break;
            }
            NPC.remove();
        }
    }

}












package com.i0dev.Reclaim.utility;

import com.i0dev.Reclaim.objects.ColorConfigItem;
import com.i0dev.Reclaim.objects.ConfigItem;
import com.i0dev.Reclaim.objects.Cuboid;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utility {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(List<String> ss) {
        List<String> ret = new ArrayList<>();
        ss.forEach(s -> ret.add(color(s)));
        return ret;
    }

    public static ItemStack makeItemStackManual(Material material, int amount, short data, String name, List<String> lore, boolean glow) {
        if (lore == null)
            lore = new ArrayList<>();
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore(color(lore));
        if (glow) meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    @SafeVarargs
    public static String pair(String msg, MsgUtil.Pair<String, String>... pairs) {
        for (MsgUtil.Pair<String, String> pair : pairs) {
            msg = msg.replace(pair.getKey(), pair.getValue());
        }
        return msg;
    }

    @SafeVarargs
    public static ItemStack makeItemStackFromConfigItem(ConfigItem c, MsgUtil.Pair<String, String>... pairs) {
        List<String> lore = new ArrayList<>();
        c.lore.forEach(s -> lore.add(pair(s, pairs)));
        String displayName = pair(c.displayName, pairs);
        ItemStack item = makeItemStackManual(Material.getMaterial(c.material), c.amount, c.data, displayName, lore, c.glow);
        if (c instanceof ColorConfigItem) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            ColorConfigItem cc = (ColorConfigItem) c;
            meta.setColor(Color.fromRGB(cc.red, cc.blue, cc.green));
            item.setItemMeta(meta);
        }
        return item;
    }


    public static Integer parseIntOrNull(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void sendActionBarTextToPlayer(Player player, String message) {
        try {
            message = color(message);
            Object entity = getOBCClass("entity.CraftPlayer").cast(player);
            Object handle = entity.getClass().getMethod("getHandle").invoke(entity);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            Object chatText = getNMSClass("ChatComponentText").getConstructor(String.class).newInstance(message);
            Object packet;
            try {
                packet = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class).newInstance(chatText, (byte) 2);
            } catch (Exception ex2) {
                packet = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), getNMSClass("ChatMessageType")).newInstance(chatText, Enum.valueOf((Class<? extends Enum>) getNMSClass("ChatMessageType"), "GAME_INFO"));
            }
            connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> getNMSClass(String name, String def) {
        return getNMSClass(name) != null ? getNMSClass(name) : getNMSClass(def.split("\\.")[0]).getDeclaredClasses()[0];
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static ItemStack[] deserializeBase64ToItems(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) items[i] = (ItemStack) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
    }

    public static String formatTimePeriod(long timePeriod) {
        long millis = timePeriod;

        String output = "";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        if (days > 1) output += days + " days ";
        else if (days == 1) output += days + " day ";

        if (hours > 1) output += hours + " hours ";
        else if (hours == 1) output += hours + " hour ";

        if (minutes > 1) output += minutes + " minutes ";
        else if (minutes == 1) output += minutes + " minute ";

        if (seconds > 1) output += seconds + " seconds ";
        else if (seconds == 1) output += seconds + " second ";

        if (output.isEmpty()) return "0 seconds";

        return StringUtils.trim(output);
    }

    public static String serializeItemsToBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isLocationWithinCuboid(Cuboid cuboid, org.bukkit.Location loc) {
        if (!loc.getWorld().getName().equalsIgnoreCase(cuboid.getWorld())) return false;
        if (loc.getBlockX() < cuboid.getXMin()) return false;
        if (loc.getBlockX() > cuboid.getXMax()) return false;
        if (loc.getBlockY() < cuboid.getYMin()) return false;
        if (loc.getBlockY() > cuboid.getYMax()) return false;
        if (loc.getBlockZ() < cuboid.getZMin()) return false;
        if (loc.getBlockZ() > cuboid.getZMax()) return false;
        return true;
    }

}

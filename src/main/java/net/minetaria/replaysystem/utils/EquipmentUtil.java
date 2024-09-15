package net.minetaria.replaysystem.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EquipmentUtil {

    public static ItemStack getAnyHelmetFromItemStackArray(ItemStack[] itemStacks) {
        for (ItemStack item : itemStacks) {
            if (item != null && isHelmet(item)) {
                return item;
            }
        }
        return new ItemStack(Material.AIR); // Kein Helm gefunden
    }

    public static ItemStack getAnyChestplateFromItemStackArray(ItemStack[] itemStacks) {
        for (ItemStack item : itemStacks) {
            if (item != null && isChestplate(item)) {
                return item;
            }
        }
        return new ItemStack(Material.AIR); // Keine Brustplatte gefunden
    }

    public static ItemStack getAnyLeggingsFromItemStackArray(ItemStack[] itemStacks) {
        for (ItemStack item : itemStacks) {
            if (item != null && isLeggings(item)) {
                return item;
            }
        }
        return new ItemStack(Material.AIR); // Keine Hose gefunden
    }

    public static ItemStack getAnyBootsFromItemStackArray(ItemStack[] itemStacks) {
        for (ItemStack item : itemStacks) {
            if (item != null && isBoots(item)) {
                return item;
            }
        }
        return new ItemStack(Material.AIR); // Keine Schuhe gefunden
    }

    private static boolean isHelmet(ItemStack item) {
        Material type = item.getType();
        return type == Material.LEATHER_HELMET ||
                type == Material.IRON_HELMET ||
                type == Material.CHAINMAIL_HELMET ||
                type == Material.DIAMOND_HELMET ||
                type == Material.GOLDEN_HELMET ||
                type == Material.NETHERITE_HELMET ||
                type == Material.TURTLE_HELMET;
    }

    private static boolean isChestplate(ItemStack item) {
        Material type = item.getType();
        return type == Material.LEATHER_CHESTPLATE ||
                type == Material.IRON_CHESTPLATE ||
                type == Material.CHAINMAIL_CHESTPLATE ||
                type == Material.DIAMOND_CHESTPLATE ||
                type == Material.GOLDEN_CHESTPLATE ||
                type == Material.NETHERITE_CHESTPLATE;
    }

    private static boolean isLeggings(ItemStack item) {
        Material type = item.getType();
        return type == Material.LEATHER_LEGGINGS ||
                type == Material.IRON_LEGGINGS ||
                type == Material.CHAINMAIL_LEGGINGS ||
                type == Material.DIAMOND_LEGGINGS ||
                type == Material.GOLDEN_LEGGINGS ||
                type == Material.NETHERITE_LEGGINGS;
    }

    private static boolean isBoots(ItemStack item) {
        Material type = item.getType();
        return type == Material.LEATHER_BOOTS ||
                type == Material.IRON_BOOTS ||
                type == Material.CHAINMAIL_BOOTS ||
                type == Material.DIAMOND_BOOTS ||
                type == Material.GOLDEN_BOOTS ||
                type == Material.NETHERITE_BOOTS;
    }
}

package net.minetaria.replaysystem.utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtil implements Serializable {

	private ItemStack item;
	private ItemMeta itemMeta;

	public ItemUtil(Material material, short subID) {
		item = new ItemStack(material, 1, subID);
		itemMeta = item.getItemMeta();
	}

	public ItemUtil(Material material) {
		this(material, (short)0);
	}

	public ItemUtil setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}

	public ItemUtil setLore(String[] lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}

	public ItemUtil addLore(String lore) {
		List<String> currentLore = new ArrayList<>();
		if (itemMeta.hasLore()) {
			if (itemMeta.getLore() != null) {
				currentLore = itemMeta.getLore();
			}
		}
		currentLore.add(lore);
		itemMeta.setLore(currentLore);
		return this;
	}

	public ItemUtil setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemUtil makeUndestroyable() {
		itemMeta.setUnbreakable(true);
		return this;
	}

	public ItemUtil setGlow() {
		itemMeta.addEnchant(Enchantment.POWER, 1, true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}

	public ItemUtil addEnchantment(Enchantment enchantment, int level) {
		itemMeta.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemStack build() {
		item.setItemMeta(itemMeta);
		return item;
	}
}

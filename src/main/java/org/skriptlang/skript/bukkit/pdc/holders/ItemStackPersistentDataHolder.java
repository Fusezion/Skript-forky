package org.skriptlang.skript.bukkit.pdc.holders;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class ItemStackPersistentDataHolder implements CustomPersistentDataHolder {

	private final ItemStack itemStack;

	public ItemStackPersistentDataHolder(@NotNull ItemStack itemStack) {
		Preconditions.checkNotNull(itemStack.getItemMeta(), "ItemStack must be able to store item meta");
		this.itemStack = itemStack;
	}

	@Override
	public @NotNull PersistentDataContainer getPersistentDataContainer() {
		return itemStack.getItemMeta().getPersistentDataContainer();
	}

	@Override
	public void setPersistentDataContainer(@NotNull PersistentDataContainer persistentDataContainer) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		assert itemMeta != null;
		persistentDataContainer.copyTo(itemMeta.getPersistentDataContainer(), true);
		itemStack.setItemMeta(itemMeta);
	}

}

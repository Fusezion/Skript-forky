package org.skriptlang.skript.bukkit.pdc.holders;

import ch.njol.skript.util.slot.Slot;
import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class SlotPersistentDataHolder implements CustomPersistentDataHolder {

	private final Slot slot;

	public SlotPersistentDataHolder(@NotNull Slot slot) {
		Preconditions.checkNotNull(slot.getItem(), "Slot must have a backend item stored");
		Preconditions.checkNotNull(slot.getItem().getItemMeta(), "Slot item must be able to store item meta");
		this.slot = slot;
	}

	@Override
	public @NotNull PersistentDataContainer getPersistentDataContainer() {
		assert slot.getItem() != null;
		return slot.getItem().getItemMeta().getPersistentDataContainer();
	}

	@Override
	public void setPersistentDataContainer(@NotNull PersistentDataContainer persistentDataContainer) {
		ItemStack itemStack = slot.getItem();
		assert itemStack != null;
		ItemMeta itemMeta = itemStack.getItemMeta();
		assert itemMeta != null;
		persistentDataContainer.copyTo(itemMeta.getPersistentDataContainer(), true);
		itemStack.setItemMeta(itemMeta);
		slot.setItem(itemStack);
	}

}

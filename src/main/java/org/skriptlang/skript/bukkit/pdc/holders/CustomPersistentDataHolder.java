package org.skriptlang.skript.bukkit.pdc.holders;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.util.slot.Slot;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for quick instance checks when needing to update the persistent data container of specific classes.
 * These classes include {@linkplain ItemType}, {@linkplain ItemStack}, {@linkplain Slot}, and {@linkplain Block}
 */
public interface CustomPersistentDataHolder extends PersistentDataHolder {

	public void setPersistentDataContainer(@NotNull PersistentDataContainer persistentDataContainer);

}

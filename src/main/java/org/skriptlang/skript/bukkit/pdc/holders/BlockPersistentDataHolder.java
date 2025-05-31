package org.skriptlang.skript.bukkit.pdc.holders;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class BlockPersistentDataHolder implements CustomPersistentDataHolder {

	private final Block block;

	public BlockPersistentDataHolder(@NotNull Block block) {
		Preconditions.checkArgument(block.getState() instanceof TileState, "Block state must implement TileState");
		this.block = block;
	}

	@Override
	public @NotNull PersistentDataContainer getPersistentDataContainer() {
		return ((TileState) block.getState()).getPersistentDataContainer();
	}

	public void setPersistentDataContainer(@NotNull PersistentDataContainer persistentDataContainer) {
		TileState holder = ((TileState) block.getState());
		persistentDataContainer.copyTo(holder.getPersistentDataContainer(), true);
		holder.update();
	}

}

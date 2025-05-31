package org.skriptlang.skript.bukkit.pdc;

import ch.njol.skript.Skript;

public class PDCUtils {

	public static final boolean HAS_VIEW_HOLDER = Skript.classExists("io.papermc.paper.persistence.PersistentDataViewHolder");
	public static final boolean HAS_VIEW_CONTAINER = Skript.classExists("io.papermc.paper.persistence.PersistentDataContainerView");

}

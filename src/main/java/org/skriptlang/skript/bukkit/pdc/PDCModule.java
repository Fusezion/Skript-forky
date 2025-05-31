package org.skriptlang.skript.bukkit.pdc;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import org.bukkit.persistence.PersistentDataHolder;

import java.io.IOException;
import java.util.regex.Pattern;

public class PDCModule {

	// Paper changed the max NamespacedKey early into 1.20.X, I was unable to find a class that existed or which point in time it was changed,
	// In order to still properly support older versions, we need to check if a paper class exists and if the version is 1.20.X or higher
	private static final boolean CLEAN_CHECK = Skript.classExists("io.papermc.paper.entity.Shearable") && Skript.isRunningMinecraft(1,20,6);
	protected static final Pattern SAFE_NAMESPACED_KEY_PATTERN = Pattern.compile("([a-z0-9._-]+:)?[/a-z0-9._-]+");
	protected static final short MAX_KEY_LENGTH = CLEAN_CHECK ? Short.MAX_VALUE : 255;

	public static void load() throws IOException {
		Skript.getAddonInstance().loadClasses("org.skriptlang.skript.bukkit.pdc", "elements");

		ClassInfo<?> holderClassInfo = new ClassInfo<>(PersistentDataHolder.class, "persistentdataholder");
		if (Skript.classExists("io.papermc.paper.persistence.PersistentDataViewHolder"))
			holderClassInfo = new ClassInfo<>(PersistentDataHolder.class, "persistentdataholder");

		Classes.registerClass(holderClassInfo
			.user("persistent ?data holders?")
			.name("Persistent Data Holder")
			.description("""
				Represents an object that can store persistent data.
				While offline players cna hold persistent data is only readable and cannot be not changed.
				Support for reading offline player persistent data requires a PaperMC server.
				""")
			.requiredPlugins("PaperMC (reading offlineplayer data)")
			.examples("""
				set persistent data value "favoriteColor" of player to random color out of colors
				set persistent string list tag "favoriteWords" of player to "geiger counter", "uranium", "radiation"
				""")
		);
	}

}

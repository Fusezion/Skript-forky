package ch.njol.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventConverter;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.slot.Slot;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ch.njol.skript.util.slot.EquipmentSlot.*;

public class EvtEntityShootBow extends SkriptEvent {

	static {
		Skript.registerEvent("On Entity Shoot Bow", EvtEntityShootBow.class, EntityShootBowEvent.class,
			"%entitydata% shoot[ing] (bow|projectile)")
			.description("""
				Called when an entity shoots a bow.
				event-entity refers to the shot projectile/entity.
				""")
			.examples("""
				on player shoot bow:
					chance of 30%:
						damage event-slot by 10
						send "Your bow has taken increased damage!" to shooter
				
				on stray shooting bow:
					set {_e} to event-entity
					spawn a cow at {_e}:
						set velocity of entity to velocity of {_e}
					set event-entity to last spawned entity
				""")
			.since("INSERT VERSION");

		EventValues.registerEventValue(EntityShootBowEvent.class, ItemStack.class, EntityShootBowEvent::getBow);

		EventValues.registerEventValue(EntityShootBowEvent.class, Entity.class, new EventConverter<>() {
			@Override
			public void set(EntityShootBowEvent event, @Nullable Entity entity) {
				if (entity == null)
					return;
				event.setProjectile(entity);
			}

			@Override
			public @NotNull Entity convert(EntityShootBowEvent from) {
				return from.getProjectile();
			}
		});

		EventValues.registerEventValue(EntityShootBowEvent.class, Slot.class, event -> {
			EntityEquipment equipment = event.getEntity().getEquipment();
			EquipSlot equipmentSlot = event.getHand() == EquipmentSlot.OFF_HAND ? EquipSlot.OFF_HAND : EquipSlot.TOOL;
			return new ch.njol.skript.util.slot.EquipmentSlot(equipment, equipmentSlot);
		});

	}

	private Literal<EntityData<?>> entityData;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
		//noinspection unchecked
		entityData = (Literal<EntityData<?>>) args[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof EntityShootBowEvent shootBowEvent))
			return false;
		EntityData<?> entityData = this.entityData.getSingle();
		if (entityData == null) return false;
		return entityData.isInstance(shootBowEvent.getEntity());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return this.entityData.toString(event, debug) + " shoot bow";
	}

}

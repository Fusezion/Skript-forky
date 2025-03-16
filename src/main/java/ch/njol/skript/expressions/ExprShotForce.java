package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.EventRestrictedSyntax;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.Nullable;

@Name("Shot Force")
@Description("Returns the force at which a projectile was shot within an entity shoot bow event.")
@Example("""
	on entity shoot projectile:
		set the velocity of shooter to vector(0,1,0) * shot force
	""")
@Since("INSERT VERSION")
public class ExprShotForce extends SimpleExpression<Float> implements EventRestrictedSyntax {

	static {
		Skript.registerExpression(ExprShotForce.class, Float.class, ExpressionType.SIMPLE,
			"[the] shot force");
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		return true;
	}

	@Override
	protected Float @Nullable [] get(Event event) {
		if (!(event instanceof EntityShootBowEvent shotBowEvent))
			return null;
		return new Float[]{shotBowEvent.getForce()};
	}

	@Override
	public Class<? extends Event>[] supportedEvents() {
		return CollectionUtils.array(EntityShootBowEvent.class);
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Float> getReturnType() {
		return Float.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "shot force";
	}
}

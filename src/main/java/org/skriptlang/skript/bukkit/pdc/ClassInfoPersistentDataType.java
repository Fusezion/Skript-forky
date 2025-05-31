package org.skriptlang.skript.bukkit.pdc;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.classes.YggdrasilSerializer;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

/**
 * This class is a wrapper for a {@link ClassInfo}'s serializer, following a {@link PersistentDataContainer} based serialization.
 * Values within this class are thrown through their respective {@linkplain ClassInfo} serializer.
 * </p>
 * The format of a container depends on the {@link Fields} from the class serializer.
 * Some may classes use the {@link YggdrasilSerializer} which forces a hexadecimal string format.
 * {@code
 * <p>
 * }
 */
public class ClassInfoPersistentDataType<Type> implements PersistentDataType<PersistentDataContainer, Type> {

    private final ClassInfo<Type> classInfo;

    public ClassInfoPersistentDataType(ClassInfo<Type> classInfo) {
        this.classInfo = classInfo;
    }

    public ClassInfo<Type> getClassInfo() {
        return classInfo;
    }

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Type> getComplexType() {
        return classInfo.getC();
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Type complex, @NotNull PersistentDataAdapterContext context) {
        //noinspection unchecked
        Serializer<? super Type> serializer = (Serializer<? super Type>) Classes.getSerializer(classInfo);
        assert serializer != null;
		PersistentDataContainer serializedData = context.newPersistentDataContainer();
        try {
            Fields fields = serializer.serialize(complex);
            fields.forEach(field -> {
                NamespacedKey namespacedKey = NamespacedKey.fromString(field.getID(), Skript.getInstance());
				assert namespacedKey != null;
				Object fieldValue;
				try {
					fieldValue = field.isPrimitive() ? field.getPrimitive() : field.getObject();
				} catch (StreamCorruptedException e) {
					throw new RuntimeException(e);
				}
				assert fieldValue != null;
				//noinspection rawtypes
                PersistentDataType persistentDataType = getExpectedDataType(fieldValue);
                //noinspection unchecked
                serializedData.set(namespacedKey, persistentDataType, persistentDataType.getComplexType().cast(fieldValue));
            });
        } catch (NotSerializableException e) {
            throw new RuntimeException(e);
        }
		return serializedData;
    }

    @Override
    public @NotNull Type fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
		//noinspection unchecked
		Serializer<? super Type> serializer = (Serializer<? super Type>) Classes.getSerializer(classInfo);
        return null;
    }

	private PersistentDataType<?, ?> getExpectedDataType(Object object) {
		boolean isArray = object.getClass().isArray();
		//noinspection IfCanBeSwitch - Java 21 support only
		if (object instanceof String)
			return isArray ? PersistentDataType.LIST.strings() : PersistentDataType.STRING;
		else if (object instanceof Boolean)
			return isArray ? PersistentDataType.LIST.booleans() : PersistentDataType.BOOLEAN;
		else if (object instanceof Byte)
			return isArray ? PersistentDataType.LIST.bytes() : PersistentDataType.BYTE;
		else if (object instanceof Double)
			return isArray ? PersistentDataType.LIST.doubles() : PersistentDataType.DOUBLE;
		else if (object instanceof Float)
			return isArray ? PersistentDataType.LIST.floats() : PersistentDataType.FLOAT;
		else if (object instanceof Integer)
			return isArray ? PersistentDataType.LIST.integers() : PersistentDataType.INTEGER;
		else if (object instanceof Long)
			return isArray ? PersistentDataType.LIST.longs() : PersistentDataType.LONG;
		else if (object instanceof Short)
			return isArray ? PersistentDataType.LIST.shorts() : PersistentDataType.SHORT;
		else if (object instanceof PersistentDataContainer)
			return isArray ? PersistentDataType.LIST.dataContainers() : PersistentDataType.TAG_CONTAINER;
		throw new IllegalArgumentException("Unhandled persistent data type: " + object.getClass().getName());
	}
}

package org.skriptlang.skript.bukkit.pdc;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.variables.SerializedVariable;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A custom {@link PersistentDataType} that serializes and deserializes a {@link ClassInfo} type into a byte array.
 * Most of the backend of this class was taken from https://github.com/SkriptLang/Skript/pull/2837
 */
public class ByteSerializationPersistentDataType implements PersistentDataType<byte[], SerializedVariable.Value> {

	private static final int INT_LENGTH = Integer.BYTES;
	private static final Charset SERIALIZED_CHARSET = StandardCharsets.UTF_8;

	@Override
	public @NotNull Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public @NotNull Class<SerializedVariable.Value> getComplexType() {
		return SerializedVariable.Value.class;
	}

	@Override
	public byte @NotNull [] toPrimitive(@NotNull SerializedVariable.Value complex, @NotNull PersistentDataAdapterContext context) {
		byte[] type = complex.type.getBytes(SERIALIZED_CHARSET);
		ByteBuffer byteBuffer = ByteBuffer.allocate(INT_LENGTH + type.length + complex.data.length);
		byteBuffer.putInt(type.length);
		byteBuffer.put(type);
		byteBuffer.put(complex.data);

		return byteBuffer.array();
	}

	@Override
	public @NotNull SerializedVariable.Value fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {

		ByteBuffer byteBuffer = ByteBuffer.wrap(primitive);

		int typeLength = byteBuffer.getInt();
		byte[] typeBytes = new byte[typeLength];
		byteBuffer.get(typeBytes, 0, typeLength);
		String type = new String(typeBytes, SERIALIZED_CHARSET);
		byte[] data = new byte[byteBuffer.remaining()];
		byteBuffer.get(data);

		return new SerializedVariable.Value(type, data);
	}

}

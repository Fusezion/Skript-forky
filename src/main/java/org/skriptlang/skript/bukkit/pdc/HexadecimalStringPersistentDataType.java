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
 * A custom {@link PersistentDataType} that serializes and deserializes a {@link ClassInfo} type into a hexadecimal string.
 * @apiNote This class is very similar to the {@link ByteSerializationPersistentDataType} class, most of the logic is repeated.
 */
public class HexadecimalStringPersistentDataType implements PersistentDataType<String, SerializedVariable.Value> {

	private static final int HEXADECIMAL_RADIX = 16;
	private static final int INT_LENGTH = Integer.BYTES;
	private static final Charset SERIALIZED_CHARSET = StandardCharsets.UTF_8;

	@Override
	public @NotNull Class<String> getPrimitiveType() {
		return String.class;
	}

	@Override
	public @NotNull Class<SerializedVariable.Value> getComplexType() {
		return SerializedVariable.Value.class;
	}

	@Override
	public @NotNull String toPrimitive(SerializedVariable.@NotNull Value complex, @NotNull PersistentDataAdapterContext context) {
		byte[] type = complex.type.getBytes(SERIALIZED_CHARSET);
		ByteBuffer byteBuffer = ByteBuffer.allocate(INT_LENGTH + type.length + complex.data.length);
		byteBuffer.putInt(type.length);
		byteBuffer.put(type);
		byteBuffer.put(complex.data);

		StringBuilder hexadecimal = new StringBuilder();
		for (byte byteVal : byteBuffer.array()) {
			hexadecimal.append(String.format("%02X", byteVal));
		}

		return hexadecimal.toString();
	}

	@Override
	public @NotNull SerializedVariable.Value fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
		int length = primitive.length();
		byte[] result = new byte[length / 2];

		for (int i = 0; i < length; i += 2) {
			result[i / 2] = (byte) Integer.parseInt(primitive.substring(i, i + 2), HEXADECIMAL_RADIX);
		}

		ByteBuffer byteBuffer = ByteBuffer.wrap(result);
		int intLength = byteBuffer.getInt();
		byte[] typeBytes = new byte[intLength];
		byteBuffer.get(typeBytes, 0, intLength);
		String type = new String(typeBytes, SERIALIZED_CHARSET);
		byte[] data = new byte[byteBuffer.remaining()];
		byteBuffer.get(data);
		return new SerializedVariable.Value(type, data);
	}
}

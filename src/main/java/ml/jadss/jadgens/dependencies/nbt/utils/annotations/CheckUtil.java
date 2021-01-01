package ml.jadss.jadgens.dependencies.nbt.utils.annotations;

import java.lang.reflect.Method;

import ml.jadss.jadgens.dependencies.nbt.NbtApiException;
import ml.jadss.jadgens.dependencies.nbt.utils.MinecraftVersion;

public class CheckUtil {

	public static boolean isAvaliable(Method method) {
		if(MinecraftVersion.getVersion().getVersionId() < method.getAnnotation(AvaliableSince.class).version().getVersionId())
			throw new NbtApiException("The Method '" + method.getName() + "' is only avaliable for the Versions " + method.getAnnotation(AvaliableSince.class).version() + "+, but still got called!");
		return true;
	}
	
}

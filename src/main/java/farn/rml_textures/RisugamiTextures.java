package farn.rml_textures;

import net.minecraft.client.Minecraft;
import net.ornithemc.osl.lifecycle.api.MinecraftEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class RisugamiTextures implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("Risugami Textures");

	public static Minecraft mc;

	@Override
	public void init() {
		LOGGER.info("initializing risugami");
		MinecraftEvents.READY.register(minecraft -> {
			mc = minecraft;
		});
	}
}

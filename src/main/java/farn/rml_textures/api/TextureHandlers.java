package farn.rml_textures.api;

import farn.rml_textures.RisugamiTextures;
import net.minecraft.client.render.texture.TextureManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;

public class TextureHandlers {

	private static final boolean[] usedItemSprites = new boolean[256];
	private static final boolean[] usedTerrainSprites = new boolean[256];
	private static int terrainSpriteIndex = 0;
	private static int itemSpriteIndex = 0;
	private static final Map<Integer, Map<String, Integer>> overrides = new HashMap<>();
	public static boolean texturesOverridden = false;

	static {
		String usedItemSpritesString = "1111111111111011111111111111001111111111111110011111111111110001111111111111110011111001111000001111100110000000111110000000000011111001100000000000000100000000000000010000000000000000000000000000000000000000000000000000000000000000000000001100000000000000";
		String usedTerrainSpritesString = "1111111111111111111111111111110111111111111111011111111111111100111111111111000011111111111111111111111111000000100100111000000010000000000000000000000000000000000000000000000000000000000000000000000000000111000000000000001100000000000001111111111111000011";

		for(int e = 0; e < 256; ++e) {
			usedItemSprites[e] = usedItemSpritesString.charAt(e) == 49;
			usedTerrainSprites[e] = usedTerrainSpritesString.charAt(e) == 49;
		}
	}

	private static int getUniqueItemSpriteIndex() {
		while(itemSpriteIndex < usedItemSprites.length) {
			if(!usedItemSprites[itemSpriteIndex]) {
				usedItemSprites[itemSpriteIndex] = true;
				return itemSpriteIndex++;
			}

			++itemSpriteIndex;
		}

		throw new RuntimeException("No more empty item sprite indices left!");
	}

	private static int getUniqueSpriteIndex(String path) {
		if(path.equals("/gui/items.png")) {
			return getUniqueItemSpriteIndex();
		} else if(path.equals("/terrain.png")) {
			return getUniqueTerrainSpriteIndex();
		} else {
			throw new RuntimeException("No registry for this texture: " + path);
		}
	}

	public static int addOverride(String path, String overlayPath) {
		int type;
		if(overlayPath.equals("/terrain.png")) {
			type = 0;
		} else if(overlayPath.equals("/gui/items.png")) {
			type = 1;
		} else {
			throw new RuntimeException("No registry for this texture: " + overlayPath);
		}

		Map<String, Integer> overlays = overrides.get(type);
		if(overlays == null) {
			overlays = new HashMap<>();
			overrides.put(type, overlays);
		}

		int textureIds = getUniqueSpriteIndex(overlayPath);
		overlays.put(path, textureIds);
		RisugamiTextures.LOGGER.info("Override " + overlayPath + " slot " + textureIds + " with " + overlayPath);
		return textureIds;
	}

	private static int getUniqueTerrainSpriteIndex() {
		while(terrainSpriteIndex < usedTerrainSprites.length) {
			if(!usedTerrainSprites[terrainSpriteIndex]) {
				usedTerrainSprites[terrainSpriteIndex] = true;
				return terrainSpriteIndex++;
			}

			++terrainSpriteIndex;
		}

		throw new RuntimeException("No more empty terrain sprite indices left!");
	}

	public static BufferedImage loadImage(String path) {
		try {
			InputStream input = TextureManager.class.getResourceAsStream(path);
			if(input != null) {
				return ImageIO.read(input);
			} else {
				throw new RuntimeException("Image not found: " + path);
			}
		} catch (Exception var4) {
			throw new RuntimeException(var4);
		}
	}

	public static void RegisterAllTextureOverrides(TextureManager texCache) {
		Iterator overrideList = overrides.entrySet().iterator();

		while(overrideList.hasNext()) {
			Map.Entry entry = (Map.Entry) overrideList.next();
			Iterator textureMap = ((Map) entry.getValue()).entrySet().iterator();

			while(textureMap.hasNext()) {
				Map.Entry var4 = (Map.Entry) textureMap.next();
				String var6 = (String)var4.getKey();
				int var7 = (int)var4.getValue();
				int var8 = (int) entry.getKey();

				try {
					BufferedImage var9 = loadImage(var6);
					StaticTextures var10 = new StaticTextures(var7, var8, var9);
					texCache.addSprite(var10);
				} catch (Exception var11) {
					throw new RuntimeException(var11);
				}
			}
		}


	}

}

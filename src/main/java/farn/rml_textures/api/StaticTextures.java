package farn.rml_textures.api;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import farn.rml_textures.RisugamiTextures;
import net.minecraft.client.render.texture.TextureAtlas;
import org.lwjgl.opengl.GL11;

public class StaticTextures extends TextureAtlas {
	private boolean oldanaglyph;
	private int[] pixels;

	public StaticTextures(int var1, int var2, BufferedImage var3) {
		this(var1, 1, var2, var3);
	}

	public StaticTextures(int var1, int var2, int var3, BufferedImage var4) {
		super(var1);
		this.pixels = null;
		this.resolution = var2;
		this.type = var3;
		this.bind(RisugamiTextures.mc.textureManager);
		int var5 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / 16;
		int var6 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT) / 16;
		int var7 = var4.getWidth();
		int var8 = var4.getHeight();
		this.pixels = new int[var5 * var6];
		this.buffer = new byte[var5 * var6 * 4];
		if(var7 == var8 && var7 == var5) {
			var4.getRGB(0, 0, var7, var8, this.pixels, 0, var7);
		} else {
			BufferedImage var9 = new BufferedImage(var5, var6, 6);
			Graphics2D var10 = var9.createGraphics();
			var10.drawImage(var4, 0, 0, var5, var6, 0, 0, var7, var8, (ImageObserver)null);
			var9.getRGB(0, 0, var5, var6, this.pixels, 0, var5);
			var10.dispose();
		}

		this.update();
	}

	public void update() {
		for(int var1 = 0; var1 < this.pixels.length; ++var1) {
			int var2 = this.pixels[var1] >> 24 & 255;
			int var3 = this.pixels[var1] >> 16 & 255;
			int var4 = this.pixels[var1] >> 8 & 255;
			int var5 = this.pixels[var1] >> 0 & 255;
			if(this.anaglyph) {
				int var6 = (var3 + var4 + var5) / 3;
				var5 = var6;
				var4 = var6;
				var3 = var6;
			}

			this.buffer[var1 * 4 + 0] = (byte)var3;
			this.buffer[var1 * 4 + 1] = (byte)var4;
			this.buffer[var1 * 4 + 2] = (byte)var5;
			this.buffer[var1 * 4 + 3] = (byte)var2;
		}

		this.oldanaglyph = this.anaglyph;
	}

	public void tick() {
		if(this.oldanaglyph != this.anaglyph) {
			this.update();
		}

	}
}

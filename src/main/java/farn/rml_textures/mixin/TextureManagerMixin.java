package farn.rml_textures.mixin;

import farn.rml_textures.api.TextureHandlers;
import net.minecraft.client.render.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

	@Inject(method="tick", at = @At("TAIL"))
	private void initRegister(CallbackInfo ci) {
		if(!TextureHandlers.texturesOverridden) {
			TextureHandlers.texturesOverridden = true;
			TextureHandlers.RegisterAllTextureOverrides((TextureManager) (Object) this);
		}
	}
}

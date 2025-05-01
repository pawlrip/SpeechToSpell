package net.pawlrip.mixin;

import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextureIcon.class)
public interface TextureIconAccessor {
    @Accessor
    Texture getTexture();
}

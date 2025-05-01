package net.pawlrip.mixin;

import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemIcon.class)
public interface ItemIconAccessor {
    @Accessor
    ItemStack getStack();
}

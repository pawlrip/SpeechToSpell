package net.pawlrip.mixin;

import net.minecraft.block.entity.ViewerCountManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ViewerCountManager.class)
public class ViewerCountManagerMixin {

    // TODO currently this isn't working
    /*@ModifyVariable(method = "getInRangeViewerCount", at = @At(value = "STORE"), ordinal = 5)
    private Box renderHeldItemTooltip(Box box) {
        int size = CistemAperio.RANGE;
        return box.expand(size, size, size);
    }*/
}

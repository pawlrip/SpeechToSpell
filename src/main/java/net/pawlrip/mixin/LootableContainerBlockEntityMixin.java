package net.pawlrip.mixin;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LootableContainerBlockEntity.class)
public class LootableContainerBlockEntityMixin {

    @Inject(method = "canPlayerUse", at = @At("HEAD"), cancellable = true)
    private void canPlayerUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        // boolean bl = Components.MAGICIAN.get(player).getActiveSpell().getType() == chest opening spell type;
        boolean bl = false;
        LootableContainerBlockEntity this_instance = (LootableContainerBlockEntity) (Object) this;
        if (bl && Objects.requireNonNull(this_instance.getWorld()).getBlockEntity(this_instance.getPos()) == this_instance) {
            cir.setReturnValue(true);
        }
    }
}

package yolojj333.anarchyserver.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemEntity.class)
public abstract class ItemEntityMixin {
    @Redirect(
            method = "canMerge()Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"
            )
    )
    private int changeItemStackCount(ItemStack instance) {
        if (instance.isOf(Items.EGG)) {
            return 16;
        } else {
            return instance.getMaxCount();
        }
    }
}

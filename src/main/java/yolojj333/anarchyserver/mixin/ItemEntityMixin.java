package yolojj333.anarchyserver.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemEntity.class)
public abstract class ItemEntityMixin {
    // would redirect .getMaxCount() but conflicts w/ carpet mod
    @Redirect(
            method = "canMerge()Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getCount()I"
            )
    )
    private int changeItemStackCount(ItemStack instance) {
        if (instance.isOf(Items.EGG)) {
            return instance.getCount() - 12;
        } else {
            return instance.getCount();
        }
    }
}

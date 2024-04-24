package yolojj333.anarchyserver.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemEntity.class)
public abstract class ItemEntityMixin {
    // would redirect .getMaxCount() but conflicts w/ carpet mod so have to do these 3 redirects
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
        }
        return instance.getCount();
    }

    @Redirect(
            method = "canMerge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"
            )
    )
    private static int changeItemStackCount2(ItemStack instance) {
        if (instance.isOf(Items.EGG)) {
            return 16;
        }
        return instance.getMaxCount();
    }

    @Redirect(
            method = "merge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"
            )
    )
    private static int changeItemStackCount3(ItemStack instance) {
        if (instance.isOf(Items.EGG)) {
            return 16;
        }
        return instance.getMaxCount();
    }
}

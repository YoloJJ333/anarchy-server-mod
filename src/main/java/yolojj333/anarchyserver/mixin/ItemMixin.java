package yolojj333.anarchyserver.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import yolojj333.anarchyserver.util.ItemInterface;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemInterface {
    @Final
    @Mutable
    @Shadow
    private int maxCount;

    @Override
    public void setMaxCount(int count) {
        this.maxCount = count;
    }
}

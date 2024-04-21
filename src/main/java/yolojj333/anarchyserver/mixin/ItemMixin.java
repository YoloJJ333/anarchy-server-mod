package yolojj333.anarchyserver.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.*;
import yolojj333.anarchyserver.util.ItemInterface;
@Debug(export = true)
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

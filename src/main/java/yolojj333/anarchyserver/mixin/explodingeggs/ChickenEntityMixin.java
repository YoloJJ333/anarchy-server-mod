package yolojj333.anarchyserver.mixin.explodingeggs;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChickenEntity.class)
public class ChickenEntityMixin {
    @Redirect(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/ChickenEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;",
                    ordinal = 0
            )
    )
    private ItemEntity dropEggs(ChickenEntity instance, ItemConvertible itemConvertible) {
        instance.getWorld().createExplosion(instance, instance.getX(), instance.getY(), instance.getZ(), 2.42069F, World.ExplosionSourceType.MOB);
        for(int egg = 0; egg < 16; egg++) {
            instance.dropItem(itemConvertible);
        }

        for(int egg = 0; egg < 16; egg++) {
            instance.dropItem(itemConvertible);
        }

        return null;
    }

    @Redirect(
            method = "tickMovement",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/passive/ChickenEntity;eggLayTime:I",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 1
            )
    )
    private void setEggLayTime(ChickenEntity chicken, int value) {
        chicken.eggLayTime = 18000;
    }
}

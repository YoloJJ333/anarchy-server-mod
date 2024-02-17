package myanarchyserver.mymod.mixin;

import myanarchyserver.mymod.Log;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {
    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

//    @Redirect(
//            method = "moveOnRail",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/lang/Math;min(DD)D"
//            )
//    )
//    private double decreaseSpeedCap(double a, double b) {
//        return Math.min(1.0, b);
//    }

    @Redirect(
            method = "moveOnRail",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;",
                    ordinal = 5
            )
    )
    private Vec3d increaseAccel(Vec3d vec, double x, double y, double z) {
        Vec3d newvec = vec.add(x, y, z);
        try {
            if (this.getFirstPassenger().getHandItems().iterator().next().isOf(Items.REDSTONE_TORCH)) {
                return newvec.multiply(5.0);
            }
        } catch (Exception ignored) {
        }
        return newvec;
    }

    @Inject(
            method = "getMaxSpeed",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    private void increaseMaxSpeed(CallbackInfoReturnable<Double> cir) {
        Log.info(String.valueOf(Math.sqrt(Math.pow(this.getVelocity().getX(), 2) + Math.pow(this.getVelocity().getZ(), 2))));
        if (Math.sqrt(Math.pow(this.getVelocity().getX(), 2) + Math.pow(this.getVelocity().getZ(), 2)) >= 1) {
            try {
                if (this.getFirstPassenger().getHandItems().iterator().next().isOf(Items.REDSTONE_TORCH)) {
                    cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) / 5);
                } else {
                    cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) / 10);
                }
            } catch (Exception ignored) {
                cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) / 10);
            }
        }
    }
}

package myanarchyserver.mymod.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {
    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private int speed = 0;

    @Redirect(
            method = {"moveOnRail"},
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;min(DD)D"
            )
    )
    private double increaseSpeedCap(double a, double b) {
        return Math.min(3.0, b);
    }

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
        if(this.world.getBlockState(new BlockPos(MathHelper.floor(this.getX()), MathHelper.floor(this.getY()) - 1, MathHelper.floor(this.getZ()))).isOf(Blocks.REDSTONE_BLOCK)){
            speed = 240;
            return newvec.multiply(10.0);
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
        if (speed > 0) {
            try {
                if (this.getFirstPassenger().getHandItems().iterator().next().isOf(Items.REDSTONE_TORCH)) {
                    cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) / 4);
                } else {
                    cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) / 5);
                }
            } catch (Exception ignored) {
                cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) / 5);
            }
        }
        speed = Math.max(0, --speed);
    }
}

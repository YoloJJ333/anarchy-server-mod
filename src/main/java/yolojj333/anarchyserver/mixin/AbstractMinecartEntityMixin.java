package yolojj333.anarchyserver.mixin;

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

    private int fast = 0;

    @Redirect(
            method = {"moveOnRail"},
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;min(DD)D"
            )
    )
    private double increaseVelocityCap(double a, double b) {
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
        if (this.world.getBlockState(new BlockPos(MathHelper.floor(this.getX()), MathHelper.floor(this.getY()) - 1, MathHelper.floor(this.getZ()))).isOf(Blocks.REDSTONE_BLOCK)) {
            fast = 240;
            return vec.multiply(10.0);
        }
        return vec.add(x, y, z);
    }

    @Inject(
            method = "getMaxSpeed",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    private void increaseMaxInGameSpeed(CallbackInfoReturnable<Double> cir) {
        if (fast > 0) {
            fast--;
            try {
                if (this.getFirstPassenger().getHandItems().iterator().next().isOf(Items.REDSTONE_TORCH)) {
                    cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) * (Math.min(60, Math.max(30, fast)) / 60.0) / 4.0);
                    return;
                }
            } catch (Exception ignored) {
            }
            cir.setReturnValue((this.isTouchingWater() ? 4.0 : 8.0) * (Math.min(60, Math.max(30, fast)) / 60.0) / 5.0);
        }
    }
}

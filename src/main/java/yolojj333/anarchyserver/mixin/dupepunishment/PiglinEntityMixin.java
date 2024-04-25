package yolojj333.anarchyserver.mixin.dupepunishment;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static yolojj333.anarchyserver.AnarchyServer.log;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity {
    public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "dropEquipment",
            at = @At("HEAD")
    )
    private void punish(DamageSource source, int lootingMultiplier, boolean allowDrops, CallbackInfo ci) {
        if (this.isBaby() && this.getActivity().equals(PiglinActivity.ADMIRING_ITEM)) {
            TntEntity tnt = new TntEntity(this.world, MathHelper.floor(this.getX()) + 0.5, this.getY() + 0.5, MathHelper.floor(this.getZ()) + 0.5, null);
            tnt.setFuse(20);
            tnt.setPower(0.75f);
            tnt.setFire(true);
            tnt.setVelocity(Vec3d.ZERO);
            this.world.spawnEntity(tnt);
            log.info("Baby Piglin Duped at: {}", new Vec3d(this.getX(), this.getY(), this.getZ()));
        }
    }
}

package myanarchyserver.mymod.mixin.dupepunishment;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            TntEntity tnt = new TntEntity(this.world, this.getX(), this.getY(), this.getZ(), null);
            tnt.setFuse(20);
            tnt.setPower(0.5f);
            tnt.setFire(true);
            tnt.setVelocity(Vec3d.ZERO);
            this.world.spawnEntity(tnt);
        }
    }
}

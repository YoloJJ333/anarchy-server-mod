package myanarchyserver.mymod.mixin.explodingeggs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EggEntity.class)
public abstract class EggEntityMixin extends ThrownEntity {
    protected EggEntityMixin(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    protected EggEntityMixin(EntityType<? extends ThrownEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    protected EggEntityMixin(EntityType<? extends ThrownEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
    }

    @Inject(
            method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V",
            at = @At("HEAD")
    )
    private void onCollision(HitResult hitResult, CallbackInfo ci) {
        TntEntity tnt = new TntEntity(this.world, hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ(), null);
        tnt.setFuse(0);
        this.world.spawnEntity(tnt);
    }

    @Inject(
            method = "onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V",
            at = @At("TAIL")
    )
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity owner = this.getOwner();
        Entity hitEntity = entityHitResult.getEntity();
        if (hitEntity.getType().equals(EntityType.PLAYER)) {
            this.world.createExplosion(this, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), 6.0F, World.ExplosionSourceType.BLOCK);
            this.world.createExplosion(this, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), 6.0F, World.ExplosionSourceType.BLOCK);
            if (owner != null) {
                this.world.createExplosion(this, owner.getX(), owner.getY(), owner.getZ(), 5.0F, World.ExplosionSourceType.BLOCK);
            }
        }

        TntEntity tnt = new TntEntity(hitEntity.getWorld(), hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), null);
        tnt.setFuse(60*20);
        this.world.spawnEntity(tnt);
    }

    private int fuse = 100;
    private boolean call = true;

    public void tick() {
        super.tick();
        if (call) {
            TntEntity tnt = new TntEntity(this.world, this.getX(), this.getY(), this.getZ(), null);
            tnt.setFuse(fuse);
            this.world.spawnEntity(tnt);
        }

        call = !call;
        fuse--;
    }
}

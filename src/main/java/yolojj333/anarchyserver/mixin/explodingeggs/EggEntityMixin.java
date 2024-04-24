package yolojj333.anarchyserver.mixin.explodingeggs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static yolojj333.anarchyserver.AnarchyServer.log;

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
            method = "onCollision",
            at = @At("HEAD")
    )
    private void explodeCollision(HitResult hitResult, CallbackInfo ci) {
        if (((EntityHitResult) hitResult).getEntity().getType().equals(EntityType.TNT)) { // need to disable this.discard if collide w/ tnt
            return;
        }
        double x = hitResult.getPos().getX();
        double y = hitResult.getPos().getY();
        double z = hitResult.getPos().getZ();
        TntEntity tnt = new TntEntity(this.world, x, y, z, null);
        tnt.setFuse(0);
        this.world.spawnEntity(tnt);

    }

    @Inject(
            method = "onCollision",
            at = @At("TAIL")
    )
    private void ignoreTNT(HitResult hitResult, CallbackInfo ci) {
        if (((EntityHitResult) hitResult).getEntity().getType().equals(EntityType.TNT)) { // need to disable this.discard if collide w/ tnt
            // spawn back egg
            Entity egg = this;
            egg.addVelocity(0, 10, 0);
            this.world.spawnEntity(egg);
        }
    }

    @Inject(
            method = "onEntityHit",
            at = @At("TAIL")
    )
    private void explodePlayer(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity owner = this.getOwner();
        Entity hitEntity = entityHitResult.getEntity();
        double hitX = hitEntity.getX();
        double hitY = hitEntity.getY();
        double hitZ = hitEntity.getZ();
        if (hitEntity.getType().equals(EntityType.PLAYER)) {
            this.world.createExplosion(this, hitX, hitY, hitZ, 6.0F, World.ExplosionSourceType.TNT);
            this.world.createExplosion(this, hitX, hitY, hitZ, 6.0F, World.ExplosionSourceType.TNT);
            if (owner != null) {
                this.world.createExplosion(this, owner.getX(), owner.getY(), owner.getZ(), 5.0F, World.ExplosionSourceType.TNT);
                log.info(String.format("Player threw: %1s, At: %2s", owner.getName(), new Vec3d(owner.getX(), owner.getY(), owner.getZ())));
            }
            log.info(String.format("Player hit: %1s, At: %2s", hitEntity.getName(), new Vec3d(hitX, hitY, hitZ)));
        } else if (hitEntity.getType().equals(EntityType.TNT)) {
            return;
        }

        TntEntity tnt = new TntEntity(hitEntity.getWorld(), hitX, hitY, hitZ, null);
        tnt.setFuse(1200);
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

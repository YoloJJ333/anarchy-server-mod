package yolojj333.anarchyserver.mixin.explodingeggs;

import yolojj333.anarchyserver.AnarchyServer;
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
    private void onCollision(HitResult hitResult, CallbackInfo ci) {
        double x = hitResult.getPos().getX();
        double y = hitResult.getPos().getY();
        double z = hitResult.getPos().getZ();
        TntEntity tnt = new TntEntity(this.world, x, y, z, null);
        tnt.setFuse(0);
        this.world.spawnEntity(tnt);
    }

    @Inject(
            method = "onEntityHit",
            at = @At("TAIL")
    )
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity owner = this.getOwner();
        Entity hitEntity = entityHitResult.getEntity();
        double hitX = hitEntity.getX();
        double hitY = hitEntity.getY();
        double hitZ = hitEntity.getZ();
        if (hitEntity.getType().equals(EntityType.PLAYER)) {
            this.world.createExplosion(this, hitX, hitY, hitZ, 6.0F, World.ExplosionSourceType.BLOCK);
            this.world.createExplosion(this, hitX, hitY, hitZ, 6.0F, World.ExplosionSourceType.BLOCK);
            if (owner != null) {
                this.world.createExplosion(this, owner.getX(), owner.getY(), owner.getZ(), 5.0F, World.ExplosionSourceType.BLOCK);
                AnarchyServer.log.warn("Player threw: " + new Vec3d(owner.getX(), owner.getY(), owner.getZ()));
            }
            AnarchyServer.log.warn("Player hit: " + new Vec3d(hitX, hitY, hitZ));
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

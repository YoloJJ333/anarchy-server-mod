package yolojj333.anarchyserver.mixin.dupepunishment;

import org.spongepowered.asm.mixin.Unique;
import yolojj333.anarchyserver.util.TntEntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity implements TntEntityInterface {
    public TntEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    float power = 4.0f;
    @Unique
    boolean fire = false;

    @Override
    public void setPower(float power) {
        this.power = power;
    }

    @Override
    public void setFire(boolean fire) {
        this.fire = fire;
    }

    @Redirect(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"
            )
    )
    private Explosion customExplosion(World instance, Entity entity, double x, double y, double z, float power, World.ExplosionSourceType explosionSourceType) {
        return entity.world.createExplosion(entity, x, y, z, this.power, fire, explosionSourceType);
    }
}
package yolojj333.anarchyserver.mixin.explodingeggs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EggItem.class)
public class EggItemMixin extends Item {
    public EggItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(
            method = "use",
            at = @At("HEAD")
    )
    private void setItemCooldown(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        int count = itemStack.getCount();
        if (count > 4) {
            user.getItemCooldownManager().set(this, count * 9);
            if (!world.isClient) {
                for (int i = 0; i < count; i++) {
                    EggEntity eggEntity = new EggEntity(world, user);
                    eggEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 35.0f);
                    world.spawnEntity(eggEntity);
                }
            }
            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(count - 1);
            }
        } else {
            user.getItemCooldownManager().set(this, 10);
        }
    }
}

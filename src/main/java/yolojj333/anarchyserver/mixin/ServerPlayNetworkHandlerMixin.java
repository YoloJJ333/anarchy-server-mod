package yolojj333.anarchyserver.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @ModifyConstant(
            method = "tick",
            constant = @Constant(
                intValue = 80
            )
    )
    private int nerfFlying(int constant){
        return 30;
    }
}

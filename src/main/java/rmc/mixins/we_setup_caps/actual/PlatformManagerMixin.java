package rmc.mixins.we_setup_caps.actual;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extension.platform.PlatformManager;

/**
 * Developed by RMC Team, 2021
 */
@Mixin(value = PlatformManager.class)
public abstract class PlatformManagerMixin {

    @Shadow @Final private List<Platform> platforms;

    @Inject(method = "Lcom/sk89q/worldedit/extension/platform/PlatformManager;findMostPreferred(Lcom/sk89q/worldedit/extension/platform/Capability;)Lcom/sk89q/worldedit/extension/platform/Platform;",
            remap = false,
            cancellable = true,
            at = @At(value = "HEAD"))
    private void mangleCapsPriority(Capability cap, CallbackInfoReturnable<Platform> mixin) {
        Platform forge = null;
        for (Platform platform : this.platforms) {
            if (platform.getPlatformName().equals("Forge-Official")) {
                forge = platform;
                break;
            }
        }
        if (forge != null
         && (cap == Capability.GAME_HOOKS
          || cap == Capability.WORLDEDIT_CUI)) {
            mixin.setReturnValue(forge);
        }
        if (forge == null
         && cap == Capability.WORLD_EDITING) {
            mixin.setReturnValue(null);
        }
    }

}
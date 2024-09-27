package victorgponce.com.autismonmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import victorgponce.com.autismonmod.screens.CustomMainMenu;


import static victorgponce.com.autismonmod.client.AUTISMON_MODClient.LOGGER_CLIENT;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin {
    @Inject(method = "disconnect", at = @At("TAIL"))
    public void disconnect(CallbackInfo ci) {

        LOGGER_CLIENT.info("Setting CustomMainMenu as Parent");

        MinecraftClient.getInstance().disconnect();

        CustomMainMenu customMainMenu = new CustomMainMenu();
        MinecraftClient.getInstance().setScreen(new MultiplayerScreen(customMainMenu));

    }
}

package victorgponce.com.autismonmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import victorgponce.com.autismonmod.screens.CustomMainMenu;
import victorgponce.com.autismonmod.screens.CustomOptionsScreen;

import java.util.List;

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

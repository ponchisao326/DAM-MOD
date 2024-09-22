package victorgponce.com.autismonmod.mixin;

import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SplashOverlay.class)
public interface SplashOverlayAccessor {

    @Accessor("progress") // Si el campo se llama "progress"
    float getProgress();

    @Accessor("progress") // Añadir el setter si el campo es accesible
    void setProgress(float progress);

    @Accessor("reload")
    ResourceReload getReload();
}

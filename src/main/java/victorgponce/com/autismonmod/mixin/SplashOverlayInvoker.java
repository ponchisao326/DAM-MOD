package victorgponce.com.autismonmod.mixin;

import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SplashOverlay.class)
public interface SplashOverlayInvoker {
    @Invoker("renderProgressBar")
    void invokeRenderProgressBar(DrawContext drawContext, int minX, int minY, int maxX, int maxY, float alpha);
}

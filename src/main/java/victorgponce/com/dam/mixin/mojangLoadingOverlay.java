package victorgponce.com.dam.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import victorgponce.com.dam.client.DAM_MODClient;

import java.util.Optional;

@Mixin(SplashOverlay.class)
public class mojangLoadingOverlay {

    @Unique
    Identifier INTRO_BACKGROUND_TEXTURE = new Identifier("victorgponce_com_autismon", "mixin/title_screen/mojang-menu.png");


    // Variable para guardar el tiempo final al llegar al 100%
    @Unique
    private long overlayEndTime = -1;
    @Unique
    private final long delayDuration = 1000; // 1000 ms = 1 segundo

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRenderOverlay(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // Renderizar la textura de fondo
        Identifier introBackgroundTextureId = INTRO_BACKGROUND_TEXTURE;
        Optional<Resource> introResourceOptional = client.getResourceManager().getResource(introBackgroundTextureId);

        if (introResourceOptional.isPresent()) {
            // Renderizar la textura de introducción
            RenderSystem.setShaderTexture(0, introBackgroundTextureId);
            drawContext.drawTexture(introBackgroundTextureId, 0, 0, 0.0F, 0.0F, width, height, width, height);

            if (!client.getWindow().isFullscreen()) {
                client.getWindow().toggleFullscreen();
            }

            long currentTime = System.currentTimeMillis();

            // Obtener el progreso del ResourceReload
            float progress = ((SplashOverlayAccessor) this).getReload().getProgress();

            // Calculamos las posiciones para la barra de progreso
            int minX = width / 2 - 100;
            int maxX = width / 2 + 100;
            int minY = height - 50; // 30 píxeles más arriba
            int maxY = height - 40; // 30 píxeles más arriba

            // Renderizar la barra de progreso
            ((SplashOverlayInvoker) this).invokeRenderProgressBar(drawContext, minX, minY, maxX, maxY, 1.0F);

            // Si el progreso llega a 1.0, eliminamos el overlay
            if (progress >= 1.0F) {
                // Establecer el tiempo de finalización si no está ya configurado
                if (overlayEndTime == -1) {
                    overlayEndTime = System.currentTimeMillis();
                }

                // Comparar el tiempo actual con el tiempo final + retraso
                if (currentTime - overlayEndTime > delayDuration) {
                    // Cuando el tiempo haya pasado el retraso, ocultar el overlay
                    MinecraftClient.getInstance().setOverlay((Overlay) null);
                }
            }

            // Actualizamos el progreso con interpolación suave para que avance visualmente
            float currentProgress = ((SplashOverlayAccessor) this).getProgress();
            float newProgress = MathHelper.clamp(currentProgress * 0.95F + progress * 0.05F, 0.0F, 1.0F);
            ((SplashOverlayAccessor) this).setProgress(newProgress);

        } else {
            // La textura no se pudo cargar, imprimir un mensaje de error
            DAM_MODClient.LOGGER_CLIENT.error("Error: No se pudo cargar la textura de fondo.");
        }

        // Cancelar la renderización original de la pantalla de carga
        ci.cancel();
    }
}
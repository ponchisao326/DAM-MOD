package victorgponce.com.autismonmod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.Resource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import victorgponce.com.autismonmod.AUTISMON_MOD;
import victorgponce.com.autismonmod.client.AUTISMON_MODClient;

import java.util.Optional;

@Mixin(SplashOverlay.class)
public class mojangLoadingOverlay {

    Identifier BACKGROUND_TEXTURE = Identifier.of("victorgponce_com_autismon", "mixin/title_screen/background-1.png"); // "Bienvenido..."
    Identifier INTRO_BACKGROUND_TEXTURE = Identifier.of("victorgponce_com_autismon", "mixin/title_screen/mojang-menu.png"); // "INICIANDO CLIENTE"

    private String[] MESSAGES = {"Cargando.", "Cargando..", "Cargando..."};
    private int messageIndex = 0;
    private static final long MESSAGE_CHANGE_INTERVAL = 1000L;

    private long lastMessageChangeTime = 0L;
    private int currentMessageIndex = 0;
    private int contador;

    // Fade duration in milliseconds
    private static final int FADE_DURATION = 1200;
    private static final int FADE_IN_DURATION = FADE_DURATION / 2;
    private static final int FADE_OUT_DURATION = FADE_DURATION / 2;
    private long fadeInStartTime = -1;
    private long fadeOutStartTime = -1;
    private boolean fadeInComplete = false;
    private boolean fadeOutComplete = false;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRenderOverlay(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // Renderizar la textura de fondo
        Identifier backgroundTextureId = BACKGROUND_TEXTURE;
        Identifier introBackgroundTextureId = INTRO_BACKGROUND_TEXTURE;
        Optional<Resource> resourceOptional = client.getResourceManager().getResource(backgroundTextureId);
        Optional<Resource> introResourceOptional = client.getResourceManager().getResource(introBackgroundTextureId);

        if (resourceOptional.isPresent() && introResourceOptional.isPresent()) {
            // Renderizar la textura de introducción
            RenderSystem.setShaderTexture(0, introBackgroundTextureId);
            drawContext.drawTexture(introBackgroundTextureId, 0, 0, 0.0F, 0.0F, width, height, width, height);

            if (!client.getWindow().isFullscreen()) {
                client.getWindow().toggleFullscreen();
            }
            TextRenderer textRenderer = client.textRenderer;

            // Obtener el mensaje actual
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMessageChangeTime >= MESSAGE_CHANGE_INTERVAL) {
                currentMessageIndex = (currentMessageIndex + 1) % MESSAGES.length;
                lastMessageChangeTime = currentTime;
                if (contador >= 11) {
                    MinecraftClient.getInstance().setOverlay((Overlay) null);
                    return;
                }
                contador++;
                fadeInStartTime = currentTime;
                fadeOutStartTime = fadeInStartTime + FADE_IN_DURATION;
            }
            String message = MESSAGES[currentMessageIndex];
            if (contador > 4) {
                // Renderizar la textura de fondo
                RenderSystem.setShaderTexture(0, backgroundTextureId);
                drawContext.drawTexture(backgroundTextureId, 0, 0, 0.0F, 0.0F, width, height, width, height);

                // Calcular la transparencia para el efecto de desvanecimiento
                int alpha = 255;
                if (!fadeInComplete && currentTime < fadeInStartTime + FADE_IN_DURATION) {
                    alpha = (int) MathHelper.clamp((currentTime - fadeInStartTime) * 255.0 / FADE_IN_DURATION, 0, 255);
                } else {
                    fadeInComplete = true;
                }

                if (fadeInComplete && !fadeOutComplete && currentTime > fadeOutStartTime && currentTime < fadeOutStartTime + FADE_OUT_DURATION) {
                    alpha = (int) MathHelper.clamp(255 - (currentTime - fadeOutStartTime) * 255.0 / FADE_OUT_DURATION, 0, 255);
                } else if (currentTime > fadeOutStartTime + FADE_OUT_DURATION) {
                    fadeOutComplete = true;
                }

                // Dibujar el mensaje de carga
                Text loading = Text.of(message);
                int loadingTextWidth = textRenderer.getWidth(loading);
                int textHeight = textRenderer.fontHeight;
                int loadingTextX = (width - loadingTextWidth) / 2;
                int loadingTextY = height - textHeight - 10;
                drawContext.drawText(textRenderer, loading, loadingTextX, loadingTextY, 0x000000 | (alpha << 24), false);

                // Renderizar texto en medio de la pantalla, pero en la parte superior
                String username = client.getSession().getUsername();
                Text text = Text.of("¡Bienvenido " + username + " al Survival!");
                int textWidth = textRenderer.getWidth(text);
                int textX = (width - textWidth) / 2;

                // Posicionar el texto en la parte superior (ajustando textY)
                int textY = 20; // Ajusta este valor para posicionarlo más arriba o más abajo

                // Dibujar el texto
                drawContext.drawText(textRenderer, text, textX, textY, 0x000000 | (alpha << 24), false);

            }
        } else {
            // La textura no se pudo cargar, imprimir un mensaje de error
            AUTISMON_MODClient.LOGGER_CLIENT.error("Error: No se pudo cargar la textura de fondo.");
        }

        // Cancelar la renderización original de la pantalla de carga
        ci.cancel();
    }

}

package victorgponce.com.autismonmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import victorgponce.com.autismonmod.loading.SoundLoader;
import victorgponce.com.autismonmod.screens.PressToContinue;
import victorgponce.com.autismonmod.screens.*;

import static com.mojang.text2speech.Narrator.LOGGER;

public class AUTISMON_MODClient implements ClientModInitializer {

    public static final String MOD_ID_CLIENT = "autismon-client";
    public static final Logger LOGGER_CLIENT = LoggerFactory.getLogger(MOD_ID_CLIENT);


    @Override
    public void onInitializeClient() {

        LOGGER.info("AUTISMON: Iniciando cliente");
        LOGGER.info("Autor: Ponchisao326 (PonchisaoHosting)");

        // Registrar sonidos
        SoundLoader.registerSounds();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            MinecraftClient.getInstance().execute(() -> {
                PressToContinue pressToContinueScreen = new PressToContinue();
                MinecraftClient.getInstance().setScreen(pressToContinueScreen);
                MinecraftClient.getInstance().getWindow().setTitle("AUTISMON 1.20.1 - PonchisaoHosting");
            });
        });
    }
}
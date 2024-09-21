package victorgponce.com.autismonmod.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import static com.mojang.text2speech.Narrator.LOGGER;

public class AUTISMON_MODClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        LOGGER.info("AUTISMON: Iniciando cliente");
        LOGGER.info("Autor: Ponchisao326 (PonchisaoHosting)");

    }
}

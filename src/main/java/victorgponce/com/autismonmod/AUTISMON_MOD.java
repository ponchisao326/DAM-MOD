package victorgponce.com.autismonmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AUTISMON_MOD implements ModInitializer {

    public static final String MOD_ID = "autismon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("AUTISMON: Iniciando server-side");
        LOGGER.info("Autor: Ponchisao326 (PonchisaoHosting)");

        ServerPlayConnectionEvents.JOIN.register((ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) -> {
            // Obtener el jugador desde el handler
            ServerPlayerEntity player = handler.player;

            // Mensaje de bienvenida con color dorado
            player.sendMessage(Text.literal("§6¡Bienvenido a ").formatted(Formatting.GOLD)
                    .append(Text.literal("§bAUTISMON!").formatted(Formatting.AQUA)), false);

            // Mensaje de información con colores azul y verde
            player.sendMessage(Text.literal("§9Si necesitas información y/o ayuda sobre el servidor usa ")
                    .formatted(Formatting.BLUE)
                    .append(Text.literal("§a/info").formatted(Formatting.GREEN)), false);
        });

    }
}

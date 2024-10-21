package victorgponce.com.dam;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import victorgponce.com.dam.commands.Info;
import victorgponce.com.dam.loading.SoundLoader;

import java.io.InputStream;
import java.util.List;

public class DAM_MOD implements ModInitializer {

    public static final String MOD_ID_SERVER = "dam-server";
    public static final Logger LOGGER_SERVER = LoggerFactory.getLogger(MOD_ID_SERVER);

    @Override
    public void onInitialize() {

        LOGGER_SERVER.info("DAM: Iniciando server-side");
        LOGGER_SERVER.info("Autor: Ponchisao326 (PonchisaoHosting)");

        // Registrar sonidos
        SoundLoader.registerSounds();

        ServerPlayConnectionEvents.JOIN.register((ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) -> {
            // Obtener el jugador desde el handler
            ServerPlayerEntity player = handler.player;

            // Obtener el PlayerManager desde el servidor
            PlayerManager playerManager = server.getPlayerManager();

            // Obtener la lista de jugadores desde el PlayerManager
            List<ServerPlayerEntity> players = playerManager.getPlayerList();

            // Crear el prefijo [DAM MOD] con colores personalizados
            Text prefix = Text.literal("[").formatted(Formatting.GRAY)
                    .append(Text.literal("DAM MOD").formatted(Formatting.RED, Formatting.BOLD))
                    .append(Text.literal("] ").formatted(Formatting.GRAY))
                    .append(Text.literal("» ").formatted(Formatting.AQUA));

            // Mensaje de bienvenida con el prefijo
            player.sendMessage(prefix.copy()
                    .append(Text.literal("¡Bienvenido a ").formatted(Formatting.GOLD))
                    .append(Text.literal("REVOLUT SERVER!").formatted(Formatting.AQUA)), false);

            // Mensaje de información con el prefijo
            player.sendMessage(prefix.copy()
                    .append(Text.literal("Si necesitas información y/o ayuda sobre el servidor usa ").formatted(Formatting.BLUE))
                    .append(Text.literal("/info").formatted(Formatting.GREEN)), false);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> Info.registerCommands(dispatcher));
    }
}
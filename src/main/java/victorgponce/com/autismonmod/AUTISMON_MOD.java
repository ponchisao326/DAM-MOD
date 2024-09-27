package victorgponce.com.autismonmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.Icons;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import victorgponce.com.autismonmod.commands.Info;
import victorgponce.com.autismonmod.loading.SoundLoader;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class AUTISMON_MOD implements ModInitializer {

    public static final String MOD_ID_SERVER = "autismon-server";
    public static final Logger LOGGER_SERVER = LoggerFactory.getLogger(MOD_ID_SERVER);

    private InputStream smallIconStream;
    private InputStream bigIconStream;

    @Override
    public void onInitialize() {

        LOGGER_SERVER.info("AUTISMON: Iniciando server-side");
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

            // Crear el prefijo [AUTISMON MOD] con colores personalizados
            Text prefix = Text.literal("[").formatted(Formatting.GRAY)
                    .append(Text.literal("AUTISMON MOD").formatted(Formatting.RED, Formatting.BOLD))
                    .append(Text.literal("] ").formatted(Formatting.GRAY))
                    .append(Text.literal("» ").formatted(Formatting.AQUA));

            // Mensaje de bienvenida con el prefijo
            player.sendMessage(prefix.copy()
                    .append(Text.literal("¡Bienvenido a ").formatted(Formatting.GOLD))
                    .append(Text.literal("AUTISMON!").formatted(Formatting.AQUA)), false);

            // Mensaje de información con el prefijo
            player.sendMessage(prefix.copy()
                    .append(Text.literal("Si necesitas información y/o ayuda sobre el servidor usa ").formatted(Formatting.BLUE))
                    .append(Text.literal("/info").formatted(Formatting.GREEN)), false);

            switch (player.getDisplayName().getString().toLowerCase()) {
                case "jokyo" -> {
                    player.playSound(SoundLoader.Jokyo, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Jokyo, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "ikertc" -> {
                    player.playSound(SoundLoader.Iker, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Iker, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "ponchisao326" -> {
                    player.playSound(SoundLoader.Ponchisao, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Ponchisao, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "mapeda2018" -> {
                    player.playSound(SoundLoader.Mapeda, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Mapeda, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "arrkness" -> {
                    player.playSound(SoundLoader.Arkness, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Arkness, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "soy_un_arbol" -> {
                    player.playSound(SoundLoader.Arbol, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Arbol, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "jotaromc_yt" -> {
                    player.playSound(SoundLoader.Jotaro, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Jotaro, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "Apin_827" -> {
                    player.playSound(SoundLoader.Apin, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Apin, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
                case "thezakto" -> {
                    player.playSound(SoundLoader.Zakto, SoundCategory.MASTER, 20, 1.0f);
                    for (ServerPlayerEntity otherPlayer : players) {
                        otherPlayer.playSound(SoundLoader.Zakto, SoundCategory.MASTER, 20, 1.0f);
                    }
                }
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> Info.registerCommands(dispatcher));
    }
}
package victorgponce.com.dam.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Info {

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("info")
                .requires(source -> true) // Permitir que todos puedan usar el comando
                .executes(context -> {
                    sendInfoMessage(context.getSource());
                    return 1;
                }));
    }

    // Método para enviar el mensaje informativo
    private static void sendInfoMessage(ServerCommandSource source) {

        // Crear el prefijo [DAM MOD] con colores personalizados
        Text prefix = Text.literal("[").formatted(Formatting.GRAY)
                .append(Text.literal("DAM MOD").formatted(Formatting.RED, Formatting.BOLD))
                .append(Text.literal("] ").formatted(Formatting.GRAY))
                .append(Text.literal("» ").formatted(Formatting.AQUA));

        // Mensaje adicional: información sobre el creador y enlaces
        source.sendMessage(prefix.copy()
                .append(Text.literal("Mod creado por Victor Gomez (PonchisaoHosting). ").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD))
                .append(Text.literal("Visita mi web: ").formatted(Formatting.LIGHT_PURPLE))
                .append(Text.literal("https://victorgponce.com").formatted(Formatting.BLUE, Formatting.UNDERLINE)));

        source.sendMessage(prefix.copy()
                .append(Text.literal("Puedes encontrar el código fuente en GitHub: ").formatted(Formatting.LIGHT_PURPLE))
                .append(Text.literal("https://github.com/ponchisao326/DAM-MOD").formatted(Formatting.BLUE, Formatting.UNDERLINE)));
    }
}
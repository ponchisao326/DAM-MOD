package victorgponce.com.dam.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Info {

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("info")
                .executes(context -> {
                    sendInfoMessage(context.getSource());
                    return 1;
                }));
    }

    // Método para enviar el mensaje informativo
    private static void sendInfoMessage(ServerCommandSource source) {

        // Crear el prefijo [AUTISMON MOD] con colores personalizados
        Text prefix = Text.literal("[").formatted(Formatting.GRAY)
                .append(Text.literal("AUTISMON MOD").formatted(Formatting.RED, Formatting.BOLD))
                .append(Text.literal("] ").formatted(Formatting.GRAY))
                .append(Text.literal("» ").formatted(Formatting.AQUA));

        // Enviar el mensaje con el prefijo y el contenido
        source.sendMessage(prefix.copy()
                .append(Text.literal("Para empezar tu aventura necesitas un Pokemon. Pulsa la letra ").formatted(Formatting.YELLOW))
                .append(Text.literal("M").formatted(Formatting.GREEN, Formatting.BOLD))
                .append(Text.literal(" para seleccionar tu inicial. Si usas la rueda en la parte izquierda verás más opciones de iniciales. ")));

        source.sendMessage(prefix.copy()
                .append(Text.literal("Para sacar tu Pokemon elegido o combatir contra otro Pokemon, deberás usar la ").formatted(Formatting.YELLOW))
                .append(Text.literal("R").formatted(Formatting.GREEN, Formatting.BOLD))
                .append(Text.literal(", tecla que está siendo usada por otro mod. Tendrás que buscar en los controles el mod que la esté utilizando y desvincularla.")));

        source.sendMessage(prefix.copy()
                .append(Text.literal("Una vez tengas esto, usa otra vez la ").formatted(Formatting.YELLOW))
                .append(Text.literal("M").formatted(Formatting.GREEN, Formatting.BOLD))
                .append(Text.literal(" para ver tu/tus Pokemon, y te saldrá toda la información de tu Pokemon, como habilidades, movimientos y estadísticas.")));

        source.sendMessage(prefix.copy()
                .append(Text.literal("Una vez llegues aquí, todo depende de ti. Si necesitas más ayuda, usa una Pokepedia (combina un libro con un Apricorn).").formatted(Formatting.YELLOW)));

        // Mensaje adicional: "Mod creado por Ponchisao326"
        source.sendMessage(prefix.copy()
                .append(Text.literal("Mod creado por Ponchisao326").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD)));


    }

}

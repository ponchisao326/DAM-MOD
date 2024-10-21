package victorgponce.com.dam.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameMenuScreen.class)
public interface GameMenuScreenAccessor {

    // Define un método accessor para acceder a DISCONNECT_TEXT
    @Accessor("DISCONNECT_TEXT")
    static Text getDisconnectText() {
        throw new AssertionError();
    }
}

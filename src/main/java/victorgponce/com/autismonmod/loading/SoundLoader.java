package victorgponce.com.autismonmod.loading;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundLoader {

    public static final Identifier PRESS_BG_1_ID = new Identifier("victorgponce_com_autismon", "press_bg");
    public static final SoundEvent PRESS_BG_1 = SoundEvent.of(PRESS_BG_1_ID);

    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, PRESS_BG_1_ID, PRESS_BG_1);
    }
}
package victorgponce.com.dam.loading;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundLoader {

    public static final Identifier PRESS_BG_1_ID = new Identifier("victorgponce_com_autismon", "press_bg");
    public static final Identifier mainThemeId = new Identifier("victorgponce_com_autismon", "main_menu");
    public static final Identifier ErikaId = new Identifier("victorgponce_com_autismon", "erika");
    public static final Identifier JokyoId = new Identifier("victorgponce_com_autismon", "jokyo");
    public static final Identifier IkerId = new Identifier("victorgponce_com_autismon", "iker");
    public static final Identifier PonchisaoId = new Identifier("victorgponce_com_autismon", "ponchisao");
    public static final Identifier MapedaId = new Identifier("victorgponce_com_autismon", "mapeda");
    public static final Identifier ArknessId = new Identifier("victorgponce_com_autismon", "arkness");
    public static final Identifier ArbolId = new Identifier("victorgponce_com_autismon", "arbol");
    public static final Identifier JotaroId = new Identifier("victorgponce_com_autismon", "jotaro");
    public static final Identifier ApinId = new Identifier("victorgponce_com_autismon", "apin");
    public static final Identifier R0valId = new Identifier("victorgponce_com_autismon", "r0val");
    public static final Identifier ZaktoId = new Identifier("victorgponce_com_autismon", "zakto");
    public static final SoundEvent PRESS_BG_1 = SoundEvent.of(PRESS_BG_1_ID);
    public static final SoundEvent mainTheme = SoundEvent.of(mainThemeId);
    public static final SoundEvent Erika = SoundEvent.of(ErikaId);
    public static final SoundEvent Jokyo = SoundEvent.of(JokyoId);
    public static final SoundEvent Iker = SoundEvent.of(IkerId);
    public static final SoundEvent Ponchisao = SoundEvent.of(PonchisaoId);
    public static final SoundEvent Mapeda = SoundEvent.of(MapedaId);
    public static final SoundEvent Arkness = SoundEvent.of(ArknessId);
    public static final SoundEvent Arbol = SoundEvent.of(ArbolId);
    public static final SoundEvent Jotaro = SoundEvent.of(JotaroId);
    public static final SoundEvent Apin = SoundEvent.of(ApinId);
    public static final SoundEvent R0val = SoundEvent.of(R0valId);
    public static final SoundEvent Zakto = SoundEvent.of(ZaktoId);

    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, PRESS_BG_1_ID, PRESS_BG_1);
        Registry.register(Registries.SOUND_EVENT, mainThemeId, mainTheme);
        Registry.register(Registries.SOUND_EVENT, ErikaId, Erika);
        Registry.register(Registries.SOUND_EVENT, JokyoId, Jokyo);
        Registry.register(Registries.SOUND_EVENT, IkerId, Iker);
        Registry.register(Registries.SOUND_EVENT, PonchisaoId, Ponchisao);
        Registry.register(Registries.SOUND_EVENT, MapedaId, Mapeda);
        Registry.register(Registries.SOUND_EVENT, ArknessId, Arkness);
        Registry.register(Registries.SOUND_EVENT, ArbolId, Arbol);
        Registry.register(Registries.SOUND_EVENT, JotaroId, Jotaro);
        Registry.register(Registries.SOUND_EVENT, ApinId, Apin);
        Registry.register(Registries.SOUND_EVENT, R0valId, R0val);
    }
}
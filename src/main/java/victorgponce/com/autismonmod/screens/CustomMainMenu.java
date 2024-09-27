package victorgponce.com.autismonmod.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.gui.widget.ModMenuButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.resource.Resource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import victorgponce.com.autismonmod.loading.SoundLoader;
import java.io.IOException;
import java.net.Socket;

import java.util.Optional;

import static victorgponce.com.autismonmod.client.AUTISMON_MODClient.LOGGER_CLIENT;


public class CustomMainMenu extends Screen {
    public ButtonWidget serverButton;
    public ButtonWidget quitGame;
    public ButtonWidget optionsButton;
    public ButtonWidget modsButton;
    private TextWidget pingTextWidget;


    private static final String SERVER_ADDRESS = "node-marb.ponchisaohosting.xyz:25566";
    private static final String NODE = "node-marb.ponchisaohosting.xyz";
    private static final int PORT = 25565;
    private static final String CONNECT_BUTTON_TEXT = "AUTISMON";
    private static final Text MENU = Text.literal("Menu by PonchisaoHosting");
    private static final Identifier menuTitleId = new Identifier("victorgponce_com_autismon:title_screen/autismon.png");
    private Screen parent;
    private final Identifier[] backgroundTextures = new Identifier[91];

    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DURATION_MS = 60; // Duración en milisegundos de cada fotograma
    private boolean sounding = false;
    private PositionedSoundInstance soundInstance;
    private static boolean first = true;
    private double musicCategory = 0.0;
    private double masterCategory = 0.0;
    private String STATUS = "El servidor se encuentra OFFLINE";
    private static final long PING_CHECK_INTERVAL_MS = 5000; // Intervalo de 5 segundos
    private long lastPingCheckTime = 0; // Momento del último chequeo
    private Thread pingThread; // Hilo para la verificación del servidor
    private boolean stopPingThread = false; // Variable para detener el hilo cuando sea necesario



    public CustomMainMenu() {
        super(Text.of("Main Menu"));
        startPingThread();

        // Inicializar los identificadores de textura
        for (int i = 0; i < backgroundTextures.length; i++) {
            backgroundTextures[i] = new Identifier("victorgponce_com_autismon:title_screen/" + (i + 1) + ".png");
        }
    }

    // Método para iniciar el hilo de verificación del servidor
    private void startPingThread() {
        pingThread = new Thread(() -> {
            while (!stopPingThread) {
                if (Pinger(NODE, PORT)) {
                    STATUS = "El servidor se encuentra ONLINE";
                } else {
                    STATUS = "El servidor se encuentra OFFLINE";
                }
                // Esperar el intervalo de tiempo definido antes de la siguiente verificación
                try {
                    Thread.sleep(PING_CHECK_INTERVAL_MS);
                } catch (InterruptedException e) {
                    LOGGER_CLIENT.error("El hilo de verificación de ping fue interrumpido: " + e.getMessage());
                }
            }
        });
        pingThread.start();
    }

    @Override
    protected void init() {
        super.init();

        // Obtener la mitad de la pantalla para centrar en el eje X
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Crear los botones con las nuevas posiciones
        serverButton = new ButtonWidget.Builder(Text.of(CONNECT_BUTTON_TEXT), (buttonWidget) -> {
            ServerInfo info = new ServerInfo(I18n.translate("selectServer.defaultName"), SERVER_ADDRESS, false);
            info.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.PROMPT);
            ConnectScreen.connect(this, this.client, ServerAddress.parse(SERVER_ADDRESS), info, true);
            buttonWidget.playDownSound(this.client.getSoundManager());
            changeMusicOnPress();
        }).dimensions(centerX - 100, centerY - 12, 200, 20).build(); // Ajustado hacia arriba

        modsButton = new ModMenuButtonWidget(centerX - 100, centerY + 9, 200, 20, Text.of("Mods"), this); // Ajustado hacia arriba

        // Botón Options... (ancho 100) centrado a la izquierda
        optionsButton = new ButtonWidget.Builder(Text.of("Options..."), (buttonWidget) -> {
            MinecraftClient.getInstance().setScreen(new CustomOptionsScreen(this, this.client.options));
            changeMusicOnPress();
        }).dimensions(centerX - 100, centerY + 30, 99, 20).build(); // Ajustado hacia abajo

        // Botón Quit Game (ancho 100) centrado a la derecha
        quitGame = ButtonWidget.builder(Text.literal("Quit Game"), button -> {
            this.client.scheduleStop();
        }).dimensions(centerX + 1, centerY + 30, 99, 20).build(); // Ajustado hacia abajo


        // Texto PingText alineado a la izquierda
        pingTextWidget = new TextWidget(10, this.height - 20, this.textRenderer.getWidth(STATUS), 10, Text.of(STATUS), this.textRenderer);

        // Agregar los botones a la pantalla
        addDrawableChild(serverButton);
        addDrawableChild(modsButton);
        addDrawableChild(optionsButton);
        addDrawableChild(quitGame);
        this.addDrawableChild(pingTextWidget);

        // Agregar el texto del menú en la parte inferior derecha
        int i = this.textRenderer.getWidth(MENU);
        int j = this.width - i - 2;
        this.addDrawableChild(new PressableTextWidget(j - 3, this.height - 20, i, 10, MENU, (button -> Util.getOperatingSystem().open("https://victorgponce.com")), this.textRenderer));
    }

    private void changeMusicOnPress() {
        LOGGER_CLIENT.info("Seteando Opción 'Musica' al " + musicCategory*100 + "% (Tecla Presionada)");
        LOGGER_CLIENT.info("Seteando Opción 'Master' al " + masterCategory*100 + "% (Tecla Presionada)");
        MinecraftClient.getInstance().options.getSoundVolumeOption(SoundCategory.MUSIC).setValue(musicCategory);
        MinecraftClient.getInstance().options.getSoundVolumeOption(SoundCategory.MUSIC).setValue(masterCategory);
        first = true;
    }

    public static boolean Pinger(String address, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(address, port), 1000); // Tiempo de espera de 1 segundo
            return true; // Si la conexión es exitosa
        } catch (IOException e) {
            LOGGER_CLIENT.error("Error al conectar con el servidor: " + e.getMessage());
        }
        return false; // Si ocurre algún error o no se puede conectar
    }

    @Override
    public void close() {
        stopPingThread = true; // Indicar que el hilo debe detenerse
        try {
            if (pingThread != null && pingThread.isAlive()) {
                pingThread.join(); // Esperar a que el hilo termine
            }
        } catch (InterruptedException e) {
            LOGGER_CLIENT.error("Error al detener el hilo de ping: " + e.getMessage());
        }
        this.client.setScreen(parent);
    }


    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        // Sonido
        if (first) {
            // Guardar configuración del sonido en variables (float)
            musicCategory = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MUSIC);
            masterCategory = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER);
            LOGGER_CLIENT.info("Configuración de Musica: " + musicCategory);
            LOGGER_CLIENT.info("Configuración Master: " + masterCategory);
            LOGGER_CLIENT.info("Seteando boolean first a true...");
            first = false;

            // Setear nuevas configuraciones temporales para evitar musica que no queremos
            LOGGER_CLIENT.info("Seteando Opción 'Musica' al 0% (Inicio Screen)");
            LOGGER_CLIENT.info("Seteando Opción 'Master' al 50% (Inicio Screen)");
            if (musicCategory != 0.0) {MinecraftClient.getInstance().options.getSoundVolumeOption(SoundCategory.MUSIC).setValue(0.0);}
            if (masterCategory != 0.5) {MinecraftClient.getInstance().options.getSoundVolumeOption(SoundCategory.MASTER).setValue(0.5);}
        }

        if (!sounding) {
            SoundEvent mainThemeEvent = SoundLoader.mainTheme;
            soundInstance = PositionedSoundInstance.master(mainThemeEvent, 1.0f);
            MinecraftClient.getInstance().getSoundManager().play(soundInstance);
            sounding = true;
            LOGGER_CLIENT.info("Ajustando sounding a true");
        }

        // Verificar si el sonido está en reproducción
        if (soundInstance != null && !MinecraftClient.getInstance().getSoundManager().isPlaying(soundInstance)) {
            MinecraftClient.getInstance().getSoundManager().stopAll();
            sounding = false;
            LOGGER_CLIENT.info("Ajustando sounding a false");
        }

        // Obtener el tiempo actual
        long currentTimePing = System.currentTimeMillis();

        // Actualizar el contenido del TextWidget con el nuevo estado
        pingTextWidget.setMessage(Text.of(STATUS));

        // Renderizar la textura de fondo
        Identifier backgroundTextureId = backgroundTextures[currentFrame];
        Optional<Resource> resourceOptional = this.client.getResourceManager().getResource(backgroundTextureId);
        Optional<Resource> resourceTitle = this.client.getResourceManager().getResource(menuTitleId);

        if (resourceOptional.isPresent() && resourceTitle.isPresent()) {
            // La textura se cargó correctamente, renderizarla
            Resource resource = resourceOptional.get();
            RenderSystem.setShaderTexture(0, backgroundTextureId);
            drawContext.drawTexture(backgroundTextureId, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);

            // Textura del Titulo

            int titleWidth = (int) Math.round(869 / 3);
            int titleHeight = (int) Math.round(157 / 3);

            int titleX = (this.width - titleWidth) / 2 + 3;
            int titleY = 35;

            Resource resourceTitleGet = resourceTitle.get();
            RenderSystem.setShaderTexture(0, backgroundTextureId);
            drawContext.drawTexture(menuTitleId, titleX, titleY, 0.0F, 0.0F, titleWidth, titleHeight, titleWidth, titleHeight);
        } else {
            // La textura no se pudo cargar, imprimir un mensaje de error
            LOGGER_CLIENT.info("Error: No se pudo cargar la textura de fondo.");
        }

        // Renderizar elementos en la pantalla
        super.render(drawContext, mouseX, mouseY, delta);

        // Actualizar el fotograma actual basado en el tiempo
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > FRAME_DURATION_MS) {
            currentFrame = (currentFrame + 1) % backgroundTextures.length;
            lastFrameTime = currentTime;
        }
    }

}
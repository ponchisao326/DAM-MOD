package victorgponce.com.dam.screens;

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
import victorgponce.com.dam.loading.SoundLoader;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static victorgponce.com.dam.client.DAM_MODClient.LOGGER_CLIENT;

public class CustomMainMenu extends Screen {
    public ButtonWidget serverButton;
    public ButtonWidget quitGame;
    public ButtonWidget optionsButton;
    public ButtonWidget modsButton;
    private TextWidget pingTextWidget;

    private static final String SERVER_ADDRESS = "node-marb.ponchisaohosting.xyz:25565";
    private static final String NODE = "node-marb.ponchisaohosting.xyz";
    private static final int PORT = 25565;
    private static final String CONNECT_BUTTON_TEXT = "DAM SERVER";
    private static final Text MENU = Text.literal("Menu by PonchisaoHosting");
    private static final Identifier menuTitleId = new Identifier("victorgponce_com_autismon:title_screen/autismon.png");
    private final Identifier[] backgroundTextures = new Identifier[932];

    private Screen parent;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DURATION_MS = 16;
    private boolean sounding = false;
    private PositionedSoundInstance soundInstance;
    private static boolean first = true;
    private double musicCategory = 0.0;
    private double masterCategory = 0.0;
    private String STATUS = "El servidor se encuentra OFFLINE";

    private static final long PING_CHECK_INTERVAL_MS = 4000;
    private boolean stopPingThread = false;
    private List<Resource> loadedTextures = new ArrayList<Resource>();
    // Almacenar la textura del título
    private Optional<Resource> titleResource = Optional.empty();

    public CustomMainMenu() {
        super(Text.of("Main Menu"));
        startPingThread();

        // Inicialización de texturas de fondo
        for (int i = 0; i < backgroundTextures.length; i++) {
            backgroundTextures[i] = new Identifier("victorgponce_com_autismon:title_screen/" + (i + 1) + ".png");
        }

        // Lanzar los demás hilos
        startTextureLoadingThread();
        startTitleLoadingThread();
        startMusicThread();
    }

    // Iniciar el hilo de ping
    private void startPingThread() {
        new Thread(() -> {
            while (!stopPingThread) {
                if (Pinger(NODE, PORT)) {
                    STATUS = "El servidor se encuentra ONLINE";
                } else {
                    STATUS = "El servidor se encuentra OFFLINE";
                }
                try {
                    Thread.sleep(PING_CHECK_INTERVAL_MS);
                } catch (InterruptedException e) {
                    LOGGER_CLIENT.error("El hilo de verificación de ping fue interrumpido: " + e.getMessage());
                }
            }
        }).start();
    }

    // Iniciar hilo para cargar las texturas de fondo
    private void startTextureLoadingThread() {
        new Thread(() -> {
            try {
                for (Identifier texture : backgroundTextures) {
                    MinecraftClient.getInstance().execute(() -> { // Ejecutar en el hilo principal
                        Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(texture);
                        resource.ifPresent(res -> {
                            synchronized (loadedTextures) {
                                loadedTextures.add(res);
                            }
                        });
                    });
                }
                LOGGER_CLIENT.info("Todas las texturas de fondo han sido cargadas.");
            } catch (Exception e) {
                LOGGER_CLIENT.error("Error al cargar texturas: " + e.getMessage());
            }
        }).start();
    }



    // Iniciar hilo para cargar el título
    private void startTitleLoadingThread() {
        new Thread(() -> {
            try {
                MinecraftClient.getInstance().execute(() -> { // Ejecutar en el hilo principal
                    titleResource = MinecraftClient.getInstance()
                            .getResourceManager()
                            .getResource(menuTitleId);
                    if (titleResource.isPresent()) {
                        LOGGER_CLIENT.info("Título cargado correctamente.");
                    } else {
                        LOGGER_CLIENT.warn("No se pudo cargar el título.");
                    }
                });
            } catch (Exception e) {
                LOGGER_CLIENT.error("Error al cargar el título: " + e.getMessage());
            }
        }).start();
    }


    // Iniciar el hilo de música
    private void startMusicThread() {
        new Thread(() -> {
            try {
                SoundEvent mainThemeEvent = SoundLoader.mainTheme;
                soundInstance = PositionedSoundInstance.master(mainThemeEvent, 1.0f);
                MinecraftClient.getInstance().getSoundManager().play(soundInstance);
                LOGGER_CLIENT.info("Reproduciendo música principal.");
                sounding = true;

                // Verificar si el sonido sigue reproduciéndose
                while (sounding) {
                    MinecraftClient.getInstance().execute(() -> {
                        if (soundInstance != null &&
                                MinecraftClient.getInstance().getSoundManager().isPlaying(soundInstance) == Boolean.TRUE) {
                        } else {
                            LOGGER_CLIENT.info("Música detenida.");
                            sounding = false;
                        }
                    });
                    Thread.sleep(1000); // Esperar un segundo antes de volver a verificar
                }
            } catch (Exception e) {
                LOGGER_CLIENT.error("Error en el hilo de música: " + e.getMessage());
            }
        }).start();
    }


    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        serverButton = new ButtonWidget.Builder(Text.of(CONNECT_BUTTON_TEXT), button -> {
            ServerInfo info = new ServerInfo(I18n.translate("selectServer.defaultName"), SERVER_ADDRESS, false);
            ConnectScreen.connect(this, this.client, ServerAddress.parse(SERVER_ADDRESS), info, true);
            button.playDownSound(this.client.getSoundManager());
            changeMusicOnPress();
        }).dimensions(centerX - 100, centerY - 12, 200, 20).build();

        modsButton = new ModMenuButtonWidget(centerX - 100, centerY + 9, 200, 20, Text.of("Mods"), this);
        optionsButton = new ButtonWidget.Builder(Text.of("Options..."), button -> {
            MinecraftClient.getInstance().setScreen(new CustomOptionsScreen(this, this.client.options));
            changeMusicOnPress();
        }).dimensions(centerX - 100, centerY + 30, 99, 20).build();

        quitGame = ButtonWidget.builder(Text.literal("Quit Game"), button -> this.client.scheduleStop())
                .dimensions(centerX + 1, centerY + 30, 99, 20).build();

        pingTextWidget = new TextWidget(10, this.height - 20, this.textRenderer.getWidth(STATUS), 10, Text.of(STATUS), this.textRenderer);

        addDrawableChild(serverButton);
        addDrawableChild(modsButton);
        addDrawableChild(optionsButton);
        addDrawableChild(quitGame);
        addDrawableChild(pingTextWidget);

        int i = this.textRenderer.getWidth(MENU);
        int j = this.width - i - 2;
        addDrawableChild(new PressableTextWidget(j - 3, this.height - 20, i, 10, MENU, button -> Util.getOperatingSystem().open("https://victorgponce.com"), this.textRenderer));
    }

    private void changeMusicOnPress() {
        LOGGER_CLIENT.info("Seteando 'Musica' al " + musicCategory * 100 + "% y 'Master' al " + masterCategory * 100 + "%.");
        MinecraftClient.getInstance().options.getSoundVolumeOption(SoundCategory.MUSIC).setValue(musicCategory);
        MinecraftClient.getInstance().options.getSoundVolumeOption(SoundCategory.MASTER).setValue(masterCategory);
        first = true;
    }

    public static boolean Pinger(String address, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(address, port), 1000);
            return true;
        } catch (IOException e) {
            LOGGER_CLIENT.error("Error al conectar con el servidor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void close() {
        stopPingThread = true;
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > FRAME_DURATION_MS) {
            currentFrame = (currentFrame + 1) % backgroundTextures.length;
            lastFrameTime = currentTime;
        }

        Identifier backgroundTextureId = backgroundTextures[currentFrame];
        RenderSystem.setShaderTexture(0, backgroundTextureId);
        drawContext.drawTexture(backgroundTextureId, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
        pingTextWidget.setMessage(Text.of(STATUS));
        super.render(drawContext, mouseX, mouseY, delta);

        // Actualizar el mensaje de estado
        pingTextWidget.setMessage(Text.of(STATUS));

        // Calcular el ancho y alto del título
        int titleWidth = (int) Math.round(824 / 3.5);
        int titleHeight = (int) Math.round(219 / 3.5);

        // Dibujar el título si se ha cargado
        if (titleResource.isPresent()) {
            RenderSystem.setShaderTexture(0, menuTitleId);
            int titleX = (this.width - titleWidth) / 2 + 5; // Centrar el título
            int topMargin = -120; // Margen desde el top
            int titleY = topMargin + (this.height / 2);
            drawContext.drawTexture(menuTitleId, titleX, titleY, 0, 0, titleWidth, titleHeight, titleWidth, titleHeight);
        }
    }
}

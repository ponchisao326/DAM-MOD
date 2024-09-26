package victorgponce.com.autismonmod.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.gui.widget.ModMenuButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.resource.Resource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;


public class CustomMainMenu extends Screen {
    public ButtonWidget serverButton;
    public ButtonWidget quitGame;
    public ButtonWidget optionsButton;
    public ButtonWidget modsButton;
    public ButtonWidget customCreditsButton;

    private static final String SERVER_ADDRESS = "node-marb.ponchisaohosting.xyz:25565";
    private static final String CONNECT_BUTTON_TEXT = "AUTISMON";
    private static final Text MENU = Text.literal("Menu by PonchisaoHosting");
    private Screen parent;
    private final Identifier[] backgroundTextures = new Identifier[91];

    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DURATION_MS = 60; // Duración en milisegundos de cada fotograma

    public CustomMainMenu() {
        super(Text.of("Main Menu"));

        // Inicializar los identificadores de textura
        for (int i = 0; i < backgroundTextures.length; i++) {
            backgroundTextures[i] = new Identifier("victorgponce_com_autismon:title_screen/" + (i + 1) + ".png");
        }
    }

    @Override
    protected void init() {
        super.init();

        ServerInfo info = new ServerInfo(I18n.translate("selectServer.defaultName"), SERVER_ADDRESS, false);
        info.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.PROMPT);

        serverButton = new ButtonWidget.Builder(Text.of(CONNECT_BUTTON_TEXT), (buttonWidget) -> {
            ConnectScreen.connect(this, this.client, ServerAddress.parse(SERVER_ADDRESS), info, true);
            buttonWidget.playDownSound(this.client.getSoundManager());
        }).dimensions(this.width / 2 - 100, this.height / 2 - 30, 200, 20).build();

        quitGame = ButtonWidget.builder(Text.literal("Quit Game"), button -> {
            this.client.scheduleStop();
        }).dimensions(this.width / 2 + 2, this.height / 4 + 48 + 75, 98, 20).build();

        optionsButton = new ButtonWidget.Builder(Text.of("Options..."), (buttonWidget) -> {
            MinecraftClient.getInstance().setScreen(new OptionsScreen(this, this.client.options));
        }).dimensions((int) (this.width / 2 - 100), this.height / 4 + 48 + 75, 100, 20).build();

        customCreditsButton = new ButtonWidget.Builder(Text.of("Custom Credits"), (buttonWidget) -> {
            showCustomCredits();
        }).dimensions(this.width / 2 - 100, this.height / 4 + 48 + 52, 200, 20).build();

        modsButton = new ModMenuButtonWidget(this.width / 2 - 100, this.height / 4 + 48 + 30, 200, 20, Text.of("Mods"), this);

        addDrawableChild(serverButton);
        addDrawableChild(quitGame);
        addDrawableChild(optionsButton);
        addDrawableChild(modsButton);
        addDrawableChild(customCreditsButton);

        int i = this.textRenderer.getWidth(MENU);
        int j = this.width - i - 2;

        this.addDrawableChild(new TextWidget(j - 3, this.height - 20, i, 10, MENU, this.textRenderer));
    }

    public void showCustomCredits() {
        // CustomCreditsScreen creditsScreen = new CustomCreditsScreen(this);
        // this.client.setScreen(creditsScreen);
    }

    @Override
    public void close() {
        if (this.parent != null) {
            client.setScreen(parent);
        } else {
            // Si no hay una pantalla padre, se comporta como la pantalla principal y sale del juego
            this.client.openPauseMenu(false);
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        // Renderizar la textura de fondo
        Identifier backgroundTextureId = backgroundTextures[currentFrame];
        Optional<Resource> resourceOptional = this.client.getResourceManager().getResource(backgroundTextureId);

        if (resourceOptional.isPresent()) {
            // La textura se cargó correctamente, renderizarla
            Resource resource = resourceOptional.get();
            RenderSystem.setShaderTexture(0, backgroundTextureId);
            drawContext.drawTexture(backgroundTextureId, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
        } else {
            // La textura no se pudo cargar, imprimir un mensaje de error
            System.out.println("Error: No se pudo cargar la textura de fondo.");
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
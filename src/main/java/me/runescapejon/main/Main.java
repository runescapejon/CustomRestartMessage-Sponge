package me.runescapejon.main;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "customrestartmessage-sponge", name = "CustomRestartMessage-Sponge", authors = {
		"runescapejon" }, description = "Have a custom restart/stop message that fits your server needs when you stop/restart your server", version = "1.0")
public class Main {
	private ConfigurationNode Config;
	private File DConfig;
	private ConfigurationLoader<CommentedConfigurationNode> ConfigNode;
	public static Main instance;

	public ConfigurationLoader<CommentedConfigurationNode> getConfig() {
		return this.ConfigNode;
	}

	@Inject
	public Main(@DefaultConfig(sharedRoot = false) File DConfig,
			@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> Configs) {
		this.DConfig = DConfig;
		this.ConfigNode = Configs;
		instance = this;
	}

	@Listener
	public void onPreInit(GamePreInitializationEvent event) throws IOException {
		try {
			if (!this.DConfig.exists()) {
				this.DConfig.createNewFile();

				this.Config = getConfig().load();
				this.Config.getNode("Languages", "CustomRestartMessage")
						.setValue("&6&lServer is restarting, please re-login in about 1 minute thank you <3");
				getConfig().save(this.Config);
			}
			this.Config = getConfig().load();
			Language.SetLang(TextSerializers.FORMATTING_CODE
					.deserialize(this.Config.getNode("Languages", "CustomRestartMessage").getString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//using vanilla permission to override /stop and /restart so the message can appear when automatically restarting with task or other means of restarting
	@Listener
	public void init(GameInitializationEvent e) {
		CommandSpec restartSpec = CommandSpec.builder().permission("minecraft.command.stop").executor(this::execute)
				.build();
		Sponge.getCommandManager().register(this, restartSpec,  "stop", "restart");
	}

	public CommandResult execute(CommandSource src, CommandContext args) {
		Text msg = Text.builder().append(Language.getLang()).build();
		Sponge.getServer().shutdown(msg);
		return CommandResult.success();
	}
}

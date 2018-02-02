package me.runescapejon.main;

import org.spongepowered.api.text.Text;

public class Language {
	private static Text msg = Text.builder()
			.append(Text.of("&6&lYour whole inventory was saved from your recent Death.")).build();

	public static void SetLang(Text textmsg) {
		msg = textmsg;
	}

	public static Text getLang() {
		return msg;
	}
}
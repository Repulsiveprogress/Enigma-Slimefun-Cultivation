package dev.sefiraat.cultivation.managers;

import dev.sefiraat.cultivation.Cultivation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads localization YAML files from the plugin jar (resources/lang/<code>.yml)
 * and from plugins/Cultivation/lang/<code>.yml (community overrides).
 * <p>
 * Lookup order: selected language -> en -> hardcoded fallback string.
 */
public class LanguageManager {

    private static final String DEFAULT_LANG = "en";
    private static final String[] BUNDLED_LANGS = {"en", "ru"};

    private final FileConfiguration selected;
    private final FileConfiguration english;
    private final String code;

    public LanguageManager(@Nonnull String code) {
        this.code = code;
        extractBundled();
        this.english = load(DEFAULT_LANG);
        this.selected = code.equalsIgnoreCase(DEFAULT_LANG) ? this.english : load(code);
    }

    private void extractBundled() {
        Cultivation plugin = Cultivation.getInstance();
        File langDir = new File(plugin.getDataFolder(), "lang");
        if (!langDir.exists() && !langDir.mkdirs()) {
            plugin.getLogger().warning("Could not create lang folder.");
        }
        for (String c : BUNDLED_LANGS) {
            File target = new File(langDir, c + ".yml");
            if (target.exists()) {
                continue;
            }
            try (InputStream in = plugin.getResource("lang/" + c + ".yml")) {
                if (in != null) {
                    Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to extract lang/" + c + ".yml: " + e.getMessage());
            }
        }
    }

    @Nonnull
    private FileConfiguration load(@Nonnull String c) {
        Cultivation plugin = Cultivation.getInstance();
        File external = new File(plugin.getDataFolder(), "lang/" + c + ".yml");
        if (external.exists()) {
            return YamlConfiguration.loadConfiguration(external);
        }
        InputStream in = plugin.getResource("lang/" + c + ".yml");
        if (in != null) {
            return YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        return new YamlConfiguration();
    }

    /**
     * Returns the localized string for the given key, falling back to English,
     * then to the supplied default.
     */
    @Nonnull
    public String get(@Nonnull String key, @Nonnull String fallback) {
        String value = selected.getString(key);
        if (value != null) {
            return value;
        }
        value = english.getString(key);
        return value != null ? value : fallback;
    }

    @Nullable
    public String getOrNull(@Nonnull String key) {
        String value = selected.getString(key);
        if (value != null) {
            return value;
        }
        return english.getString(key);
    }

    /**
     * Returns the localized list (e.g. lore) for the given key. Falls back to English,
     * then to the supplied default list.
     */
    @Nonnull
    public List<String> getList(@Nonnull String key, @Nonnull List<String> fallback) {
        if (selected.isList(key)) {
            List<String> list = selected.getStringList(key);
            if (!list.isEmpty()) {
                return list;
            }
        }
        if (english.isList(key)) {
            List<String> list = english.getStringList(key);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return new ArrayList<>(fallback);
    }

    @Nonnull
    public String getCode() {
        return code;
    }
}

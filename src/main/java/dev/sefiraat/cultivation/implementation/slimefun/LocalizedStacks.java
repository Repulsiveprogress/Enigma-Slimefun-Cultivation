package dev.sefiraat.cultivation.implementation.slimefun;

import dev.sefiraat.cultivation.Cultivation;
import dev.sefiraat.cultivation.managers.LanguageManager;
import dev.sefiraat.sefilib.string.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Builds {@link SlimefunItemStack} instances using localization keys.
 * <p>
 * For each item id (e.g. {@code CLT_TREE_BANANA}) the manager looks up:
 * <ul>
 *     <li>{@code items.<ID>.name} — display name</li>
 *     <li>{@code items.<ID>.lore} — string list lore</li>
 * </ul>
 * Missing keys fall back to the supplied English defaults so the build never breaks
 * when the lang files are incomplete.
 */
public final class LocalizedStacks {

    private LocalizedStacks() {
        throw new IllegalStateException("Utility class");
    }

    @Nonnull
    public static SlimefunItemStack themed(@Nonnull String id,
                                           @Nonnull ItemStack base,
                                           @Nonnull Theme theme,
                                           @Nonnull String defaultName,
                                           @Nonnull List<String> defaultLore) {
        LanguageManager lm = Cultivation.getLanguageManager();
        String name = lm != null ? lm.get("items." + id + ".name", defaultName) : defaultName;
        List<String> lore = lm != null ? lm.getList("items." + id + ".lore", defaultLore) : defaultLore;
        return Theme.themedSlimefunItemStack(id, base, theme, name, lore);
    }

    /**
     * Varargs lore overload — matches the original {@code Theme.themedSlimefunItemStack(id, base, theme, name, "l1", "l2", ...)} shape.
     */
    @Nonnull
    public static SlimefunItemStack themed(@Nonnull String id,
                                           @Nonnull ItemStack base,
                                           @Nonnull Theme theme,
                                           @Nonnull String defaultName,
                                           @Nonnull String... defaultLore) {
        return themed(id, base, theme, defaultName, List.of(defaultLore));
    }

    /** Material+varargs lore overload (matches original {@code Theme.themedSlimefunItemStack(id, mat, theme, name, "l1", ...)}). */
    @Nonnull
    public static SlimefunItemStack themed(@Nonnull String id,
                                           @Nonnull Material base,
                                           @Nonnull Theme theme,
                                           @Nonnull String defaultName,
                                           @Nonnull String... defaultLore) {
        return themed(id, new ItemStack(base), theme, defaultName, List.of(defaultLore));
    }

    /** Material+List lore overload. */
    @Nonnull
    public static SlimefunItemStack themed(@Nonnull String id,
                                           @Nonnull Material base,
                                           @Nonnull Theme theme,
                                           @Nonnull String defaultName,
                                           @Nonnull List<String> defaultLore) {
        return themed(id, new ItemStack(base), theme, defaultName, defaultLore);
    }
}

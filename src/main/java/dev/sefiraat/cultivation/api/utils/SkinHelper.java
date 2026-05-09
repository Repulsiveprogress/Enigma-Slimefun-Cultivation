package dev.sefiraat.cultivation.api.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.util.Base64;
import java.util.UUID;

/**
 * Replacement for dough's PlayerHead/PlayerSkin using Paper API directly.
 * Avoids CustomGameProfile which cannot extend final GameProfile in MC 26.1+.
 */
public final class SkinHelper {

    private SkinHelper() {}

    /**
     * Creates a player head ItemStack with the given texture hash.
     */
    @Nonnull
    public static ItemStack getSkullFromHash(@Nonnull String hash) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setPlayerProfile(buildProfile(hash));
            skull.setItemMeta(meta);
        }
        return skull;
    }

    /**
     * Applies a texture hash to an existing block (sets type to PLAYER_HEAD first).
     */
    public static void setSkinOnBlock(@Nonnull Block block, @Nonnull String hash, boolean applyPhysics) {
        block.setType(Material.PLAYER_HEAD, applyPhysics);
        if (block.getState() instanceof Skull skull) {
            skull.setPlayerProfile(buildProfile(hash));
            skull.update(true, applyPhysics);
        }
    }

    @Nonnull
    private static PlayerProfile buildProfile(@Nonnull String hash) {
        PlayerProfile profile = Bukkit.createProfile(UUID.nameUUIDFromBytes(hash.getBytes()), "CultivationSkin");
        // Mojang texture value is base64-encoded JSON with the URL inside
        String textureUrl = "https://textures.minecraft.net/texture/" + hash;
        String textureJson = "{\"textures\":{\"SKIN\":{\"url\":\"" + textureUrl + "\"}}}";
        String encoded = Base64.getEncoder().encodeToString(textureJson.getBytes());
        profile.setProperty(new ProfileProperty("textures", encoded));
        return profile;
    }
}

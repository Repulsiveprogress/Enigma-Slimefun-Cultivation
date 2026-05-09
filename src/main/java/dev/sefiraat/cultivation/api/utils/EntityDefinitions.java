package dev.sefiraat.cultivation.api.utils;

import dev.sefiraat.cultivation.Cultivation;
import dev.sefiraat.sefilib.entity.LivingEntityCategory;
import dev.sefiraat.sefilib.entity.LivingEntityDefinition;
import dev.sefiraat.sefilib.entity.LivingEntitySelector;
import dev.sefiraat.sefilib.dough.versions.SemanticVersion;
import org.bukkit.Server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class EntityDefinitions {

    private EntityDefinitions() {
        throw new IllegalStateException("Utility class");
    }

    private static Set<LivingEntityDefinition> passiveMobs;
    private static Set<LivingEntityDefinition> hostileMobs;
    private static Set<LivingEntityDefinition> bossMobs;
    private static Set<LivingEntityDefinition> flyingMobs;

    static {
        Server server = Cultivation.getInstance().getServer();
        SemanticVersion version;
        try {
            version = SemanticVersion.parse(server.getMinecraftVersion());
        } catch (Exception e) {
            version = new SemanticVersion(26, 1);
        }

        passiveMobs = LivingEntitySelector.start()
            .includeCategories(LivingEntityCategory.PASSIVE)
            .setVersion(version)
            .process(LivingEntitySelector.MatchType.MATCH_ALL);

        hostileMobs = LivingEntitySelector.start()
            .includeCategories(LivingEntityCategory.HOSTILE)
            .setVersion(version)
            .process(LivingEntitySelector.MatchType.MATCH_ALL);

        bossMobs = LivingEntitySelector.start()
            .includeCategories(LivingEntityCategory.BOSS)
            .setVersion(version)
            .process(LivingEntitySelector.MatchType.MATCH_ALL);

        flyingMobs = LivingEntitySelector.start()
            .includeCategories(LivingEntityCategory.FLYING)
            .setVersion(version)
            .process(LivingEntitySelector.MatchType.MATCH_ALL);
    }

    public static Set<LivingEntityDefinition> getPassiveMobs() {
        return Collections.unmodifiableSet(passiveMobs);
    }

    public static Set<LivingEntityDefinition> getHostileMobs() {
        return Collections.unmodifiableSet(hostileMobs);
    }

    public static Set<LivingEntityDefinition> getBossMobs() {
        return Collections.unmodifiableSet(bossMobs);
    }

    public static Set<LivingEntityDefinition> getFlyingMobs() {
        return flyingMobs;
    }
}

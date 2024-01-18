package dev.worldgen.lithostitched.worldgen.modifier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.LithostitchedCommon;
import dev.worldgen.lithostitched.access.StructurePoolAccess;
import dev.worldgen.lithostitched.mixin.common.StructureTemplatePoolAccessor;
import dev.worldgen.lithostitched.worldgen.modifier.predicate.ModifierPredicate;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.ShufflingList;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Modifier} implementation that adds template pool elements to a {@link StructureTemplatePool} entry.
 *
 * @author Apollo
 */
public class  AddTemplatePoolElementsModifier extends Modifier {
    public static final Codec<AddTemplatePoolElementsModifier> CODEC = RecordCodecBuilder.create(instance -> addModifierFields(instance).and(instance.group(
        ResourceLocation.CODEC.fieldOf("template_pool").forGetter(AddTemplatePoolElementsModifier::rawTemplatePoolLocation),
        Codec.mapPair(
            StructurePoolElement.CODEC.fieldOf("element"),
            Codec.intRange(1, 150).fieldOf("weight")
        ).codec().listOf().fieldOf("elements").forGetter(AddTemplatePoolElementsModifier::elements),
        RegistryOps.retrieveGetter(Registries.TEMPLATE_POOL)
    )).apply(instance, AddTemplatePoolElementsModifier::new));

    private final ResourceKey<StructureTemplatePool> EMPTY_TEMPLATE_POOL = ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(LithostitchedCommon.MOD_ID, "empty"));
    private final ResourceLocation rawTemplatePoolLocation;
    private final Holder<StructureTemplatePool> templatePool;
    private final List<Pair<StructurePoolElement, Integer>> elements;

    public AddTemplatePoolElementsModifier(ModifierPredicate predicate, ResourceLocation rawTemplatePoolLocation, List<Pair<StructurePoolElement, Integer>> elements, HolderGetter<StructureTemplatePool> getter) {
        super(predicate, ModifierPhase.ADD);
        this.rawTemplatePoolLocation = rawTemplatePoolLocation;
        var templatePoolEntry = getter.get(predicate.test() ? ResourceKey.create(Registries.TEMPLATE_POOL, rawTemplatePoolLocation) : EMPTY_TEMPLATE_POOL);
        this.templatePool = templatePoolEntry.get();
        this.elements = elements;
    }

    public Holder<StructureTemplatePool> templatePool() {
        return this.templatePool;
    }
    public ResourceLocation rawTemplatePoolLocation() {
        return this.rawTemplatePoolLocation;
    }

    public List<Pair<StructurePoolElement, Integer>> elements() {
        return this.elements;
    }

    @Override
    public Codec<? extends Modifier> codec() {
        return CODEC;
    }

    @Override
    public void applyModifier() {
        if (this.templatePool.is(EMPTY_TEMPLATE_POOL)) return;

        StructureTemplatePoolAccessor structurePoolAccessor = ((StructureTemplatePoolAccessor)this.templatePool().value());

        List<Pair<StructurePoolElement, Integer>> originalElementCounts = new ArrayList<>(structurePoolAccessor.getRawTemplates());
        originalElementCounts.addAll(this.elements());
        structurePoolAccessor.setRawTemplates(originalElementCounts);


        ObjectArrayList<StructurePoolElement> originalElements = new ObjectArrayList<>(structurePoolAccessor.getTemplates());
        ShufflingList<StructurePoolElement> lithostitchedPoolElements = new ShufflingList<>();
        for (Pair<StructurePoolElement, Integer> pair : this.elements()) {
            lithostitchedPoolElements.add(pair.getFirst(), pair.getSecond());
            StructurePoolElement structurePoolElement = pair.getFirst();

            for (int i = 0; i < pair.getSecond(); ++i) {
                originalElements.add(structurePoolElement);
            }
        }
        structurePoolAccessor.setTemplates(originalElements);
        ((StructurePoolAccess)this.templatePool().value()).lithostitched$setStructurePoolElements(lithostitchedPoolElements);
    }
}

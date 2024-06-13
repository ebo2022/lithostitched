package dev.worldgen.lithostitched.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.mixin.common.StructureSetAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Modifier} implementation that adds structure set entries to a {@link StructureSet} entry.
 *
 * @author Apollo
 */
public record AddStructureSetEntriesModifier(Holder<StructureSet> structureSet, List<StructureSet.StructureSelectionEntry> entries) implements Modifier {
    public static final MapCodec<AddStructureSetEntriesModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StructureSet.CODEC.fieldOf("structure_set").forGetter(AddStructureSetEntriesModifier::structureSet),
        StructureSet.StructureSelectionEntry.CODEC.listOf().fieldOf("entries").forGetter(AddStructureSetEntriesModifier::entries)
    ).apply(instance, AddStructureSetEntriesModifier::new));

    @Override
    public ModifierPhase getPhase() {
        return ModifierPhase.ADD;
    }

    @Override
    public void applyModifier() {
        StructureSetAccessor structureSetAccessor = ((StructureSetAccessor)(Object)this.structureSet().value());
        List<StructureSet.StructureSelectionEntry> structureSelectionEntries = new ArrayList<>(this.structureSet().value().structures());
        structureSelectionEntries.addAll(this.entries());
        structureSetAccessor.setStructures(structureSelectionEntries);
    }

    @Override
    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}

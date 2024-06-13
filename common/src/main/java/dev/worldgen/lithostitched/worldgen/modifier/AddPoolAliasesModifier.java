package dev.worldgen.lithostitched.worldgen.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.worldgen.lithostitched.mixin.common.JigsawStructureAccessor;
import dev.worldgen.lithostitched.worldgen.structure.AlternateJigsawStructure;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.ArrayList;
import java.util.List;

public record AddPoolAliasesModifier(Holder<Structure> structure, List<PoolAliasBinding> poolAliases) implements Modifier {
    public static final MapCodec<AddPoolAliasesModifier> CODEC = RecordCodecBuilder.<AddPoolAliasesModifier>mapCodec(instance -> instance.group(
        Structure.CODEC.fieldOf("structure").forGetter(AddPoolAliasesModifier::structure),
        Codec.list(PoolAliasBinding.CODEC).fieldOf("pool_aliases").forGetter(AddPoolAliasesModifier::poolAliases)
    ).apply(instance, AddPoolAliasesModifier::new)).validate(AddPoolAliasesModifier::validate);

    private static DataResult<AddPoolAliasesModifier> validate(AddPoolAliasesModifier modifier) {
        Structure structure = modifier.structure.value();
        if (!(structure instanceof JigsawStructure || structure instanceof AlternateJigsawStructure)) {
            return DataResult.error(() -> "Target structure for pool alias additions should be a jigsaw structure");
        }
        return DataResult.success(modifier);
    }

    @Override
    public ModifierPhase getPhase() {
        return ModifierPhase.ADD;
    }

    @Override
    public void applyModifier() {
        Structure structure = this.structure.value();

        if (structure instanceof AlternateJigsawStructure alternateJigsaw) {
            alternateJigsaw.addPoolAliases(this.poolAliases);
        } else {
            List<PoolAliasBinding> mergedAliases = new ArrayList<>(((JigsawStructureAccessor)structure).getPoolAliases());
            mergedAliases.addAll(this.poolAliases);
            ((JigsawStructureAccessor)structure).setPoolAliases(mergedAliases);
        }
    }

    @Override
    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}

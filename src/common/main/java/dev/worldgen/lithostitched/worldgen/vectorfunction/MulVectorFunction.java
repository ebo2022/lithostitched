package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public record MulVectorFunction(VectorFunction argument1, VectorFunction argument2, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) implements VectorFunction.PureBiTransformer {
    public static final MapCodec<MulVectorFunction> CODEC = LithostitchedCodecs.doubleArgument(
            VectorFunction.HOLDER_HELPER_CODEC,
            Codec.withAlternative(VectorFunction.HOLDER_HELPER_CODEC, DensityFunction.HOLDER_HELPER_CODEC, df -> new ComponentsVectorFunction(df, df, df)),
            MulVectorFunction::new,
            MulVectorFunction::argument1,
            MulVectorFunction::argument2
    );

    public MulVectorFunction(VectorFunction argument1, VectorFunction argument2) {
        this(
                argument1, argument2,
                argument1.minX() * argument2.minX(),
                argument1.maxX() * argument2.maxX(),
                argument1.minY() * argument2.minY(),
                argument2.maxY() * argument2.maxY(),
                argument1.minZ() * argument2.minZ(),
                argument2.maxZ() * argument2.maxZ()
        );
    }

    @Override
    public Vec3 transform(Vec3 argument1, Vec3 argument2) {
        return argument1.multiply(argument2);
    }

    @Override
    public void fillArray(Vec3[] output, ContextProvider provider) {
        this.argument1.fillArray(output, provider);

        Vec3[] array2 = new Vec3[output.length];
        this.argument2.fillArray(array2, provider);

        for (int i = 0; i < output.length; ++i) {
            output[i] = output[i].multiply(array2[i]);
        }
    }

    @Override
    public VectorFunction mapAll(Visitor visitor) {
        return visitor.visit(new MulVectorFunction(this.argument1.mapAll(visitor), this.argument2.mapAll(visitor)));
    }

    @Override
    public MapCodec<? extends VectorFunction> codec() {
        return CODEC;
    }
}

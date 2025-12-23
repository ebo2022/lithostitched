package dev.worldgen.lithostitched.worldgen.vectorfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.registry.LithostitchedRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

/**
 * A {@link Vec3}-returning analogue to vanilla density functions.
 * <p>Custom types should be registered to {@link LithostitchedRegistryKeys#VECTOR_FUNCTION_TYPE}.
 *
 * @author ebo2022
 */
public interface VectorFunction {
    @SuppressWarnings("unchecked")
    Codec<VectorFunction> DIRECT_CODEC = Codec.withAlternative(Codec.lazyInitialized(() -> {
        var registry = BuiltInRegistries.REGISTRY.getOptional(LithostitchedRegistryKeys.VECTOR_FUNCTION_TYPE.location());
        if (registry.isEmpty()) throw new NullPointerException("Vector function type registry does not exist yet!");
        return ((Registry<MapCodec<? extends VectorFunction>>) registry.get()).byNameCodec();
    }).dispatch(VectorFunction::codec, Function.identity()), Vec3.CODEC, ConstantVectorFunction::new);
    Codec<Holder<VectorFunction>> CODEC = RegistryFileCodec.create(LithostitchedRegistryKeys.VECTOR_FUNCTION, DIRECT_CODEC);
    Codec<VectorFunction> HOLDER_HELPER_CODEC = CODEC.xmap(VectorFunctionHolderHolder::new, function ->
            function instanceof VectorFunctionHolderHolder(Holder<VectorFunction> wrapped)
                    ? wrapped
                    : new Holder.Direct<>(function)
    );

    Vec3 compute(FunctionContext context);

    void fillArray(Vec3[] array, ContextProvider provider);

    VectorFunction mapAll(Visitor visitor);

    MapCodec<? extends VectorFunction> codec();

    interface FunctionContext extends DensityFunction.FunctionContext {}

    record SinglePointContext(int blockX, int blockY, int blockZ) implements FunctionContext {}

    interface ContextProvider extends DensityFunction.ContextProvider {

        void fillAllDirectly(Vec3[] vec3s, VectorFunction function);
    }

    interface Visitor {
        VectorFunction apply(VectorFunction function);

        default DensityFunction visitDensity(DensityFunction function) {
            return function;
        }

        default DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder holder) {
            return holder;
        }
    }

    interface SimpleFunction extends VectorFunction {
        @Override
        default void fillArray(Vec3[] array, ContextProvider provider) {
            provider.fillAllDirectly(array, this);
        }

        @Override
        default VectorFunction mapAll(Visitor visitor) {
            return visitor.apply(this);
        }
    }

    interface PureTransformer extends VectorFunction {
        VectorFunction input();

        Vec3 transform(Vec3 input);

        @Override
        default Vec3 compute(FunctionContext context) {
            return this.transform(this.input().compute(context));
        }

        @Override
        default void fillArray(Vec3[] array, ContextProvider provider) {
            this.input().fillArray(array, provider);

            for(int $$2 = 0; $$2 < array.length; ++$$2) {
                array[$$2] = this.transform(array[$$2]);
            }
        }
    }

    interface PureBiTransformer extends VectorFunction {
        VectorFunction argument1();

        VectorFunction argument2();

        Vec3 transform(Vec3 argument1, Vec3 argument2);

        @Override
        default Vec3 compute(FunctionContext context) {
            return this.transform(this.argument1().compute(context), this.argument2().compute(context));
        }

        @Override
        default void fillArray(Vec3[] array, ContextProvider provider) {
            this.argument1().fillArray(array, provider);

            Vec3[] array2 = new Vec3[array.length];
            this.argument2().fillArray(array2, provider);

            for (int i = 0; i < array.length; ++i) {
                array[i] = this.transform(array[i], array2[i]);
            }
        }
    }

    interface TransformerWithContext extends VectorFunction {
        VectorFunction input();

        Vec3 transform(Vec3 input, FunctionContext context);

        @Override
        default Vec3 compute(FunctionContext context) {
            return this.transform(this.input().compute(context), context);
        }
    }
}

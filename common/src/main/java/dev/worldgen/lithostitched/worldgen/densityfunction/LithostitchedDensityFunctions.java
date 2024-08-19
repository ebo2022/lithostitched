package dev.worldgen.lithostitched.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.function.BiFunction;
import java.util.function.Function;

public class LithostitchedDensityFunctions {
    private static <A, O> KeyDispatchDataCodec<O> singleArgumentCodec(Codec<A> codec, Function<A, O> function, Function<O, A> function2) {
        return KeyDispatchDataCodec.of(codec.fieldOf("argument").xmap(function, function2));
    }

    private static <O> KeyDispatchDataCodec<O> singleFunctionArgumentCodec(Function<DensityFunction, O> function, Function<O, DensityFunction> function2) {
        return singleArgumentCodec(DensityFunction.HOLDER_HELPER_CODEC, function, function2);
    }

    private static <O> KeyDispatchDataCodec<O> doubleFunctionArgumentCodec(BiFunction<DensityFunction, DensityFunction, O> constructor, Function<O, DensityFunction> arg1Getter, Function<O, DensityFunction> arg2Getter) {
        return KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(instance -> instance.group(
                DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(arg1Getter),
                DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(arg2Getter)
        ).apply(instance, constructor)));
    }


    private record IntMapped(Type type, DensityFunction input, double minValue, double maxValue) implements DensityFunctions.PureTransformer {

        private static double transform(Type type, double input) {
            return switch (type) {
                case FLOOR -> Mth.floor(input);
                case CEIL -> Mth.ceil(input);
            };
        }

        public static IntMapped create(Type type, DensityFunction input) {
            double minValue = transform(type, input.minValue());
            double maxValue = transform(type, input.maxValue());
            return new IntMapped(type, input, minValue, maxValue);
        }

        @Override
        public DensityFunction input() {
            return this.input;
        }

        @Override
        public double transform(double input) {
            return transform(this.type, input);
        }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return create(this.type, this.input.mapAll(visitor));
        }

        @Override
        public double minValue() {
            return this.minValue;
        }

        @Override
        public double maxValue() {
            return this.maxValue;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return this.type.codec;
        }

        private enum Type implements StringRepresentable {
            FLOOR("floor"),
            CEIL("ceil");

            private final String name;
            private final KeyDispatchDataCodec<IntMapped> codec = singleFunctionArgumentCodec(input -> create(this, input), IntMapped::input);

            Type(String name) {
                this.name = name;
            }

            @Override
            public String getSerializedName() {
                return this.name;
            }
        }
    }
}

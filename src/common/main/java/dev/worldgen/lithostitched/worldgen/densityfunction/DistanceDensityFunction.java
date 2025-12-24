package dev.worldgen.lithostitched.worldgen.densityfunction;

import dev.worldgen.lithostitched.worldgen.LithostitchedCodecs;
import dev.worldgen.lithostitched.worldgen.vectorfunction.VectorFunction;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.phys.Vec3;

public class DistanceDensityFunction implements VectorOpDensityFunction {
    private final VectorFunction argument1;
    private final VectorFunction argument2;
    private final double minValue = this.computeMinValue();
    private final double maxValue = this.computeMaxValue();
    public static final KeyDispatchDataCodec<DistanceDensityFunction> CODEC = KeyDispatchDataCodec.of(LithostitchedCodecs.doubleArgument(VectorFunction.HOLDER_HELPER_CODEC, DistanceDensityFunction::new, DistanceDensityFunction::argument1, DistanceDensityFunction::argument2));

    public DistanceDensityFunction(VectorFunction argument1, VectorFunction argument2) {
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    @Override
    public VectorFunction argument1() {
        return this.argument1;
    }

    @Override
    public VectorFunction argument2() {
        return this.argument2;
    }

    @Override
    public double transform(Vec3 argument1, Vec3 argument2) {
        return argument1.distanceTo(argument2);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        VectorFunction.Visitor vecVisitor = (VectorFunction.Visitor) visitor;
        return visitor.apply(new DistanceDensityFunction(this.argument1.mapAll(vecVisitor), this.argument2.mapAll(vecVisitor)));
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
    public KeyDispatchDataCodec<DistanceDensityFunction> codec() {
        return CODEC;
    }

    private double computeMinValue() {
        return 0;
    }

    private double computeMaxValue() {
        return 0;
    }
}

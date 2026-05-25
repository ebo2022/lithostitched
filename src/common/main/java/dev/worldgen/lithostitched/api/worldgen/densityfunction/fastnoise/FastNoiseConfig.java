package dev.worldgen.lithostitched.api.worldgen.densityfunction.fastnoise;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.worldgen.lithostitched.api.registry.LithostitchedBuiltInRegistries;
import dev.worldgen.lithostitched.api.registry.LithostitchedRegistries;
import dev.worldgen.lithostitched.impl.worldgen.densityfunction.fastnoise.config.SimpleFastNoiseConfigBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Function;

public abstract class FastNoiseConfig {
    public static final Codec<FastNoiseConfig> CODEC = LithostitchedBuiltInRegistries.FAST_NOISE_CONFIG_TYPE.byNameCodec().dispatch(FastNoiseConfig::getCodec, Function.identity());

    public abstract void configure(Builder builder);

    public abstract MapCodec<? extends FastNoiseConfig> getCodec();

    protected final FNL fnl;
    private final float frequency;
    private final int salt;

    protected FastNoiseConfig(float frequency, int salt) {
        this.salt = salt;
        this.fnl = new FNL();
        this.frequency = frequency;
        fnl.SetFrequency(frequency);
        this.configure(new SimpleFastNoiseConfigBuilder(fnl));
    }

    public float frequency() {
        return frequency;
    }

    public int salt() {
        return salt;
    }

    public void bind(long seed) {
        fnl.SetSeed((int) seed + salt);
    }

    public double sample(double x, double y, double z) {
        return fnl.GetNoise(x, y, z);
    }

    public interface Builder {
        Builder noiseType(FNL.NoiseType type);
        Builder rotationType3D(FNL.RotationType3D type);
        Builder fractalType(FNL.FractalType type);
        Builder fractalOctaves(int octaves);
        Builder fractalLacunarity(float lacunarity);
        Builder fractalGain(float gain);
        Builder fractalWeightedStrength(float weightedStrength);
        Builder fractalPingPongStrength(float pingPongStrength);
        Builder cellularDistanceFunction(FNL.CellularDistanceFunction distanceFunction);
        Builder cellularReturnType(FNL.CellularReturnType cellularReturnType);
        Builder cellularJitter(float cellularJitter);
        Builder domainWarpType(FNL.DomainWarpType domainWarpType);
        Builder domainWarpAmp(float domainWarpAmp);
    }
}

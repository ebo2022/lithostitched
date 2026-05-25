package dev.worldgen.lithostitched.impl.worldgen.densityfunction.fastnoise.config;

import dev.worldgen.lithostitched.api.worldgen.densityfunction.fastnoise.FNL;
import dev.worldgen.lithostitched.api.worldgen.densityfunction.fastnoise.FastNoiseConfig;

public record SimpleFastNoiseConfigBuilder(FNL fnl) implements FastNoiseConfig.Builder {
    @Override
    public FastNoiseConfig.Builder noiseType(FNL.NoiseType noiseType) {
        this.fnl.SetNoiseType(noiseType);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder rotationType3D(FNL.RotationType3D rotationType3D) {
        this.fnl.SetRotationType3D(rotationType3D);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder fractalType(FNL.FractalType type) {
        this.fnl.SetFractalType(type);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder fractalOctaves(int octaves) {
        this.fnl.SetFractalOctaves(octaves);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder fractalLacunarity(float lacunarity) {
        this.fnl.SetFractalLacunarity(lacunarity);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder fractalGain(float gain) {
        this.fnl.SetFractalGain(gain);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder fractalWeightedStrength(float weightedStrength) {
        this.fnl.SetFractalWeightedStrength(weightedStrength);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder fractalPingPongStrength(float pingPongStrength) {
        this.fnl.SetFractalPingPongStrength(pingPongStrength);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder cellularDistanceFunction(FNL.CellularDistanceFunction distanceFunction) {
        this.fnl.SetCellularDistanceFunction(distanceFunction);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder cellularReturnType(FNL.CellularReturnType cellularReturnType) {
        this.fnl.SetCellularReturnType(cellularReturnType);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder cellularJitter(float cellularJitter) {
        this.fnl.SetCellularJitter(cellularJitter);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder domainWarpType(FNL.DomainWarpType domainWarpType) {
        this.fnl.SetDomainWarpType(domainWarpType);
        return this;
    }

    @Override
    public FastNoiseConfig.Builder domainWarpAmp(float domainWarpAmp) {
        this.fnl.SetDomainWarpAmp(domainWarpAmp);
        return this;
    }
}

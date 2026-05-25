package dev.worldgen.lithostitched.mixin.common.c2me;

import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGenRegistry;
import dev.worldgen.lithostitched.compat.c2me.ast.misc.SelectNode;
import dev.worldgen.lithostitched.compat.c2me.gen.jvm.emitters.LithostitchedUnaryNodeBytecodeEmitters;
import dev.worldgen.lithostitched.compat.c2me.gen.jvm.emitters.misc.SelectNodeBytecodeEmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BytecodeGenRegistry.class)
public class BytecodeGenRegistryMixin {
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void registerLithostitchedEmitters(CallbackInfo ci) {
        LithostitchedUnaryNodeBytecodeEmitters.register(BytecodeGenRegistry.REGISTRY);
        BytecodeGenRegistry.REGISTRY.registerExactMatch(SelectNode.class, SelectNodeBytecodeEmitter.INSTANCE);
    }
}

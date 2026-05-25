package dev.worldgen.lithostitched.mixin.common.c2me;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.McToAst;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.worldgen.lithostitched.compat.c2me.ast.unary.*;
import dev.worldgen.lithostitched.impl.worldgen.densityfunction.*;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(McToAst.class)
public abstract class McToAstMixin {

    @Shadow
    public static AstNode toAst(DensityFunction df) {
        throw new AssertionError();
    }

    @ModifyReturnValue(method = "toAst", at = @At("TAIL"))
    private static AstNode handleLithostitchedAst(AstNode original, DensityFunction df) {
        return switch (df) {
            // unary
            case CeilDensityFunction f -> new CeilNode(toAst(f.argument()));
            case CosDensityFunction f -> new CosNode(toAst(f.argument()));
            case FloorDensityFunction f -> new FloorNode(toAst(f.argument()));
            case SinDensityFunction f -> new SinNode(toAst(f.argument()));
            case SqrtDensityFunction f -> new SqrtNode(toAst(f.argument()));
            default -> original;
        };
    }
}

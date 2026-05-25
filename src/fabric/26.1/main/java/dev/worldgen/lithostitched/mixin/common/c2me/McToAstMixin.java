package dev.worldgen.lithostitched.mixin.common.c2me;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.McToAst;
import com.ishland.c2me.opts.dfc.common.ast.binary.AddNode;
import com.ishland.c2me.opts.dfc.common.ast.binary.MaxNode;
import com.ishland.c2me.opts.dfc.common.ast.binary.MinNode;
import com.ishland.c2me.opts.dfc.common.ast.binary.MulNode;
import com.ishland.c2me.opts.dfc.common.ast.misc.ConstantNode;
import com.ishland.c2me.opts.dfc.common.ast.misc.CoordinateNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.NegMulNode;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.worldgen.lithostitched.compat.c2me.ast.misc.SelectNode;
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

            // misc
            case AxisDensityFunction f -> new CoordinateNode(switch (f.axis()) {
                case X -> CoordinateNode.Axis.X;
                case Y -> CoordinateNode.Axis.Y;
                case Z -> CoordinateNode.Axis.Z;
            });
            case SelectDensityFunction f -> new SelectNode(toAst(f.input()), toAst(f.fallback()), f.selections().stream().map(s -> new SelectNode.Selection(s.range(), toAst(s.function()))).toArray(SelectNode.Selection[]::new));
            default -> original;
        };
    }
}

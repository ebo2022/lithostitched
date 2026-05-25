package dev.worldgen.lithostitched.mixin.common.c2me;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.misc.ConstantNode;
import com.ishland.c2me.opts.dfc.common.ast.opto.passes.FoldConstants;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.worldgen.lithostitched.compat.c2me.ast.unary.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoldConstants.class)
public class FoldConstantsMixin {

    @ModifyReturnValue(method = "transform", at = @At("TAIL"))
    private AstNode transformLithostitchedNodes(AstNode original, AstNode astNode) {
        return switch (astNode) {
            case CeilNode ceilNode -> {
                if (ceilNode.operand instanceof ConstantNode c) {
                    yield new ConstantNode(Math.ceil(c.getValue()));
                }
                yield ceilNode;
            }
            case CosNode cosNode -> {
                if (cosNode.operand instanceof ConstantNode c) {
                    yield new ConstantNode(Math.cos(c.getValue()));
                }
                yield cosNode;
            }
            case FloorNode floorNode -> {
                if (floorNode.operand instanceof ConstantNode c) {
                    yield new ConstantNode(Math.floor(c.getValue()));
                }
                yield floorNode;
            }
            case SinNode sinNode -> {
                if (sinNode.operand instanceof ConstantNode c) {
                    yield new ConstantNode(Math.sin(c.getValue()));
                }
                yield sinNode;
            }
            case SqrtNode sqrtNode -> {
                if (sqrtNode.operand instanceof ConstantNode c) {
                    yield new ConstantNode(Math.sqrt(c.getValue()));
                }
                yield sqrtNode;
            }
            default -> original;
        };
    }
}

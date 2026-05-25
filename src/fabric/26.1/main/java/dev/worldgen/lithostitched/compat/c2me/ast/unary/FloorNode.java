package dev.worldgen.lithostitched.compat.c2me.ast.unary;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.AbstractUnaryNode;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefD;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;

public class FloorNode extends AbstractUnaryNode {
    public FloorNode(AstNode operand) {
        super(operand);
    }

    @Override
    protected AstNode newInstance(AstNode operand) {
        return new FloorNode(operand);
    }

    @Override
    protected String getDotGenDescription() {
        return "floor";
    }

    @Override
    public String doCLGen(OpenCLGen.Context context) {
        ValuesMethodDefD operand = context.newMethod(this.operand);
        return "return floor(" + context.callDelegate(operand) + ");\n";
    }
}

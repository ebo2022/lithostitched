package dev.worldgen.lithostitched.compat.c2me.ast.unary;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.unary.AbstractUnaryNode;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefD;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;

public class CeilNode extends AbstractUnaryNode {
    public CeilNode(AstNode operand) {
        super(operand);
    }

    @Override
    protected AstNode newInstance(AstNode operand) {
        return new CeilNode(operand);
    }

    @Override
    protected String getDotGenDescription() {
        return "ceil";
    }

    @Override
    public String doCLGen(OpenCLGen.Context context) {
        ValuesMethodDefD operand = context.newMethod(this.operand);
        return "return ceil(" + context.callDelegate(operand) + ");\n";
    }
}

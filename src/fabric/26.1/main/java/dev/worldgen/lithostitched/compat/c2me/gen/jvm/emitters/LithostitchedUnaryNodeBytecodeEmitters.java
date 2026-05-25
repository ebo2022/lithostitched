package dev.worldgen.lithostitched.compat.c2me.gen.jvm.emitters;

import com.ishland.c2me.opts.dfc.common.gen.CodeGenRegistry;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeEmitter;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGen;
import com.ishland.c2me.opts.dfc.common.gen.jvm.emitters.UnaryNodeBytecodeEmitters.AbstractGenericUnaryNodeBytecodeEmitter;
import dev.worldgen.lithostitched.compat.c2me.ast.unary.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;

public class LithostitchedUnaryNodeBytecodeEmitters {
    public static void register(CodeGenRegistry<BytecodeEmitter<?>> registry) {
        registry.registerExactMatch(CeilNode.class, CeilNodeEmitter.INSTANCE);
        registry.registerExactMatch(CosNode.class, CosNodeEmitter.INSTANCE);
        registry.registerExactMatch(FloorNode.class, FloorNodeEmitter.INSTANCE);
        registry.registerExactMatch(SinNode.class, SinNodeEmitter.INSTANCE);
        registry.registerExactMatch(SqrtNode.class, SqrtNodeEmitter.INSTANCE);
    }

    public static class CeilNodeEmitter extends AbstractGenericUnaryNodeBytecodeEmitter<CeilNode> {
        private static final CeilNodeEmitter INSTANCE = new CeilNodeEmitter();

        private CeilNodeEmitter() {
        }

        @Override
        protected void bytecodeGenInstruction(CeilNode node, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
            m.invokestatic(
                    Type.getInternalName(Math.class),
                    "ceil",
                    Type.getMethodDescriptor(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE),
                    false
            );
        }
    }

    public static class CosNodeEmitter extends AbstractGenericUnaryNodeBytecodeEmitter<CosNode> {
        private static final CosNodeEmitter INSTANCE = new CosNodeEmitter();

        private CosNodeEmitter() {
        }

        @Override
        protected void bytecodeGenInstruction(CosNode node, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
            m.invokestatic(
                    Type.getInternalName(Math.class),
                    "cos",
                    Type.getMethodDescriptor(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE),
                    false
            );
        }
    }

    public static class FloorNodeEmitter extends AbstractGenericUnaryNodeBytecodeEmitter<FloorNode> {
        private static final FloorNodeEmitter INSTANCE = new FloorNodeEmitter();

        private FloorNodeEmitter() {
        }

        @Override
        protected void bytecodeGenInstruction(FloorNode node, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
            m.invokestatic(
                    Type.getInternalName(Math.class),
                    "floor",
                    Type.getMethodDescriptor(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE),
                    false
            );
        }
    }

    public static class SinNodeEmitter extends AbstractGenericUnaryNodeBytecodeEmitter<SinNode> {
        private static final SinNodeEmitter INSTANCE = new SinNodeEmitter();

        private SinNodeEmitter() {
        }

        @Override
        protected void bytecodeGenInstruction(SinNode node, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
            m.invokestatic(
                    Type.getInternalName(Math.class),
                    "sin",
                    Type.getMethodDescriptor(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE),
                    false
            );
        }
    }

    public static class SqrtNodeEmitter extends AbstractGenericUnaryNodeBytecodeEmitter<SqrtNode> {
        private static final SqrtNodeEmitter INSTANCE = new SqrtNodeEmitter();

        private SqrtNodeEmitter() {
        }

        @Override
        protected void bytecodeGenInstruction(SqrtNode node, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
            m.invokestatic(
                    Type.getInternalName(Math.class),
                    "sqrt",
                    Type.getMethodDescriptor(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE),
                    false
            );
        }
    }
}

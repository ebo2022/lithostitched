package dev.worldgen.lithostitched.compat.c2me.gen.jvm.emitters.misc;

import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeEmitter;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGen;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefD;
import dev.worldgen.lithostitched.compat.c2me.ast.misc.SelectNode;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;

public class SelectNodeBytecodeEmitter implements BytecodeEmitter<SelectNode> {
    public static final SelectNodeBytecodeEmitter INSTANCE = new SelectNodeBytecodeEmitter();

    private SelectNodeBytecodeEmitter() {
    }

    @Override
    public void doBytecodeGenSingle(SelectNode node, BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
        ValuesMethodDefD inputMethod = context.newSingleMethod(node.input);
        ValuesMethodDefD fallbackMethod = context.newSingleMethod(node.fallback);
        ValuesMethodDefD[] selectionMethods = new ValuesMethodDefD[node.selections.length];
        for (int i = 0; i < node.selections.length; i++) {
            selectionMethods[i] = context.newSingleMethod(node.selections[i].node());
        }

        int inputValue = localVarConsumer.createLocalVariable("inputValue", Type.DOUBLE_TYPE.getDescriptor());
        context.callDelegateSingle(m, inputMethod);
        m.store(inputValue, Type.DOUBLE_TYPE);

        for (int i = 0; i < node.selections.length; i++) {
            SelectNode.Selection selection = node.selections[i];
            Label nextSelection = new Label();

            m.load(inputValue, Type.DOUBLE_TYPE);
            m.dconst(selection.range().minInclusive());
            m.cmpl(Type.DOUBLE_TYPE);
            m.iflt(nextSelection);

            m.load(inputValue, Type.DOUBLE_TYPE);
            m.dconst(selection.range().maxInclusive());
            m.cmpg(Type.DOUBLE_TYPE);
            m.ifgt(nextSelection);

            if (selectionMethods[i].equals(inputMethod)) {
                m.load(inputValue, Type.DOUBLE_TYPE);
            } else {
                context.callDelegateSingle(m, selectionMethods[i]);
            }
            m.areturn(Type.DOUBLE_TYPE);

            m.visitLabel(nextSelection);
        }

        if (fallbackMethod.equals(inputMethod)) {
            m.load(inputValue, Type.DOUBLE_TYPE);
        } else {
            context.callDelegateSingle(m, fallbackMethod);
        }
        m.areturn(Type.DOUBLE_TYPE);
    }

    @Override
    public void doBytecodeGenMulti(SelectNode node, BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {
        ValuesMethodDefD inputSingle = context.newSingleMethod(node.input);
        ValuesMethodDefD fallbackSingle = context.newSingleMethod(node.fallback);
        ValuesMethodDefD inputMulti = context.newMultiMethod(node.input);
        ValuesMethodDefD[] selectionSingles = new ValuesMethodDefD[node.selections.length];
        for (int i = 0; i < node.selections.length; i++) {
            selectionSingles[i] = context.newSingleMethod(node.selections[i].node());
        }

        context.callDelegateMulti(m, inputMulti);

        context.doCountedLoop(m, localVarConsumer, idx -> {
            Label end = new Label();

            m.load(1, InstructionAdapter.OBJECT_TYPE);
            m.load(idx, Type.INT_TYPE);

            for (int i = 0; i < node.selections.length; i++) {
                SelectNode.Selection selection = node.selections[i];
                Label nextSelection = new Label();

                m.load(1, InstructionAdapter.OBJECT_TYPE);
                m.load(idx, Type.INT_TYPE);
                m.aload(Type.DOUBLE_TYPE);
                m.dconst(selection.range().minInclusive());
                m.cmpl(Type.DOUBLE_TYPE);
                m.iflt(nextSelection);

                m.load(1, InstructionAdapter.OBJECT_TYPE);
                m.load(idx, Type.INT_TYPE);
                m.aload(Type.DOUBLE_TYPE);
                m.dconst(selection.range().maxInclusive());
                m.cmpg(Type.DOUBLE_TYPE);
                m.ifgt(nextSelection);

                if (selectionSingles[i].equals(inputSingle)) {
                    m.load(1, InstructionAdapter.OBJECT_TYPE);
                    m.load(idx, Type.INT_TYPE);
                    m.aload(Type.DOUBLE_TYPE);
                } else {
                    context.callDelegateSingleFromMulti(m, selectionSingles[i], idx);
                }
                m.astore(Type.DOUBLE_TYPE);
                m.goTo(end);

                m.visitLabel(nextSelection);
            }

            if (fallbackSingle.equals(inputSingle)) {
                m.load(1, InstructionAdapter.OBJECT_TYPE);
                m.load(idx, Type.INT_TYPE);
                m.aload(Type.DOUBLE_TYPE);
            } else {
                context.callDelegateSingleFromMulti(m, fallbackSingle, idx);
            }
            m.astore(Type.DOUBLE_TYPE);

            m.visitLabel(end);
        });

        m.areturn(Type.VOID_TYPE);
    }
}

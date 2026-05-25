package dev.worldgen.lithostitched.compat.c2me.ast.misc;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.AstTransformer;
import com.ishland.c2me.opts.dfc.common.gen.dot.DotGen;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefD;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;
import net.minecraft.util.InclusiveRange;

import java.util.Arrays;
import java.util.Objects;

public class SelectNode implements AstNode {
    public final AstNode input;
    public final AstNode fallback;
    public final Selection[] selections;

    public SelectNode(AstNode input, AstNode fallback, Selection[] selections) {
        this.input = input;
        this.fallback = fallback;
        this.selections = selections;
    }

    @Override
    public AstNode[] getChildren() {
        AstNode[] children = new AstNode[this.selections.length + 2];
        children[0] = this.input;
        children[1] = this.fallback;
        for (int i = 0; i < this.selections.length; i++) {
            children[i + 2] = this.selections[i].node;
        }
        return children;
    }

    @Override
    public AstNode transform(AstTransformer transformer) {
        AstNode input = this.input.transform(transformer);
        AstNode fallback = this.fallback.transform(transformer);
        AstNode[] originalSelections = new AstNode[this.selections.length],
                transformedSelections = new AstNode[this.selections.length];
        for (int i = 0; i < this.selections.length; i++) {
            originalSelections[i] = this.selections[i].node;
            transformedSelections[i] = this.selections[i].node.transform(transformer);
        }
        if (this.input == input && this.fallback == fallback && Arrays.equals(originalSelections, transformedSelections)) {
            return transformer.transform(this);
        } else {
            Selection[] newSelections = new Selection[transformedSelections.length];
            for (int i = 0; i < transformedSelections.length; i++) {
                newSelections[i] = new Selection(this.selections[i].range, transformedSelections[i]);
            }
            return new SelectNode(input, fallback, newSelections);
        }
    }

    @Override
    public String doCLGen(OpenCLGen.Context context) {
        ValuesMethodDefD input = context.newMethod(this.input);
        ValuesMethodDefD fallback = context.newMethod(this.fallback);
        ValuesMethodDefD[] selectionMethods = new ValuesMethodDefD[this.selections.length];
        for (int i = 0; i < this.selections.length; i++) {
            selectionMethods[i] = context.newMethod(this.selections[i].node);
        }

        // functionally equivalent to a for-loop, TODO maybe look into a better way to do this?
        StringBuilder b = new StringBuilder();
        b.append("double value = ").append(context.callDelegate(input)).append(";\n");
        for (int i = 0; i < this.selections.length; i++) {
            Selection selection = this.selections[i];
            b.append("if (value >= ")
                    .append(OpenCLGen.literal(selection.range.minInclusive()))
                    .append(" && value <= ")
                    .append(OpenCLGen.literal(selection.range.maxInclusive()))
                    .append(") return ")
                    .append(context.callDelegate(selectionMethods[i]))
                    .append(";\n");
        }
        b.append("return ").append(context.callDelegate(fallback)).append(";\n");
        return b.toString();
    }

    @Override
    public int doDotGen(DotGen.Context context, DotGen.Context.Builder builder) {
        builder.diamondShape()
                .label("Select")
                .edge(context.generate(input)).label("input").color("blue").finish()
                .edge(context.generate(fallback)).label("fallback").color("red").finish();
        for (int i = 0; i < this.selections.length; i++) {
            builder.edge(context.generate(selections[i].node)).label(String.valueOf(i)).color("white").finish();
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectNode that = (SelectNode) o;
        return Objects.equals(input, that.input) && Objects.equals(fallback, that.fallback) && Arrays.equals(selections, that.selections);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + this.getClass().hashCode();
        result = 31 * result + this.input.hashCode();
        result = 31 * result + this.fallback.hashCode();
        for (int i = 0; i < this.selections.length; i++) {
            result = 31 * result + this.selections[i].hashCode();
        }

        return result;
    }

    @Override
    public boolean relaxedEquals(AstNode o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectNode that = (SelectNode) o;
        return input.relaxedEquals(that.input) && fallback.relaxedEquals(that.fallback) && selectionsRelaxedEquals(selections, that.selections);
    }

    @Override
    public int relaxedHashCode() {
        int result = 1;

        result = 31 * result + this.getClass().hashCode();
        result = 31 * result + this.input.relaxedHashCode();
        result = 31 * result + this.fallback.relaxedHashCode();
        for (int i = 0; i < this.selections.length; i++) {
            result = 31 * result + this.selections[i].relaxedHashCode();
        }

        return result;
    }

    public static boolean selectionsRelaxedEquals(Selection[] a, Selection[] a2) {
        if (a == a2)
            return true;
        if (a== null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            if (!a[i].relaxedEquals(a2[i]))
                return false;
        }

        return true;
    }

    public record Selection(InclusiveRange<Double> range, AstNode node) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Selection that = (Selection) o;
            return Double.compare(this.range.minInclusive(), that.range.minInclusive()) == 0 && Double.compare(this.range.maxInclusive(), that.range.maxInclusive()) == 0 && this.node.relaxedEquals(that.node);
        }

        @Override
        public int hashCode() {
            int result = 1;

            result = 31 * result + this.getClass().hashCode();
            result = 31 * result + Double.hashCode(this.range.minInclusive());
            result = 31 * result + Double.hashCode(this.range.maxInclusive());
            result = 31 * result + this.node.hashCode();

            return result;
        }

        public boolean relaxedEquals(Selection o) {
            if (this == o) return true;
            if (o == null) return false;
            return Double.compare(this.range.minInclusive(), o.range.minInclusive()) == 0 && Double.compare(this.range.maxInclusive(), o.range.maxInclusive()) == 0 && this.node.relaxedEquals(o.node);
        }

        public int relaxedHashCode() {
            int result = 1;

            result = 31 * result + this.getClass().hashCode();
            result = 31 * result + Double.hashCode(this.range.minInclusive());
            result = 31 * result + Double.hashCode(this.range.maxInclusive());
            result = 31 * result + this.node.relaxedHashCode();

            return result;
        }
    }
}

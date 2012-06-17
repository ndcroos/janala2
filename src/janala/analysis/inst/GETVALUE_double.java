/*
 * Author: Koushik Sen (ksen@cs.berkeley.edu)
 */

package janala.analysis.inst;

public class GETVALUE_double extends Instruction {
    double v;

    public GETVALUE_double(double v) {
        super(-1, -1);
        this.v = v;
    }

    public void visit(IVisitor visitor) {
	visitor.visitGETVALUE_double(this);
    }

    @Override
    public String toString() {
        return "GETVALUE_double v="+v;
    }
}
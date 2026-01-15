import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;

public class SampleBodyTransform extends BodyTransformer {
    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        StringBuilder st = new StringBuilder();

        // Print information about the method being analyzed
        st.append("METHOD " + body.getMethod().getDeclaringClass().getName() + "." + body.getMethod().getName() + "\n");

        // Get the list of units in the method body
        PatchingChain<Unit> units = body.getUnits();

        // Print each assignment unit
        for (Unit u : units) {
            if (u instanceof JAssignStmt) {
                st.append("Assignment statement: " + u + "\n");
            }
        }
        System.out.println(st);
    }
}

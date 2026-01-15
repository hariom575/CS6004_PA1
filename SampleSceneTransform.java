import java.util.Map;
import java.util.List;

import soot.Unit;
import soot.PatchingChain;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.internal.JAssignStmt;

    public class SampleSceneTransform extends SceneTransformer {
    @Override
    protected void internalTransform(String phaseName, Map<String, String> options) {
         Chain<SootClass> classes = Scene.v().getApplicationClasses();
         for(SootClass sc : classes){
            int objectSize = printFieldInfoandReturnObjectSize(sc);
            printObjectSize(objectSize);
            int methodCount = sc.getMethods().size();
            System.out.println("METHODS \n");
            for (SootMethod mtd : sc.getMethods()) {
                printMethodInfo(mtd);
            }
        }
    }

    static void printMethodInfo(Sto mtd) {
        StringBuilder st = new StringBuilder();

        // Print information about the method being analyzed
        List<Type> papametertypes = mtd.getParameterTypes();
        st.append(mtd.getDeclaringClass().getName() + " :: " +mtd.getReturnType() + " " + mtd.getName() +  "\n");

        // Get the list of units in the method body
        PatchingChain<Unit> units = mtd.retrieveActiveBody().getUnits();

        // Print each assignment unit
        for (Unit u : units) {
            if (u instanceof JAssignStmt) {
                st.append("Assignment statement: " + u + "\n");
            }
        }
        System.out.println(st);
    }
    static void printClasInfo(SootClass cls){
        StringBuilder st = new StringBuilder();
        st.append("CLASS " + cls.getDeclaringClass.getName() + "\n");
        System.out.println(st);;
    }
    static void printObjectSize(int objectSize){
        StringBuilder st = new StringBuilder();
        st.append("OBJECT_SIZE \n");
        st.append(objectSize);
        System.out.println(st);
    }
    static int printFieldInfoandReturnObjectSize(SootClass sc){
           int objectSize = 12;
           StringBuilder st = new StringBuilder();
           st.append("FIELDS");
           st.append("\n");
            for(SootField field: sc.getFileds()){
                String fieldName = field.getName();
                String fieldType = field.getType();
                st.append(sc.getName() + " :: " + fieldType + " " + fieldName + "\n");
                if(fieldType=="boolean" || fieldType == "byte"){
                    objectSize+=1;
                }else if(fieldType=="char" || fieldType =="short") objectSize+=2;
                else if(fieldType == "int" || fieldType == "float"){
                    objectSize+=4;
                }else if(fieldType == "long" || fieldType == "double") objectSize+=8;
                else{
                    objectSize+=4;
                }
            }
            return objectSize;
    }
}

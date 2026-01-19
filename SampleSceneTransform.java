import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import soot.FastHierarchy;
import soot.SootField;

import soot.Unit;
import soot.PatchingChain;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.internal.JAssignStmt;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.ShortType;
import soot.IntType;
import soot.FloatType;
import soot.LongType;
import soot.DoubleType;
import soot.Type;

    public class SampleSceneTransform extends SceneTransformer {
    @Override
    protected void internalTransform(String phaseName, Map<String, String> options) {
        //  Chain<SootClass> classes = Scene.v().getApplicationClasses();
        //  for(SootClass sc : classes){
        //     int objectSize = printFieldInfoandReturnObjectSize(sc);
        //     printObjectSize(objectSize);
        //     int methodCount = sc.getMethods().size();
        //     System.out.println("METHODS \n");
        //     for (SootMethod mtd : sc.getMethods()) {
        //         printMethodInfo(mtd);
        //     }
        // }
        FastHierarchy fh = Scene.v().getFastHierarchy();
        for(SootClass sc : Scene.v().getApplicationClasses()){
            System.out.println("\n Class" + sc.getName());
            System.out.println("Fields");
            for(SootClass c : getClassHierachyTopDown(sc)){
                for(SootField field : c.getFields()){
                    System.out.println("  " + field.getSubSignature() + " [defined in " + c.getName() + "]");
                }
            }
            System.out.println(" Methods (vtable order); ");
            LinkedHashMap<String,SootMethod> vtable = new LinkedHashMap<>();
            for(SootClass c: getClassHierachyTopDown(sc)){
                for(SootMethod m : c.getMethods()){
                   if(isVirtualMethod(m)){
                        String subsig = m.getSubSignature();
                        vtable.put(subsig, m);
                    }
                }
            }
            for(SootMethod m : vtable.values()){
                  System.out.println("    " + m.getSubSignature()
                        + "  [defined in " + m.getDeclaringClass().getName() + "]");
            }
            int objSize = 12 + getObjectSize(sc); /// 12 bytes for header
            System.out.println(" Object size (approx): " + objSize + " bytes");
        }
    }

    // static void printMethodInfo(Sto mtd) {
    //     StringBuilder st = new StringBuilder();

    //     // Print information about the method being analyzed
    //     List<Type> papametertypes = mtd.getParameterTypes();
    //     st.append(mtd.getDeclaringClass().getName() + " :: " +mtd.getReturnType() + " " + mtd.getName() +  "\n");

    //     // Get the list of units in the method body
    //     PatchingChain<Unit> units = mtd.retrieveActiveBody().getUnits();

    //     // Print each assignment unit
    //     for (Unit u : units) {
    //         if (u instanceof JAssignStmt) {
    //             st.append("Assignment statement: " + u + "\n");
    //         }
    //     }
    //     System.out.println(st);
    // }
    static void printClasInfo(SootClass cls){
        StringBuilder st = new StringBuilder();
        st.append("CLASS " + cls.getName() + "\n");
        System.out.println(st);;
    }
    static void printObjectSize(int objectSize){
        StringBuilder st = new StringBuilder();
        st.append("OBJECT_SIZE \n");
        st.append(objectSize);
        System.out.println(st);
    }
    // static int printFieldInfoandReturnObjectSize(SootClass sc){
    //        int objectSize = 12;
    //        StringBuilder st = new StringBuilder();
    //        st.append("FIELDS");
    //        st.append("\n");
    //         for(SootField field: sc.getFileds()){
    //             String fieldName = field.getName();
    //             String fieldType = field.getType();
    //             st.append(sc.getName() + " :: " + fieldType + " " + fieldName + "\n");
    //             if(fieldType=="boolean" || fieldType == "byte"){
    //                 objectSize+=1;
    //             }else if(fieldType=="char" || fieldType =="short") objectSize+=2;
    //             else if(fieldType == "int" || fieldType == "float"){
    //                 objectSize+=4;
    //             }else if(fieldType == "long" || fieldType == "double") objectSize+=8;
    //             else{
    //                 objectSize+=4;
    //             }
    //         }
    //         return objectSize;
    // }
    private List<SootClass> getClassHierachyTopDown(SootClass sc){
        LinkedList<SootClass> hierarchy = new LinkedList<>();
        SootClass cur = sc;
        while (cur != null && cur.isApplicationClass()) {
              hierarchy.addFirst(cur);
             if (!cur.hasSuperclass()) break;
             cur = cur.getSuperclass();
       }
        return hierarchy;
    }
    private int getTypeSize(Type t) {
    if (t instanceof BooleanType) return 1;
    if (t instanceof ByteType)    return 1;
    if (t instanceof CharType)    return 2;
    if (t instanceof ShortType)   return 2;
    if (t instanceof IntType)     return 4;
    if (t instanceof FloatType)   return 4;
    if (t instanceof LongType)    return 8;
    if (t instanceof DoubleType)  return 8;

    // reference type
    return 4;
    }
    private int getObjectSize(SootClass sc) {
    int size = 0;

    SootClass cur = sc;
    while (cur != null && cur.isApplicationClass()) {
        for (SootField f : cur.getFields()) {
            if (!f.isStatic()) {
                size += getTypeSize(f.getType());
            }
        }
        if (!cur.hasSuperclass()) break;
        cur = cur.getSuperclass();
    }
    return size;
}

    private boolean isVirtualMethod(SootMethod m) {
    return m.getDeclaringClass().isApplicationClass()
            && !m.isStatic()
            && !m.isConstructor();
}

}

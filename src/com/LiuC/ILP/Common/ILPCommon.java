package com.LiuC.ILP.Common;

import gurobi.*;

import java.util.ArrayList;

/**
 * Created by Freiheiter on 2016/12/20.
 */
public class ILPCommon {


    public static ArrayList<Integer> printSolution(GRBModel model, GRBVar[] men4entBinary, ArrayList<String> mention, ArrayList<String> entity) throws GRBException {
        ArrayList<Integer> varExist=new ArrayList<>();
        if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
            System.out.println("\nCost: " + model.get(GRB.DoubleAttr.ObjVal));
            for(int i=0;i<men4entBinary.length;i++){
                if(men4entBinary[i].get(GRB.DoubleAttr.X)!=0){
                    System.out.println("mention:"+mention.get(i)+"==>entity:"+entity.get(i));
                    varExist.add(i);
                }
            }
        }
        else {
            System.out.println("No solution");
        }
        return varExist;
    }

    //
    public static ArrayList<Integer> getSolution(GRBModel model, GRBVar[] men4entBinary) throws GRBException {
        ArrayList<Integer> varExist=new ArrayList<>();
        if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
            System.out.println("\nCost: " + model.get(GRB.DoubleAttr.ObjVal));
            for(int i=0;i<men4entBinary.length;i++){
                if(men4entBinary[i].get(GRB.DoubleAttr.X)!=0){
                    //System.out.println(triples.get(i).toString());
                    varExist.add(i);
                }
            }
        }
        else {
            System.out.println("No solution");
        }
        return varExist;
    }

    public static ArrayList<Integer> getSolution(GRBModel model, GRBVar[] men4entBinary,GRBVar[] menBinary,ArrayList<Integer> men4entStart,ArrayList<Integer> men4entNum,ArrayList<Integer> tripleResult) throws GRBException {
        if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
            System.out.println("\nCost: " + model.get(GRB.DoubleAttr.ObjVal));

            for(int i=0;i<menBinary.length;i++){
                for(int j=0;j<men4entNum.get(i);j++){
                    int men4entIndex=men4entStart.get(i)+j;
                    if(men4entBinary[men4entIndex].get(GRB.DoubleAttr.X)*menBinary[i].get(GRB.DoubleAttr.X)!=0){
                        //System.out.println(triples.get(i).toString());
                        tripleResult.add(men4entIndex);
                    }
                }
            }

        }
        else {
            System.out.println("No solution");
        }
        return tripleResult;
    }

    /**
     * add constraint
     * @param model
     * @param constraints
     * @param men4entBinary
     * @param type: part of name string
     * @throws GRBException
     */
    public static void addConstraint(GRBModel  model,ArrayList<ArrayList<Integer>> constraints,GRBVar[] men4entBinary,String type)throws GRBException{
        for(int i=0;i<constraints.size();i++){
            ArrayList<Integer> constraint_=constraints.get(i);
            GRBLinExpr expr = new GRBLinExpr();
            for(int j=0;j<constraint_.size();j++){
                expr.addTerm(1.0,men4entBinary[constraint_.get(j)]);
            }
            model.addConstr(expr,GRB.LESS_EQUAL,1.0,type+"\t"+String.valueOf(i));
        }
    }

}

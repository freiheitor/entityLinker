package com.LiuC.ILP.tripleILP;

import com.LiuC.ILP.Common.ILPCommon;
import gurobi.*;

import java.util.ArrayList;

/**
 * Created by Freiheiter on 2016/12/20.
 */
public class ILPtriple {
    public static void main(String[] args) {

    }

    public static void calILPtriple(ILPTripStru ilpTripStru){
        try{
            ArrayList<Integer> men4entNum = ilpTripStru.getMen4entNum();//each mention to entity num:5,10,8,7
            ArrayList<Integer> men4entStart = ilpTripStru.getMen4entStart();;//each mention start index,0,5,15,23

            ArrayList<Triple> triples = ilpTripStru.getTriples();
            ArrayList<Double> mention_LengthWei=ilpTripStru.getMention_LengthWei();

            //conflict
            ArrayList<ArrayList<Integer>> mention_overlap =ilpTripStru.getMention_overlap();
            ArrayList<ArrayList<Integer>> mention_contain = ilpTripStru.getMention_contain();
            ArrayList<ArrayList<Integer>> object_contain = ilpTripStru.getObject_contain();

            // Model
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "ILPtriple");

            //create variable
            int mentionSize=men4entNum.size();
            int entitySize=men4entStart.get(mentionSize-1)+men4entNum.get(mentionSize-1);

            GRBVar[] menBinary = new GRBVar[mentionSize];
            for(int i=0;i<menBinary.length;i++){
                menBinary[i]=model.addVar(0.0, 1.0, 0.0, GRB.BINARY,String.valueOf(i));
            }

            GRBVar[] men4entBinary = new GRBVar[entitySize];
            for(int i=0;i<men4entBinary.length;i++){
                men4entBinary[i]=model.addVar(0.0, 1.0, 0.0, GRB.BINARY,String.valueOf(i));
            }

            //set objective
            GRBQuadExpr obj = new GRBQuadExpr();
            for(int i=0;i<menBinary.length;i++){
                for(int j=0;j<men4entNum.get(i);j++){
                    obj.addTerm(mention_LengthWei.get(i), menBinary[i], men4entBinary[men4entStart.get(i)+j]);
                }
            }
            model.setObjective(obj, GRB.MAXIMIZE);


            ILPCommon ilpCommon = new ILPCommon();

            //add constraint
            ilpCommon.addConstraint(model,mention_overlap,menBinary,"mention overlap");
            ilpCommon.addConstraint(model,mention_contain,menBinary,"mention contain");
            ilpCommon.addConstraint(model,object_contain,men4entBinary,"object contain");


            //optimize
            model.optimize();

            //solution
            ArrayList<Integer> tripleResult=ilpCommon.getSolution(model,men4entBinary,menBinary,men4entStart,men4entNum);
            for (int i=0;i<tripleResult.size();i++){
                int tripleID=tripleResult.get(i);
                System.out.println(triples.get(tripleID));
            }

            // Dispose of model and environment
            model.dispose();
            env.dispose();


        }
        catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }

    }

}

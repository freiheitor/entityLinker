package com.LiuC.ILP.basicILP;

import com.LiuC.ILP.Common.ILPCommon;
import gurobi.*;

import java.util.ArrayList;

/**
 * Created by Freiheiter on 2016/12/2.
 */

public class ILPentityLinking {
    public static void main(String[] args) {

    }

    public static void calILPentityLinking(ILPdataStru ilPdataStru) {
        try {
            ArrayList<Integer> men4entNum = ilPdataStru.getMen4entNum();//each mention to entity num:5,10,8,7
            ArrayList<Integer> men4entStart = ilPdataStru.getMen4entStart();;//each mention start index,0,5,15,23
            ArrayList<Integer> men4entStarInStr = ilPdataStru.getMen4entStarInStr();// each mention and entity pair stars location in String
            ArrayList<String> mention = ilPdataStru.getMention();//memory overlap mention if there are more than one mention to entity pair.
            ArrayList<String> entity = ilPdataStru.getEntity();//memory each entity
            ArrayList<Double> men4entWei = ilPdataStru.getMen4entWei();//memory weight of mention for entity
            ArrayList<Double> men4Wei=ilPdataStru.getMenWei(); //mention for weight
            //conflict
            ArrayList<ArrayList<Integer>> menOnlyOneEnt = ilPdataStru.getMenOnlyOneEnt();
            ArrayList<ArrayList<Integer>> menOverlapMen = ilPdataStru.getMenOverlapMen();
            ArrayList<ArrayList<Integer>> menContainMen = ilPdataStru.getMenContainMen();

            // Model
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.StringAttr.ModelName, "ILPentityLinking");

            //create variable
            GRBVar[] men4entBinary = new GRBVar[entity.size()];
            for(int i=0;i<men4entBinary.length;i++){
                men4entBinary[i]=model.addVar(0.0, 1.0, 0.0, GRB.BINARY,String.valueOf(i));
            }

            //set objective
            int timesMen4wei=1;
            GRBLinExpr expr = new GRBLinExpr();
            for(int i=0;i<men4entBinary.length;i++){
                expr.addTerm(men4entWei.get(i),men4entBinary[i]);
                expr.addTerm(timesMen4wei*men4Wei.get(i),men4entBinary[i]);
            }
            model.setObjective(expr, GRB.MAXIMIZE);

            ILPCommon ilpCommon=new ILPCommon();
            //add constraint
            ilpCommon.addConstraint(model,menOnlyOneEnt,men4entBinary,"menOnlyOneEnt");
            ilpCommon.addConstraint(model,menOverlapMen,men4entBinary,"menOverlapMen");
            ilpCommon.addConstraint(model,menContainMen,men4entBinary,"menContainMen");

            //Optimize
            model.optimize();
            ArrayList varExist=ilpCommon.printSolution(model,men4entBinary,mention,entity);

            // Dispose of model and environment
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }
    }



}

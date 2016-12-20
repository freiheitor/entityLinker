package com.LiuC.ILP.basicILP;

import com.LiuC.ILP.Common.StringControl;
import com.LiuC.common.FileUtil;

import java.util.*;

/**
 * Created by Freiheiter on 2016/11/30.
 */
public class CalILPdata {

    /**
     * read dic to Map<mention,Map<entity,weight>>
     * @param dicIn
     * @param men4entWei
     */
    public static void readDic(String dicIn, HashMap<String, HashMap<String,Double>> men4entWei,HashMap<String,Double> men4Wei){
        FileUtil fileUtil = new FileUtil();
        ArrayList<String> line = new ArrayList<>();
        fileUtil.readLines(dicIn,line);
        Iterator<String> it = line.iterator();
        while(it.hasNext()){
            String aline=it.next();
            String[] lines = aline.split("\t");
//            if (lines.length<=8){
//                System.out.println("error input in read dic,discard it: "+aline+"  index:"+line.indexOf(aline));
//                continue;
//            }
            try{
                //mention and entity exit or not
                String metion=lines[0];
                double ent_wei=Double.parseDouble(lines[4]);
                men4Wei.put(metion,Double.parseDouble(lines[7]));//mention for weight
                if (men4entWei.containsKey(metion)){
                    HashMap<String, Double> ent4wei = men4entWei.get(metion);
                    if (ent4wei.containsKey(lines[1])){
                        System.out.println("[mention and value is overlap]:"+it.next());
                    }
                    else{
                        ent4wei.put(lines[1],ent_wei);
                        men4entWei.put(metion,ent4wei);
                    }
                }
                else{
                    HashMap<String,Double> ent4wei=new HashMap<>();
                    ent4wei.put(lines[1],Double.parseDouble(lines[4]));
                    men4entWei.put(metion,ent4wei);
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("error input in readDic(CalILPdata),discard it: "+aline+"  index:"+line.indexOf(aline));
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * input a sentence, calculate mention and entity datas that corresponded to
     * @param strIn
     * @param men4entWei
     */
    public static void calSentILP(String strIn,HashMap<String, HashMap<String,Double>> men4entWei,HashMap<String,Double> men4wei,ILPdataStru ilPdataStru){
        ArrayList<ArrayList<String>> allsubStr = new ArrayList<>();
        StringControl stringControl=new StringControl();
        StringControl.getAllsubStr(strIn,allsubStr);// each array is locations which the location of starting is same
        //int men4entstarInStrIndex=0;
        int Men4entStart=0;
        for(int i=0;i<allsubStr.size();i++){
            ArrayList<String> subStrlist=allsubStr.get(i);// each array is locations which the location of starting is same
            for (int j=0;j<subStrlist.size();j++){
                String mention=subStrlist.get(j);
                if (men4entWei.containsKey(mention)){
                    HashMap<String,Double> ent4wei = men4entWei.get(mention);
                    ilPdataStru.addMen4entNum(ent4wei.size());
                    ilPdataStru.addMen4entStart(Men4entStart);
                    Men4entStart+=ent4wei.size();
                    Double men_wei=men4wei.get(mention);
                    //men4entstarInStrIndex+=mention.length();

                    Set<?> s_ent4wei=ent4wei.entrySet();
                    Iterator it_ent4wei=s_ent4wei.iterator();
                    while (it_ent4wei.hasNext()){
                        Map.Entry m_ent4wei=(Map.Entry)it_ent4wei.next();
                        ilPdataStru.addMen4entStarInStr(i);
                        ilPdataStru.addMention(mention);
                        ilPdataStru.addEntity((String)m_ent4wei.getKey());
                        ilPdataStru.addMen4entWei((Double) m_ent4wei.getValue());
                        ilPdataStru.addMenWei(men_wei);
                    }
                }
//                else {// if short mention is not contained, it unlikely to contain the long mention
//                    break;
//                }
            }
        }
    }

    /**
     * calculate overlap conflict
     * @param ilPdataStru
     */
    public static void calConflict(ILPdataStru ilPdataStru){
        ArrayList<Integer> men4entNum = ilPdataStru.getMen4entNum();//each mention to entity num:5,10,8,7
        ArrayList<Integer> men4entStart = ilPdataStru.getMen4entStart();;//each mention start index,0,5,15,23
        ArrayList<Integer> men4entStarInStr = ilPdataStru.getMen4entStarInStr();// each mention and entity pair stars location in String
        ArrayList<String> mention = ilPdataStru.getMention();//memory overlap mention if there are more than one mention to entity pair.
        ArrayList<String> entity = ilPdataStru.getEntity();//memory each entity
        ArrayList<Double> men4entWei = ilPdataStru.getMen4entWei();//memory weight of mention for entity

        for (int i=0; i<men4entStart.size()-1;i++){  //one mention(i)
            for(int j=i+1; j<men4entStart.size();j++){ //another mention(j)
                int menIndex_i = men4entStart.get(i);
                int menIndex_j = men4entStart.get(j);
                String mention_i=mention.get(menIndex_i);
                String mention_j=mention.get(menIndex_j);

                //overlap conflict
                StringControl stringControl=new StringControl();
                if (stringControl.judgeOverlapIndex(mention_i, menIndex_i, mention_j, menIndex_j, men4entStarInStr)||stringControl.judgeOverlapIndex(mention_j, menIndex_j, mention_i, menIndex_i, men4entStarInStr)){
                    ArrayList<Integer> over_men=new ArrayList<>();
//                    for(int k=0;k<men4entNum.get(i);k++){
//                        over_men.add(menIndex_i+k);
//                    }
//                    for(int k=0;k<men4entNum.get(j);k++){
//                        over_men.add(menIndex_j+k);
//                    }
                    addList(menIndex_i,men4entNum.get(i),over_men);
                    addList(menIndex_j,men4entNum.get(j),over_men);
                    ilPdataStru.addMenOverlapMen(over_men);
                }
                //contain conflict except overlap(such as "A" and "AB")
                if (stringControl.judgeContainIndex(mention_i,menIndex_i,mention_j,menIndex_j,men4entStarInStr)||stringControl.judgeContainIndex(mention_j,menIndex_j,mention_i,menIndex_i,men4entStarInStr)){
                    ArrayList<Integer> contain_men=new ArrayList<>();
                    addList(menIndex_i,men4entNum.get(i),contain_men);
                    addList(menIndex_j,men4entNum.get(j),contain_men);
//
//                    for(int k=0;k<men4entNum.get(i);k++){
//                        contain_men.add(menIndex_i+k);
//                    }
//                    for(int k=0;k<men4entNum.get(j);k++){
//                        contain_men.add(menIndex_j+k);
//                    }
                    ilPdataStru.addMenContainMen(contain_men);
                }
            }
        }

        //one mention only contain one entity
        for (int i=0; i<men4entStart.size();i++) {  //one mention(i)
            ArrayList<Integer> menOnlyOneEnt_=new ArrayList<>();
            addList(men4entStart.get(i),men4entNum.get(i),menOnlyOneEnt_);
            ilPdataStru.addMenOnlyOneEnt(menOnlyOneEnt_);
        }
    }

    public static void addList(Integer startIndex, Integer stepNum,ArrayList<Integer> listOut){
        for(int i=0;i<stepNum;i++){
            listOut.add(startIndex+i);
        }
    }


    /**
     * calculate contain conflict
     * @param ilPdataStru
     */
    public static void calContainConflict(ILPdataStru ilPdataStru){
        ArrayList<Integer> men4entNum = ilPdataStru.getMen4entNum();//each mention to entity num:5,10,8,7
        ArrayList<Integer> men4entStart = ilPdataStru.getMen4entStart();;//each mention start index,0,5,15,23
        ArrayList<Integer> men4entStarInStr = ilPdataStru.getMen4entStarInStr();// each mention and entity pair stars location in String
        ArrayList<String> mention = ilPdataStru.getMention();//memory overlap mention if there are more than one mention to entity pair.
        ArrayList<String> entity = ilPdataStru.getEntity();//memory each entity
        ArrayList<Double> men4entWei = ilPdataStru.getMen4entWei();//memory weight of mention for entity


        for (int i=0; i<men4entStart.size()-1;i++){  //one mention(i)
            for(int j=i+1; j<men4entStart.size();j++){ //another mention(j)
                int menIndex_i = men4entStart.get(i);
                int menIndex_j = men4entStart.get(j);
                String mention_i=mention.get(menIndex_i);
                String mention_j=mention.get(menIndex_j);
                StringControl stringControl=new StringControl();
                if (stringControl.judgeContainIndex(mention_i,menIndex_i,mention_j,menIndex_j,men4entStarInStr)||stringControl.judgeContainIndex(mention_j,menIndex_j,mention_i,menIndex_i,men4entStarInStr)){
                    ArrayList<Integer> contain_men=new ArrayList<>();
                    for(int k=0;k<men4entNum.get(i);k++){
                        contain_men.add(menIndex_i+k);
                    }
                    for(int k=0;k<men4entNum.get(j);k++){
                        contain_men.add(menIndex_j+k);
                    }
                    ilPdataStru.addMenContainMen(contain_men);
                }
            }
        }
    }


    public static void getILPdata(String str,HashMap<String, HashMap<String,Double>> men4entWei, HashMap<String,Double> men4wei,ILPdataStru ilPdataStru){
        CalILPdata calIlPData=new CalILPdata();

        calIlPData.calSentILP(str, men4entWei,men4wei,ilPdataStru);
        calIlPData.calConflict(ilPdataStru);
        //calIlPData.calContainConflict(ilPdataStru);
    }

    public static void main(String[] args) {
        String dicIn="E:\\project\\entityLinking\\data\\wikiDic\\wikiDic_after_filter_menNorm_100000";
        //String dicIn="/data/liuc/entitylinking/data/wikiDic/wikiDic_after_filter_menNorm";
        //String strIn="数学家学习数学，语言研究数量";
        String strIn=         "语言研究数量、结构[2]、变化[3][4]以及空间[1]等概念的一门学科，" +
                "从某种角度看属于形式科学的一种，数学透过抽象化和逻辑推理的使用，由计数、计算、量度和对物体形状及运动的" +
                "观察而产生，数学家们拓展这些概念，为了公式化新的猜想以及从选定的公理及定义中建立起严谨推导出的定理。";
        CalILPdata calIlPData=new CalILPdata();
        HashMap<String, HashMap<String,Double>> men4entWei =new HashMap<>();
        HashMap<String,Double> men4wei=new HashMap<>();
        calIlPData.readDic(dicIn,men4entWei,men4wei);
        String [] strIns=strIn.split("，");
        ILPentityLinking iLPentityLinking=new ILPentityLinking();
        for(String str:strIns){
            ILPdataStru ilPdataStru=new ILPdataStru();
            getILPdata(str,men4entWei,men4wei, ilPdataStru);
            iLPentityLinking.calILPentityLinking(ilPdataStru);
            System.out.println();
        }
    }
}

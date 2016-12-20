package com.LiuC.ILP.tripleILP;

import com.LiuC.common.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class LoadData {
    public static void main(String[] args) {
        String fileInput="E:\\project\\entityLinking\\data\\triple\\triple_all.all_100000";
        ArrayList<Triple> triples=new ArrayList<>();
        HashMap<String,ArrayList<Integer>> subject4tripID=new HashMap<>();
        LoadData loadData = new LoadData();
        loadData.loadTriple(fileInput,triples,subject4tripID);
    }

    /**
     * load triple file to get ArrayList<triple> and subject for different triple ID
     * @param fileInput:
     * @param triples: ArrayList<triple>
     * @param mention4tripID: subject for different triple
     */
    public static void loadTriple(String fileInput, ArrayList<Triple> triples, HashMap<String,ArrayList<Integer>> mention4tripID){
        System.out.println("load triples start:");
        FileUtil fileUtil=new FileUtil();
        ArrayList<String> linesList = new ArrayList<>();
        fileUtil.readLines(fileInput,linesList);
        int ID=0;
        for(int i=0; i<linesList.size();i++){
            String[] lines = linesList.get(i).split("\t");
            try{
                String subject =lines[0];
                String relation = lines[1];
                String object  = lines[2];

                //set triple
                Triple triple = new Triple(ID);

                triple.setSubject(subject);
                triple.setRelation(relation);
                triple.setObject(object);
                triples.add(triple);

                //get subject for different triple
                fileUtil.addMapList(subject,ID,mention4tripID);

                ID++;

            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                continue;
            }
        }
        System.out.println("load triples finish!");
    }

}

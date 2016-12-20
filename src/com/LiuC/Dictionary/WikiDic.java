package com.LiuC.Dictionary;

import com.LiuC.common.ComUtil;
import com.LiuC.common.FileUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Freiheiter on 2016/11/27.
 */
public class WikiDic {
    /**
     *
     * @param anchorIn: input file
     * @param wikiDicOut:output file
     */
    public static void getWikiDic(String anchorIn, String wikiDicOut){
        FileUtil fileUtil = new FileUtil();
        ArrayList<String> lines = new ArrayList<>();
        fileUtil.readLines(anchorIn, lines);
        //Map(mention,Map<entity,num>)
        Map<String,Map<String,Integer>> men4ent = new HashMap<String,Map<String,Integer>>();
        for (int i=0; i < lines.size(); i++){
            String line = lines.get(i);
            dealLine(line,men4ent);
        }
        wriDic(men4ent,wikiDicOut);
    }

    /**
     * write file and normalization by the number of entity
     * @param men4ent
     * @param wikiDicOut
     */
    public static void wriDic(Map<String,Map<String,Integer>> men4ent, String wikiDicOut){
        FileUtil fileUtil = new FileUtil();
        ComUtil comUtil = new ComUtil();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(wikiDicOut),"UTF-8"));//(wikiDicOut, true),add text in the end
            //writer = new BufferedWriter(new FileWriter(new File(wikiDicOut),"UTF-8"));
            for(Map.Entry<String, Map<String,Integer>> entry : men4ent.entrySet()) { //entity is each mention
                Map<String, Integer> ent4Num = entry.getValue(); //entity and num
                ent4Num = (HashMap<String, Integer>) comUtil.sortByValue(ent4Num, 0);
                //normalization2: the sum of square
                double squSum=fileUtil.getSquValue(ent4Num);
                int men4numSize=men4ent.size();
                //normalization1: (current-min)/(max-min)
                ArrayList<Integer> orderNum=fileUtil.orderValue(ent4Num);
                double maxSub=orderNum.get(0) - orderNum.get(ent4Num.size()-1)+men4numSize;

                Set<?> s = ent4Num.entrySet();
                Iterator<?> it = s.iterator();
                while (it.hasNext()) {
                    Map.Entry m = (Map.Entry) it.next();
                    int key = (Integer) m.getValue();
                    double normValue = 0.0D;
                    //normValue = key/squSum;
//                    if(maxSub == 0){
//                        normValue=1;
//                    }
                    if(maxSub == men4numSize){
                        normValue=1;
                    }
                    else{
                        normValue = (key - orderNum.get(ent4Num.size()-1)+1)/maxSub;
                    }
                    writer.write(entry.getKey()+"\t"+m.getKey() + "\t" + m.getValue() + "\t"+normValue+"\t"+key/squSum+"\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void dealLine(String line, Map<String,Map<String,Integer>> men4ent){
        line = line.replaceAll("Category:","");
        FileUtil fileUtil = new FileUtil();
        String [] lines = line.split("\t");
        if (lines.length != 3 || lines[0].isEmpty())
        {
            return;
        }
        //mention and entity exit or not
        if (men4ent.containsKey(lines[0])){
            Map<String, Integer> ent4num = men4ent.get(lines[0]);
            if (ent4num.containsKey(lines[1])){
                ent4num.put(lines[1],ent4num.get(lines[1])+Integer.parseInt(lines[2]));
            }
            else{
                ent4num.put(lines[1],Integer.parseInt(lines[2]));
            }
        }
        else{
            Map<String,Integer> ent4num=new HashMap<>();
            ent4num.put(lines[1],Integer.parseInt(lines[2]));
            men4ent.put(lines[0],ent4num);
        }
    }

    public static void main(String[] args) {
        getWikiDic("E:\\project\\entity linking\\wikipedia\\zhwiki-20160501\\anchor_entity_map","Data/Dic/wikiDic");
    }
}

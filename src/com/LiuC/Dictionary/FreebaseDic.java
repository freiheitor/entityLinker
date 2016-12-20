package com.LiuC.Dictionary;

import com.LiuC.common.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Freiheiter on 2016/11/29.
 */
public class FreebaseDic {

    public static void getFreebaseDic(ArrayList<String> freebaseIn, String freebaseOut){
        HashMap<String,ArrayList<String>> men4ent = new HashMap<>();
        FileUtil fileUtil =new FileUtil();
        ArrayList<String> lines = new ArrayList<>();
        for(int i=0; i<freebaseIn.size();i++){
            fileUtil.readLines(freebaseIn.get(i),lines);
            calMen4ent(lines,men4ent);
        }
        System.out.println("mention size: "+men4ent.size());
        fileUtil.writeLinesApped(freebaseOut,men4ent);

    }

    /**
     * calculate mention for entity, and entity is list
     * @param line
     * @param men4ent
     */
    public static void calMen4ent(ArrayList<String> line,Map<String,ArrayList<String>> men4ent){
        FileUtil fileUtil = new FileUtil();
        for(int i=0; i<line.size();i++){
            String [] lines = line.get(i).split("\t");
            if(lines.length > 1)
            {
                for(int j=1;j<lines.length;j++){
                    fileUtil.addMapList(lines[j],lines[0],men4ent);
                }
            }
            else{
                System.out.println("can't split by \t error: "+lines);
            }
        }
    }



    public static void main(String[] args) {
        String FreeBaseOut = "/data/liuc/entitylinking/dic/freeBaseDic";
        ArrayList<String> FreeBaseIn = new ArrayList<>();
        String mid2labels = "/data/liuc/freebase/mid2labels";
        String mid2name = "/data/liuc/freebase/mid2names";
        FreeBaseIn.add(mid2labels);
        FreeBaseIn.add(mid2name);
        getFreebaseDic(FreeBaseIn,FreeBaseOut);
    }
}

package com.LiuC.ILP.Common;

import jdk.nashorn.internal.ir.ContinueNode;

import java.util.ArrayList;

/**
 * Created by Freiheiter on 2016/12/19.
 */
public class StringControl {

    public static void main(String[] args) {

        System.out.println(getOverlapString("好妈妈","你妈妈"));

//        System.out.println(getOverlapString("好妈妈","你妈妈"));
    }

    /**
     * get all substring from input string
     * @param str
     * @param allsubStr：Arraylist<Arraylist<same location to start>>
     */
    public static void getAllsubStr(String str,ArrayList<ArrayList<String>> allsubStr){
        for(int k=0;k<str.length();k++){//控制步长
            ArrayList<String> subStrlist=new ArrayList<>();
            for(int i=k;i<str.length();i++){//对字符串逐步扫描
                String substr=str.substring(k, i+1);
                subStrlist.add(substr);
            }
            allsubStr.add(subStrlist);
        }
    }

    //overlap string and its indexing
    public static boolean judgeOverlapIndex(String mention_i,int menIndex_i,String mention_j,int menIndex_j,ArrayList<Integer> men4entStarInStr){
        boolean conflict=false;

        //mention i in the front
        try {
            if(getOverlapString(mention_i,mention_j)!=null){ //exist overlap
                int subIndex_ji=men4entStarInStr.get(menIndex_j)-men4entStarInStr.get(menIndex_i);
                if (subIndex_ji<0){
                    conflict=false;
                }
                else if(mention_i.length()>subIndex_ji){ //length conflict
                    conflict=true;
                }
            }
        }catch (Exception e){
            System.out.println("get overlap string error. mention_i->"+mention_i+" menIndex_i->"+men4entStarInStr.get(menIndex_i)+" mention_j->"+mention_j+" menIndex_j->"+men4entStarInStr.get(menIndex_j));
            e.printStackTrace();
        }

        return conflict;
    }

    /**
     * clean the overlap between the ending of str1 and the starting of str2, and merge them
     * if existing overlap, return null;
     * hava a bug: 出现叠词的： 好妈妈 妈妈
     * @param str1
     * @param str2
     * @return
     */
//    public static String getOverlapString(String str1,String str2) {
//        int index = -1;//重叠的开始位置
//        int len = 0;//重叠串的长度
//        String result = "";
//        for (int i = 0; i < str1.length(); i++) {//用前串控制外层循环,“指针”向右移动
//            if(str1.charAt(i) == str2.charAt(0)){//判断右移过程“指针”位置的字符是否与后串的第一个字符匹配，需匹配才有重叠
//                index = i;
//                len ++;
//                if(str1.length() - i > str2.length()){//如果前串的指针位置比后串的长度还要长，则退出，即没有重叠串
//                    index = -1;
//                    break;
//                }
//                for (int j = 1; j < str1.length() - i; j++) {
//                    if (str1.charAt(i + j) == str2.charAt(j)) {//前后串移动匹配，找出最长重叠串
//                        len ++;
//                    }else{
//                        index = -1;
//                        len = 0;
//                        break;
//                    }
//                }
//            }
//        }
//        if(index == -1){
//            result = null;
//        }else {
//            if(str2.length()<len){
//                System.out.println("get overlap string error: "+str1+" "+str2);
//            }else{
//                result = str1 + str2.substring(0,len);
//            }
//        }
//        return result;
//    }

    /**
     * contain and conflict with its indexing
     * @param mention_i
     * @param menIndex_i
     * @param mention_j
     * @param menIndex_j
     * @param men4entStarInStr
     * @return
     */
    public static boolean judgeContainIndex(String mention_i,int menIndex_i,String mention_j,int menIndex_j,ArrayList<Integer> men4entStarInStr){
        boolean conflict=false;
        if (mention_i.contains(mention_j)){
            if (men4entStarInStr.get(menIndex_i)<=men4entStarInStr.get(menIndex_j)&&(men4entStarInStr.get(menIndex_j)+mention_j.length())<=(men4entStarInStr.get(menIndex_i)+mention_i.length())){
                conflict=true;
            }
        }
        return conflict;
    }


    public static String getOverlapString(String str1, String str2){
        int str1Len=str1.length();
        String overStr="";
        String returnStr=null;

        for(int i=0;i<str2.length();i++){
            String tmpStr2=str2.substring(0,i+1);
            if (str1.length()>=tmpStr2.length()&&str1.substring(str1Len-tmpStr2.length(),str1Len).equals(tmpStr2)){
                overStr=tmpStr2;
            }
//            else {
//                break;
//            }
        }

        if (!overStr.isEmpty()){
            returnStr=str1+str2.substring(overStr.length(),str2.length());
        }
        return returnStr;
    }
}

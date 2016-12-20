package com.LiuC.ILP.basicILP;

import java.util.ArrayList;

/**
 * Created by Freiheiter on 2016/11/30.
 */
public class ILPdataStru {

    private ArrayList<Integer> men4entNum = new ArrayList<>();//each mention to entity num:5,10,8,7
    private ArrayList<Integer> men4entStart = new ArrayList<>();//each mention start index,0,5,15,23
    private ArrayList<Integer> men4entStarInStr = new ArrayList<>();// each mention and entity pair stars location in String
    private ArrayList<String> mention = new ArrayList<>();//memory overlap mention if there are more than one mention to entity pair.
    private ArrayList<String> entity = new ArrayList<>();//memory each entity
    private ArrayList<Double> men4entWei = new ArrayList<>();//memory weight of mention for entity
    private ArrayList<Double> menWei=new ArrayList<>(); //mention for weight

    //conflict
    private ArrayList<ArrayList<Integer>> menOverlapMen = new ArrayList<>();  //all mention for entity which conflict get the only one
    private ArrayList<ArrayList<Integer>> menContainMen = new ArrayList<>();  //all mention for entity which conflict get the only one
    private ArrayList<ArrayList<Integer>> menOnlyOneEnt = new ArrayList<>();


    public ArrayList<ArrayList<Integer>> getMenOnlyOneEnt() {
        return menOnlyOneEnt;
    }

    public void addMenOnlyOneEnt(ArrayList<Integer> add_menOnlyOneEnt) {
        this.menOnlyOneEnt.add(add_menOnlyOneEnt);
    }


    public ArrayList<Double> getMen4entWei() {
        return men4entWei;
    }

    public void addMen4entWei(double  add_men4entWei) {
        this.men4entWei.add(add_men4entWei);
    }

    public ArrayList<Integer> getMen4entNum() {
        return men4entNum;
    }

    public void addMen4entNum(int add_men4entNum) {
        this.men4entNum.add(add_men4entNum);
    }

    public ArrayList<Integer> getMen4entStart() {
        return men4entStart;
    }

    public void addMen4entStart(int add_men4entStart) {
        this.men4entStart.add(add_men4entStart);
    }

    public ArrayList<Integer> getMen4entStarInStr() {
        return men4entStarInStr;
    }

    public void addMen4entStarInStr(int add_men4entStarInStr) {
        this.men4entStarInStr.add(add_men4entStarInStr);
    }


    public ArrayList<String> getMention() {
        return mention;
    }

    public void addMention(String add_mention) {
        this.mention.add(add_mention);
    }

    public ArrayList<String> getEntity() {
        return entity;
    }

    public void addEntity(String add_entity) {
        this.entity.add(add_entity);
    }

    public ArrayList<Double> getMenWei() {
        return menWei;
    }

    public void addMenWei(double add_menWei) {
        this.menWei.add(add_menWei);
    }


    public ArrayList<ArrayList<Integer>> getMenContainMen() {
        return menContainMen;
    }

    public void addMenContainMen(ArrayList<Integer> add_men4entOnlyOne) {
        this.menContainMen.add(add_men4entOnlyOne);
    }

    public ArrayList<ArrayList<Integer>> getMenOverlapMen() {
        return menOverlapMen;
    }

    public void addMenOverlapMen(ArrayList<Integer> add_menOverlapMen) {
        this.menOverlapMen.add(add_menOverlapMen);
    }


}

package com.example.wolframapitestapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MathGeneratorMark2 {



    private String[] numbers=new String[5];
    private int correctnumber;



    private String problemid;
    private int wincount=0;
    private String difficulty;
    private String category;
    private String topic;
    private String instruction;

    public MathGeneratorMark2(JSONObject jsonObject){

        try {
            correctnumber =  Integer.parseInt( jsonObject.getString("correct_choice"));
            JSONArray jsonArray = jsonObject.getJSONArray("choices");
            for(int i=0;i<jsonArray.length();++i) {



                numbers[i] =xmltostring(jsonArray.getString(i));
            }
            problemid = jsonObject.getString("id");
            difficulty = jsonObject.getString("difficulty");
            category = jsonObject.getString("category");
            topic= jsonObject.getString("topic");
            instruction = jsonObject.getString("instruction");


        } catch (JSONException e) {
            e.printStackTrace();
        }




//>"
    }

 private String xmltostring (String xml)
 {
     String result = "";
     while (xml.contains(">")) {

         //if(xml.indexOf(">") + 1!=""))
         xml = xml.substring(xml.indexOf(">") + 1);
         if (!xml.contains("<")) break;
         result += xml.substring(0, xml.indexOf("<"));
        // xml = xml.substring(xml.indexOf("<"));
     }


     return result;

 }



    //public MathGeneratorMark2(JSONArray choices,String ID, int correctnumber){
//
    //    try {
    //        correctnumber = Integer.parseInt( choices.getString(correctnumber));
    //
    //
    //    } catch (JSONException e) {
    //        e.printStackTrace();
    //    }
    //     for(int i=0;i<choices.length();++i){
    //     try {
    //         numbers[i] = Integer.parseInt( choices.getString(i) );
    //     } catch (JSONException e) {
    //         e.printStackTrace();
    //     }
//
//
    //         }

   // }


    public void setWincount(int wincount) {
        this.wincount = wincount;
    }

    public String getProblemid() {
        return problemid;
    }

    public void checkanser(int pick){
        if (pick==correctnumber)
                getawin();


    }

    private void getawin() {
        this.wincount = ++this.wincount;
    }


    public String getNumber(int i) {
        return numbers[i];
    }



    public int getWincount() {
        return wincount;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getTopic() {
        return topic;
    }

    public String getInstruction() {
        return instruction;
    }
}


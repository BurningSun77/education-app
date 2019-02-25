package com.example.cerebral;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONInterpreter {

    private String[] numbers=new String[5];
    private int correctNumber;

    private String problemID;
    private int winCount = 0;
    private String difficulty;
    private String category;
    private String topic;
    private String instruction;

    public JSONInterpreter(JSONObject jsonObject){

        try {

            correctNumber =  Integer.parseInt( jsonObject.getString("correct_choice"));
            JSONArray jsonArray = jsonObject.getJSONArray("choices");
            for(int i=0;i<jsonArray.length();++i) {

                numbers[i] = parseXML(jsonArray.getString(i));
            }
            problemID = jsonObject.getString("id");
            difficulty = jsonObject.getString("difficulty");
            category = jsonObject.getString("category");
            topic= jsonObject.getString("topic");
            instruction = jsonObject.getString("instruction");
        } catch (JSONException e) {

            e.printStackTrace();
        }




//>"
    }

    private String parseXML(String xml) {

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



    //public JSONInterpreter(JSONArray choices,String ID, int correctNumber){
    //
    //    try {
    //        correctNumber = Integer.parseInt( choices.getString(correctNumber));
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


    public void setWinCount(int winCount) {

        this.winCount = winCount;
    }

    public String getProblemID() {

        return problemID;
    }

    public void checkAnswer(int pick) {

        if (pick== correctNumber)
                addWin();
    }

    private void addWin() {

        this.winCount = ++this.winCount;
    }


    public String getNumber(int i) {

        return numbers[i];
    }

    public int getWinCount() {

        return winCount;
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


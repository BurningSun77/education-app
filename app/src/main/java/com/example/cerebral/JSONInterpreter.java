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

                numbers[i] = parseMathMLFull(jsonArray.getString(i));
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

    private String parseMathMLFull(@org.jetbrains.annotations.NotNull String m) {

        String result = "";
        while (m.contains("<")) {

            m = m.substring(m.indexOf("<") + 1);
            String tag = m.substring(0, m.indexOf(">"));
            String root;
            String temp;

            m = m.replace("<mrow>", "").replace("</mrow>", "");

            m = m.substring(m.indexOf(">") + 1);
            switch (tag) {

                case "msqrt":
                    result += "sqrt(";
                    break;
                case "\\/msqrt":
                    result += ")";
                    break;
                case "mroot":
                    root = m.substring(0, m.indexOf("/mroot"));
                    temp = "";
                    while (root.contains(">")) {

                        root = root.substring(root.indexOf(">") + 1);
                        if (!root.contains("<")) break;
                        if (temp.length() == 0) {

                            temp = root.substring(0, root.indexOf("<"));
                        } else {

                            root = root.substring(root.indexOf(">") + 1);
                            temp = root.substring(0, root.indexOf("<")) + "root(" + temp + ")";
                            break;
                        }
                        root = root.substring(root.indexOf("<"));
                    }
                    result += temp;
                    break;
                case "mfrac":
                    m = m.substring(m.indexOf(">") + 1);
                    result += "(" + m.substring(0, m.indexOf("<")) + " / ";
                    m = m.substring(m.indexOf(">") + 1);
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<")) + ")";
                    break;
                case "msup":
                    m = m.substring(m.indexOf(">") + 1);
                    result += "(" + m.substring(0, m.indexOf("<")) + "^";
                    m = m.substring(m.indexOf(">") + 1);
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<")) + ")";
                    break;
                case "msub":
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<"));
                    m = m.substring(m.indexOf(">") + 1);
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<"));
                    break;
                case "mi":
                case "mn":
                    result += m.substring(0, m.indexOf("<")) + " ";
                    break;
                case "mo":
                    if (m.substring(1, 2).equals("&")) {

                        switch (m.substring(1, m.indexOf("<") - 1)) {

                            case "&Cross;":
                                result += "×";
                                break;
                            case "&#xF7;":
                                result += "÷";
                                break;
                            case "&DifferentialD;":
                                result += "dx/dy";
                            default:
                                result += m.substring(0, m.indexOf("<"));
                                break;
                        }
                    } else {

                        result += m.substring(0, m.indexOf("<"));
                    }
                    break;
                case "/math":
                    if(m.contains("<"))
                    result += m.substring(0, m.indexOf("<"));
                    break;
            }
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


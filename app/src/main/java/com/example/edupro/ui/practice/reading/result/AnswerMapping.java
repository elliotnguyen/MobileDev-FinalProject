package com.example.edupro.ui.practice.reading.result;

public class AnswerMapping {
    public static String mapAnswer(int type, String answerIdx) {
        if (type == 0) {
            switch (answerIdx) {
                case "0":
                    answerIdx = "TRUE";
                    break;
                case "1":
                    answerIdx = "FALSE";
                    break;
                case "2":
                    answerIdx = "NOT GIVEN";
                    break;
                default:
                    answerIdx = "";
                    break;
            }
        } else if (type == 1) {
            switch (answerIdx) {
                case "0":
                    answerIdx = "A";
                    break;
                case "1":
                    answerIdx = "B";
                    break;
                case "2":
                    answerIdx = "C";
                    break;
                case "3":
                    answerIdx = "D";
                    break;
                case "4":
                    answerIdx = "E";
                    break;
                default:
                    answerIdx = "";
                    break;
            }
        }
        return answerIdx;
    }
}

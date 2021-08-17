package com.app.skinCancerDetection.Model.Skin_cancer;


public class PredictResponse {
    private String classification;
    private String percentage;

    public PredictResponse(String classification, String percentage) {
        this.classification = classification;
        this.percentage = percentage;
    }

    public String getClassification() {
        return classification;
    }
    public String getPercentage() {
        return percentage;
    }
}
package com.ipcb.ad.microservicogestao.Models;

public class PredictionDTO {
    private float confidence;
    private String label;
    private float x_min;
    private float y_min;
    private float x_max;
    private float y_max;

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getX_min() {
        return x_min;
    }

    public void setX_min(float x_min) {
        this.x_min = x_min;
    }

    public float getY_min() {
        return y_min;
    }

    public void setY_min(float y_min) {
        this.y_min = y_min;
    }

    public float getX_max() {
        return x_max;
    }

    public void setX_max(float x_max) {
        this.x_max = x_max;
    }

    public float getY_max() {
        return y_max;
    }

    public void setY_max(float y_max) {
        this.y_max = y_max;
    }
}

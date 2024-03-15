package com.ipcb.ad.microservicoia.Models;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Detector {
    private String message;
    private float count;
    ArrayList< Prediction > predictions = new ArrayList < Prediction > ();
    private boolean success;
    private float processMs;
    private float inferenceMs;
    private float code;
    private String command;
    private String moduleId;
    private String executionProvider;
    private boolean canUseGPU;
    private float analysisRoundTripMs;
    private String processedBy;
}
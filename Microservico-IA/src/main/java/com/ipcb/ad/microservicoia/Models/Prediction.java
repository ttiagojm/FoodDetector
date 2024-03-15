package com.ipcb.ad.microservicoia.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float confidence;
    private String label;
    private float x_min;
    private float y_min;
    private float x_max;
    private float y_max;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_id")
    @JsonIgnoreProperties("objetosIdentificados")
    private Tarefa tarefa;

    public Prediction(float confidence, String label, float x_min, float y_min, float x_max, float y_max) {
        this.confidence = confidence;
        this.label = label;
        this.x_min = x_min;
        this.y_min = y_min;
        this.x_max = x_max;
        this.y_max = y_max;
    }
}

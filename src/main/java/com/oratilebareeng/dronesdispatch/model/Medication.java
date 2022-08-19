package com.oratilebareeng.dronesdispatch.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Medication {
    @Id
    private String code;
    private String name;
    private String image; // todo: implement image upload, update
    private int weight;
    @ManyToMany( mappedBy = "loadedMedication")
    private List<Drone> dronesLoadedOn = new ArrayList<>();

}

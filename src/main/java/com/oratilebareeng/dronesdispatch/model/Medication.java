package com.oratilebareeng.dronesdispatch.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotNull
    @NotBlank
    // validate code input
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Only Uppercase letters, numbers, \'_\' allowed")
    private String code;
    @NotNull
    @NotBlank
    // validated medication name input
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Only letters, numbers, \'-\', \'_\' allowed")
    private String name;
    private String image; // todo: implement image upload, update
    private int weight;
    @ManyToMany( mappedBy = "loadedMedication")
    private List<Drone> dronesLoadedOn = new ArrayList<>();

}

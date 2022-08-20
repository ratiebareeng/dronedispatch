package com.oratilebareeng.dronesdispatch.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class Drone {
    @Id
    @Size(max = 100, message = "Serial Number must be 100 characters max")
    @Column(length = 100)
    @Valid
    @NotNull(message = "Please provide a Serial Number")
    @NotBlank
    //@Pattern()
    private final String serialNumber;
    @NotNull(message = "Please provide drone model")
    @NotBlank
    private final String model;
    @NotNull(message = "Please provide maximum weight drone can carry")
    @Min(10)
    @Max(500)
    private final double weight;
    @NotNull(message = "Please provide drone battery capacity")
    @Min(0)
    @Max(100)
    private int batteryCapacity;
    @Transient
    private int loadedCapacity = 0;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please provide drone state")
    private DroneState state;
    @ManyToMany
    // allow adding medication not in db
    //@NotFound(action = NotFoundAction.IGNORE)
    // allow serializing nested object
    @JsonBackReference
    @JoinTable(
            name = "loadedMedication",
            joinColumns = @JoinColumn(name = "serialNumber"),
            inverseJoinColumns = @JoinColumn(name = "code")
    )
    private List<Medication> loadedMedication = new ArrayList<>();


    public Drone(String serialNumber, String model, double weight, int batteryCapacity, DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weight = weight;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
    }

    // load medication into drone
    public void loadMedication(Medication medication){
        loadedMedication.add(medication);
    }

    // get drone's total loaded capacity
    public int getLoadedCapacity() {
        return this.loadedMedication.stream()
                .collect(Collectors.summingInt(medication -> medication.getWeight()));

    }
}


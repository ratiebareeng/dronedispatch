package com.oratilebareeng.dronesdispatch.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
    private final String serialNumber;
    private final String model;
    private final double weight;
    private int batteryCapacity;
    @Transient
    private int loadedCapacity = 0;
    @Enumerated(EnumType.STRING)
    private DroneState state;
    @OneToMany
            (
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "drone_serial_number")
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


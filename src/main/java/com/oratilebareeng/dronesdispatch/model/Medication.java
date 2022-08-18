package com.oratilebareeng.dronesdispatch.model;

import lombok.*;

import javax.persistence.*;

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
    @SequenceGenerator(
            name = "medication_id_sequence",
            sequenceName = "medication_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "medication_id_sequence"
    )
    private final Long id;
    private final String name;
    private final String code;
    private final String image; // todo: implement image upload, update
    private final int weight;

}

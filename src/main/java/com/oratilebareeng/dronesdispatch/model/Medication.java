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
    private String code;
    private String name;
    private String image; // todo: implement image upload, update
    private int weight;

}

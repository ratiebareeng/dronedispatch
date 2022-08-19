package com.oratilebareeng.dronesdispatch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@Getter
@Setter
public class MedicationPage {
    private int pageNumber = 0;
    private int pageSize = 5;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "code";
}

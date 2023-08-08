package com.iplat.master.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableEntity {
    private int minRow;
    private int maxRow;

    private TypeTable type;

    List<FieldSample> fieldSamples;

}

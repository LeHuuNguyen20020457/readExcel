package com.iplat.master.file;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetEntity {
    private int sheetNumber;

    private Coordinate nameMethod;

    private Coordinate input;

    private Coordinate export;

    private Coordinate location;

    private List<TableEntity> tableEntities;


}

package com.iplat.master.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldSample {
    private String type;

    private String name;

    private Integer length;

    private Integer accuracy;

    private boolean required;

    public String getGetterAndSetterField() {
        String str = this.name;
        String firstLetter = str.substring(0, 1).toUpperCase();
        String restOfWord = str.substring(1).toLowerCase();

        String result = firstLetter + restOfWord;
        return result;
    }
}

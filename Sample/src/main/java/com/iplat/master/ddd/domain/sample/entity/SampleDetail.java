package com.iplat.master.ddd.domain.sample.entity;

import java.io.Serializable;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    
        
                private String gascode;
    
        
                private String businesscode;
    
        
                private String businessname;
    
        
                private String versionno;
    
}
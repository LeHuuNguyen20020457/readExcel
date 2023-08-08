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
public class SampleRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    
        
                private String gascode;
    
        
                @Length(max = 4)
                private String retailerid;
    
                @NotBlank(message = "Field required")
        
                private String starttime;
    
                @NotBlank(message = "Field required")
        
                private String endtime;
    
                @NotBlank(message = "Field required")
        
                private Integer tax;
    
        
                private String renewableid;
    
}
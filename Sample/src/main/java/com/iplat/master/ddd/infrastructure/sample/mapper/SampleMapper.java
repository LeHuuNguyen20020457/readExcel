package com.iplat.master.ddd.infrastructure.sample.mapper;

import com.iplat.master.ddd.domain.sample.entity.SampleDetail;
import com.iplat.master.ddd.domain.sample.entity.SampleEntity;
import com.iplat.master.ddd.domain.sample.entity.SampleRequest;
import org.mapstruct.Mapper;

@Mapper
public interface SampleMapper {
    /**
     * Request function to get Sample Detail
     * @Param sampleRequest request get SampleEntity
     * @return SampleEntity return SampleEntity
     */
    SampleDetail getSampleDetail(SampleRequest sampleRequest);


    /**
     * Mapper function to get Sample Detail
     * @param businessCode Business Code
     * @return SampleEntity return SampleEntity
     */
    SampleEntity getSampleByBusinessCode(String businessCode);
}

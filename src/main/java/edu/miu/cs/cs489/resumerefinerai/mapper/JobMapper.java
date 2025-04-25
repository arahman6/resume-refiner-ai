package edu.miu.cs.cs489.resumerefinerai.mapper;

import edu.miu.cs.cs489.resumerefinerai.dto.response.JobResponse;
import edu.miu.cs.cs489.resumerefinerai.model.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {
    JobResponse toDto(Job job);
}

package com.project.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.Constant.Status;
import com.project.DTO.Error.ErrorDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

//This is a generic class that is designed to handle errors messages returned from fields validations and exceptions
@Builder
@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWithError
{
    private Status status;
    private List<ErrorDTO> errors ;
}

package com.project.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.Constant.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

//This class will provide a unified response for data or errors returned by the apis.
@ToString
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWithData<T>
{
    private Status status;
    private T data;
}

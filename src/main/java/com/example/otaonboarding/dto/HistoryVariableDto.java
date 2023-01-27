package com.example.otaonboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryVariableDto {
    public String  activityInstanceId;
    public String batchId;
    public String caseExecutionId;
    public String caseInstanceId;
    public String errorMessage;
    public String executionId;
    public String id;
    public String name;
    public String processDefinitionId;
    public String processInstanceId;
    public String taskId;
    public String tenantId;
    public String type;
    public  Object value;
    public Object valueInfo;


}

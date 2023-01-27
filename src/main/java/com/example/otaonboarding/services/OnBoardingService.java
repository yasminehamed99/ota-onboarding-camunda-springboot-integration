package com.example.otaonboarding.services;
import com.example.otaonboarding.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class OnBoardingService {
    @Value(value = "${start.url}")
    private String startProcessUrl;
    @Value("${variableInstance.url}")
    private String variableInstanceUrl;
    @Value("${getTask.url}")
    private String getTaskIdUrl;

    ResonseData resonseData=new ResonseData();
    Vat vat=new Vat();
    ObjectMapper mapper = new ObjectMapper();
    HttpHeaders headers = new HttpHeaders();
    RestTemplate restTemplate=new RestTemplate();
    public String login() throws JsonProcessingException {
        String vat="312345678900003";
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response= restTemplate.postForEntity(startProcessUrl, entity, String.class);

        resonseData= mapper.readValue(response.getBody(), ResonseData.class);
        return vat;

    }
    public List<String> generateOTP(String numberOfOtps,String vatNumber) throws JsonProcessingException {
        headers.setContentType(APPLICATION_JSON);
        String id=getTaskId();
        OtpNum otpNum =new OtpNum(numberOfOtps,"String");
        vat=new Vat(vatNumber,"String");
        GenerateOTPDto variable=new GenerateOTPDto(otpNum,vat);
        GenerateOTPRoot root=new GenerateOTPRoot(variable);
        HttpEntity<GenerateOTPRoot> entity = new HttpEntity<GenerateOTPRoot>(root,headers);
        String urlComplete
                = "http://localhost:8080/engine-rest/task/"+id+"/complete";;
         restTemplate.postForEntity(urlComplete, entity, String.class);

        List<String> otps= (List<String>) getValue(resonseData.id,3);

        return otps;

    }
    public ValidationResponse ValidateOtp(String otpString,String vatNumber) throws JsonProcessingException {
        Otp otp=new Otp(otpString,"String");
        Vat vatNum=new Vat(vatNumber,"String");
        ValidateOTPDto variables=new ValidateOTPDto(otp,vatNum);
        ValidateOTPRoot root=new ValidateOTPRoot(variables);
        HttpEntity<ValidateOTPRoot> entity = new HttpEntity<ValidateOTPRoot>(root,headers);
        String id=getTaskId();
        String urlComplete
                = "http://localhost:8080/engine-rest/task/"+id+"/complete";;
        ResponseEntity<String> response3=restTemplate.postForEntity(urlComplete, entity, String.class);
        String validationResult= (String) getValue(resonseData.id,8);
        ObjectMapper mapper2 = new ObjectMapper();
        ValidationResponse validationResponse= mapper2.readValue(validationResult, ValidationResponse.class);


       return validationResponse;

    }
    public Object getValue(String id ,int valueId) throws JsonProcessingException {
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                variableInstanceUrl+id, HttpMethod.GET, requestEntity, String.class);
        List<HistoryVariableDto> participantJsonList = mapper.readValue(response.getBody(), new TypeReference<List<HistoryVariableDto>>(){});
        HistoryVariableDto responseDataDto=participantJsonList.get(valueId);
        return responseDataDto.getValue();
    }
    String getTaskId() throws JsonProcessingException {
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        String url
                = getTaskIdUrl+resonseData.id;;
        ResponseEntity<String> response = restTemplate.exchange(
                getTaskIdUrl+resonseData.id, HttpMethod.GET, requestEntity, String.class);
        List<ResponseDataDto> participantJsonList = mapper.readValue(response.getBody(), new TypeReference<List<ResponseDataDto>>(){});
        ResponseDataDto responseDataDto=participantJsonList.get(0);
        return responseDataDto.id;
    }

}

package com.phuonghuynh.ws;

import com.phuonghuynh.ws.model.TinServiceUser;
import com.phuonghuynh.ws.model.ServiceStatusResult;
import com.phuonghuynh.ws.model.TinName;
import com.phuonghuynh.ws.model.ValidateTinNameResult;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Created by phuonghqh on 4/28/15.
 */

@WebService(targetNamespace = "http://www.TinCheck.com/WebServices/PVSService/")
public interface TinWebService {

  @WebMethod(operationName = "ServiceStatus")
  @WebResult(name = "ServiceStatusResult", targetNamespace = "http://www.TinCheck.com/WebServices/PVSService/")
  ServiceStatusResult serviceStatus(
    @WebParam(name = "CurUser", targetNamespace = "http://www.TinCheck.com/WebServices/PVSService/")
    TinServiceUser input);

  @WebMethod(operationName = "ValidateTinName")
  @WebResult(name = "ValidateTinNameResult", targetNamespace = "http://www.TinCheck.com/WebServices/PVSService/")
  ValidateTinNameResult validateTinName(
    @WebParam(name = "TinName", targetNamespace = "http://www.TinCheck.com/WebServices/PVSService/")
    TinName tinName,
    @WebParam(name = "CurUser", targetNamespace = "http://www.TinCheck.com/WebServices/PVSService/")
    TinServiceUser input);



}

package org.abrahamalarcon.datastore.controller;

import org.abrahamalarcon.datastore.dom.response.BaseError;
import org.abrahamalarcon.datastore.dom.response.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by AbrahamAlarcon on 3/21/2017.
 */
@Controller
public class ErrorHandlingController {

    @RequestMapping("/404")
    public @ResponseBody
    BaseResponse notFound() {
        BaseResponse jsonResponse = new BaseResponse();
        BaseError baseError = new BaseError();
        jsonResponse.setError(baseError);
        jsonResponse.getError().setMessage("Not Found");
        jsonResponse.getError().setStatus(404);
        return jsonResponse;
    }
    
    @RequestMapping("/500")
    public @ResponseBody BaseResponse serverError() {
        BaseResponse jsonResponse = new BaseResponse();
        BaseError baseError = new BaseError();
        jsonResponse.setError(baseError);
        jsonResponse.getError().setMessage("Internal Server Error");
        jsonResponse.getError().setStatus(500);
        return jsonResponse;
    }
}

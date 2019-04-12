package com.demo.mapper;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.demo.misc.ErrorCode;
import com.demo.pojo.ErrorResponse;

@Provider
public class WebApplicationExceptionMapper implements
        ExceptionMapper<javax.ws.rs.WebApplicationException> {
	@Produces("application/json")
	public Response toResponse(javax.ws.rs.WebApplicationException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(ErrorCode.MALFORMED_REQUEST);
		errorResponse.setErrorMessage(ex.getMessage());
		return Response.status(500).header("Content-Type","application/json").entity(errorResponse).build();
    }
}


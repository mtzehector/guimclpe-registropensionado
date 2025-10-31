/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.util;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.registropensionadofront.exception.UserOrPwdInvalidRuntimeException;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

/**
 *
 * @author gabriel.rios
 */
@Priority(4000)                                            
public class ExceptionMapper implements ResponseExceptionMapper<RuntimeException> {  
    protected final Logger log = Logger.getLogger( getClass().getName() );
    
  @Override
  public RuntimeException toThrowable(Response response) {
    int status = response.getStatus();                    

    String msg = getBody(response); 

    RuntimeException re ;
    log.log(Level.INFO, ">>>>>>>ExceptionMapper status : {0}", status);
    switch (status) {
      case 500: re = new UserOrPwdInvalidRuntimeException(msg);         
      break;
      default:
        re = new WebApplicationException(msg,status);          
    }
    return re;
  }
  

    private String getBody(Response response) {
        ByteArrayInputStream is = (ByteArrayInputStream) response.getEntity();
        byte[] bytes = new byte[is.available()];
        is.read(bytes, 0, is.available());
        String body = new String(bytes);
        return body;
    }
}

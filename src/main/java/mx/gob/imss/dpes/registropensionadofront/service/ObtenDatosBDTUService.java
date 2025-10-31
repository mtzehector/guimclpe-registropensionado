/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.io.Serializable;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.BDTURequest;
import mx.gob.imss.dpes.registropensionadofront.model.BDTURequestIn;
import mx.gob.imss.dpes.registropensionadofront.model.BDTURequestOut;
import mx.gob.imss.dpes.registropensionadofront.restclient.BDTUPersonaClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.montesh
 */
@Provider
public class ObtenDatosBDTUService extends ServiceDefinition<ActivarRegistroRequest, ActivarRegistroRequest> {

    @Inject
    @RestClient
    BDTUPersonaClient bdtuPersonaClient;
    
    @Override
    public Message<ActivarRegistroRequest> execute(Message<ActivarRegistroRequest> request) throws BusinessException {
        BDTURequest bdtuRequest = new BDTURequest();
        BDTURequestIn in = new BDTURequestIn();
        
        in.setCurp(request.getPayload().getInfoToken().getCurp());
        
        log.log(Level.INFO, ">>>>>>>>>Antes de ejecutar el servicio bdtu");
        Response response = bdtuPersonaClient.load(in.getCurp(),request.getPayload().getInfoToken().getNss());   
        BDTURequestOut out = response.readEntity(BDTURequestOut.class);
        log.log(Level.INFO, ">>>>>>>>>Después de ejecutar el servicio bdtu= {0}: ", out);
        bdtuRequest.setBdtuOut(validarCadenas(out));
        request.getPayload().setBdtuRequest(bdtuRequest);
        
        return request;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private BDTURequestOut validarCadenas(BDTURequestOut in){
       
        String nuevoNomPersona = "";
        String nuevoApePatPersona = "";
        String nuevoApeMatPersona = "";

        if (in.getNombre().contains("#")) {
            nuevoNomPersona = in.getNombre().replace("#", "Ñ");
            in.setNombre(nuevoNomPersona);
        }
        if (in.getPrimerApellido().contains("#")) {
            nuevoApePatPersona = in.getPrimerApellido().replace("#", "Ñ");
            in.setPrimerApellido(nuevoApePatPersona);
        }
        if (in.getSegundoApellido().contains("#")) {
            nuevoApeMatPersona = in.getSegundoApellido().replace("#", "Ñ");
            in.setSegundoApellido(nuevoApeMatPersona);
        }
        
        return in;
    }
    
}

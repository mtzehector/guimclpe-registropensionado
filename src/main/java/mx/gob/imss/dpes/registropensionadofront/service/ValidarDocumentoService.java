package mx.gob.imss.dpes.registropensionadofront.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.OtrosDatosJson;
import mx.gob.imss.dpes.registropensionadofront.restclient.DocumentoBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author luisr.rodriguez
 */
@Provider
public class ValidarDocumentoService extends ServiceDefinition<ActivarRegistroRequest,ActivarRegistroRequest>{
    @Inject
    @RestClient
    private DocumentoBackClient client;

    @Override
    public Message<ActivarRegistroRequest> execute(Message<ActivarRegistroRequest> responseAux) throws BusinessException {
        responseAux.getPayload().getPersona().getId();
        
        // TODO: Actualizar cve persona en el registro de documento
        log.log(Level.INFO, "responseAux.getPayload().getInfoToken().getOtrosDatosJson() JGV: {0}", responseAux.getPayload().getInfoToken().getOtrosDatosJson());
        ObjectMapper objectMapper = new ObjectMapper();
        OtrosDatosJson datos;            
        try {
            datos = objectMapper.readValue(
                    responseAux.getPayload().getInfoToken().getOtrosDatosJson(),
                    OtrosDatosJson.class
            );
            
            //Eliminamos documentos anterior
            client.bajaByPersona(responseAux.getPayload().getPersona().getId());
            
            //Registramos el nuevo documento
            Documento doc = new Documento();
            doc.setId(datos.getDocumentIdentificacionOficialId());
            doc.setCvePersona( responseAux.getPayload().getPersona().getId());
            client.load(doc);
            
            log.log(Level.INFO, "otrosDatosJson JGV: {0}", datos);
        } catch (RuntimeException ex) {
            Logger.getLogger(ValidarDocumentoService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ValidarDocumentoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return responseAux;
    }

    
    
}

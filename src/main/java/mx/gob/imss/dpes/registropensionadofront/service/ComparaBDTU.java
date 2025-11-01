/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpOut;
import mx.gob.imss.dpes.registropensionadofront.exception.CurpInvalidStatusException;
import mx.gob.imss.dpes.registropensionadofront.exception.CurpNotBelongsToNSSException;
import mx.gob.imss.dpes.registropensionadofront.exception.RenapoBDTUInfoNotMacthingException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.registropensionadofront.model.BDTURequest;
import mx.gob.imss.dpes.registropensionadofront.model.BDTURequestIn;
import mx.gob.imss.dpes.registropensionadofront.model.BDTURequestOut;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.BDTUPersonaClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.montesh
 */
@Provider
public class ComparaBDTU extends ServiceDefinition<RegistroRequest, RegistroRequest> {

    @Inject
    @RestClient
    BDTUPersonaClient bdtuPersonaClient;

    final static String DATE_FORMAT = "dd/MM/yyyy";
    final static DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {
        BDTURequest bdtuRequest = new BDTURequest();
        BDTURequestIn in = new BDTURequestIn();

        in.setCurp(request.getPayload().getCurp());

        log.log(Level.INFO, ">>>>>>>>>  Antes de ejecutar el servicio bdtu " + request.getPayload().getCurp() + " - " + request.getPayload().getNss());
        Response response = null;
        try {
            response = bdtuPersonaClient.load(in.getCurp(), request.getPayload().getNss());
        } catch (RuntimeException e) {

            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());

            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                ExceptionUtils.throwServiceException("BDTU");
            }

            if (e.getMessage().contains("Unknown error, status code 400")) {
                throw new RenapoBDTUInfoNotMacthingException("msg355");
            }

            if (e.getMessage().contains("Unknown error, status code 500")) {
                throw new RenapoBDTUInfoNotMacthingException("msg356");
            }

            throw new UnknowException();
        }
        BDTURequestOut out = response.readEntity(BDTURequestOut.class);
        log.log(Level.INFO, ">>>>>>>>>  Después de ejecutar el servicio bdtu {0}", out);

        bdtuRequest.setBdtuOut(out);
        request.getPayload().setBdtuRequest(bdtuRequest);
        comparaDatos(request.getPayload());
        return request;
    }

    private void comparaDatos(RegistroRequest registroData) throws BusinessException {
        RenapoCurpOut renapo = registroData.getRenapoRequest().getRenapoCurpOut();
        BDTURequestOut bdtu = registroData.getBdtuRequest().getBdtuOut();

        if (!registroData.getNss().equals(bdtu.getNss())) {
            throw new CurpNotBelongsToNSSException();
        }

        String nomPersona = bdtu.getNombre();
        String apePatPersona = bdtu.getPrimerApellido();
        String apeMatPersona = bdtu.getSegundoApellido();
        String nuevoNomPersona = "";
        String nuevoApePatPersona = "";
        String nuevoApeMatPersona = "";

        if (nomPersona != null && nomPersona.contains("#")) {
            nuevoNomPersona = nomPersona.replace("#", "Ñ");
        }
        if (apePatPersona != null && apePatPersona.contains("#")) {
            nuevoApePatPersona = apePatPersona.replace("#", "Ñ");
        }
        if (apeMatPersona != null && apeMatPersona.contains("#")) {
            nuevoApeMatPersona = apeMatPersona.replace("#", "Ñ");
        }

        if (!nuevoNomPersona.equals("")) {
            if (!renapo.getNombres().equals(nuevoNomPersona)) {
                log.log(Level.SEVERE, ">>>Diferencia en nombre");
                throw new RenapoBDTUInfoNotMacthingException();
            }
        } else {
            if (!renapo.getNombres().equals(bdtu.getNombre() == null ? "" : bdtu.getNombre())) {
                log.log(Level.SEVERE, ">>>>>>>>>  RenapoBDTUInfoNotMacthingException getNombres");
                throw new RenapoBDTUInfoNotMacthingException();
            }
        }

        if (!nuevoApePatPersona.equals("")) {
            if (!renapo.getApellido1().equals(nuevoApePatPersona)) {
                log.log(Level.SEVERE, ">>>Diferencia en apellido paterno");
                throw new RenapoBDTUInfoNotMacthingException();
            }
        } else {
            if (!renapo.getApellido1().equals(bdtu.getPrimerApellido() == null ? "" : bdtu.getPrimerApellido())) {
                log.log(Level.SEVERE, ">>>>>>>>>  RenapoBDTUInfoNotMacthingException getPrimerApellido");
                throw new RenapoBDTUInfoNotMacthingException();
            }
        }

        if (!nuevoApeMatPersona.equals("")) {
            if (!renapo.getApellido2().equals(nuevoApeMatPersona)) {
                log.log(Level.SEVERE, ">>>Diferencia en apellido materno");
                throw new RenapoBDTUInfoNotMacthingException();
            }
        } else {
            if (!renapo.getApellido2().equals(bdtu.getSegundoApellido() == null ? "":bdtu.getSegundoApellido())) {
                log.log(Level.SEVERE, ">>>>>>>>>  RenapoBDTUInfoNotMacthingException getSegundoApellido");
                throw new RenapoBDTUInfoNotMacthingException();
            }
        }

        Date renapoFecNac = null;
        Date bdtuFecNac = null;
        try {
            renapoFecNac = format.parse(renapo.getFechNac());
            bdtuFecNac = format.parse(bdtu.getFechaNacimiento());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (renapoFecNac == null || bdtuFecNac == null || renapoFecNac.compareTo(bdtuFecNac) != 0) {
            log.log(Level.SEVERE, ">>>>>>>>>  RenapoBDTUInfoNotMacthingException renapo.getFechNac()={0}", renapo.getFechNac());
            log.log(Level.SEVERE, ">>>>>>>>>  RenapoBDTUInfoNotMacthingException bdtu.getFechaNacimiento()={0}", bdtu.getFechaNacimiento());
            log.log(Level.SEVERE, ">>>>>>>>>  RenapoBDTUInfoNotMacthingException getFechaNacimiento");
            throw new RenapoBDTUInfoNotMacthingException();
        }

        if (renapo.getEstatusCURP() != null) {
            /*
                        Activas
            Estatus	Definición
            AN	Alta Normal
            AH	Alta con Homonimia
            CRA	CURP ReActivada
            RCN	Registro de Cambio No afectando a CURP
            RCC	Registro de Cambio Afectando a CURP

                    Desactivadas
            Estatus	Definición
            BAP	Baja por Documento Apócrifo
            BD	Baja por Defunción
            BDA	Baja por Duplicidad
            BCC	Baja por Cambio en CURP
            BCN	Baja no afectando a CURP
             */

            if (!(renapo.getEstatusCURP().equals("AN")
                    || renapo.getEstatusCURP().equals("AH")
                    || renapo.getEstatusCURP().equals("CRA")
                    || renapo.getEstatusCURP().equals("RCN")
                    || renapo.getEstatusCURP().equals("RCC"))) {

                List parameters = new LinkedList();
                parameters.add(renapo.getEstatusCURP());
                throw new CurpInvalidStatusException(parameters);
            }
        }
    }

}

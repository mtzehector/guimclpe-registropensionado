/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpIn;
import mx.gob.imss.dpes.interfaces.sistrap.model.Pensionado;
import mx.gob.imss.dpes.registropensionadofront.exception.SistrapCurpException;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.SistrapOsbClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class SistrapOsbService extends ServiceDefinition<RegistroRequest, RegistroRequest> {

    @Inject
    @RestClient
    SistrapOsbClient client;

    Pensionado titular;

    final static String DATE_FORMAT = "dd/MM/yyyy";
    final static DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {

        log.log(Level.INFO, "Ejecutando Servicio SistrapOsbService: {0} ", request.getPayload());

        RenapoCurpIn curp = new RenapoCurpIn(request.getPayload().getCurp());
        log.log(Level.INFO, "SistrapOsbService(curp): {0} ", curp);
        Response loadResponse = client.load(curp.getCurp(), request.getPayload().getNss());
        log.log(Level.INFO, "Response Servicio SistrapOsbService: {0} ", loadResponse);

        titular = loadResponse.readEntity(Pensionado.class);

        log.log(Level.INFO, "ConsultaPensionesResponse titular: {0} ", titular);
        log.log(Level.INFO, "titular.getCodigoError(): {0} ", titular.getCodigoError());

        if (titular.getCodigoError().equals("401")) {
            log.log(Level.INFO, " Cayo en 401 ");
            throw new SistrapCurpException("msg359");
        }
        if (titular.getCodigoError().equals("402")) {
            log.log(Level.INFO, " Cayo en 402 ");
            throw new SistrapCurpException("msg360");
        }
        if (titular.getCodigoError().equals("407")) {
            log.log(Level.INFO, " Cayo en 407 ");
            throw new SistrapCurpException("msg363");
        }
        log.log(Level.INFO, "getCodigoError: ", titular.getCodigoError());

        if (titular.getCodigoError().equals("200") || titular.getIdNss() == null) {

            String nssRequest = request.getPayload().getNss();
            String nssSistrap = titular.getIdNss();

            if (!nssRequest.equals(nssSistrap)) {
                throw new SistrapCurpException("msg355");
            }

        } else {
            throw new SistrapCurpException("msg348");
        }
        log.log(Level.INFO, "Pasa validaciones 1 SISTRAP exitosamente.");

        if (!isValidPersona(request)) {
            log.log(Level.INFO, "NO PASA validaciones 2 SISTRAP.");
            throw new SistrapCurpException("msg361");
        }
        log.log(Level.INFO, "Pasa validaciones 2 SISTRAP exitosamente.");
        return request;
    }

    private Boolean isValidPersona(Message<RegistroRequest> request) {

        log.log(Level.INFO, "IS VALIDDDD -> isValidPersona :");
        log.log(Level.INFO, "Titular: {0} ", titular);
        log.log(Level.INFO, "Renapo: {0} ", request.getPayload().getRenapoRequest().getRenapoCurpOut());

        /*if(titular.getNomNombre().contains("?")){
            String nuevoNombre = titular.getNomNombre().replace("?", "Ñ");
            titular.setNomNombre(nuevoNombre);
        }*/
        titular.setNomNombre(remplazaCaracteres(titular.getNomNombre()));
        log.log(Level.INFO, "Titular Nombre: {0} ", titular.getNomNombre());
        log.log(Level.INFO, "Renapo Nombre: {0} ", request.getPayload().getRenapoRequest().getRenapoCurpOut().getNombres());
        if (request.getPayload().getRenapoRequest().getRenapoCurpOut().getNombres().length() > 40){
            if (!(request.getPayload().getRenapoRequest().getRenapoCurpOut().getNombres().substring(0, 40).equals(
                titular.getNomNombre()))) {
            return false;
            }
        }else{
            if (!(request.getPayload().getRenapoRequest().getRenapoCurpOut().getNombres().equals(
                titular.getNomNombre()))) {
            return false;
            }
        }
        titular.setNomApellidoPaterno(remplazaCaracteres(titular.getNomApellidoPaterno()));
        log.log(Level.INFO, "Titular ap paterno: {0} ", titular.getNomApellidoPaterno());
        log.log(Level.INFO, "Renapo ap paterno: {0} ", request.getPayload().getRenapoRequest().getRenapoCurpOut().getApellido1());
        if (!(request.getPayload().getRenapoRequest().getRenapoCurpOut().getApellido1().equals(
                titular.getNomApellidoPaterno()))) {
            return false;
        }
        titular.setNomApellidoMaterno(remplazaCaracteres(titular.getNomApellidoMaterno()));
        log.log(Level.INFO, "Titular ap materno: {0} ", titular.getNomApellidoMaterno());
        log.log(Level.INFO, "Renapo ap materno: {0} ", request.getPayload().getRenapoRequest().getRenapoCurpOut().getApellido2());
        if (request.getPayload().getRenapoRequest().getRenapoCurpOut().getApellido2() != null && !request.getPayload().getRenapoRequest().getRenapoCurpOut().getApellido2().equals("")) {
            if (!(request.getPayload().getRenapoRequest().getRenapoCurpOut().getApellido2().equals(
                    titular.getNomApellidoMaterno()))) {
                return false;
            }
        } else {
            if (titular.getNomApellidoMaterno() != null) {
                return false;
            }
        }

        String sexo = "HOMBRE";

        if (titular.getSexo().equals("2")) {
            sexo = "MUJER";
        }

        log.log(Level.INFO, "Titular Sexo: {0} ", titular.getSexo());
        log.log(Level.INFO, "Renapo Sexo: {0} ", request.getPayload().getRenapoRequest().getRenapoCurpOut().getSexo());
        log.log(Level.INFO, "Sexo: {0} ", sexo);

        if (!(request.getPayload().getRenapoRequest().getRenapoCurpOut().getSexo().equals(
                sexo
        ))) {
            return false;
        }

        //log.log(Level.INFO, "Titular Fecha Nacimiento: {0} ", titular.getFecNacimiento());     
        //log.log(Level.INFO, "Renapo Fecha Nacimiento: {0} ", request.getPayload().getRenapoRequest().getRenapoCurpOut().getFechNac());
        //Comparacion FECHA NACIMIENTO
        String fechaNacRenapo = null;
        if (request.getPayload().getRenapoRequest().getRenapoCurpOut().getFechNac() != null && !request.getPayload().getRenapoRequest().getRenapoCurpOut().getFechNac().equals("")) {
            fechaNacRenapo = request.getPayload().getRenapoRequest().getRenapoCurpOut().getFechNac().substring(6, 10) + "-"
                    + request.getPayload().getRenapoRequest().getRenapoCurpOut().getFechNac().substring(3, 5) + "-"
                    + request.getPayload().getRenapoRequest().getRenapoCurpOut().getFechNac().substring(0, 2);
            log.log(Level.SEVERE, ">>> CompararRenapoVsSistrap fecha de nacimiento fechaNacRenapo {0}", fechaNacRenapo);
        } else {
            log.log(Level.SEVERE, ">>> CompararRenapoVsSistrap fecha de nacimiento renapo es null o vacia");
            return false;
        }

        if ((titular.getFecNacimiento() != null && !titular.getFecNacimiento().equals(""))) {
            if (!fechaNacRenapo.equals(titular.getFecNacimiento())) {
                log.log(Level.SEVERE, ">>> CompararRenapoVsSistrap fecha de nacimiento no son iguales");
                return false;
            }
        }else{
            if(fechaNacRenapo == null){
               log.log(Level.SEVERE, ">>> CompararRenapoVsSistrap fecha de nacimiento sistrap vacia");
               return false;
            }
            
            
        }

        return true;
    }
    
    private String remplazaCaracteres(String nombre){
        if(nombre!= null){
            if (nombre.contains("?")){
                nombre = nombre.replace("?", "Ñ");
            }
            if (nombre.contains("#")){
                nombre = nombre.replace("#", "Ñ");
            }
        }
        return nombre;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.assembler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroPersona;

/**
 *
 * @author eduardo.montesh
 */
public class RegistroPersonaAssembler extends BaseAssembler<ActivarRegistroRequest, RegistroPersona> {

    @Override
    public RegistroPersona assemble(ActivarRegistroRequest source) {

        RegistroPersona persona = new RegistroPersona();

        if (source.getBdtuRequest() != null) {
            try {
                Date fn = new SimpleDateFormat("dd/MM/yyyy").parse(source.getBdtuRequest().getBdtuOut().getFechaNacimiento());
                persona.setFecNacimiento(fn);
            } catch (ParseException ex) {
                Logger.getLogger(RegistroPersonaAssembler.class.getName()).log(Level.SEVERE, null, ex);
            }
            persona.setPrimerApellido(source.getBdtuRequest().getBdtuOut().getPrimerApellido());
            persona.setSegundoApellido(source.getBdtuRequest().getBdtuOut().getSegundoApellido());
            persona.setNombre(source.getBdtuRequest().getBdtuOut().getNombre());
        } else {
            try {
                Date fn = new SimpleDateFormat("dd/MM/yyyy").parse(source.getRenapoRequest().getRenapoCurpOut().getFechNac());
                persona.setFecNacimiento(fn);
            } catch (ParseException ex) {
                Logger.getLogger(RegistroPersonaAssembler.class.getName()).log(Level.SEVERE, null, ex);
            }
            persona.setPrimerApellido(source.getRenapoRequest().getRenapoCurpOut().getApellido1());
            persona.setSegundoApellido(source.getRenapoRequest().getRenapoCurpOut().getApellido2());
            persona.setNombre(source.getRenapoRequest().getRenapoCurpOut().getNombres());
        }
        persona.setPassword(source.getPassword());
        persona.setInfoToken(source.getInfoToken());

        if (source.getRenapoRequest().getRenapoCurpOut().getSexo().equals("HOMBRE")) {
            persona.setCveSexo(1);
        } else {
            persona.setCveSexo(2);
        }

        log.log(Level.INFO, ">>>>valor de persona : {0}", persona);
        return persona;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

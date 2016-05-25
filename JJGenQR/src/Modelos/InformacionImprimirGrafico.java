package Modelos;

import java.util.LinkedList;

public class InformacionImprimirGrafico {
    String accion;
    
    public LinkedList getInformacion(){
	LinkedList info = new LinkedList();
	ModeloImprimirGraficos consulta;
        accion = ItemSeleccionado.accionBoton;
        consulta = new ModeloImprimirGraficos();
        //agregamos la informacion a el objeto consulta
        consulta.setContador(ItemSeleccionado.rol);
        info.add(consulta);
        return info;
    }
}

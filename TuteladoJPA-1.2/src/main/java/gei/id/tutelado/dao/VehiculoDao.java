package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Vehiculo;

public interface VehiculoDao {
    	
	void setup (Configuracion config);
	
	Vehiculo recuperaPorMatricula (String matricula);
	Vehiculo almacena (Vehiculo vehiculo);
	void elimina (Vehiculo vehiculo);
	Vehiculo modifica (Vehiculo vehiculo);
}

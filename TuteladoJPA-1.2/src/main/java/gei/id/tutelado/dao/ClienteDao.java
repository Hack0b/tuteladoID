package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;

public interface ClienteDao {
    	
	void setup (Configuracion config);
	
	Cliente recuperaPorDni (String dni);
	Cliente almacena (Cliente client);
	void elimina (Cliente client);
	Cliente modifica (Cliente client);

	Cliente restauraVentas (Cliente client);
	List<Cliente> recuperaSinVentas ();
	List<Cliente> recuperaPrecioVehiculos (float precio);
	List<Cliente> recuperaNumVehiculos (int numVehiculos);
}

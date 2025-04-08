package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;
import gei.id.tutelado.model.Venta;

public interface VentaDao {
    
	void setup (Configuracion config);
	
	// OPERACIONS CRUD BASICAS
	/* MO4.1 */ Venta recuperaPorCodigo (String codigo);
	/* MO4.2 */ Venta almacena (Venta venta);
	/* MO4.3 */ void elimina (Venta venta);
	/* MO4.4 */ Venta modifica (Venta venta);

	// CONSULTAS JPQL
	/* MO4.6.a */ List<Venta> recuperaTodasCliente(Cliente client);

}

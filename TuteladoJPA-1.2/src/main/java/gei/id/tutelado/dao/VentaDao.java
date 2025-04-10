package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;
import gei.id.tutelado.model.Venta;

public interface VentaDao {
    
	void setup (Configuracion config);
	
	Venta recuperaPorCodigo (String codigo);
	Venta almacena (Venta venta);
	void elimina (Venta venta);
	Venta modifica (Venta venta);

	List<Venta> recuperaTodasCliente(Cliente client);

}

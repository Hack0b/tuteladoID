package gei.id.tutelado.dao;

import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;

public interface ClienteDao {
    	
	void setup (Configuracion config);
	
    /* MO4.1 */ 
	Cliente recuperaPorDni (String dni);
	/* MO4.2 */ 
    Cliente almacena (Cliente user);
	/* MO4.3 */ 
    void elimina (Cliente user);
	/* MO4.4 */ 
    Cliente modifica (Cliente user);

	// OPERACIONS POR ATRIBUTOS LAZY
	/* MO4.5 */ Cliente restauraEntradasLog (Cliente user);
			// Recibe un usuario coa coleccion de entradas de log como proxy SEN INICIALIZAR
			// Devolve unha copia do usuario coa coleccion de entradas de log INICIALIZADA

	// CONSULTAS JPQL
			//* Aqui irian o resto das consultas que se piden no enunciado (non implementadas)

}

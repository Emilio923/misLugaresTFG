package net.iescierva.mim.mislugares2019.datos;

import net.iescierva.mim.mislugares2019.modelo.Lugar;

public interface RepositorioLugares {
    Lugar elemento(int id); //Devuelve el elemento dado su id

    void add(Lugar lugar); //Añade el elemento indicado

    int add_blank(); //Añade un elemento en blanco y devuelve su id

    void delete(int id); //Elimina el elemento con el id indicado

    int size(); //Devuelve el número de elementos

    void update(int id, Lugar lugar); //Reemplaza un elemento
}

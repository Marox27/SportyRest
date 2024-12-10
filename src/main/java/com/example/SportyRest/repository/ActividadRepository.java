package com.example.SportyRest.repository;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {

    // buscar un usuario por su nombre de usuario:
    Actividad findById(int id);

    void deleteById(int id);

    // Devuelve las actvidades dado el id del
    List<Actividad> findByCreador(int creador);


    @Query("SELECT a FROM Actividad a WHERE " + "earth_distance(ll_to_earth(:latitud, :longitud)," +
            " ll_to_earth(a.latitud, a.longitud)) <= :distancia")
    List<Actividad> findActividadesCercanas(@Param("latitud") double latitud,
                                            @Param("longitud") double longitud,
                                            @Param("distancia") double distancia);


}
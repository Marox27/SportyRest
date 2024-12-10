package com.example.SportyRest.repository;

import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // buscar un usuario por su nombre de usuario:
    Usuario findByNickname(String nickname);

    Usuario findById(int id);

    Usuario findByMail(String mail);


}

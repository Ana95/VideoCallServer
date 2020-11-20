package ftn.it.videoserver.vonageapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ftn.it.videoserver.vonageapi.model.TokenDTO;

public interface TokenRepository extends JpaRepository<TokenDTO, String>{

}

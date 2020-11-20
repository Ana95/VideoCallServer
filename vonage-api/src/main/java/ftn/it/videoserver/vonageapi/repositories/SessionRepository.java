package ftn.it.videoserver.vonageapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ftn.it.videoserver.vonageapi.model.SessionDTO;


public interface SessionRepository extends JpaRepository<SessionDTO, String>{

}

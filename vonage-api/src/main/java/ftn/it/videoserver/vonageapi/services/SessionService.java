package ftn.it.videoserver.vonageapi.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.it.videoserver.vonageapi.model.SessionDTO;
import ftn.it.videoserver.vonageapi.repositories.SessionRepository;

@Service
public class SessionService{
	
	@Autowired
	private SessionRepository sessionRepository;
	
	public SessionDTO save(SessionDTO sessionDTO){
        return sessionRepository.save(sessionDTO);
    }

    public Optional<SessionDTO> findById(String sessionId){
        return sessionRepository.findById(sessionId);
    }

    public Collection<SessionDTO> findAll(){
        return sessionRepository.findAll();
    }

    public void deleteById(String sessionId){
        sessionRepository.deleteById(sessionId);
    }


}

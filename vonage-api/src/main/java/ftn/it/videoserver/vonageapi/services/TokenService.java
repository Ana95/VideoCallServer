package ftn.it.videoserver.vonageapi.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.it.videoserver.vonageapi.model.TokenDTO;

@Service
public class TokenService {
	
	@Autowired
	private TokenService tokenRepository;
	
	public TokenDTO save(TokenDTO tokenDTO){
        return tokenRepository.save(tokenDTO);
    }

    public Optional<TokenDTO> findById(String tokenId){
        return tokenRepository.findById(tokenId);
    }

    public Collection<TokenDTO> findAll(){
        return tokenRepository.findAll();
    }

    public void deleteById(String tokenId){
        tokenRepository.deleteById(tokenId);
    }

}

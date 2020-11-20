package ftn.it.videoserver.vonageapi.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.opentok.MediaMode;
import com.opentok.OpenTok;
import com.opentok.Session;
import com.opentok.SessionProperties;
import com.opentok.exception.OpenTokException;

import ftn.it.videoserver.vonageapi.model.CreateTokenThread;
import ftn.it.videoserver.vonageapi.model.SessionDTO;
import ftn.it.videoserver.vonageapi.model.TokenDTO;
import ftn.it.videoserver.vonageapi.services.SessionService;
import ftn.it.videoserver.vonageapi.services.TokenService;

@RestController
@RequestMapping( value = "/sessions" )
public class SessionController {
	
	private int API_KEY = 46977624;
	private String API_SECRET = "fed836d9db34fad3ba9a3c507cccee5960271df7";
	private OpenTok sdk = new OpenTok(API_KEY, API_SECRET);
	private Integer numOfTokens = 0;
	private boolean sessionCreated = false;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<JSONObject> createSession() throws OpenTokException, InterruptedException{
		numOfTokens += 1;
		JSONObject response = null;
		if(saveSessionId()){
			ArrayList<SessionDTO> sessions = getSortedListOfSessions();
			SessionDTO sessionDTO = sessions.get(sessions.size() - 1);
			System.out.println("sessionDTO: " + sessionDTO.getSessionId());
			
			/*String tokenId = sdk.generateToken(sessionDTO.getSessionId());
			System.out.println("TOKEN: " + tokenId);
				
			TokenDTO tokenDTO = new TokenDTO();
			tokenDTO.setTokenId(tokenId);
			sessionDTO.getTokens().add(tokenDTO);
			System.out.println(sessionDTO.getTokens());
			sessionService.save(sessionDTO);*/
			String value = "";
			System.out.println("num of tokens: " + numOfTokens);
			for(int i = 0; i<numOfTokens; i++){
				CreateTokenThread createToken = new CreateTokenThread(sessionDTO);
				Thread thread = new Thread(createToken);
				thread.start();
				thread.join();
				value = createToken.getValue();
				TokenDTO tokenDTO = new TokenDTO();
				tokenDTO.setTokenId(value);
				sessionDTO.getTokens().add(tokenDTO);
				System.out.println(sessionDTO.getTokens());
				sessionService.save(sessionDTO);
			}
			
			response = new JSONObject();
			response.put("apiKey", API_KEY);
			response.put("sessionId", sessionDTO.getSessionId());
			response.put("token", value);
		}
		
		return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
	}
	
	@RequestMapping(
		value = "/{sessionId}",
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<JSONObject> setSessionExpire(@PathVariable String sessionId){
		System.out.println("Set session expire");
		JSONObject response = new JSONObject();
		SessionDTO sessionDTO = sessionService.findById(sessionId).get();
		System.out.println("Session: " + sessionDTO);
		if(sessionDTO != null){
			sessionDTO.setExpire(true);
			sessionService.save(sessionDTO);
			response.put("sessionId", sessionId);
			response.put("updated", "successfully");
			response.put("expire", String.valueOf(sessionDTO.getExpire()));	
		}
		return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
	}
		
	private synchronized boolean saveSessionId() throws OpenTokException{
		if(!isSessionCreated()){
			System.out.println("Kreiranje sesije");
			SessionProperties sessionProperties = new SessionProperties.Builder().mediaMode(MediaMode.ROUTED).build();
			Session session = sdk.createSession(sessionProperties);
			String sessionId = session.getSessionId();
			SessionDTO sessionDTO = new SessionDTO();
			sessionDTO.setSessionId(sessionId);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			sessionDTO.setDateOfCreation(date);
			Date expiryDate = DateUtils.addHours(sessionDTO.getDateOfCreation(), 24);
			sessionDTO.setExpire(false);
			sessionService.save(sessionDTO);
			System.out.println("Sesija sacuvana");
			sessionCreated = true;
		}
		return sessionCreated;
	}
	
	private JSONObject createToken(SessionDTO sessionDTO) throws OpenTokException{
		String tokenId = sdk.generateToken(sessionDTO.getSessionId());
		System.out.println("TOKEN: " + tokenId);
			
		TokenDTO tokenDTO = new TokenDTO();
		tokenDTO.setTokenId(tokenId);
		sessionDTO.getTokens().add(tokenDTO);
		System.out.println(sessionDTO.getTokens());
		sessionService.save(sessionDTO);
		
		JSONObject response = new JSONObject();
		response.put("apiKey", API_KEY);
		response.put("sessionId", sessionDTO.getSessionId());
		response.put("token", tokenId);
		return response;
	}
	
	private Boolean isSessionCreated(){
		ArrayList<SessionDTO> sessions = getSortedListOfSessions();
		System.out.println("Size: " + sessions.size());
		if(!sessions.isEmpty()){
			SessionDTO sessionDTO = sessions.get(sessions.size() - 1);
			if(sessionDTO.getExpire()){
				System.out.println("Isticuci datum prije trenutnog");
				return false;
			}else{
				System.out.println("Isticuci datum posle trenutnog");
				return true;
			}
		}else{
			return false;
		}
	}
	
	private ArrayList<SessionDTO> getSortedListOfSessions(){
		ArrayList<SessionDTO> sessions = (ArrayList<SessionDTO>) sessionService.findAll();
		Collections.sort(sessions, new Comparator<SessionDTO>(){
			public int compare (SessionDTO s1, SessionDTO s2){
				return s1.getDateOfCreation().compareTo(s2.getDateOfCreation());
			}
		});
		return sessions;
	}
	
}
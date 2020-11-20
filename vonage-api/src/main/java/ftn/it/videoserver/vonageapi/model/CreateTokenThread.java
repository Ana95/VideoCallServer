package ftn.it.videoserver.vonageapi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;

import com.opentok.OpenTok;
import com.opentok.exception.OpenTokException;

import ftn.it.videoserver.vonageapi.services.SessionService;

public class CreateTokenThread implements Runnable{
	
	private volatile String tokenId;
	
	private int API_KEY = 46977624;
	
	private String API_SECRET = "fed836d9db34fad3ba9a3c507cccee5960271df7";
	
	private OpenTok sdk = new OpenTok(API_KEY, API_SECRET);
	
	private SessionDTO session;
	
	public CreateTokenThread(SessionDTO session) {
		this.session = session;
	}

	@Override
	public void run() {
		System.out.println("sessionDTO: " + session.getSessionId());
		
		try {
			tokenId = sdk.generateToken(session.getSessionId());
			System.out.println("TOKEN: " + tokenId);
		} catch (OpenTokException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getValue() {
        return tokenId;
    }

}

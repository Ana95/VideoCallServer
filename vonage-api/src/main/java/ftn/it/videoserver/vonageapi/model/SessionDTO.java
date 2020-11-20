package ftn.it.videoserver.vonageapi.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
public class SessionDTO {
	
	@Id
	private String sessionId;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<TokenDTO> tokens;
	
	private Date dateOfCreation;
	
	private Boolean expire;
	
	public SessionDTO() {
        this.tokens = new HashSet<>();
    }

}

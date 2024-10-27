// DATABASE TABLE ERROR

package com.example;

import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.*;
import jakarta.transaction.*;
import jakarta.inject.Named;
import java.io.Serializable;
import org.apache.logging.log4j.*;



@SessionScoped
@Entity
@Table(name = "error")
@Transactional
public class Error implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String wrongWord;
    private String correctWord;
    
    @PersistenceContext(unitName = "exercise2")
    @Transient private transient EntityManager em;

    @Transient private static final Logger logger = LogManager.getLogger (Error.class );

    public String add ( ) {
        try {
            em.merge ( new Error(id, wrongWord, correctWord));
        } catch (Exception e) {
            logger.error ("Insert of words failed: %s", e.getMessage() );
        }
        return "";
    }

    public Error() {
        this.correctWord = "";
        this.wrongWord = "";   
    }

    public Error(Long id, String wrongWord, String correctWord) {
        this.id = id;
        this.correctWord = correctWord;
        this.wrongWord = wrongWord;   
    }

    public void init() {
        this.id = null;
        this.correctWord = "";
        this.wrongWord = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public String getWrongWord() {
        return wrongWord;
    }

    public void setWrongWord(String wrongWord) {
        this.wrongWord = wrongWord;
    }
  
}
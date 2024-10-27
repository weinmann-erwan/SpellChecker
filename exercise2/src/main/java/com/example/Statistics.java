// DATABASE TABLE STATISTICS

package com.example;

import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.*;
import jakarta.transaction.*;
import jakarta.inject.Named;
import java.io.Serializable;
import org.apache.logging.log4j.*;


@Named("statistics")
@SessionScoped
@Entity(name = "statistics")
@Transactional
public class Statistics implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String wrongWord;
    private int count;
    
    @PersistenceContext(unitName = "exercise2")
    @Transient private transient EntityManager em;

    @Transient private static final Logger logger = LogManager.getLogger (Error.class );

    public String add() {
        try {
            em.merge(new Statistics(id, wrongWord, count));
        } catch (Exception e) {
            logger.error ("Insert of  wrongs words failed: %s", e.getMessage() );
        }
        return "";
    }

    public Statistics() {
        this.wrongWord = "";
        this.count = 0;
    }

    public Statistics(Long id, String wrongWord, int count) {
        this.id = id;
        this.wrongWord = wrongWord;
    }

    public void init() {
        this.id = null;
        this.wrongWord = "";
        this.count = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWrongWord() {
        return wrongWord;
    }

    public void setWrongWord(String wrongWord) {
        this.wrongWord = wrongWord;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    
}


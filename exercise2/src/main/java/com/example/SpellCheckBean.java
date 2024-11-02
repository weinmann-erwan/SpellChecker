package com.example;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List; 
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Named("check")
@SessionScoped
public class SpellCheckBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String inputText; // Text input from the user
    private List<Error> errors; // List to hold spelling errors

    @PersistenceContext(unitName = "exercise2") // Ensure this matches your persistence.xml
    private EntityManager entityManager;

    @EJB
    private SpellCheckService spellCheckService; // Injecting the EJB

    public String checkSpellingAndNavigate() {
        checkSpelling();
        return "output?faces-redirect=true";  // Add redirect to avoid view state issues
    }

    public void checkSpelling() {
        if (inputText != null && !inputText.trim().isEmpty()) {
            System.out.println("Input Text: " + inputText);
            errors = spellCheckService.checkSpelling(inputText); // Call EJB to check spelling
            
            for (Error error : errors) {
                System.out.println("Wrong Word: " + error.getWrongWord() + ", Suggested Correction: " + error.getCorrectWord());
                spellCheckService.recordError(error.getWrongWord(), error.getCorrectWord());
                
            }
        } else {
            errors = List.of(); // Reset errors to an empty list if input is empty
            System.out.println("ERROR: Input text is empty.");
        }
    }

    public void checkSpellingAjax() {
        if (inputText != null && !inputText.trim().isEmpty()) {
            errors = spellCheckService.checkSpelling(inputText);
            
          
            FacesContext context = FacesContext.getCurrentInstance();
            Iterator<FacesMessage> messages = context.getMessages();
            while (messages.hasNext()) {
                messages.next();
                messages.remove();
            }
            
           
            for (Error error : errors) {
                FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Incorrect: " + error.getWrongWord(),
                    "Correction suggérée: " + error.getCorrectWord()
                );
                context.addMessage(null, message);
            }
        }
    }

    
    public void clearStatistics() {
        spellCheckService.clearStatistics();
    }
    
    public List<Statistics> getStatistics() {
        return entityManager.createQuery("SELECT s FROM Statistics s ORDER BY s.count DESC", Statistics.class)
                            .getResultList();
    }

    // Getters and Setters
    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public List<Error> getErrors() {
        return errors;
    }

}
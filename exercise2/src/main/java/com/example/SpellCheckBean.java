package com.example;

import java.io.Serializable;
import java.util.List; // Use java.util.List instead of java.lang.reflect.Array

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("check")
@SessionScoped
public class SpellCheckBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String inputText; // Text input from the user
    private List<Error> errors; // List to hold spelling errors

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
            }
        } else {
            errors = List.of(); // Reset errors to an empty list if input is empty
            System.out.println("ERROR: Input text is empty.");
        }
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

// EJB, IMPLEMENT THE LOGIC FOR SPELL CHECKING USING LANGUAGETOOL AND MANAGE DATABASE INTERACTION FOR RECORDING ERRORS AND STATISTICS

package com.example;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;

@Stateless
public class SpellCheckService {

    @PersistenceContext(unitName = "exercise2") // Ensure this matches your persistence.xml
    private EntityManager entityManager;


    // Method to check spelling using LanguageTool
    public List<Error> checkSpelling(String text) {
        System.out.println("EJB");
        List<Error> errors = new ArrayList<>();
        try {
            
            JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
            List<RuleMatch> matches = langTool.check(text);

            for (RuleMatch match : matches) {
                String wrongWord = text.substring(match.getFromPos(), match.getToPos());
                String correctWord = match.getSuggestedReplacements().isEmpty() ? "" : match.getSuggestedReplacements().get(0);
                errors.add(new Error(null, wrongWord, correctWord));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return errors;
    }
    // Method to record an error and update statistics
    public void recordError(String wrongWord, String correctWord) {
        // Save the error
        Error error = new Error();
        error.setWrongWord(wrongWord);
        error.setCorrectWord(correctWord);
        entityManager.persist(error);

        // Update statistics
        Statistics stats = findStatisticsByWrongWord(wrongWord);

        if (stats != null) {
            stats.setCount(stats.getCount() + 1);
            entityManager.merge(stats);
        } else {
            Statistics newStats = new Statistics();
            newStats.setWrongWord(wrongWord);
            newStats.setCount(1);
            entityManager.persist(newStats);
        }
    }

    // Helper method to find statistics by wrong word
    private Statistics findStatisticsByWrongWord(String wrongWord) {
        TypedQuery<Statistics> query = entityManager.createQuery(
            "SELECT s FROM Statistics s WHERE s.wrongWord = :word", Statistics.class);
        query.setParameter("word", wrongWord);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Method to retrieve statistics (optional)
    public List<Statistics> getStatistics() {
        return entityManager.createQuery("SELECT s FROM Statistics s ORDER BY s.count DESC", Statistics.class)
                            .getResultList();
    }
}


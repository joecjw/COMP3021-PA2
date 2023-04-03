package hk.ust.comp3021.actions;

import hk.ust.comp3021.action.SearchResearcherAction;
import hk.ust.comp3021.action.SearchResearcherAction.SearchResearcherKind;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.resource.Paper;
// import hk.ust.comp3021.utils.TestKind;
import hk.ust.comp3021.MiniMendeleyEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SearchResearcherActionByLambdaTest {
    // @Tag(TestKind.PUBLIC)
    @Test
    void testSearchResearcherAction_ActionsSize() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        int originalSize = engine.getActions().size();

        SearchResearcherAction action = new SearchResearcherAction("Action_1", user, new Date(), "3", "10", SearchResearcherKind.PAPER_WITHIN_YEAR);
        engine.processSearchResearcherActionByLambda(user, action);

        int currentSize = engine.getActions().size();
        assertEquals(currentSize, originalSize + 1);
    }

    // @Tag(TestKind.PUBLIC)
    @Test
    void testSearchResearcherAction_SearchByPaperWithinYear() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SearchResearcherAction action1 = new SearchResearcherAction("Action_1", user, new Date(), "3", "10", SearchResearcherKind.PAPER_WITHIN_YEAR);
        HashMap<String, List<Paper>> searchResult1 = engine.processSearchResearcherActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 11);
        assertEquals(searchResult1.get("Xiaohong Li").get(0).getTitle(), "Proteus: Computing disjunctive loop summary via path dependency analysis");
        assertEquals(searchResult1.get("Alvin Cheung").size(), 5);
        assertEquals(searchResult1.get("Yang Liu").get(2).getTitle(), "Automatic Loop Summarization via Path Dependency Analysis");
        
        SearchResearcherAction action2 = new SearchResearcherAction("Action_2", user, new Date(), "4", "8", SearchResearcherKind.PAPER_WITHIN_YEAR);
        HashMap<String, List<Paper>> searchResult2 = engine.processSearchResearcherActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 5);
        assertEquals(searchResult2.get("Alvin Cheung").size(), 5);
        assertEquals(searchResult2.get("Alvin Cheung").get(4).getTitle(), "Synthesizing highly expressive SQL queries from input-output examples");
    }

    // @Tag(TestKind.PUBLIC)
    @Test
    void testSearchResearcherAction_SearchByJournalPublishTimes() {
     	MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SearchResearcherAction action1 = new SearchResearcherAction("Action_1", user, new Date(), "ACM SIGPLAN Notices", "200", SearchResearcherKind.JOURNAL_PUBLISH_TIMES);
        HashMap<String, List<Paper>> searchResult1 = engine.processSearchResearcherActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 28);
        assertEquals(searchResult1.get("Guilherme Ottoni").get(0).getTitle(), "The HipHop compiler for PHP");
        assertEquals(searchResult1.get("Bertrand Jeannet").get(0).getTitle(), "Abstract acceleration of general linear loops");
        assertEquals(searchResult1.get("Isil Dillig").size(), 2);
        
        SearchResearcherAction action2 = new SearchResearcherAction("Action_2", user, new Date(), "Lecture Notes in Computer Science (including subseries Lecture Notes in Artificial Intelligence and Lecture Notes in Bioinformatics)", "300", SearchResearcherKind.JOURNAL_PUBLISH_TIMES);
        HashMap<String, List<Paper>> searchResult2 = engine.processSearchResearcherActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 51);
        assertEquals(searchResult2.get("Mooly Sagiv").size(), 3);
        assertEquals(searchResult2.get("Mooly Sagiv").get(2).getTitle(), "Statically inferring complex heap, array, and numeric invariants");
        assertEquals(searchResult2.get("Thomas Reps").size(), 4);
        assertEquals(searchResult2.get("Thomas Reps").get(0).getTitle(), "Revamping TVLA: Making parametric shape analysis competitive");
    }

    // @Tag(TestKind.PUBLIC)
    @Test
    void testSearchResearcherAction_SearchByKeywordSimilarity() {
    	MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SearchResearcherAction action1 = new SearchResearcherAction("Action_1", user, new Date(), "28", "M. Martel", SearchResearcherKind.KEYWORD_SIMILARITY);
        HashMap<String, List<Paper>> searchResult1 = engine.processSearchResearcherActionByLambda(user, action1);

       assertEquals(searchResult1.size(), 5);
       assertEquals(searchResult1.get("Wei Ngan Chin").get(0).getTitle(), "Loop invariant synthesis in a combined abstract domain");
       assertEquals(searchResult1.containsKey("Shengchao Qin"), true);
       assertEquals(searchResult1.containsKey("Xin Chen"), true);

       SearchResearcherAction action2 = new SearchResearcherAction("Action_2", user, new Date(), "50", "Vivien Maisonneuve", SearchResearcherKind.KEYWORD_SIMILARITY);
       HashMap<String, List<Paper>> searchResult2 = engine.processSearchResearcherActionByLambda(user, action2);

       assertEquals(searchResult2.size(), 0);

        SearchResearcherAction action3 = new SearchResearcherAction("Action_3", user, new Date(), "35", "Woosuk Lee", SearchResearcherKind.KEYWORD_SIMILARITY);
        HashMap<String, List<Paper>> searchResult3 = engine.processSearchResearcherActionByLambda(user, action3);

        assertEquals(searchResult3.size(), 3);
        assertEquals(searchResult3.get("Nadia Polikarpova").get(0).getTitle(), "Just-in-time learning for bottom-up enumerative synthesis");
        assertEquals(searchResult3.containsKey("Hila Peleg"), true);
        assertEquals(searchResult3.containsKey("Shraddha Barke"), true);
    }
}

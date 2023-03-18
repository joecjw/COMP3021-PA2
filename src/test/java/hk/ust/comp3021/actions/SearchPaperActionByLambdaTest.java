package hk.ust.comp3021.actions;

import hk.ust.comp3021.action.SearchPaperAction;
import hk.ust.comp3021.action.SearchPaperAction.SearchPaperKind;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.utils.TestKind;
import hk.ust.comp3021.MiniMendeleyEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SearchPaperActionByLambdaTest {
    @Tag(TestKind.PUBLIC)
    @Test
    void testSearchPaperAction_ActionsSize() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        int originalSize = engine.getActions().size();

        SearchPaperAction action = new SearchPaperAction("Action_1", user, new Date(),"Liu2022", SearchPaperKind.ID);
        engine.processSearchPaperActionByLambda(user, action);

        int currentSize = engine.getActions().size();
        assertEquals(currentSize, originalSize + 1);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSearchPaperAction_SearchByID() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SearchPaperAction action1 = new SearchPaperAction("Action_1", user, new Date(),"Liu2022", SearchPaperKind.ID);
        ArrayList<Paper> searchResult1 = engine.processSearchPaperActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 1);

        SearchPaperAction action2 = new SearchPaperAction("Action_1", user, new Date(),"Wang2022", SearchPaperKind.ID);
        ArrayList<Paper> searchResult2 = engine.processSearchPaperActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 0);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSearchPaperAction_SearchByAuthor() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SearchPaperAction action1 = new SearchPaperAction("Action_1", user, new Date(),"Thomas Reps", SearchPaperKind.AUTHOR);
        ArrayList<Paper> searchResult1 = engine.processSearchPaperActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 6);

        SearchPaperAction action2 = new SearchPaperAction("Action_1", user, new Date(),"Thomas Hall", SearchPaperKind.AUTHOR);
        ArrayList<Paper> searchResult2 = engine.processSearchPaperActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 0);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSearchPaperAction_SearchByTitle() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SearchPaperAction action1 = new SearchPaperAction("Action_1", user, new Date(),"Towards a Shape Analysis for Graph Transformation Systems", SearchPaperKind.TITLE);
        ArrayList<Paper> searchResult1 = engine.processSearchPaperActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 1);
        assertEquals(searchResult1.get(0).getPaperID(), "Steenken2010");
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSearchPaperAction_SearchByJournal() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        String journalName = "Proceedings of the ACM SIGPLAN Conference on Programming Language Design and Implementation (PLDI)";
        SearchPaperAction action1 = new SearchPaperAction("Action_1", user, new Date(),journalName, SearchPaperKind.JOURNAL);
        ArrayList<Paper> searchResult1 = engine.processSearchPaperActionByLambda(user, action1);
        assertEquals(searchResult1.size(), 4);
    }
}

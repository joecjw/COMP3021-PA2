package hk.ust.comp3021.actions;

import hk.ust.comp3021.action.SortPaperAction;
import hk.ust.comp3021.action.SortPaperAction.SortBase;
import hk.ust.comp3021.action.SortPaperAction.SortKind;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.utils.TestKind;
import hk.ust.comp3021.MiniMendeleyEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SortPaperActionByLambdaTest {
    @Tag(TestKind.PUBLIC)
    @Test
    void testSortPaperAction_ActionsSize() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        int originalSize = engine.getActions().size();

        SortPaperAction action = new SortPaperAction("Action_1", user, new Date(), SortBase.ID, SortKind.ASCENDING);
        engine.processSortPaperActionByLambda(user, action);

        int currentSize = engine.getActions().size();
        assertEquals(currentSize, originalSize + 1);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSortPaperAction_SortByID() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SortPaperAction action1 = new SortPaperAction("Action_1", user, new Date(), SortBase.ID, SortKind.ASCENDING);
        List<Paper> searchResult1 = engine.processSortPaperActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 123);
        assertEquals(searchResult1.get(0).getPaperID(), "Abdulla2016");
        assertEquals(searchResult1.get(5).getPaperID(), "Arndt2018");
        assertEquals(searchResult1.get(10).getPaperID(), "Bagnara2003");

        SortPaperAction action2 = new SortPaperAction("Action_2", user, new Date(), SortBase.ID, SortKind.DESCENDING);
        List<Paper> searchResult2 = engine.processSortPaperActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 123);
        assertEquals(searchResult2.get(0).getPaperID(), "Zhou2022");
        assertEquals(searchResult2.get(5).getPaperID(), "Yu2003");
        assertEquals(searchResult2.get(10).getPaperID(), "Wang2017");
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSortPaperAction_SortByAuthor() {
    	MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SortPaperAction action1 = new SortPaperAction("Action_1", user, new Date(), SortBase.AUTHOR, SortKind.ASCENDING);
        List<Paper> searchResult1 = engine.processSortPaperActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 123);
        assertEquals(searchResult1.get(0).getAuthors().size(), 1);
        assertEquals(String.join(", ", searchResult1.get(1).getAuthors()), "Alexey Gotsman, Josh Berdine, Byron Cook");
        assertEquals(searchResult1.get(6).getAuthors().size(), 3);
        assertEquals(String.join(", ", searchResult1.get(7).getAuthors()), "Axel Simon, Andy King");
        assertEquals(searchResult1.get(20).getAuthors().size(), 5);
        assertEquals(String.join(", ", searchResult1.get(20).getAuthors()), "Christian Dietrich, Valentin Rothberg, Ludwig Füracker, Andreas Ziegler, Daniel Lohmann");

        SortPaperAction action2 = new SortPaperAction("Action_2", user, new Date(), SortBase.AUTHOR, SortKind.DESCENDING);
        List<Paper> searchResult2 = engine.processSortPaperActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 123);
        assertEquals(searchResult2.get(0).getAuthors().size(), 7);
        assertEquals(String.join(", ", searchResult2.get(0).getAuthors()), "Zheng Guo, Michael James, David Justo, Jiaxiao Zhou, Ziteng Wang, Ranjit Jhala, Nadia Polikarpova");
        assertEquals(searchResult2.get(6).getAuthors().size(), 1);
        assertEquals(String.join(", ", searchResult2.get(6).getAuthors()), "Yijun Yu");
        assertEquals(searchResult2.get(9).getAuthors().size(), 10);
        assertEquals(String.join(", ", searchResult2.get(9).getAuthors()), "Xiaoxuan Liu, Shuxian Wang, Mengzhu Sun, Sharon Lee, Sicheng Pan, Joshua Wu, Cong Yan, Junwen Yang, Shan Lu, Alvin Cheung");
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSortPaperAction_SortByTitle() {
    	MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SortPaperAction action1 = new SortPaperAction("Action_1", user, new Date(), SortBase.TITLE, SortKind.ASCENDING);
        List<Paper> searchResult1 = engine.processSortPaperActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 123);
        assertEquals(searchResult1.get(0).getTitle(), "A model for detecting faults in build specifications");
        assertEquals(searchResult1.get(43).getTitle(), "Efficient context-sensitive shape analysis with graph based heap models");
        assertEquals(searchResult1.get(57).getTitle(), "Learning shape analysis");

        SortPaperAction action2 = new SortPaperAction("Action_2", user, new Date(), SortBase.TITLE, SortKind.DESCENDING);
        List<Paper> searchResult2 = engine.processSortPaperActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 123);
        assertEquals(searchResult2.get(0).getTitle(), "eTainter: Detecting Gas-Related Vulnerabilities in Smart Contracts");
        assertEquals(searchResult2.get(9).getTitle(), "Verification of heap manipulating programs with ordered data by extended forest automata");
        assertEquals(searchResult2.get(33).getTitle(), "S-Looper: Automatic summarization for multipath string loops");
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testSortPaperAction_SortByJournal() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        SortPaperAction action1 = new SortPaperAction("Action_1", user, new Date(), SortBase.JOURNAL, SortKind.ASCENDING);
        List<Paper> searchResult1 = engine.processSortPaperActionByLambda(user, action1);

        assertEquals(searchResult1.size(), 123);
        assertEquals(searchResult1.get(0).getJournal(), null);
        assertEquals(searchResult1.get(17).getJournal(), "2011 International Symposium on Software Testing and Analysis, ISSTA 2011 - Proceedings");
        assertEquals(searchResult1.get(30).getJournal(), "Atc'17");

        SortPaperAction action2 = new SortPaperAction("Action_2", user, new Date(), SortBase.JOURNAL, SortKind.DESCENDING);
        List<Paper> searchResult2 = engine.processSortPaperActionByLambda(user, action2);

        assertEquals(searchResult2.size(), 123);
        assertEquals(searchResult2.get(0).getJournal(), "Αγαη");
        assertEquals(searchResult2.get(4).getJournal(), "Reverse Engineering - Working Conference Proceedings");
        assertEquals(searchResult2.get(20).getJournal(), "Proceedings of the ACM SIGPLAN Conference on Programming Language Design and Implementation (PLDI)");
    }
}

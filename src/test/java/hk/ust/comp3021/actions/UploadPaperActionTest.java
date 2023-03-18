package hk.ust.comp3021.actions;

import hk.ust.comp3021.action.UploadPaperAction;
import hk.ust.comp3021.person.Researcher;
import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.utils.TestKind;
import hk.ust.comp3021.MiniMendeleyEngine;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UploadPaperActionTest {

    @Tag(TestKind.PUBLIC)
    @Test
    void testUploadPaperAction_ActionsSize() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        int originalSize = engine.getActions().size();
        String bibFilePath = "resources/bibdata/PAUploadData1.bib";
        UploadPaperAction action = new UploadPaperAction("Action_1", user, new Date(), bibFilePath);
        engine.processUploadPaperAction(user, action);

        int currentSize = engine.getActions().size();
        assertEquals(currentSize, originalSize + 1);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testUploadPaperAction_IsSuccessCheck() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        String bibFilePath = "resources/bibdata/PAUploadData1.bib";
        UploadPaperAction action = new UploadPaperAction("Action_1", user, new Date(), bibFilePath);
        engine.processUploadPaperAction(user, action);
        assertTrue(action.getActionResult());
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testUploadPaperAction_CheckPaperSet() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        String bibFilePath = "resources/bibdata/PAUploadData1.bib";
        UploadPaperAction action = new UploadPaperAction("Action_1", user, new Date(), bibFilePath);
        engine.processUploadPaperAction(user, action);
        Set<String> uploadedPaperIDs = action.getUploadedPapers().keySet();
        assertEquals(uploadedPaperIDs.size(), 3);
        assertTrue(uploadedPaperIDs.contains("Chase1990"));
        assertTrue(uploadedPaperIDs.contains("Hutchison1973"));
        assertTrue(uploadedPaperIDs.contains("Jones1979"));
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testUploadPaperAction_CheckPaperContent() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        String bibFilePath = "resources/bibdata/PAUploadData1.bib";
        UploadPaperAction action = new UploadPaperAction("Action_1", user, new Date(), bibFilePath);
        engine.processUploadPaperAction(user, action);
        HashMap<String, Paper> papers = action.getUploadedPapers();

        String journalStr = "Proceedings of the ACM SIGPLAN Conference on Programming Language Design and Implementation (PLDI)";
        assertEquals(papers.get("Chase1990").getJournal(), journalStr);
        assertNotEquals(papers.get("Hutchison1973").getJournal(), journalStr);
        assertNotEquals(papers.get("Jones1979").getJournal(), journalStr);

        assertEquals(papers.get("Chase1990").getTitle(), "Analysis of pointers and structures");
        assertEquals(papers.get("Hutchison1973").getTitle(), "Lecture Notes in Computer Science");
        assertEquals(papers.get("Jones1979").getTitle(), "Flow analysis and optimization of LISP-like structures");
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testUploadPaperAction_CheckResearchInfo() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        String bibFilePath = "resources/bibdata/PAUploadData1.bib";
        UploadPaperAction action = new UploadPaperAction("Action_1", user, new Date(), bibFilePath);
        engine.processUploadPaperAction(user, action);

        String researcherName1 = "David R. Chase";
        String researcherName2 = "F. Kenneth Zadeck";
        Researcher researcher1 = null;
        Researcher researcher2 = null;
        for (Researcher researcher : engine.getResearchers()) {
            if (researcher.getName().equals(researcherName1)) {
                researcher1 = researcher;
            }
            if (researcher.getName().equals(researcherName2)) {
                researcher2 = researcher;
            }
        }
        assertNotNull(researcher1);
        assertNotNull(researcher2);

        boolean isAttached = false;
        for (Paper paper : researcher1.getPapers()) {
            if (paper.getPaperID().equals("Chase1990")) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);

        isAttached = false;
        for (Paper paper : researcher2.getPapers()) {
            if (paper.getPaperID().equals("Chase1990")) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testUploadPaperAction_CheckPaperBase() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        int originalPaperNumber = engine.getPaperBase().size();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        String bibFilePath = "resources/bibdata/PAUploadData1.bib";
        UploadPaperAction action = new UploadPaperAction("Action_1", user, new Date(), bibFilePath);
        engine.processUploadPaperAction(user, action);
        int currentPaperNumber = engine.getPaperBase().size();
        assertEquals(currentPaperNumber, originalPaperNumber + 3);
    }
}

package hk.ust.comp3021.actions;

import hk.ust.comp3021.action.AddLabelAction;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.resource.Label;
import hk.ust.comp3021.utils.TestKind;
import hk.ust.comp3021.MiniMendeleyEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class AddLabelActionTest {

    @Tag(TestKind.PUBLIC)
    @Test
    void testAddLabelAction_ActionsSize() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        int originalSize = engine.getActions().size();
        AddLabelAction action = new AddLabelAction("Action_1", user, new Date(),
                "Abstract Domain", "Simon2004");
        engine.processAddLabelAction(user, action);
        int currentSize = engine.getActions().size();
        assertEquals(currentSize, originalSize + 1);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testAddLabelAction_PaperLabelOwnership() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        AddLabelAction action = new AddLabelAction("Action_1", user, new Date(),
                "Abstract Domain", "Simon2004");
        engine.processAddLabelAction(user, action);

        boolean isAttached = false;
        for (Label label : engine.getPaperBase().get("Simon2004").getLabels()) {
            if (label.getContent().equals("Abstract Domain")) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testAddLabelAction_UserOwnership() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        AddLabelAction action = new AddLabelAction("Action_1", user, new Date(),
                "Abstract Domain", "Simon2004");
        engine.processAddLabelAction(user, action);

        assertNotNull(user);
        boolean isAttached = false;
        for (Label label : user.searchLabelByPaperID("Simon2004")) {
            if (label.getContent().equals("Abstract Domain")) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }
    
    @Tag(TestKind.PUBLIC)
    @Test
    void testAddLabelAction_UserOwnership_Lambda() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        AddLabelAction action = new AddLabelAction("Action_1", user, new Date(),
                "Abstract Domain", "Simon2004");
        engine.processAddLabelAction(user, action);

        assertNotNull(user);
        boolean isAttached = false;
        for (Label label : user.searchLabelByPaperIDByLambda("Simon2004")) {
            if (label.getContent().equals("Abstract Domain")) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testAddLabelAction_LabelsInEngineOwnership() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());

        AddLabelAction action = new AddLabelAction("Action_1", user, new Date(),
                "Abstract Domain", "Simon2004");
        engine.processAddLabelAction(user, action);

        boolean isAttached = false;
        for (Label label : engine.getLabels()) {
            if (label.getPaperID().equals("Simon2004") && label.getContent().equals("Abstract Domain")) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }
}

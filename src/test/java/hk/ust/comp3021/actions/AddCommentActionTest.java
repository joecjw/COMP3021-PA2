package hk.ust.comp3021.actions;

import hk.ust.comp3021.MiniMendeleyEngine;
import hk.ust.comp3021.action.AddCommentAction;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.resource.Comment.*;
import hk.ust.comp3021.resource.Comment;
import hk.ust.comp3021.utils.TestKind;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class AddCommentActionTest {
    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_ActionsSize() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        int originalSize = engine.getActions().size();
        AddCommentAction action = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        engine.processAddCommentAction(user, action);
        int currentSize = engine.getActions().size();
        assertEquals(currentSize, originalSize + 1);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_OwnershipOfCommentOfPaper() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        AddCommentAction action = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        Comment newComment = engine.processAddCommentAction(user, action);

        boolean isAttached = false;
        for (Comment comment : engine.getPaperBase().get("Simon2004").getComments()) {
            if (comment.getContent().equals(newComment.getContent())) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_OwnershipOfCommentOfComment() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        AddCommentAction action1 = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        Comment newComment1 = engine.processAddCommentAction(user, action1);
        AddCommentAction action2 = new AddCommentAction("Action_2", user, new Date(),
                "Very good comment", CommentType.COMMENT_OF_COMMENT, newComment1.getCommentID());
        Comment newComment2 = engine.processAddCommentAction(user, action2);

        boolean isAttached = false;
        for (Comment comment : newComment1.getAttachedComments()) {
            if (comment.getContent().equals("Very good comment")) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }

    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_UserOwnershipOfCommentOfPaper() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        AddCommentAction action = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        Comment newComment = engine.processAddCommentAction(user, action);

        boolean isAttached = false;
        for (Comment comment : user.searchCommentByPaperObjID("Simon2004")) {
            if (comment == newComment) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }
    
    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_UserOwnershipOfCommentOfPaper_Lambda() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        AddCommentAction action = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        Comment newComment = engine.processAddCommentAction(user, action);

        boolean isAttached = false;
        for (Comment comment : user.searchCommentByPaperObjIDByLambda("Simon2004")) {
            if (comment == newComment) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }


    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_UserOwnershipOfCommentOfComment() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        AddCommentAction action1 = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        Comment newComment1 = engine.processAddCommentAction(user, action1);
        AddCommentAction action2 = new AddCommentAction("Action_2", user, new Date(),
                "Very good comment", CommentType.COMMENT_OF_COMMENT, newComment1.getCommentID());
        Comment newComment2 = engine.processAddCommentAction(user, action2);

        boolean isAttached = false;
        for (Comment comment : user.searchCommentByCommentObjID(newComment1.getCommentID())) {
            if (comment == newComment2) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }
    
    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_UserOwnershipOfCommentOfComment_Lambda() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        AddCommentAction action1 = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        Comment newComment1 = engine.processAddCommentAction(user, action1);
        AddCommentAction action2 = new AddCommentAction("Action_2", user, new Date(),
                "Very good comment", CommentType.COMMENT_OF_COMMENT, newComment1.getCommentID());
        Comment newComment2 = engine.processAddCommentAction(user, action2);

        boolean isAttached = false;
        for (Comment comment : user.searchCommentByCommentObjIDByLambda(newComment1.getCommentID())) {
            if (comment == newComment2) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }


    @Tag(TestKind.PUBLIC)
    @Test
    void testAddCommentAction_CommentsInEngineOwnership() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();
        String userID = "User_" + engine.getUsers().size();
        User user = engine.processUserRegister(userID, "testUser", new Date());
        AddCommentAction action = new AddCommentAction("Action_1", user, new Date(),
                "Very interesting work", CommentType.COMMENT_OF_PAPER, "Simon2004");
        Comment newComment = engine.processAddCommentAction(user, action);

        boolean isAttached = false;
        for (Comment comment : engine.getComments()) {
            if (comment.getCommentID().equals(newComment.getCommentID())) {
                isAttached = true;
                break;
            }
        }
        assertTrue(isAttached);
    }
}

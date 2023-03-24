package hk.ust.comp3021.person;

import hk.ust.comp3021.resource.Comment;
import hk.ust.comp3021.resource.Label;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User extends Person {
    private final Date registerDate;

    private final ArrayList<Comment> userComments = new ArrayList<>();

    private final ArrayList<Label> userLabels = new ArrayList<>();


    public User(String id, String name, Date registerDate) {
        super(id, name);
        this.registerDate = registerDate;
    }

    public void appendNewComment(Comment comment) {
        userComments.add(comment);
    }

    public ArrayList<Comment> searchCommentByPaperObjID(String id) {
        ArrayList<Comment> res = new ArrayList<>();
        for (Comment comment : userComments) {
            if (comment.getType() == Comment.CommentType.COMMENT_OF_PAPER) {
                if (comment.getCommentObjId().equals(id)) {
                    res.add(comment);
                }
            }
        }
        return res;
    }

    /**
     * Rewrite `searchCommentByPaperObjID` method with Lambda expressions following the original logic.
     * @param the paper id
     * @return the list of comments based on the input id
     */
    public ArrayList<Comment> searchCommentByPaperObjIDByLambda(String id) {
        ArrayList<Comment> res = new ArrayList<>();
        Predicate<Comment> isCorrectType = c -> c.getType() == Comment.CommentType.COMMENT_OF_PAPER;
        Predicate<Comment> isCorrectID = c -> c.getCommentObjId().equals(id);
        res.addAll(this.userComments.stream()
                                    .filter(isCorrectType.and(isCorrectID))
                                    .collect(Collectors.toList()));
        return res;
    }

    public ArrayList<Comment> searchCommentByCommentObjID(String id) {
        ArrayList<Comment> res = new ArrayList<>();
        for (Comment comment : userComments) {
            if (comment.getType() == Comment.CommentType.COMMENT_OF_COMMENT) {
                if (comment.getCommentObjId().equals(id)) {
                    res.add(comment);
                }
            }
        }
        return res;
    }

    /**
     * Rewrite `searchCommentByCommentObjID` method with Lambda expressions following the original logic.
     * @param the comment id
     * @return the list of comments based on the input id
     */
    public ArrayList<Comment> searchCommentByCommentObjIDByLambda(String id) {
        ArrayList<Comment> res = new ArrayList<>();
        Predicate<Comment> isCorrectType = c -> c.getType() == Comment.CommentType.COMMENT_OF_COMMENT;
        Predicate<Comment> isCorrectID = c -> c.getCommentObjId().equals(id);
        res.addAll(this.userComments.stream()
                                    .filter(isCorrectType.and(isCorrectID))
                                    .collect(Collectors.toList()));
        return res;
    }

    public void appendNewLabel(Label label) {
        userLabels.add(label);
    }

    public ArrayList<Label> searchLabelByPaperID(String id) {
        ArrayList<Label> res = new ArrayList<>();
        for (Label label : userLabels) {
            if (label.getPaperID().equals(id)) {
                res.add(label);
            }
        }
        return res;
    }

    /**
     * Rewrite `searchLabelByPaperIDByLambda` method with Lambda expressions following the original logic.
     * @param the paper id
     * @return the list of labels based on the input id
     */
    public ArrayList<Label> searchLabelByPaperIDByLambda(String id) {
        ArrayList<Label> res = new ArrayList<>();
        Predicate<Label> isCorrectID = l -> l.getPaperID().equals(id);
        res.addAll(this.userLabels.stream()
                                  .filter(isCorrectID)
                                  .collect(Collectors.toList()));
        return res;
    }
}

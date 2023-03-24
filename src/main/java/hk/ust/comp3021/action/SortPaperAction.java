package hk.ust.comp3021.action;

import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.person.User;
import java.util.*;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SortPaperAction extends Action {
    public enum SortBase {
        ID,
        TITLE,
        AUTHOR,
        JOURNAL,
    };

    public enum SortKind {
        ASCENDING,
        DESCENDING,
    };

    private SortBase base;

    private SortKind kind;

    private final List<Paper> actionResult = new ArrayList<>();

    public SortPaperAction(String id, User user, Date time, SortBase base, SortKind kind) {
        super(id, user, time, ActionType.SORT_PAPER);
        this.base = base;
        this.kind = kind;
    }

    public SortBase getBase() {
        return base;
    }

    public void setBase(SortBase base) {
        this.base = base;
    }

    public SortKind getKind() {
        return kind;
    }

    public void setKind(SortKind kind) {
        this.kind = kind;
    }

    public List<Paper> getActionResult() {
        return actionResult;
    }

    public void appendToActionResult(Paper paper) {
        this.actionResult.add(paper);
    }

    /**
     * TODO `appendToActionResultByLambda` appends one paper into `actionResult` each time.
     * @param paper to be appended into `actionResult`
     * @return null
     */
    public Consumer<Paper> appendToActionResultByLambda = paper -> this.actionResult.add(paper);

    /**
     * TODO `kindPredicate` determine whether the sort kind is `SortKind.DESCENDING`.
     * @param kind to be compared with `SortKind.DESCENDING`
     * @return boolean variable that indicates whether they are equal
     */
    public Predicate<SortKind> kindPredicate = kind -> this.kind.equals(SortKind.DESCENDING);

    /**
     * TODO `comparator` requires you to implement four custom comparators for different scenarios.
     * @param paper to be sorted
     * @return Given the sort base, there are three conditions for the return:
     * 1) if a = b then return 0;
     * 2) if a > b, then return 1;
     * 3) if a < b, then return -1;
     * PS1: if a = null, then a is considered as smaller than non-null b;
     * PS2: if a and b are both null, then they are considered equal;
     */
    public Comparator<Paper> comparator = (p1, p2)-> {
        if(this.getBase() == SortBase.ID){
            return p1.getPaperID().compareTo(p2.getPaperID());
        } else if (this.getBase() == SortBase.TITLE) {
            return p1.getTitle().compareTo(p2.getTitle());
        } else if (this.getBase() == SortBase.AUTHOR) {
            String s1 = p1.getAuthors().stream()
                                       .collect(Collectors.joining());
            String s2 = p2.getAuthors().stream()
                                       .collect(Collectors.joining());
            return s1.compareTo(s2);
        }
        if(p1.getJournal() == null && p2.getJournal() == null){
            return 0;
        }else if(p1.getJournal() == null){
            return -1;
        } else if (p2.getJournal() == null) {
            return 1;
        }
        return p1.getJournal().compareTo(p2.getJournal());
    };

    /**
     * TODO `sortFunc` provides a unified interface for sorting papers
     * @param a list of papers to be sorted into `actionResult`
     * @return `actionResult` that contains the papers sorted in the specified order
     */
    public Supplier<List<Paper>> sortFunc = () -> {
        if(this.getKind() == SortKind.ASCENDING){
            this.getActionResult().sort(this.comparator);
        }else {
            this.getActionResult().sort(this.comparator.reversed());
        }
        return this.actionResult;
   };

}

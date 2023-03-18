package hk.ust.comp3021.action;

import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.person.User;
import java.util.*;
import java.util.function.Function;

public class StatisticalInformationAction extends Action {
    public enum InfoKind {
        AVERAGE,
        MAXIMAL,
    };

    private InfoKind kind;

    private final Map<String, Double> actionResult = new HashMap<String, Double>();

    public StatisticalInformationAction(String id, User user, Date time, InfoKind kind) {
        super(id, user, time, ActionType.STATISTICAL_INFO);
        this.kind = kind;
    }

    public InfoKind getKind() {
        return kind;
    }

    public void setKind(InfoKind kind) {
        this.kind = kind;
    }

    public Map<String, Double> getActionResult() {
        return actionResult;
    }

    public void appendToActionResult(String key, Double value) {
        this.actionResult.put(key, value);
    }

    /**
     * TODO `obtainer1` indicates the first profiling criterion,
     *    i.e., Obtain the average number of papers published by researchers per year.
     * @param a list of papers to be profiled
     * @return `actionResult` that contains the target result
     */
    public Function<List<Paper>, Map<String, Double>> obtainer1;

    /**
     * TODO `obtainer2` indicates the second profiling criterion,
     *    i.e., Obtain the journals that receive the most papers every year.
     * @param a list of papers to be profiled
     * @return `actionResult` that contains the target result
     * PS: If two journals receive the same number of papers in a given year, then we take the default order.
     */
    public Function<List<Paper>, Map<String, Double>> obtainer2;

}

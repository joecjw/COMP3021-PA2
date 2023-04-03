package hk.ust.comp3021.action;

import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.person.User;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SearchResearcherAction extends Action {
    public enum SearchResearcherKind {
        PAPER_WITHIN_YEAR,
        JOURNAL_PUBLISH_TIMES,
        KEYWORD_SIMILARITY,
    };

    private String searchFactorX;
    private String searchFactorY;
    private SearchResearcherKind kind;

    private final HashMap<String, List<Paper>> actionResult = new HashMap<String, List<Paper>>();

    public SearchResearcherAction(String id, User user, Date time, String searchFactorX, String searchFactorY, SearchResearcherKind kind) {
        super(id, user, time, ActionType.SEARCH_PAPER);
        this.searchFactorX = searchFactorX;
        this.searchFactorY = searchFactorY;
        this.kind = kind;
    }

    public String getSearchFactorX() {
        return searchFactorX;
    }

    public String getSearchFactorY() {
        return searchFactorY;
    }

    public void setSearchFactorX(String searchFactorX) {
        this.searchFactorX = searchFactorX;
    }

    public void setSearchFactorY(String searchFactorY) {
        this.searchFactorY = searchFactorY;
    }

    public SearchResearcherKind getKind() {
        return kind;
    }

    public void setKind(SearchResearcherKind kind) {
        this.kind = kind;
    }

    public HashMap<String, List<Paper>> getActionResult() {
        return actionResult;
    }

    public void appendToActionResult(String researcher, Paper paper) {
        List<Paper> paperList = this.actionResult.get(researcher);
        if (paperList == null) {
            paperList = new ArrayList<Paper>();
            this.actionResult.put(researcher, paperList);
        }
        paperList.add(paper);
    }

    /**
     * TODO `searchFunc1` indicates the first searching criterion,
     *    i.e., Search researchers who publish papers more than X times in the recent Y years
     * @param null
     * @return `actionResult` that contains the relevant researchers
     */
    public Supplier<HashMap<String, List<Paper>>> searchFunc1 = () -> {
        List<String> researchersNotFulfillXY = new ArrayList<>();
        this.getActionResult().forEach((name, paperList) -> {
            List<Paper> papersWithinTime = new ArrayList<>();
            paperList.forEach(paper -> {
                if(2023 - paper.getYear() <= Integer.parseInt(this.searchFactorY)){
                    papersWithinTime.add(paper);
                }
            });
            if(papersWithinTime.size() < Integer.parseInt(this.getSearchFactorX())){
                researchersNotFulfillXY.add(name);
            }
        });

        researchersNotFulfillXY.forEach(this.actionResult::remove);
        return  this.actionResult;
    };

    /**
     * TODO `searchFunc2` indicates the second searching criterion,
     *    i.e., Search researchers whose papers published in the journal X have abstracts of the length more than Y.
     * @param null
     * @return `actionResult` that contains the relevant researchers
     */
    public Supplier<HashMap<String, List<Paper>>> searchFunc2 = () -> {
        Predicate<Paper> paperNotSatisfyX = paper -> {
            if (paper.getJournal() == null || paper.getJournal().isEmpty()){
                return true;
            }
            if (paper.getJournal().compareTo(this.searchFactorX) == 0){
                return  false;
            }
            return  true;
        };

        Predicate<Paper> paperNotSatisfyY = paper -> {
            if(paper.getAbsContent() == null || paper.getAbsContent().isEmpty()){
                return  true;
            }
            if(paper.getAbsContent().length() > Integer.parseInt(this.searchFactorY)){
                return false;
            }
            return true;
        };

        Predicate<Map.Entry<String, List<Paper>>> isPaperListEmpty = entry -> {
            return  entry.getValue().size() == 0;
        };

        this.getActionResult().entrySet().forEach(entry -> {
            entry.getValue().removeIf(paperNotSatisfyX.or(paperNotSatisfyY));
        });

        this.getActionResult().entrySet().removeIf(isPaperListEmpty);

        return this.actionResult;
    };


    public static int getLevenshteinDistance(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        int[][] dp = new int[m + 1][n + 1]; // dp[i][j] := min # of operations to convert str1[0..i) to str2[0..j)

        for (int i = 1; i <= m; ++i){
            dp[i][0] = i;
        }

        for (int j = 1; j <= n; ++j){
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; ++i){
            for (int j = 1; j <= n; ++j){
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; //no operation
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }

        return dp[m][n];
    }

    public double getSimilarity(String str1, String str2) {
        if(str1 == null || str2 == null || str1.isEmpty() || str2.isEmpty()) {
            return 0.0;
        }

       return (1 - (double)getLevenshteinDistance(str1,str2) / (double)Math.max(str1.length(), str2.length())) * 100;
    }

    /**
     * TODO `searchFunc3` indicates the third searching criterion
     *    i.e., Search researchers whoes keywords have more than similarity X% as one of those of the researcher Y.
     * @param null
     * @return `actionResult` that contains the relevant researchers
     * PS: 1) In this method, you are required to implement an extra method that calculates the Levenshtein Distance for
     *     two strings S1 and S2, i.e., the edit distance. Based on the Levenshtein Distance, you should calculate their
     *     similarity like `(1 - levenshteinDistance / max(S1.length, S2.length)) * 100`.
     *     2) Note that we need to remove paper(s) from the paper list of whoever are co-authors with the given researcher.
     */
    public Supplier<HashMap<String, List<Paper>>> searchFunc3 = () ->{
        HashMap<String, List<Paper>> result = new HashMap();
        this.actionResult.forEach((name, paperList) -> {
            List<Paper> paperListOfY = new ArrayList<>();
            paperListOfY.addAll(this.getActionResult().get(this.getSearchFactorY()));

            paperList.forEach(paper -> {
                paperListOfY.forEach(paperOfY -> {
                    String keyWordsOfCurrentPaper = paper.getKeywords().stream().collect(Collectors.joining());
                    String keyWordsOfCurrentPaperOfY = paperOfY.getKeywords().stream().collect(Collectors.joining());
                    keyWordsOfCurrentPaper = keyWordsOfCurrentPaper.strip().toLowerCase();
                    keyWordsOfCurrentPaperOfY = keyWordsOfCurrentPaperOfY.strip().toLowerCase();
                    double sim = getSimilarity(keyWordsOfCurrentPaper, keyWordsOfCurrentPaperOfY);
                    if(sim > Double.parseDouble(this.searchFactorX) && !result.containsKey(name) && !paperListOfY.contains(paper)){
                        if(result.isEmpty()){
                            result.put(name, new ArrayList<>());
                            result.get(name).add(paper);
                        }else {
                            if(result.containsKey(name)){
                                if(result.values().stream().filter(paperList1 -> paperList1.contains(paper))
                                        .collect(Collectors.toList()).size() == 0){
                                    result.get(name).add(paper);
                                }
                            }else{
                                result.put(name, new ArrayList<>());
                                if(result.values().stream().filter(paperList1 -> paperList1.contains(paper))
                                        .collect(Collectors.toList()).size() == 0){
                                    result.get(name).add(paper);
                                }
                            }
                        }
                    }
                });
            });
        });
        this.actionResult.clear();
        this.actionResult.putAll(result);
        return this.actionResult;
    };

}

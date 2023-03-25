package hk.ust.comp3021.action;

import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.person.User;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
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
     *    i.e., Search researchers whose papers published in the journal X have abstracts more than Y words.
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
        int len1 = str1.length();
        int len2 = str2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = str1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = str2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }

    public double getSimilarity(String str1, String str2) {
        if(str1 == null || str2 == null || str1.isEmpty() || str2.isEmpty())
        {
            return 0.0;
        }
       return (1 - getLevenshteinDistance(str1,str2) / Math.max(str1.length(), str2.length())) * 100;
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
        List<String> researchersNotFulfillXY = new ArrayList<>();
        this.actionResult.forEach((name, paperList) -> {
            List<Paper> paperListOfY = new ArrayList<>();
            paperListOfY.addAll(this.getActionResult().get(this.getSearchFactorY()));
            Predicate<Paper> isPaperinY = paper -> paperListOfY.contains(paper);
            List<Paper> repeatedPapers = paperList.stream()
                                                  .filter(isPaperinY)
                                                  .collect(Collectors.toList());
            paperListOfY.removeAll(repeatedPapers);
            paperList.removeAll(repeatedPapers);

            String keyWordsOfY = paperListOfY.stream()
                                             .map(paper ->  paper.getKeywords().stream().collect(Collectors.joining()))
                                             .collect(Collectors.joining());

            String keyWordsOfCurrentResearcher = paperList.stream()
                                                          .map(paper ->  paper.getKeywords().stream().collect(Collectors.joining()))
                                                          .collect(Collectors.joining());

            if(getSimilarity(keyWordsOfCurrentResearcher, keyWordsOfY) <= Double.parseDouble(this.searchFactorX)){
                researchersNotFulfillXY.add(name);
            }
        });

        researchersNotFulfillXY.forEach(name -> {
            this.actionResult.remove(name);
        });

        return this.actionResult;
    };

}

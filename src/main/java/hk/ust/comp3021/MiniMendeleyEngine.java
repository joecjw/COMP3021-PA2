package hk.ust.comp3021;

import hk.ust.comp3021.action.*;
import hk.ust.comp3021.person.Researcher;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.resource.Comment;
import hk.ust.comp3021.resource.Label;
import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.utils.BibExporter;
import hk.ust.comp3021.utils.BibParser;
import hk.ust.comp3021.resource.Comment.*;
import hk.ust.comp3021.action.SearchPaperAction.*;
import hk.ust.comp3021.action.SearchResearcherAction.*;
import hk.ust.comp3021.action.SortPaperAction.*;
import hk.ust.comp3021.action.StatisticalInformationAction.InfoKind;
import hk.ust.comp3021.utils.UserRegister;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiniMendeleyEngine {
    private final String defaultBibFilePath = "resources/bibdata/PAData.bib";
    private final HashMap<String, Paper> paperBase = new HashMap<>();
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Researcher> researchers = new ArrayList<>();

    private final ArrayList<Comment> comments = new ArrayList<>();

    private final ArrayList<Label> labels = new ArrayList<>();

    private final ArrayList<Action> actions = new ArrayList<>();

    public MiniMendeleyEngine() {
        populatePaperBaseWithDefaultBibFile();
    }

    public void populatePaperBaseWithDefaultBibFile() {
        User user = new User("User_0", "root_user", new Date());
        users.add(user);
        UploadPaperAction action = new UploadPaperAction("Action_0",user, new Date() , defaultBibFilePath);
        processUploadPaperAction(user, action);
        paperBase.putAll(action.getUploadedPapers());
    }

    public String getDefaultBibFilePath() {
        return defaultBibFilePath;
    }

    public HashMap<String, Paper> getPaperBase() {
        return paperBase;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Researcher> getResearchers() {
        return researchers;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public User processUserRegister(String id, String name, Date date) {
        UserRegister ur = new UserRegister(id, name, date);
        User curUser = ur.register();
        users.add(curUser);
        return curUser;
    }

    public Comment processAddCommentAction(User curUser, AddCommentAction action) {
        actions.add(action);
        if (action.getCommentType() == CommentType.COMMENT_OF_COMMENT) {
            String objCommentID = action.getObjectId();
            for (Comment comment : comments) {
                if (objCommentID.equals(comment.getCommentID())) {
                    String commentID = "Comment" + String.valueOf(comments.size() + 1);
                    Comment newComment = new Comment(commentID, action.getTime(), action.getCommentStr(),
                            action.getUser(), action.getCommentType(), action.getObjectId());
                    comments.add(newComment);
                    comment.appendComment(newComment);
                    curUser.appendNewComment(newComment);
                    action.setActionResult(true);
                    return newComment;
                }
            }
        } else if (action.getCommentType() == CommentType.COMMENT_OF_PAPER) {
            String objCommentID = action.getObjectId();
            for (Map.Entry<String, Paper> entry : paperBase.entrySet()) {
                String paperID = entry.getKey();
                if (paperID.equals(objCommentID)) {
                    String commentID = "Comment" + String.valueOf(comments.size() + 1);
                    Comment newComment = new Comment(commentID, action.getTime(), action.getCommentStr(),
                            action.getUser(), action.getCommentType(), action.getObjectId());
                    comments.add(newComment);
                    entry.getValue().appendComment(newComment);
                    curUser.appendNewComment(newComment);
                    action.setActionResult(true);
                    return newComment;
                }
            }
        }
        action.setActionResult(false);
        return null;
    }

    public Label processAddLabelAction(User curUser, AddLabelAction action) {
        actions.add(action);
        String paperID = action.getPaperID();
        String labelID = "Label" + String.valueOf(labels.size() + 1);
        Label newLabel = new Label(labelID, action.getPaperID(), action.getTime(), action.getLabelStr(), action.getUser());

        if (paperBase.containsKey(paperID)) {
            paperBase.get(paperID).appendLabelContent(newLabel);
            curUser.appendNewLabel(newLabel);
            labels.add(newLabel);
            action.setActionResult(true);
            return newLabel;
        } else {
            action.setActionResult(false);
            return null;
        }
    }

    public void processDownloadPaperAction(User curUser, DownloadPaperAction action) {
        actions.add(action);
        String path = action.getDownloadPath();
        String content = "";
        HashMap<String, Paper> downloadedPapers = new HashMap<>();
        for (String paperID : action.getPaper()) {
            if (paperBase.containsKey(paperID)) {
                downloadedPapers.put(paperID, paperBase.get(paperID));
            } else {
                action.setActionResult(false);
                return;
            }
        }
        BibExporter exporter = new BibExporter(downloadedPapers, path);
        exporter.export();
        action.setActionResult(!exporter.isErr());
    }

    public ArrayList<Paper> processSearchPaperAction(User curUser, SearchPaperAction action) {
        actions.add(action);
        switch (action.getKind()) {
            case ID:
                for (Map.Entry<String, Paper> entry : paperBase.entrySet()) {
                    if (action.getSearchContent().equals(entry.getKey())) {
                        action.appendToActionResult(entry.getValue());
                    }
                }
                break;
            case TITLE:
                for (Map.Entry<String, Paper> entry : paperBase.entrySet()) {
                    if (action.getSearchContent().equals(entry.getValue().getTitle())) {
                        action.appendToActionResult(entry.getValue());
                    }
                }
                break;
            case AUTHOR:
                for (Map.Entry<String, Paper> entry : paperBase.entrySet()) {
                    if (entry.getValue().getAuthors().contains(action.getSearchContent())) {
                        action.appendToActionResult(entry.getValue());
                    }
                }
                break;
            case JOURNAL:
                for (Map.Entry<String, Paper> entry : paperBase.entrySet()) {
                    if (action.getSearchContent().equals(entry.getValue().getJournal())) {
                        action.appendToActionResult(entry.getValue());
                    }
                }
                break;
            default:
                break;
        }
        return action.getActionResult();
    }

    /**
     * TODO: Rewrite the searching part with Lambda expressions using functional interfaces.
     * You should follow the original logic in `processSearchPaperAction` to complete four kinds of searching.
     * The things you need to do are: 1) implement the functional interfaces; 2) fulfill the logic here.
     * The prototypes for the functional interfaces are in `SearchPaperAction`.
     */
    public ArrayList<Paper> processSearchPaperActionByLambda(User curUser, SearchPaperAction action) {
        actions.add(action);
        switch (action.getKind()) {
            case ID:
                this.paperBase.forEach((string, paper) -> {
                    if(action.isEqual.test(string)){
                        Consumer<Paper> consumer = action.appendToActionResultByLambda;
                        consumer.accept(paper);
                    }
                });
                break;
            case TITLE:
                this.paperBase.forEach((string, paper) -> {
                    if(action.isEqual.test(paper.getTitle())){
                        Consumer<Paper> consumer = action.appendToActionResultByLambda;
                        consumer.accept(paper);
                    }
                });
                break;
            case AUTHOR:
                this.paperBase.forEach((string, paper) -> {
                    if(action.isContain.test(paper.getAuthors())){
                        Consumer<Paper> consumer = action.appendToActionResultByLambda;
                        consumer.accept(paper);
                    }
                });
                break;
            case JOURNAL:
                this.paperBase.forEach((string, paper) -> {
                    if(action.isEqual.test(paper.getJournal())){
                        Consumer<Paper> consumer = action.appendToActionResultByLambda;
                        consumer.accept(paper);
                    }
                });
                break;
            default:
                break;
        }
        return action.getActionResult();
    }

    /**
     * TODO: Implement the custom comparators for various scenarios.
     * The things you need to do: 1) implement the functional interfaces; 2) fulfill the logic here.
     * The prototypes for the functional interfaces are in `SortPaperAction`.
     * PS: You should operate directly on `actionResult` since we have already put the papers into it in the original order.
     */
    public List<Paper> processSortPaperActionByLambda(User curUser, SortPaperAction action) {
        actions.add(action);
        paperBase.entrySet().forEach(entry -> {
            action.appendToActionResultByLambda.accept(entry.getValue());
        });
        switch (action.getBase()) {
            case ID:
                Comparator<Paper> compareID = (paper_1, paper_2) -> {
                    if(action.getKind() == SortKind.DESCENDING){
                        return paper_1.getPaperID().compareToIgnoreCase(paper_2.getPaperID());
                    }
                   return -1 * (paper_1.getPaperID().compareToIgnoreCase(paper_2.getPaperID()));
                };

                action.getActionResult().stream().sorted(compareID).collect(Collectors.toList());
                break;
            case TITLE:
                Comparator<Paper> compareTitle = (paper_1, paper_2) -> {
                    if(action.getKind() == SortKind.DESCENDING){
                        return paper_1.getTitle().compareToIgnoreCase(paper_2.getTitle());
                    }
                    return -1 * (paper_1.getTitle().compareToIgnoreCase(paper_2.getTitle()));
                };

                action.getActionResult().stream().sorted(compareTitle).collect(Collectors.toList());
                break;
            case AUTHOR:
                Comparator<Paper> compareAuthor = (paper_1, paper_2) -> {
                    if(action.getKind() == SortKind.DESCENDING){
                       if(paper_1.getAuthors().size() > paper_2.getAuthors().size()){
                           for(int i = 0; i < paper_2.getAuthors().size(); i++){
                               if((paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i)) != 0)){
                                   return paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i));
                               }
                           }
                           return 1;
                       } else if (paper_1.getAuthors().size() < paper_2.getAuthors().size()) {
                           for(int i = 0; i < paper_1.getAuthors().size(); i++){
                               if((paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i)) != 0)){
                                   return paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i));
                               }
                           }
                           return -1;
                       }else {
                           for(int i = 0; i < paper_1.getAuthors().size(); i++){
                               if((paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i)) != 0)){
                                   return paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i));
                               }
                           }
                           return 0;
                       }
                    }

                    if(paper_1.getAuthors().size() > paper_2.getAuthors().size()){
                        for(int i = 0; i < paper_2.getAuthors().size(); i++){
                            if((paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i)) != 0)){
                                return -1* paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i));
                            }
                        }
                        return -1;
                    } else if (paper_1.getAuthors().size() < paper_2.getAuthors().size()) {
                        for(int i = 0; i < paper_1.getAuthors().size(); i++){
                            if((paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i)) != 0)){
                                return -1* paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i));
                            }
                        }
                        return 1;
                    }else {
                        for(int i = 0; i < paper_1.getAuthors().size(); i++){
                            if((paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i)) != 0)){
                                return -1* paper_1.getAuthors().get(i).compareToIgnoreCase(paper_2.getAuthors().get(i));
                            }
                        }
                        return 0;
                    }
                };

                action.getActionResult().stream().sorted(compareAuthor).collect(Collectors.toList());
                break;
            case JOURNAL:
                Comparator<Paper> compareJournal = (paper_1, paper_2) -> {
                    if(action.getKind() == SortKind.DESCENDING){
                        return paper_1.getJournal().compareToIgnoreCase(paper_2.getJournal());
                    }
                    return -1 * (paper_1.getJournal().compareToIgnoreCase(paper_2.getJournal()));
                };

                action.getActionResult().stream().sorted(compareJournal).collect(Collectors.toList());
                break;
            default:
                break;
        }
        action.sortFunc.get();
        return action.getActionResult();
    }

    /**
     * TODO: Implement the new searching method with Lambda expressions using functional interfaces.
     * The thing you need to do is to implement the three functional interfaces, i.e., searchFunc1 / searchFunc2 /searchFunc3.
     * The prototypes for the functional interfaces are in `SearchResearcherAction`.
     * PS: You should operate directly on `actionResult` since we have already put the papers into it.
     */
    public HashMap<String, List<Paper>> processSearchResearcherActionByLambda(User curUser, SearchResearcherAction action) {
        actions.add(action);
        paperBase.entrySet().forEach(entry -> {
            entry.getValue().getAuthors().forEach(author -> action.appendToActionResult(author, entry.getValue()));
        });
        switch (action.getKind()) {
            case PAPER_WITHIN_YEAR:
                action.searchFunc1.get();
                break;
            case JOURNAL_PUBLISH_TIMES:
                action.searchFunc2.get();
                break;
            case KEYWORD_SIMILARITY:
                action.searchFunc3.get();
                break;

            default:
                break;
        }
        return action.getActionResult();
    }

    /**
     * TODO: Implement the new profiling method with Lambda expressions using functional interfaces.
     * The thing you need to do is to implement the two functional interfaces, i.e., InfoObtainer1 / InfoObtainer2.
     * The prototypes for the functional interfaces are in `StatisticalInformationAction`.
     */
    public Map<String, Double> processStatisticalInformationActionByLambda(User curUser, StatisticalInformationAction action) {
        actions.add(action);
        List<Paper> paperList = new ArrayList<Paper>();
        paperBase.entrySet().forEach(entry -> paperList.add(entry.getValue()));
        switch (action.getKind()) {
            case AVERAGE:
                action.obtainer1.apply(paperList);
                break;
            case MAXIMAL:
                action.obtainer2.apply(paperList);
                break;
            default:
                break;
        }
        return action.getActionResult();
    }

    public void processUploadPaperAction(User curUser, UploadPaperAction action) {
        actions.add(action);
        BibParser parser = new BibParser(action.getBibfilePath());
        parser.parse();
        action.setUploadedPapers(parser.getResult());
        for (String paperID : action.getUploadedPapers().keySet()) {
            Paper paper = action.getUploadedPapers().get(paperID);
            paperBase.put(paperID, paper);
            for (String researcherName : paper.getAuthors()) {
                Researcher existingResearch = null;
                for (Researcher researcher : researchers) {
                    if (researcher.getName().equals(researcherName)) {
                        existingResearch = researcher;
                        break;
                    }
                }
                if (existingResearch == null) {
                    Researcher researcher = new Researcher("Researcher_" + researchers.size(), researcherName);
                    researcher.appendNewPaper(paper);
                    researchers.add(researcher);
                } else {
                    existingResearch.appendNewPaper(paper);
                }
            }
        }
        action.setActionResult(!parser.isErr());
    }

    public User userInterfaceForUserCreation() {
        System.out.println("Please enter your name.");
        Scanner scan2 = new Scanner(System.in);
        if (scan2.hasNextLine()) {
            String name = scan2.nextLine();
            System.out.println("Create the account with the name: " + name);
            String userID = "User_" + users.size();
            User curUser = processUserRegister(userID, name, new Date());
            System.out.println("Account created!");
            return curUser;
        }
        return null;
    }

    public void userInterfaceForPaperSearch(User curUser) {
        System.out.println("Please specify the search kind:");
        System.out.println("  1: Search by ID");
        System.out.println("  2: Search by title");
        System.out.println("  3: Search by author");
        System.out.println("  4: Search by journal");
        while (true) {
            Scanner scan3 = new Scanner(System.in);
            if (scan3.hasNextInt()) {
                int k = scan3.nextInt();
                if (k < 1 || k > 4) {
                    System.out.println("You should enter 1~4.");
                } else {
                    System.out.println("Please specify the search word:");
                    Scanner scan4 = new Scanner(System.in);
                    if (scan4.hasNextLine()) {
                        String word = scan4.nextLine();
                        SearchPaperAction action = new SearchPaperAction("Action_" + actions.size(),
                                curUser, new Date(), word, SearchPaperKind.values()[k - 1]);
                        actions.add(action);
                        processSearchPaperAction(curUser, action);

                        if (action.getActionResult().size() > 0) {
                            System.out.println("Paper found! The paper IDs are as follows:");
                            for (Paper paper : action.getActionResult()) {
                                System.out.println(paper.getPaperID());
                            }
                        } else {
                            System.out.println("Paper not found!");
                        }
                        break;
                    }
                }
            }
        }
    }

    public void userInterfaceForPaperSearchByLambda(User curUser) {
        System.out.println("Please specify the search kind:");
        System.out.println("  1: Search by ID");
        System.out.println("  2: Search by title");
        System.out.println("  3: Search by author");
        System.out.println("  4: Search by journal");
        while (true) {
            Scanner scan1 = new Scanner(System.in);
            if (scan1.hasNextInt()) {
                int k = scan1.nextInt();
                if (k < 1 || k > 4) {
                    System.out.println("You should enter 1~4.");
                } else {
                    System.out.println("Please specify the search word:");
                    Scanner scan2 = new Scanner(System.in);
                    if (scan2.hasNextLine()) {
                        String word = scan2.nextLine();
                        SearchPaperAction action = new SearchPaperAction("Action_" + actions.size(),
                                curUser, new Date(), word, SearchPaperKind.values()[k - 1]);
                        actions.add(action);
                        processSearchPaperActionByLambda(curUser, action);

                        if (action.getActionResult().size() > 0) {
                            System.out.println("Paper found! The paper IDs are as follows:");
                            for (Paper paper : action.getActionResult()) {
                                System.out.println(paper);
                            }
                        } else {
                            System.out.println("Paper not found!");
                        }
                        break;
                    }
                }
            }
        }
    }

    public void userInterfaceForPaperSortByLambda(User curUser) {
        System.out.println("Please specify the sort base:");
        System.out.println("  1: Sort by ID");
        System.out.println("  2: Sort by title");
        System.out.println("  3: Sort by author");
        System.out.println("  4: Sort by journal");
        while (true) {
            Scanner scan1 = new Scanner(System.in);
            if (scan1.hasNextInt()) {
                int k = scan1.nextInt();
                if (k < 1 || k > 4) {
                    System.out.println("You should enter 1~4.");
                } else {
                    System.out.println("Please specify the sort kind:");
                    System.out.println("  1: Sort in ascending order");
                    System.out.println("  2: Sort in descending order");
                    Scanner scan2 = new Scanner(System.in);
                    if (scan2.hasNextLine()) {
                        int m = scan2.nextInt();
                        SortPaperAction action = new SortPaperAction("Action_" + actions.size(),
                                curUser, new Date(), SortBase.values()[k - 1], SortKind.values()[m - 1]);
                        actions.add(action);
                        processSortPaperActionByLambda(curUser, action);

                        if (action.getActionResult().size() > 0) {
                            System.out.println("Paper sorted! The paper are sorted as follows:");
                            for (Paper paper : action.getActionResult()) {
                                System.out.println(paper);
                            }
                        } else {
                            System.out.println("Paper not sorted!");
                        }
                        break;
                    }
                }
            }
        }
    }

    public void userInterfaceForResearcherSearchByLambda(User curUser) {
        System.out.println("Please specify the search kind:");
        System.out.println("  1: Search researchers who publish papers more than X times in the recent Y years");
        System.out.println("  2: Search researchers whose papers published in the journal X have abstracts more than Y words");
        System.out.println("  3: Search researchers whoes keywords have more than similarity X% as one of those of the researcher Y");
        while (true) {
            Scanner scan1 = new Scanner(System.in);
            if (scan1.hasNextInt()) {
                int k = scan1.nextInt();
                if (k < 1 || k > 3) {
                    System.out.println("You should enter 1~3.");
                } else {
                    System.out.println("Please specify the X:");
                    Scanner scan2 = new Scanner(System.in);
                    if (scan2.hasNextLine()) {
                        String factorX = scan2.nextLine();
                        System.out.println("Please specify the Y:");
                        Scanner scan3 = new Scanner(System.in);
                        if (scan3.hasNextLine()) {
                            String factorY = scan3.nextLine();
                            SearchResearcherAction action = new SearchResearcherAction("Action_" + actions.size(),
                                    curUser, new Date(), factorX, factorY, SearchResearcherKind.values()[k - 1]);
                            actions.add(action);
                            processSearchResearcherActionByLambda(curUser, action);

                            if (action.getActionResult().size() > 0) {
                                System.out.println("Researcher found! The researcher information is as follows:");
                                for (Map.Entry<String, List<Paper>> entry : action.getActionResult().entrySet()) {
                                    System.out.println(entry.getKey());
                                    for (Paper paper : entry.getValue()) {
                                        System.out.println(paper);
                                    }
                                }
                            } else {
                                System.out.println("Researcher not found!");
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void userInterfaceForStatisticalInformationByLambda(User curUser) {
        System.out.println("Please specify the information:");
        System.out.println("  1: Obtain the average number of papers published by researchers per year");
        System.out.println("  2: Obtain the journals that receive the most papers every year");
        while (true) {
            Scanner scan1 = new Scanner(System.in);
            if (scan1.hasNextInt()) {
                int k = scan1.nextInt();
                if (k < 1 || k > 2) {
                    System.out.println("You should enter 1~2.");
                } else {
                    StatisticalInformationAction action = new StatisticalInformationAction("Action_" + actions.size(),
                            curUser, new Date(), InfoKind.values()[k - 1]);
                    actions.add(action);
                    processStatisticalInformationActionByLambda(curUser, action);

                    if (action.getActionResult().size() > 0) {
                        System.out.println("Information Obtained! The information is as follows:");
                        for (Map.Entry<String, Double> entry : action.getActionResult().entrySet()) {
                            System.out.println(entry.getKey() + ": " + entry.getValue());
                        }
                    } else {
                        System.out.println("Information not obtained!");
                    }
                    break;
                }
            }
        }
    }

    public void userInterfaceForPaperUpload(User curUser) {
        System.out.println("Please specify the absolute path of the bib file:");
        Scanner scan5 = new Scanner(System.in);
        if (scan5.hasNextLine()) {
            String name = scan5.nextLine();
            UploadPaperAction action = new UploadPaperAction("Action_" + actions.size(), curUser, new Date(), name);
            actions.add(action);
            processUploadPaperAction(curUser, action);
            if (action.getActionResult()) {
                System.out.println("Succeed! The uploaded papers are as follows:");
                for (String id : action.getUploadedPapers().keySet()) {
                    System.out.println(id);
                }
            } else {
                System.out.println("Fail! You need to specify an existing bib file.");
            }
        }
    }

    public void userInterfaceForPaperDownload(User curUser) {
        System.out.println("Please specify the absolute path of the bib file:");
        Scanner scan6 = new Scanner(System.in);
        if (scan6.hasNextLine()) {
            String path = scan6.nextLine();
            DownloadPaperAction action = new DownloadPaperAction("Action_" + actions.size(), curUser, new Date(), path);
            System.out.println("Please enter the paper ID line by line and end with END");
            while (true) {
                Scanner scan7 = new Scanner(System.in);
                if (scan7.hasNextLine()) {
                    String name = scan7.nextLine();
                    if (name.equals("END")) {
                        break;
                    } else {
                        action.appendPapers(name);
                    }
                }
            }
            actions.add(action);
            processDownloadPaperAction(curUser, action);
            if (action.getActionResult()) {
                System.out.println("Succeed! The downloaded paper is stored in your specified file.");
            } else {
                System.out.println("Fail! Some papers not found!");
            }
        }
    }

    public void userInterfaceForAddLabel(User curUser) {
        System.out.println("Please specify the paper ID:");
        Scanner scan8 = new Scanner(System.in);
        if (scan8.hasNextLine()) {
            String paperID = scan8.nextLine();
            System.out.println("Please specify the label");
            Scanner scan9 = new Scanner(System.in);
            if (scan9.hasNextLine()) {
                String newlabel = scan9.nextLine();
                AddLabelAction action = new AddLabelAction("Action_" + actions.size(), curUser, new Date(), newlabel, paperID);
                actions.add(action);
                processAddLabelAction(curUser, action);

                if (action.getActionResult()) {
                    System.out.println("Succeed! The label is added.");
                } else {
                    System.out.println("Fail!");
                }
            }
        }
    }

    public void userInterfaceForAddComment(User curUser) {
        System.out.println("Please specify the commented object ID:");
        Scanner scan10 = new Scanner(System.in);
        if (scan10.hasNextLine()) {
            String objID = scan10.nextLine();
            System.out.println("Please specify the comment");
            Scanner scan11 = new Scanner(System.in);
            if (scan11.hasNextLine()) {
                String newCommentStr = scan11.nextLine();
                CommentType t = null;
                if (objID.startsWith("Comment")) {
                    t = CommentType.COMMENT_OF_COMMENT;
                } else {
                    t = CommentType.COMMENT_OF_PAPER;
                }
                AddCommentAction action = new AddCommentAction("Action_" + actions.size(), curUser, new Date(), newCommentStr, t, objID);
                actions.add(action);
                processAddCommentAction(curUser, action);

                if (action.getActionResult()) {
                    System.out.println("Succeed! The comment is added.");
                } else {
                    System.out.println("Fail!");
                }
            }
        }
    }

    public void userInterface() {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("MiniMendeley is running...");
        System.out.println("Initial paper base has been populated!");
        User curUser = null;

        while (true) {
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Please select the following operations with the corresponding numbers:");
            System.out.println("  0: Register an account");
            System.out.println("  1: Search papers");
            System.out.println("  2: Upload papers");
            System.out.println("  3: Download papers");
            System.out.println("  4: Add labels");
            System.out.println("  5: Add comments");
            System.out.println("  6: Search papers via Lambda");
            System.out.println("  7: Sort papers via Lambda");
            System.out.println("  8: Search researchers via Lambda");
            System.out.println("  9: Obtain statistical information via Lambda");
            System.out.println("  10: Exit");
            System.out.println("----------------------------------------------------------------------");
            Scanner scan1 = new Scanner(System.in);
            if (scan1.hasNextInt()) {
                int i = scan1.nextInt();
                if (i < 0 || i > 9) {
                    System.out.println("You should enter 0~9.");
                    continue;
                }
                if (curUser == null && i != 0) {
                    System.out.println("You need to register an account first.");
                    continue;
                }
                switch (i) {
                    case 0 -> {
                        curUser = userInterfaceForUserCreation();
                    }
                    case 1 -> {
                        userInterfaceForPaperSearch(curUser);
                    }
                    case 2 -> {
                        userInterfaceForPaperUpload(curUser);
                    }
                    case 3 -> {
                        userInterfaceForPaperDownload(curUser);
                    }
                    case 4 -> {
                        userInterfaceForAddLabel(curUser);
                    }
                    case 5 -> {
                        userInterfaceForAddComment(curUser);
                    }
                    case 6 -> {
                        userInterfaceForPaperSearchByLambda(curUser);
                    }
                    case 7 -> {
                        userInterfaceForPaperSortByLambda(curUser);
                    }
                    case 8 -> {
                        userInterfaceForResearcherSearchByLambda(curUser);
                    }
                    case 9 -> {
                        userInterfaceForStatisticalInformationByLambda(curUser);
                    }
                    default -> {
                    }
                }
                if (i == 10) break;
            } else {
                System.out.println("You should enter integer 0~6.");
            }
        }
    }
}

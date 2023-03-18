# Mini-Mendeley

This document demonstrates the specification of the system.
In this project, you are required to implement a paper management system, named Mini-Mendeley.
It should support the following functionalities:

- Support the creation of user accounts.
- Support the users to upload and download papers with/into bib files.
- Support the users to add tags to a specific paper according to paper ID.
- Support the users to add comments to a specific paper or another comment according to paper ID or comment ID.
- <u>**Support the users to search, sort, profile specified targets with Lambda expressions.**</u>

In what follows, we provide more concrete specifications for the objects and their methods.

## Person

### Fields and methods

A `Person` object has two fields, namely `id: String` and `name: String`.

### Subclasses

The class `Person` has two subclasses, namely `Researcher` and `User`.

- The class `Researcher` has one more field than the class `Person`, i.e., `papers: ArrayList<Paper>`.

- The class `User` has three more fields than the class `Person`, namely `registerDate: Date`, `userComments: ArrayList<Comment>`, and `userTags: ArrayList<Tag>`.

## Resource

There are three kinds of resources in Mini-Mendeley, namely `Comment`, `Paper`, and `Tag`.
We have provide the complete implementation of the three classes.
For more detailed explanation of the fields and methods,
you can refer to the class files under the directory `src/main/java/hk.ust.comp3021/resource`.

## Action

An `Action` class contains four fields, namely "time: date" (indicating creation date), "id: String" (indicating the ID of the action), "action_type: ActionTyple" (indicating the type of the action). You can refer to the file Action for more detail. Apart from the class `Action`, there are five another subclasses of `Action`, which incidate five typicl operations over the paper base.

**<u>In addition to the above five subclasses for PA1, there are new subclasses `SortPaperAction`, `SearchResearcherAction` and `StatisticalInformationAction` for PA2. We also add new interfaces in the subclass `SearchPaperAction`. All new subclasses need to be implemented to support the new functionality of Mini-Mendeley. Note that we might modify some member variables and member methods for more convenient development.</u>**

## Utils

A `Utils` class is to provide the functionalities as follows:

- `BibParser`: Parse a given bib file and extract the paper objects from the bib file.
If the parsing succeeds, the `isErr` is set to `false`. Otherwise, it should be set to `true`.
The example of the bib file is `src/main/resources/bibdata/PAData.bib`.
For some paper records, the fields in the `Paper` classes do not have the corresponding attributes, so they are set to be null.
When you parse the file, you can assume that all the bib files in our testcases have the same format as our example bib file.

- `BibExporter`: Dump given papers to a bib file. 
Similar to `BibParser`, the `isErr` is set to `false`/`true` if the exporting succeeds/fails.
The format of the exported bib file is the same as our example bib file.

- `UserRegister`: Register a user. 
The method `register` returns a user with the specified user name, the assigned user ID, and a registeration time.

## MiniMendeley

The class `MiniMendeley` is the main class of our system.
After intializing all the fields, it loads all the papers in the default bib file to `paperBase`
by invoking `populatePaperBaseWithDefaultBibFile`,
which depends on the `BibParser`.
Then the method `userInterface` proccesses the commands in the console and invoke the corresponding handlers.
Initially, a user account is created and we add it to `users`.
When a new user account is created in the middle of the execution,
the current user account will be overwritten, 
i.e., the newly created user account is the one who performs the subsequent operations.
All the resources (comments, tags, and papers) are added to the corresponding fields of `MiniMendeley`,
and meanwhile,
the fields of these resources are updated, e.g., the fields `tags` and `comments` of a `Paper` object.

## What YOU need to do

* <u>**Fully implement the functional interfaces in the class `SearchPaperAction` and utilize them to implement the new method `processSearchPaperActionByLambda` following the original logic of the `processSearchPaperAction` to support the following criteria.**</u> 
  1. <u>**Search by ID**</u>
  2. <u>**Search by title**</u>
  3. <u>**Search by author**</u>
  4. <u>**Search by journal**</u>
  
  ```java
  List<Integer> numList = new ArrayList<Integer>();
  Consumer<Integer> addToSet = num -> numList.add(num);
  ```
  
* <u>**Fully implement the functional interfaces in the class `SortPaperAction` and utilize them to implement the new method `processSortPaperActionByLambda` to support the following criteria.**</u>
  1. <u>**Search by ID**</u>
  2. <u>**Search by title**</u>
  3. <u>**Search by author**</u>
  4. <u>**Search by journal**</u>
  
  ```java
  Predicate<Integer> isSame = num -> num == 1;
  ```
  
* <u>**Fully implement the functional interfaces in the class `SearchResearcherAction` and utilize them to implement the new method `processSearchResearcherActionByLambda` to support the following criteria.**</u>
  1. <u>**Search researchers who publish papers more than X times in the recent Y years**</u>
  2. <u>**Search researchers whose papers published in the journal X have abstracts more than Y words**</u>
  3. <u>**Search researchers whoes keywords have more than similarity X as one of those of the researcher Y**</u>
  
  ```java
  List<Integer> numList = numSet.stream()
    .filter(x -> x > 10)
    .sorted((x, y) -> x - y)
    .collect(Collectors.toList());
  ```
  
* <u>**Fully implement the functional interfaces in the class `StatisticalInformationAction` and utilize them to implement the new method `processStatisticalInformationActionByLambda` to support the following criteria.**</u>
  
  1. <u>**Obtain the average number of papers published by researchers per year**</u>
  2. <u>**Obtain the journals that receive the most papers every year**</u>
  
  ```java
  int numCount = numSet.stream().map(Integer::toString).distinct().count();
  ```

* <u>**Rewrite the methods `searchCommentByPaperObjID`, `searchCommentByCommentObjID` and `searchLabelByPaperID` with Lambda expressions and implement them as `searchCommentByPaperObjIDByLambda`, `searchCommentByCommentObjIDByLambda` and `searchLabelByPaperIDByLambda`, respectively.**</u> 
* **<u>Implement a custom ArrayList in `CustomArrayList()` and test it by yourself in `src/test/java/hk.ust.comp3021.utils/ TestCustomArrayList.java`. You only need to test the methods in `CustomArrayList` except constructors, with two types `Paper` and `String`.</u>**
  1. <u>**This part is to help you get familiar with Generics.**</u>
  2. <u>**We will grade this part by mutually examining your code.**</u>

## Tips

To convenience the testing and debugging, you can just run the `main` method of `MiniMendely`
to interact with the system.
An example is as follows:

````
Begin to start MiniMendeley...
----------------------------------------------------------------------
MiniMendeley is running...
Initial paper base has been populated!
----------------------------------------------------------------------
Please select the following operations with the corresponding numbers:
  0: Register an account
  1: Search papers
  2: Upload papers
  3: Download papers
  4: Add labels
  5: Add comments
  6: Search papers via Lambda
  7: Sort papers via Lambda
  8: Search researchers via Lambda
  9: Obtain statistical information via Lambda
  10: Exit
----------------------------------------------------------------------
0
Please enter your name.
Yibo
Create the account with the name: Yibo
Account created!
----------------------------------------------------------------------
Please select the following operations with the corresponding numbers:
  0: Register an account
  1: Search papers
  2: Upload papers
  3: Download papers
  4: Add labels
  5: Add comments
  6: Search papers via Lambda
  7: Sort papers via Lambda
  8: Search researchers via Lambda
  9: Obtain statistical information via Lambda
  10: Exit
----------------------------------------------------------------------
6
Please specify the search kind:
  1: Search by ID
  2: Search by title
  3: Search by author
  4: Search by journal
3
Please specify the search word:
Wonhyuk Choi
Paper found! The paper IDs are as follows:
Choi2022
----------------------------------------------------------------------
Please select the following operations with the corresponding numbers:
  0: Register an account
  1: Search papers
  2: Upload papers
  3: Download papers
  4: Add labels
  5: Add comments
  6: Search papers via Lambda
  7: Sort papers via Lambda
  8: Search researchers via Lambda
  9: Obtain statistical information via Lambda
  10: Exit
----------------------------------------------------------------------
7
Please specify the sort base:
  1: Sort by ID
  2: Sort by title
  3: Sort by author
  4: Sort by journal
1
Please specify the sort kind:
  1: Sort in ascending order
  2: Sort in descending order
1
Paper sorted! The paper are sorted as follows:
...
@article{Xie2016,
   abstract = {Loops are challenging structures for program analysis, especial-ly when loops contain multiple paths with complex interleaving executions among these paths. In this paper, we first propose a classification of multi-path loops to understand the complexity of the loop execution, which is based on the variable updates on the loop conditions and the execution order of the loop paths. Second-ly, we propose a loop analysis framework, named Proteus, which takes a loop program and a set of variables of interest as inputs and summarizes path-sensitive loop effects on the variables. The key contribution is to use a path dependency automaton (PDA) to capture the execution dependency between the paths. A DFS-based algorithm is proposed to traverse the PDA to summarize the effect for all feasible executions in the loop. The experimental results show that Proteus is effective in three applications: Proteus can 1) compute a more precise bound than the existing loop bound analysis techniques; 2) significantly outperform state-of-the-art tools for loop verification; and 3) generate test cases for deep loops within one second, while KLEE and Pex either need much more time or fail.},
   author = {Xiaofei Xie and Bihuan Chen and Yang Liu and Wei Le and Xiaohong Li},
   doi = {10.1145/2950290.2950340},
   journal = {Proceedings of the ACM SIGSOFT Symposium on the Foundations of Software Engineering},
   keywords = {Disjunctive Summary,Loop Summarization},
   title = {Proteus: Computing disjunctive loop summary via path dependency analysis},
   year = {2016},
}
...
----------------------------------------------------------------------
Please select the following operations with the corresponding numbers:
  0: Register an account
  1: Search papers
  2: Upload papers
  3: Download papers
  4: Add labels
  5: Add comments
  6: Search papers via Lambda
  7: Sort papers via Lambda
  8: Search researchers via Lambda
  9: Obtain statistical information via Lambda
  10: Exit
----------------------------------------------------------------------
8
Please specify the search kind:
  1: Search researchers who publish papers more than X times in the recent Y years
  2: Search researchers whose papers published in the journal X have abstracts more than Y words
  3: Search researchers whoes keywords have more than similarity X as one of those of the researcher Y
Please specify the X:
3
Please specify the Y:
10
Researcher found! The researcher information is as follows:
...
@article{Xie2019,
   abstract = {Analyzing loops is very important for various software engineering tasks such as bug detection, test case generation and program optimization. However, loops are very challenging structures for program analysis, especially when (nested) loops contain multiple paths that have complex interleaving relationships. In this paper, we propose the path dependency automaton (PDA) to capture the dependencies among the multiple paths in a loop. Based on the PDA, we first propose a loop classification to understand the complexity of loop summarization. Then, we propose a loop analysis framework, named Proteus, which takes a loop program and a set of variables of interest as inputs and summarizes path-sensitive loop effects (i.e., disjunctive loop summary) on the variables of interest. An algorithm is proposed to traverse the PDA to summarize the effect for all possible executions in the loop. We have evaluated Proteus using loops from five open-source projects and two well-known benchmarks and applying the disjunctive loop summary to three applications: loop bound analysis, program verification and test case generation. The evaluation results have demonstrated that Proteus can compute a more precise bound than the existing loop bound analysis techniques; Proteus can significantly outperform the state-of-the-art tools for loop program verification; and Proteus can help generate test cases for deep loops within one second, while symbolic execution tools KLEE and Pex either need much more time or fail.},
   author = {Xiaofei Xie and Bihuan Chen and Liang Zou and Yang Liu and Wei Le and Xiaohong Li},
   doi = {10.1109/TSE.2017.2788018},
   journal = {IEEE Transactions on Software Engineering},
   keywords = {Disjunctive loop summary,path dependency automaton,path interleaving},
   title = {Automatic Loop Summarization via Path Dependency Analysis},
   year = {2019},
}
...
----------------------------------------------------------------------
Please select the following operations with the corresponding numbers:
  0: Register an account
  1: Search papers
  2: Upload papers
  3: Download papers
  4: Add labels
  5: Add comments
  6: Search papers via Lambda
  7: Sort papers via Lambda
  8: Search researchers via Lambda
  9: Obtain statistical information via Lambda
  10: Exit
----------------------------------------------------------------------
9
Please specify the information:
  1: Obtain the average number of papers published by researchers per year
  2: Obtain the journals that receive the most papers every year
1
Information Obtained! The information is as follows:
...
Nurit Dor: 1.0
Byron Cook: 2.0
Mooly Sagiv: 1.3333333333333333
N. Rinetzky: 1.0
Thanh Vu Nguyen: 1.0
...
````

Lastly, it should be noting that your code will be tested by running our testcases rather than testing via the console manually. Therefore, you should make sure that: 

* your code can be complied succesfully;
* your implementation can pass the public testcases we provided in `src/test`. However, passing all the public testcases does not mean that you can obtain the full mark for the PA. We will also provide many additional testcases as the hidden ones, which are different from the ones we provided in this skeleton.
* your implementation should not yield too many errors when running `./gradlew checkstyleMain`.

If you have any question on the PA2, please email Yibo Jin via [yjinbd@cse.ust.hk](mailto:yjinbd@cse.ust.hk)

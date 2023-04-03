package hk.ust.comp3021.utils;

import hk.ust.comp3021.action.SearchPaperAction;
import hk.ust.comp3021.action.SearchPaperAction.SearchPaperKind;
import hk.ust.comp3021.person.User;
import hk.ust.comp3021.resource.Paper;
import hk.ust.comp3021.utils.TestKind;
import hk.ust.comp3021.MiniMendeleyEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestCustomArrayList {
    @Tag(TestKind.PUBLIC)
    @Test
    void testCustomArrayListWithTypePaper() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();

        CustomArrayList<Paper> paperList = new CustomArrayList<>();
        assertEquals(0, paperList.size());

        Paper firstPaper = engine.getPaperBase().get("Author2022");
        Paper falsePaper = engine.getPaperBase().get("Feng2017");
        paperList.add(firstPaper);
        assertEquals(false, paperList.isEmpty());
        assertEquals(1, paperList.size());
        assertEquals(true, paperList.contains(firstPaper));
        assertEquals(false, paperList.contains(falsePaper));
        assertEquals("Author2022", paperList.get(0).getPaperID());
        assertEquals("eTainter: Detecting Gas-Related Vulnerabilities in Smart Contracts", paperList.get(0).getTitle());
        assertEquals(null, paperList.get(-1));
        assertEquals(null, paperList.get(1));


        Paper secondPaper = engine.getPaperBase().get("Choi2022");
        Paper thirdPaper = engine.getPaperBase().get("Liu2022");
        Paper fourthPaper = engine.getPaperBase().get("Guo2022");
        Paper fifthPaper = engine.getPaperBase().get("Zhou2022");
        Paper sixthPaper = engine.getPaperBase().get("Kellogg2022");
        Paper seventhPaper = engine.getPaperBase().get("Laddad2022");
        paperList.add(secondPaper);
        paperList.add(thirdPaper);
        paperList.add(fourthPaper);
        paperList.add(fifthPaper);
        paperList.add(sixthPaper);
        paperList.add(seventhPaper);
        assertEquals(false, paperList.isEmpty());
        assertEquals(7, paperList.size());
        assertEquals(true, paperList.contains(thirdPaper));
        assertEquals(true, paperList.contains(sixthPaper));
        assertEquals(true, paperList.contains(seventhPaper));
        assertEquals(false, paperList.contains(falsePaper));
        assertEquals("Liu2022", paperList.get(2).getPaperID());
        assertEquals("Leveraging Application Data Constraints to OptimizeDatabase-Backed Web Applications", paperList.get(2).getTitle());
        assertEquals("Kellogg2022", paperList.get(5).getPaperID());
        assertEquals("Accumulation Analysis", paperList.get(5).getTitle());
        assertEquals("Laddad2022", paperList.get(6).getPaperID());
        assertEquals("Katara: synthesizing CRDTs with verified lifting", paperList.get(6).getTitle());
        assertEquals(null, paperList.get(-1));
        assertEquals(null, paperList.get(7));
    }
    
    @Tag(TestKind.PUBLIC)
    @Test
    void testCustomArrayListWithTypeString() {
        MiniMendeleyEngine engine = new MiniMendeleyEngine();

        CustomArrayList<String> stringList = new CustomArrayList<>();
        assertEquals(0, stringList.size());

        stringList.add("first element");
        assertEquals(false, stringList.isEmpty());
        assertEquals(1, stringList.size());
        assertEquals(true, stringList.contains("first element"));
        assertEquals(false, stringList.contains("element"));
        assertEquals("first element", stringList.get(0));
        assertEquals(null, stringList.get(-1));
        assertEquals(null, stringList.get(1));

        stringList.add("second element");
        stringList.add("third element");
        stringList.add("fourth element");
        stringList.add("fifth element");
        stringList.add("sixth element");
        stringList.add("seventh element");
        assertEquals(false, stringList.isEmpty());
        assertEquals(7, stringList.size());
        assertEquals(true, stringList.contains("third element"));
        assertEquals(true, stringList.contains("sixth element"));
        assertEquals(true, stringList.contains("seventh element"));
        assertEquals(false, stringList.contains("sixth"));
        assertEquals("third element", stringList.get(2));
        assertEquals("sixth element", stringList.get(5));
        assertEquals("seventh element", stringList.get(6));
        assertEquals(null, stringList.get(-1));
        assertEquals(null, stringList.get(7));
    }
}
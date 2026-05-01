package main;

import lib.SysmlParser;
import lib.SysmlLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        try {
//            String inputFileName = "Java/src/data/FGS_NLP.txt";
//            String inputFileName = "Java/src/data/Coffeemaker_NLP.txt";
//            String inputFileName = "Java/src/data/ActiveStandby_NLP.txt";
//            String inputFileName = "Java/src/data/HairDryer_NLP.txt";
            String inputFileName = "Java/src/data/VaccuumCleaner_NLP.txt";

            String filename = inputFileName.split("/")[3].split(".txt")[0];
            String inputFile = readInputFile(inputFileName);
            String input = cleanText(inputFile);

            ANTLRInputStream inputStream = new ANTLRInputStream(input);
            SysmlLexer sysmlLexer = new SysmlLexer((inputStream));
            CommonTokenStream tokenStream = new CommonTokenStream(sysmlLexer);
            SysmlParser sysmlParser = new SysmlParser(tokenStream);
            Visitor visitor = new Visitor();
            visitor.sentences = new Sentences();
            visitor.visit(sysmlParser.nlparch());

//          Create AADL Model
            CreateAADL outputAADL_Formatter = new CreateAADL(visitor.sentences, true);
            outputAADL_Formatter.generateOutput(filename);

//          Create SysML Model
            CreateSysML outputSysML_Formatter= new CreateSysML(visitor.sentences , true);
            outputSysML_Formatter.generateOutput(filename);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String readInputFile(String path) throws FileNotFoundException {
        File NLP_Output = new File(path);
        Scanner sc = new Scanner(NLP_Output);
        String text = "";
        while(sc.hasNextLine()){
            String s = sc.nextLine();
            text += s+"\n";
        }
        return text;
    }
    public static String cleanText(String s){
        String keywordBlackList[] = {};
        for(int i=0; i< keywordBlackList.length; i++){
            s = s.replaceAll(keywordBlackList[i], "");
        }

        return s;
    }
}
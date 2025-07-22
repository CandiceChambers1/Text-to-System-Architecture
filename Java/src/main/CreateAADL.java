package main;
import java.util.ArrayList;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.File;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


public class CreateAADL {
    Sentences sentences;
    boolean debug;
    AADL_Component components = new AADL_Component();

    public CreateAADL(Sentences sentences, boolean debug) {
        this.sentences = sentences;
        this.debug = debug;
    }

    public void generateTree() {
        int c = 0;
        for (Sentence s : sentences.sentences) {
            if (Objects.equals(s.sentenceType, "Structural")) {
                if (s.isInternal) {
//                    System.out.println("Internal sentence");
                    for (String name : s.structNouns) {
                        components.createSubcomponent(name, s.structNoun, "property");
                    }
                } else if (s.isPort) {
                    for (String name : s.structNouns) {
                        components.createFeature(name, s.structNoun);
                    }
                } else {
//                    System.out.println("Noun sentence: " + s.structNoun + " " + s.structNouns);
                    for (String name : s.structNouns) {
                        components.createSubcomponent(name, s.structNoun, "block");
                    }
                }
            }
        }
        for (Sentence s : sentences.sentences) {
            if (Objects.equals(s.sentenceType, "Connection")) {
                String src, dest;
                ArrayList<String> nouns = s.structNouns;
                for (int i = 0; i < nouns.size(); i += 2) {
                    src = nouns.get(i);
                    if (i + 1 < nouns.size()) {
                        dest = nouns.get(i + 1);
                        for (Sentence s1 : sentences.sentences) {
                            if (s1.isInternal) {
                                if (s1.structNouns.contains(s.structNoun) && s1.structNouns.contains(s.connectionNoun)) {
                                    System.out.println("First: "+ s.structNoun + "\t" + s1.structNoun + "\t" + s.connectionNoun + "\t" + src + "\t" + dest);
                                    components.createConnection(s1.structNoun, s.structNoun, s.connectionNoun,src, dest);
//                                    break;
                                } else if (s1.structNoun.equals(s.structNoun) && s1.structNouns.contains(s.connectionNoun)) {
//                                    System.out.println("Second "+ s.structNoun + "\t" + s1.structNoun + "\t" + s.connectionNoun + "\t" + src + "\t" + dest);
//                                    components.createConnection(s1.structNoun, s.structNoun, s.connectionNoun, src, dest);
//                                    break;
                                } else if (s1.structNouns.contains(s.structNoun) && s1.structNoun.equals(s.connectionNoun)) {
//                                    System.out.println("Third: "+s.structNoun + "\t" + s1.structNoun + "\t" + s.connectionNoun);
//                                    components.createConnection(s1.structNoun, s.structNoun, s.connectionNoun, src, dest);
//                                    break;
                                } else {
//                                    System.out.println("Never");

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void generateOutput(String filename) throws ParserConfigurationException, TransformerException {
        generateTree();

        String output = "";

        output += generateInstantiation();
        output += generateFeature();

//        System.out.println(output);
        try {
            String filePath = "./src/gen/aadl/aadl/" + filename + "_Automated.aadl";

            // Ensure the directory exists
            File file = new File(filePath);
            file.getParentFile().mkdirs();  // Create parent directories if they do not exist

            FileWriter myWriter = new FileWriter(file);
            myWriter.write(output);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    // Generate Features
    public String generateFeature(){

        StringBuilder output = new StringBuilder();

        for (Sentence s : sentences.sentences) {
            if (Objects.equals(s.sentenceType, "Structural")) {
                if (s.isPort) {
                    output.append("system ").append(s.structNoun).append("\n").append("\tfeatures\n");
                    for (Features f : components.features) {
                        if (Objects.equals(s.structNoun, f.ownerName)) {
                            output.append("\t\t").append(f.name).append(" : in out data port;\n");
                        }
                    }
                    output.append("end ").append(s.structNoun).append(";\n\n");
                }
            }
        }
        return output.toString();
    }

    // Generate Instantiation
    public String generateInstantiation() {
        StringBuilder output = new StringBuilder();

        for (Sentence s : sentences.sentences) {
            if (Objects.equals(s.sentenceType, "Structural")) {
                if (s.isInternal && !s.isPort) {
                    output.append("system implementation ").append(s.structNoun).append(".impl\n");
                    output.append("\tsubcomponents\n");
                        for(Subcomponents sub: components.subcomponents){
                            if (Objects.equals(s.structNoun, sub.ownerName)) {
                                output.append(generateSubcomponents(sub.name));
                            }
                        }
                    output.append(generateConnections(s.structNoun));
                }else if (!s.isPort){
                    output.append("package ").append(s.structNoun).append("_AADL\n");
                    output.append("public\n\n");

                    output.append("system ").append(s.structNoun).append("\n");
                    output.append("end ").append(s.structNoun).append(";\n\n");

                    output.append("system implementation ").append(s.structNoun).append(".impl\n");
                    output.append("\tsubcomponents\n");
                    for (Subcomponents sub: components.subcomponents){
                        if ((Objects.equals(s.structNoun, sub.ownerName) && (Objects.equals(sub.type,"block")))) {
                            output.append(generateSubcomponents(sub.name));
                        }else{
                            break;
                        }
                    }
                    output.append(generateConnections(s.structNoun));
                }
            }
        }
        return output.toString();
 }
    // Generate subcomponents
    public String generateSubcomponents(String name) {
        String output = "";
        output += "\t\tthis_" + name +": system " + name + ".impl;\n";
        return output;
    }

    // Generate connections
    public String generateConnections(String name) {
      StringBuilder output = new StringBuilder();
      output.append("\tconnections\n");

      int count = 0;
      String ownerName = "";
      String ownerFirstName = "";

      for (Connections con : components.connections) {
//          System.out.println(components.getSubcomponentType(con.srcBlockName));
          ownerFirstName = con.ownerName;
          ownerName = components.getSubcomponentOwnerName(con.srcBlockName);
          if (ownerName.equals(name)) {
              output.append("\t\t").append(ownerName).append(count).append(": port this_").append(con.srcBlockName).append(".").append(con.srcPortName).append("-> this_").append(con.destBlockName).append(".").append(con.destPortName).append(";\n");
              count++;
          }else if (ownerFirstName.equals(name)) {
              System.out.println("Hi mama");
              output.append("\t\t").append(ownerFirstName).append(count).append(": port this_").append(con.srcBlockName).append(".").append(con.srcPortName).append("-> this_").append(con.destBlockName).append(".").append(con.destPortName).append(";\n");
              count++;
//              System.out.println("Owner Name: " + ownerName + " Source Block: " + con.srcBlockName);
          }
      }
      output.append("end ").append(name).append(".impl;\n\n");
      return output.toString();
    }
}

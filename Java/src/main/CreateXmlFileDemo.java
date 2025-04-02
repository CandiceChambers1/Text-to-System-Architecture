package main;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
//import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class CreateXmlFileDemo {
    Sentences sentences;
    String modelxmiID;
    String xmiRootID;
    String diagramID;
    boolean debug;
    SysMLComponent components = new SysMLComponent();
    public CreateXmlFileDemo(Sentences sentences, boolean debug){
        this.sentences = sentences;
        this.debug = debug;

    }
    public void generateTree(){
        for(Sentence s : sentences.sentences){
            if (Objects.equals(s.sentenceType, "Structural")) {
                if (s.isInternal) {
                    components.getBlock(s.structNoun).diagramID = generateXMI_ID("other");
                    for (String name: s.structNouns) {
                        if(components.getBlockXMI(name) !=null){
                            components.createProperties(name, generateXMI_ID("other"), s.structNoun,
                                    components.getBlockXMI(s.structNoun), generatePropertyTypeID(components.getBlockXMI(name)));
                            components.getBlock(name).diagramID = generateXMI_ID("other");
//                            if(debug)
//                                System.out.println("Property: "+name);
                        }
                    }
                } else if (s.isPort) {
                    for (String name: s.structNouns) {
//                        System.out.println("Name: " + name+ " " + components.getBlockXMI(s.structNoun));
                        components.createPorts(name, generateXMI_ID("other"), components.getBlockXMI(s.structNoun));
//                        if(debug)
//                            System.out.println("Ports: "+name);

                    }
                } else {
                    for (String name: s.structNouns) {
                        components.createBlock(name, generateXMI_ID("other"));
//                        if(debug)
//                            System.out.println("Block: "+name);
                    }
                }
            }
            if (Objects.equals(s.sentenceType, "Instantiation")) {
                if(components.getBlockXMI(s.structNouns.get(0)) !=null){
//                    System.out.println(s.structNoun +" "+ components.getBlock(sentences.getSentenceByTypeChild("Structural",s.structNoun).structNoun).xmiID+ " "+
//                            components.getBlockXMI(sentences.getSentenceByTypeChild("Structural",s.structNoun).structNoun));
                    components.createProperties(s.structNoun, generateXMI_ID("other"), components.getBlock(sentences.getSentenceByTypeChild("Structural",s.structNoun).structNoun).name,
                            components.getBlockXMI(sentences.getSentenceByTypeChild("Structural",s.structNoun).structNoun),
                            generatePropertyTypeID(components.getBlockXMI(s.structNouns.get(0))));

//                    System.out.println("\n");
//                    if(debug)
//                        System.out.println("Property: "+s.structNoun);
                }
            }
        }
    }
    public void generateOutput(String filename) throws ParserConfigurationException, TransformerException {

        /*
            Looping through the sentences and call the appropriate functions
         */

        // Generates the package XMI ID
        String xmiPackageID = generateXMI_ID("package");

        // Creating the XML document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        // Root element Initialized
        Element rootElement = doc.createElement("XMI");
        doc.appendChild(rootElement);
        generateAttribute(doc, rootElement, "xmi.version", "1.1");
        generateAttribute(doc,rootElement,"xmlns:UML","omg.org/UML1.3");



        // Generate the Header and the namespaceContent
        generateHeader(doc, rootElement);
        Element content = generateElement(doc,rootElement,"XMI.content", "");
        Element namespaceContent = generateStartContent(doc, content, xmiPackageID);

        // Generate the Tree based on the input sentences
        // Generates blocks, ports, properties but not instantiations
        generateTree();

        // Generating the XML Code for the SysML Blocks
        for (Block b : components.blocks) {
            System.out.println("Block: " + b.name + " XMI: " + b.xmiID);
                generateBlock(doc, namespaceContent, b.name, xmiPackageID, b.xmiID,b.diagramID,sentences.getSentenceByStructNoun(b.name).isInternal);
//          if(debug)
//                System.out.println("Generated Block: "+b.name + " " + sentences.getSentenceByStructNoun(b.name).isInternal);
        }

        // Generating the XML Code for SysML Ports
        for (Port p : components.ports) {
            generatePort(doc, namespaceContent, p.name, p.xmiID , p.ownerXMI, xmiPackageID);
//            if(debug)
//                System.out.println("Generated Ports: "+p.name);
        }

        // Generating Classifier Role Blocks
        Element collaboration = generateStartCollaboration(doc, namespaceContent);
        for (Property p : components.properties) {
            generateClassifier_Property(doc, collaboration, p.name, p.xmiID, xmiPackageID, p.ownerXMI, p.propertyType);
//            if (debug)
//                System.out.println("Generated Classifier Role: "+ p.name);
        }


        // Creating the Ports for Classifier Roles
        Sentence portSentence;
        for (Property p : components.properties) {
            portSentence = sentences.getSentenceByTypePort("Structural", p.name, true);
            Sentence is = sentences.getSentenceByTypeName("Instantiation", p.name);
            // If the properties are blocks (instantiate themselves)
            if (portSentence != null){
                for (String portName : portSentence.structNouns) {
                    components.createPortProperties(portName, generateXMI_ID("other"), p.name, p.xmiID,
                            generatePropertyTypeID(components.getPortXMI(portName, components.getBlockXMI(p.name))));
                }
            } else {
                // If the properties aren't block (instantiate other blocks)
                portSentence = sentences.getSentenceByTypePort("Structural", is.structNouns.get(0),true);
                if (portSentence !=null)
                    for (String portName: portSentence.structNouns){
                        for(Port port: components.ports){
                            if(Objects.equals(portName, port.name)&& Objects.equals(port.ownerXMI, components.getBlockXMI(portSentence.structNoun))){
//                                System.out.println("Property: " +p.name + " Property XMI: " + p.xmiID);
//                                System.out.println("Property Owner: " + components.getPropertyByXMI(p.xmiID).ownerXMI);
//                                System.out.println("Port Name: " + portName);
//                                System.out.println("Port: "+ port.name + " Port XMI: "+ port.xmiID);
                                components.createPortProperties(portName, generateXMI_ID("other"), p.name,p.xmiID,
                                        generatePropertyTypeID(port.xmiID));
                            }
//                            System.out.println(port.name);
                        }
//                        System.out.println("Property: " +p.name + " Property XMI: " + p.xmiID);
//                        System.out.println("Property Owner: " + components.getPropertyByXMI(p.xmiID).ownerXMI);
//                        System.out.println(("Port Name: " + portName));
//
//                        System.out.println(components.getPortXMI(portName,p.ownerXMI));
//
//                        components.createPortProperties(portName, generateXMI_ID("other"), p.name,p.xmiID,
//                                generatePropertyTypeID(components.getPortXMI(portName,p.ownerXMI)));
                    }
            }
        }

        // Generating Ports for the Classifier Roles
        for (PortProperty pp : components.portProperties){
            generatePortProperty(doc, namespaceContent, pp.name, pp.xmiID, pp.ownerXMI, xmiPackageID,  pp.reuseProperty);
//            if (debug)
//                System.out.println("Generated Classifier Ports: "+ pp.name);
        }
        int count = 0;
        // Creating the Association for IBDs


//        for(Property property: components.properties){
//            System.out.println("Property: " + property.name + " Property XMI: " + property.xmiID);
//            System.out.println("Owner Name: " + property.ownerName + " Owner XMI: " + property.ownerXMI);
//            System.out.println("\n\n");
//        }

        for(Sentence s: sentences.sentences){
            if(Objects.equals(s.sentenceType, "Connection")){
                String src,dest;
                ArrayList<String> nouns = s.structNouns;
                for (int i = 0; i < nouns.size(); i += 2) {
                    src = nouns.get(i);
                    if (i + 1 < nouns.size()) {
                        dest = nouns.get(i + 1);

                        PortProperty srcPort,destPort;
//                        System.out.println("Source: " + s.structNoun + " Dest: " + s.connectionNoun);
//                        System.out.println(src + " " + dest);

//                        int count = 0;
//                        System.out.println("\n"+ s.structNoun + " " + s.connectionNoun);
                        for(Sentence s1: sentences.sentences){
                            if (s1.isInternal) {
//                                System.out.println(s1.structNoun +"    "+ s1.structNouns);

                                if(s1.structNouns.contains(s.structNoun) && s1.structNouns.contains(s.connectionNoun)){
//                                    System.out.println("Source: "+ src+ " Owner: "+ s.structNoun + " Owner-owner: " + s1.structNoun);
                                    Property sourceProperty = components.getProperty(s.structNoun, s1.structNoun);

//                                    System.out.println(sourceProperty.name);
                                    Property destProperty = components.getProperty(s.connectionNoun, s1.structNoun);
                                    srcPort = components.getPortProperty(src,sourceProperty.xmiID);
                                    destPort = components.getPortProperty(dest,destProperty.xmiID);
//                                    System.out.println("Source Noun: " +s.structNoun+ " Source: " + src + " "+ srcPort.xmiID);
//
//                                    System.out.println("Connection Noun: " + s.connectionNoun +" Destination: " + dest + " ");
                                    components.createAssociation(generateXMI_ID("other"),src,srcPort.xmiID, dest, destPort.xmiID);
//                                    System.out.println("\n\n");
                                    break;
                                } else if (s1.structNoun.equals(s.structNoun) && s1.structNouns.contains(s.connectionNoun)) {
//                                    System.out.println("\nSecond if");

                                    Block srcBlock = components.getBlock(s.structNoun);
                                    Property destProperty = components.getProperty(s.connectionNoun, s1.structNoun);
//                                    srcPort = components.getPort(src,srcBlock.name);
//                                    System.out.println("Source Noun: "+ s1.structNoun +" Connection noun: " + s.connectionNoun);
//                                    System.out.println(destProperty.name + " " + destProperty.xmiID);
                                    destPort = components.getPortProperty(dest,destProperty.xmiID);
//                                    System.out.println(srcBlock.name + " " + destProperty.name);

                                    components.createAssociation(generateXMI_ID("other"),src,components.getPortXMI(src,srcBlock.xmiID), dest, destPort.xmiID);
                                    break;
                                } else if (s1.structNouns.contains(s.structNoun) && s1.structNoun.equals(s.connectionNoun)) {
//                                    System.out.println("\nThird if");
                                    Property sourceProperty = components.getProperty(s.structNoun, s1.structNoun);
                                    Block destBlock = components.getBlock(s.connectionNoun);
                                    srcPort = components.getPortProperty(src,sourceProperty.xmiID);
                                    destPort = components.getPortProperty(dest,destBlock.xmiID);

                                    components.createAssociation(generateXMI_ID("other"),src,srcPort.xmiID, dest, components.getPortXMI(dest,destBlock.xmiID));
//                                    System.out.println(sourceProperty.name + " " + destBlock.name);
                                    break;
                                } else{
//                                    System.out.println("Never");

                                }
                            }
                        }
//

//                        Property sourceProperty = components.getProperty(s.structNoun,);
//                        Property destProperty = components.getProperty(s.connectionNoun, components.getPropertyXMI(s.connectionNoun));
//                        if(components.getPropertyXMI(s.structNoun) != null && components.getPropertyXMI(s.connectionNoun) != null){
////                                System.out.println("Source Property : " + s.structNoun  +" " +components.getPropertyXMI(s.structNoun)+ "           Destination Property " + s.connectionNoun  + " "+ components.getPropertyXMI(s.connectionNoun));
////                                System.out.println("Source Port: " + src + "                         Dest Port: " + dest);
////                                System.out.println("Source Port XMI: " + components.getPortProperty(src,s.structNoun).xmiID );
////                                System.out.println(" \n\n");
//
//                            srcPort = components.getPortProperty(src,s.structNoun);
//                            destPort = components.getPortProperty(dest,s.connectionNoun);
//                            components.createAssociation(generateXMI_ID("other"),src,srcPort.xmiID,dest,destPort.xmiID);
////                            generateAssociation(doc,namespaceContent,src,dest,srcPort.xmiID,destPort.xmiID);
//                        } else if(components.getPropertyXMI(s.structNoun) == null ) {
////                                System.out.println("Source Property : " + s.structNoun  +" " +components.getPortXMI(src)+ "           Destination Property " + s.connectionNoun  + " "+ components.getPropertyXMI(s.connectionNoun));
////                                System.out.println("Source Port: " + src + "                         Dest Port: " + dest);
////                                System.out.println(" \n\n");
//
////                            destPort = components.getPortProperty(dest,s.connectionNoun);
////                            components.createAssociation(generateXMI_ID("other"),src,
////                                    components.getPortXMI(src, components.getBlockXMI(s.structNoun)),dest,destPort.xmiID);
////                            generateAssociation(doc,namespaceContent,src,dest, components.getPortXMI(src), destPort.xmiID);
////                            System.out.println("Nothing");
//                        } else if (components.getPropertyXMI(s.connectionNoun) == null){
////                                System.out.println("Source Property : " + s.structNoun  +" " +components.getPropertyXMI(s.structNoun)+ "           Destination Property " + s.connectionNoun  + " "+ components.getPropertyXMI(s.connectionNoun));
////                                System.out.println("Source Port: " + src + "                         Dest Port: " + dest);
////                                System.out.println(" \n\n");
//                            srcPort = components.getPortProperty(src,s.structNoun);
//                            components.createAssociation(generateXMI_ID("other"),src,srcPort.xmiID,dest,
//                                    components.getPortXMI(dest, components.getBlockXMI(s.connectionNoun)));
////                            generateAssociation(doc,namespaceContent,src,dest,srcPort.xmiID, components.getPortXMI(s.connectionNoun));
//                        }
                    }
                }
            }
        }

        // Generate the Associations
        for (Association a: components.association){
            generateAssociation(doc,namespaceContent,a.xmiID,a.srcName,a.destName,a.srcXMI,a.destXMI);
        }

//        generateFinalContent();
        for (Block b : components.blocks) {
                generateEncapsulation(doc,content,generateXMI_ID("other"),b.xmiID);
            }

        generateDiagram(doc,content,generateXMI_ID("other"),xmiPackageID);
        generateFooter(doc,rootElement);

        // write the content into gen.sysml.xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        String name= filename;
        StreamResult result = new StreamResult(new File("src/gen/sysml/xml/" + name +".xml"));
        transformer.transform(source, result);

//         Output to console for testing
//        StreamResult consoleResult = new StreamResult(System.out);
//        transformer.transform(source, consoleResult);


    }

    private String generatePropertyTypeID(String xmiID) {
        String ID = "";

        String tokens[] = xmiID.split("_");

        ID = tokens[1]+"-"+tokens[2]+"-"+tokens[3]+"-"+tokens[4]+"-"+tokens[5];
        return ID;
    }

    public String generateXMI_ID(String type){
        String ID;
        Random rand = new Random();
        if(Objects.equals(type, "package")) {
            ID = "EAPK_" + (rand.nextInt(99999999-10000000) + 10000000) + "_" + (rand.nextInt(9999-1000)
                    +1000)+ "_" + (rand.nextInt(9999-1000) +1000) + "_" + (rand.nextInt(9999-1000) +1000)
                    + "_" + (rand.nextInt(999999-100000) + 100000) + (rand.nextInt(999999-100000) + 100000);

        }else if (Objects.equals(type,"collab")){
            ID = "EAPK_" + (rand.nextInt(99999999-10000000) + 10000000) + "_" + (rand.nextInt(9999-1000)
                    +1000)+ "_" + (rand.nextInt(9999-1000) +1000) + "_" + (rand.nextInt(9999-1000) +1000)
                    + "_" + (rand.nextInt(999999-100000) + 100000) + (rand.nextInt(999999-100000) + 100000)+ "_Collaboration";
        } else{

            ID = "EAID_"+ (rand.nextInt(99999999-10000000) + 10000000) + "_" + (rand.nextInt(9999-1000)
                    +1000)+ "_" + (rand.nextInt(9999-1000) +1000) + "_" + (rand.nextInt(9999-1000) +1000)
                    + "_" + (rand.nextInt(999999-100000) + 100000) + (rand.nextInt(999999-100000) + 100000);
        }
        return ID;
    }
    public static Element generateElement(Document doc, Element elem, String sub, String text){
        Element element = doc.createElement(sub);
        elem.appendChild(element);

        if(text.isEmpty()){
            elem.appendChild(element);
        }else {
            element.appendChild(doc.createTextNode(text));
            elem.appendChild(element);
        }

        return element;
    }

    public static void generateAttribute(Document doc, Element elem, String sub, String text){
        Attr attr = doc.createAttribute(sub);
        attr.setValue(text);
        elem.setAttributeNode(attr);
    }

    public void generateHeader(Document doc, Element root){
        Element header = generateElement(doc, root, "XMI.header", "");
        Element documentation = generateElement(doc, header, "XMI.documentation", "");
        generateElement(doc, documentation, "XMI.exporter","Enterprise Architect");
        generateElement(doc, documentation,"XMI.exporterVersion", "2.5");
    }
    public void generateFooter(Document doc, Element root){
        generateElement(doc,root,"XMI.difference", "");
        Element extension = generateElement(doc, root,"XMI.extensions", "");
        generateAttribute(doc,extension,"xmi.extender","Enterprise Architect 2.5");
    }
    public Element generateStartContent(Document doc, Element content, String xmiPackageID){

        modelxmiID = generateXMI_ID("other");
        xmiRootID = generateXMI_ID("other");

        Element model = generateElement(doc,content,"UML:Model","");
        generateAttribute(doc,model,"name","EA Model");
        generateAttribute(doc,model,"xmi.id", "MX_"+modelxmiID);

        Element namespace = generateElement(doc,model,"UML:Namespace.ownedElement","");
        Element umlClass = generateElement(doc,namespace,"UML:Class","");
        generateAttribute(doc,umlClass,"name","EARootClass");
        generateAttribute(doc,umlClass,"xmi.id",xmiRootID);

        Element umlPackage = generateElement(doc,namespace,"UML:Package","");
        generateAttribute(doc,umlPackage,"name","One Level Block Hierarchy");
        generateAttribute(doc,umlPackage,"xmi.id",xmiPackageID);

        return generateElement(doc,umlPackage,"UML:Namespace.ownedElement","");

    }
    public void generatePort(Document doc, Element namespace, String noun,String xmiID,String xmiOwnerID, String xmiPackageID){
        Element umlClass = generateElement(doc,namespace,"UML:Class","");
        generateAttribute(doc,umlClass,"name",noun);
        generateAttribute(doc,umlClass,"xmi.id",xmiID);
        generateAttribute(doc,umlClass,"namespace",xmiPackageID);

        Element modelElementTag = generateElement(doc,umlClass,"UML:ModelElement.taggedValue","");

        Element tag_1 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_1,"tag","ea_stype");
        generateAttribute(doc,tag_1,"value","Port");

        Element tag_2 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_2,"tag","package");
        generateAttribute(doc,tag_2,"value",xmiPackageID);

        Element tag_3 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_3,"tag","owner");
        generateAttribute(doc,tag_3,"value",xmiOwnerID);

        Element tag_4 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_4,"tag","package_name");
        generateAttribute(doc,tag_4,"value","One Level Block Hierarchy");

    }
    public void generatePortProperty(Document doc, Element namespace, String noun,String xmiID,String xmiOwnerID, String xmiPackageID, String reuseProperty){
        Element umlClass = generateElement(doc,namespace,"UML:Class","");
        generateAttribute(doc,umlClass,"name",noun);
        generateAttribute(doc,umlClass, "xmi.id",xmiID);
        generateAttribute(doc,umlClass,"namespace",xmiPackageID);


        Element modelElementTag = generateElement(doc,umlClass,"UML:ModelElement.taggedValue","");
        Element tag_1 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_1,"tag","ea_stype");
        generateAttribute(doc,tag_1,"value","Port");

        Element tag_2 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_2,"tag","package");
        generateAttribute(doc,tag_2,"value",xmiPackageID);

        Element tag_3 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_3,"tag","owner");
        generateAttribute(doc,tag_3,"value",xmiOwnerID);

        Element tag_4 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_4,"tag","package_name");
        generateAttribute(doc,tag_4,"value","One Level Block Hierarchy");

        Element tag_5 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_5,"tag","reusesProperty");
        generateAttribute(doc,tag_5,"value","{"+reuseProperty+"}");
    }
    public void generateBlock(Document doc, Element namespace, String noun, String xmiPackageID, String xmiID, String diagramID, boolean isInternal ){

        Element umlClass = generateElement(doc,namespace,"UML:Class", "");
        generateAttribute(doc,umlClass,"name",noun);
        generateAttribute(doc,umlClass,"xmi.id",xmiID);
        generateAttribute(doc,umlClass,"namespace",xmiPackageID);

        Element modelElementStereo = generateElement(doc,umlClass,"UML:ModelElement.stereotype","");
        Element stereotype = generateElement(doc,modelElementStereo,"UML:Stereotype","");
        generateAttribute(doc,stereotype,"name","block");

        Element modelElementTag = generateElement(doc,umlClass,"UML:ModelElement.taggedValue","");

        Element tag_1 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_1,"tag","ea_stype");
        generateAttribute(doc,tag_1,"value","Class");

        Element tag_2 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_2,"tag","package");
        generateAttribute(doc,tag_2,"value",xmiPackageID);

        Element tag_3 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_3,"tag","package_name");
        generateAttribute(doc,tag_3,"value","One Level Block Hierarchy");

        Element tag_4 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
        generateAttribute(doc,tag_4,"tag","stereotype");
        generateAttribute(doc,tag_4,"value","block");

        if(isInternal){
            Element tag_5 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
            generateAttribute(doc,tag_5,"tag","ea_ntype");
            generateAttribute(doc,tag_5,"value","8");

//            diagramID = generateXMI_ID("other");
            Element tag_6 = generateElement(doc,modelElementTag,"UML:TaggedValue","");
            generateAttribute(doc,tag_6,"tag","diagram");
            generateAttribute(doc,tag_6,"value",diagramID);

            Element tag_7 = generateElement(doc,modelElementTag, "UML:TaggedValue", "");
            generateAttribute(doc,tag_7,"tag","$ea_xref_property");
            generateAttribute(doc,tag_7,"value", "$XREFPROP=$XID={FFA1F5BF-8E6E-4d74-B245-DDDF2BD5BEE9}$XID;$NAM=DefaultDiagram$NAM;$TYP=element property$TYP;$SUP={" + generatePropertyTypeID(diagramID) + "}$SUP;$ENDXREF;");

        }
    }

    public void generateAssociation(Document doc, Element namespace, String xmiID, String srcNoun,String destNoun, String srcXmiId, String destXmiId){
//        String xmiId = generateXMI_ID("other");
        Element association = generateElement(doc,namespace,"UML:Association","");
        generateAttribute(doc,association,"xmi.id",xmiID);

        Element model = generateElement(doc,association,"UML:ModelElement.taggedValue","");
        Element tag_1 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_1,"tag","ea_type");
        generateAttribute(doc,tag_1,"value","Connector");

        Element tag_2 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_2,"tag","direction");
        generateAttribute(doc,tag_2,"value","Source -> Destination");

        Element tag_3 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_3,"tag","ea_sourceName");
        generateAttribute(doc,tag_3,"value",srcNoun);

        Element tag_4 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_4,"tag","ea_targetName");
        generateAttribute(doc,tag_4,"value",destNoun);

        Element tag_5 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_5,"tag","ea_sourceType");
        generateAttribute(doc,tag_5,"value","Port");

        Element tag_6 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_6,"tag","ea_targetType");
        generateAttribute(doc,tag_6,"value","Port");

        Element associationConnection = generateElement(doc,association,"UML:Association.connection","");
        Element associationEnd_1 = generateElement(doc,associationConnection,"UML:AssociationEnd","");
        generateAttribute(doc,associationEnd_1,"type",srcXmiId);

        Element associationEnd_2 = generateElement(doc,associationConnection,"UML:AssociationEnd","");
        generateAttribute(doc,associationEnd_2,"type",destXmiId);

    }
    public String generateIBD_Block(String xmiIdIBD) {
        String append_output = "\t\t\t<UML:TaggedValue tag=\"ea_ntype\" value=\"8\"/>\n" +
                "\t\t\t<UML:TaggedValue tag=\"ea_ntype\" value=\"8\"/>\n" +
                "\t\t\t<UML:TaggedValue tag=\"diagram\" value=\"" + xmiIdIBD + "\"/>\n" +
                "\t\t\t<UML:TaggedValue tag=\"$ea_xref_property\" value=\"\"$XREFPROP=$XID={" +
                "number {001EC013-2161-4f09-8362-A1A5114E8E8A}$XID}\"};$NAM=DefaultDiagram$NAM;$TYP=element property$TYP;$SUP={" +
                "number: {9E656A80-A25F-4ae3-A153-749CEFFFA468}\"}$SUP;$ENDXREF;\"/>\n";

        return append_output;
    }
    public Element generateStartCollaboration(Document doc, Element namespace){

        Element collaboration = generateElement(doc,namespace,"UML:Collaboration", "");
        generateAttribute(doc,collaboration,"xmi.id",modelxmiID+"_Collaboration");
        generateAttribute(doc,collaboration,"name","Collaborations");
        generateEndCollaboration(doc, collaboration);

        return generateElement(doc,collaboration,"UML:Namespace.ownedElement","");
    }

    public void generateEndCollaboration(Document doc, Element collaboration){
        Element collaborationInter = generateElement(doc,collaboration,"UML:Collaboration.interaction","");
        }
    public void generateClassifier_Property(Document doc, Element collaboration, String noun, String xmiID, String xmiPackageID, String xmiOwnerID, String xmiPropertyTypeID) {

        Element classifier =  generateElement(doc,collaboration,"UML:ClassifierRole","");
        generateAttribute(doc,classifier,"name",noun);
        generateAttribute(doc,classifier,"xmi.id",xmiID);
        generateAttribute(doc,classifier,"base",xmiRootID);

        Element model = generateElement(doc,classifier,"UML:ModelElement.taggedValue","");

        Element tag_1 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_1,"tag","ea_stype");
        generateAttribute(doc,tag_1,"value","Part");

        Element tag_2 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_2,"tag","package");
        generateAttribute(doc,tag_2,"value",xmiPackageID);

        Element tag_3 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_3,"tag","owner");
        generateAttribute(doc,tag_3,"value",xmiOwnerID);

        Element tag_4 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_4,"tag","package_name");
        generateAttribute(doc,tag_4,"value","One Level Block Hierarchy");

        Element tag_5 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_5,"tag","propertyType");
        generateAttribute(doc,tag_5,"value","{"+xmiPropertyTypeID+"}");
    }
//
//    }

//    public String generateIBD(String noun, String xmiId, String xmiPackageId, String xmiOwnerId, String xmiIdBoolean, String xmiIdCollaboration) {
//        String output = "\t<UML:Collaboration xmi.id =\"" + xmiIdCollaboration + "\" name =\"Collaborations\">\n";
//        output += "\t\t<UML:Namespace.ownedElement>\n";
////            output += generateClassifier_Property(noun, xmiId, xmiPackageId, xmiOwnerId);
//
//        //        if direction == TRUE
//        output += generateClassifier_Flow(noun, xmiId, xmiPackageId, xmiOwnerId, xmiIdBoolean);
//
//        output += "\t\t</UML:Namespace.ownedElement>\n";
//        output += "\t\t<UML:Collaboration.interaction/>\n";
//        output += "\t</UML:Collaboration>\n";
//        return output;
//    }


    public void generateEncapsulation(Document doc, Element content, String xmiId, String modelXMI_ID){
        Element tag = generateElement(doc,content,"UML:TaggedValue","");
        generateAttribute(doc,tag,"tag","isEncapsulated");
        generateAttribute(doc,tag,"xmi.id",xmiId);
        generateAttribute(doc,tag,"value","#NOTES#Values: true,false&#10;");
        generateAttribute(doc,tag,"modelElement",modelXMI_ID);
    }
    public void generateDiagram(Document doc, Element root,String xmiID, String xmiPackage) {
        int ea_localid = 2;
        generateDiagramBDD(doc,root,xmiID,xmiPackage);
        for(Sentence s: sentences.sentences) {
            if (s.isInternal) {
//                System.out.println("IBD: " + s.structNoun + " Properties: " + s.structNouns + " " + diagramID + " " + components.getBlockXMI(s.structNoun));
                generateDiagramIBD(doc,root,s.structNoun, s.structNouns, components.getBlock(s.structNoun).diagramID,xmiPackage, components.getBlockXMI(s.structNoun),ea_localid);
                ea_localid++;
            }
        }
    }


    public void generateDiagramBDD(Document doc, Element root, String xmiID, String xmiPackage) {

        Element diagram = generateElement(doc, root, "UML:Diagram", "");
        generateAttribute(doc, diagram, "name", "One Level Block Hierarchy");
        generateAttribute(doc, diagram, "xmi.id", xmiID);
        generateAttribute(doc, diagram, "diagramType", "ClassDiagram");
        generateAttribute(doc, diagram, "owner", xmiPackage);
        generateAttribute(doc, diagram, "toolName", "Enterprise Architect 2.5");

        Element model = generateElement(doc, diagram, "UML:ModelElement.taggedValue", "");

        Element tag_1 = generateElement(doc, model, "UML:TaggedValue", "");
        generateAttribute(doc, tag_1, "tag", "package");
        generateAttribute(doc, tag_1, "value", xmiPackage);

        Element tag_2 = generateElement(doc, model, "UML:TaggedValue", "");
        generateAttribute(doc, tag_2, "tag", "styleex");
        generateAttribute(doc, tag_2, "value", "MDGDgm=SysML1.4::BlockDefinition;SF=1;");

        int b_left, b_right, b_top, b_bottom;
        b_left = 60;
        b_right = 180;
        b_top = 60;
        b_bottom = 180;

        int p_left, p_right, p_top, p_bottom;
        int max_bLeft = 0;
        p_left = 60;
        p_right = 160;
        p_top = 60;
        p_bottom = 160;
        String currentXMI = " ";

        Element diagram_ele = generateElement(doc, diagram, "UML:Diagram.element", "");
        for (Block b : components.blocks) {
//            System.out.println("Blocks: " + b.name + " " +b.xmiID);
            Element diagramElement_1 = generateElement(doc, diagram_ele, "UML:DiagramElement", "");
            generateAttribute(doc, diagramElement_1, "geometry", "Left=" + b_left + ";Top=" + b_top + ";Right=" + b_right + ";Bottom=" + b_bottom + ";");
            generateAttribute(doc, diagramElement_1, "subject", b.xmiID);

        }
        for (Port p : components.ports) {
            Element diagramElement_2 = generateElement(doc, diagram_ele, "UML:DiagramElement", "");
            generateAttribute(doc, diagramElement_2, "geometry", "Left=" + p_left + ";Top=" + p_top + ";Right=" + p_right + ";Bottom=" + p_bottom + ";");
            generateAttribute(doc, diagramElement_2, "subject", p.xmiID);

            p_bottom = p_bottom + 20;
            p_top = p_top + 20;
        }

    }

    public void generateDiagramIBD(Document doc, Element root, String noun, ArrayList<String> properties, String xmiId, String xmiPackage,String parentXmi, int ea_localid){

        Element diagram = generateElement(doc,root,"UML:Diagram","");
        generateAttribute(doc,diagram,"name",noun);
        generateAttribute(doc,diagram,"xmi.id",xmiId);
        generateAttribute(doc,diagram,"diagramType","CompositeStructureDiagram");
        generateAttribute(doc,diagram,"owner",xmiPackage);
        generateAttribute(doc,diagram,"toolName","Enterprise Architect 2.5");

        Element model = generateElement(doc,diagram,"UML:ModelElement.taggedValue","");

        Element tag_1 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_1,"tag","package");
        generateAttribute(doc,tag_1,"value",xmiPackage);

        Element tag_2 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_2,"tag","type");
        generateAttribute(doc,tag_2,"value","CompositeStructure");

        Element tag_3 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_3,"tag", "ea_localid");
        generateAttribute(doc,tag_3,"value",String.valueOf(ea_localid));

        Element tag_4 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_4,"tag","parent");
        generateAttribute(doc,tag_4,"value",parentXmi);

        Element tag_5 = generateElement(doc,model,"UML:TaggedValue","");
        generateAttribute(doc,tag_5,"tag","styleex");
        generateAttribute(doc,tag_5,"value","MDGDgm=SysML1.4::InternalBlock;SF=1;");

        Element diagramElement = generateElement(doc,diagram,"UML:Diagram.element","");

//        System.out.println("IBD: "+ noun);
        Block b = components.getBlock(noun);
        ArrayList<Port> blocks_ports =  components.getListPort(b.xmiID);
        for(Port bp: blocks_ports) {
            Element tag_6 = generateElement(doc, diagramElement, "UML:DiagramElement", "");
            generateAttribute(doc, tag_6, "geometry", "Left=699;Top=129;Right=714;Bottom=144;");
            generateAttribute(doc, tag_6, "subject", bp.xmiID);
        }
        for(String p: properties){
//            System.out.println(p);
            if(components.getProperty(p,noun) != null) {

//            System.out.println(components.getProperty(p,noun).name);
                Element tag_7 = generateElement(doc, diagramElement, "UML:DiagramElement", "");
                generateAttribute(doc, tag_7, "geometry", "Left=699;Top=129;Right=714;Bottom=144;");
                generateAttribute(doc, tag_7, "subject", components.getProperty(p, noun).xmiID);


                ArrayList<PortProperty> propertyPort = components.getListPortProperty(components.getProperty(p, noun).xmiID);
                for(PortProperty pp: propertyPort){
                    Element tag_8 = generateElement(doc, diagramElement, "UML:DiagramElement", "");
                    generateAttribute(doc, tag_8, "geometry", "Left=699;Top=129;Right=714;Bottom=144;");
                    generateAttribute(doc, tag_8, "subject", pp.xmiID);
                }
            } else{
                Element tag_7 = generateElement(doc, diagramElement, "UML:DiagramElement", "");
                generateAttribute(doc, tag_7, "geometry", "Left=699;Top=129;Right=714;Bottom=144;");
                generateAttribute(doc, tag_7, "subject", components.getPropertyXMI(p));
                ArrayList<PortProperty> propertyPort = components.getListPortProperty(components.getPropertyXMI(p));
                for(PortProperty pp: propertyPort){
                    Element tag_8 = generateElement(doc, diagramElement, "UML:DiagramElement", "");
                    generateAttribute(doc, tag_8, "geometry", "Left=699;Top=129;Right=714;Bottom=144;");
                    generateAttribute(doc, tag_8, "subject", pp.xmiID);
                }
            }
//            components.getPortXMI()
//            if(Objects.equals(p, noun)){
//                System.out.println("Yes");
//            }
//                System.out.println("Port: "+ port.name +" " + " "+ port.xmiID + " "+ components.getPortXMI(port.name,components.getBlockXMI(p)));
//                System.out.println(p);
//                String portXMI = components.getPortXMI(port.name,components.getBlockXMI(p));
//                if(portXMI != null && count<=0){
//                    Element tag_7 = generateElement(doc,diagramElement,"UML:DiagramElement","");
//                    generateAttribute(doc,tag_7,"geometry", "Left=699;Top=129;Right=714;Bottom=144;");
//                    generateAttribute(doc,tag_7,"subject", portXMI);
//                    count++;
//                }
//                Port ports = components.getPortXMI(p)
            }
//            System.out.println(" ");

//            for(PortProperty portProperty: components.portProperties){
//                PortProperty pProperty = components.getPortProperty(portProperty.name,p);
//                if(pProperty != null) {
//                    System.out.println("Not null: " + pProperty.name);
//
//                }else{
//                    System.out.println("null: " + portProperty.name);
//                }
////                String port = components.;
////                System.out.println("Ports for " + p + " :" + components.getPropertyXMI());
//            }
        }


//        if (sentences.getSentenceByTypePort("Structural",noun,true).isPort) {
//            ArrayList<String> BDDports = sentences.getSentenceByTypePort("Structural", sentences.getSentenceByStructNoun(noun).structNoun, true).structNouns;
//            for (String port : BDDports) {
//                Element tag_6 = generateElement(doc,diagramElement,"UML:DiagramElement","");
//                generateAttribute(doc,tag_6,"geometry","Left=699;Top=129;Right=714;Bottom=144;");
//                generateAttribute(doc,tag_6,"subject", components.getBlockByName(port).XmiID);
//            }
//        }
//        for(Sentence s: sentences.sentences){
//            if(s.isInternal && s.structNoun.equals(noun)){
//                nounsIndv = s.structNouns;
//
//            }
//
//
//        }

//        int bLeft=60, bTop=60, bRight=160, bBottom=160;
////        System.out.println(nounsIndv);
//        for(String nounIndv: nounsIndv) {
//            System.out.println(nounIndv);
//            int pLeft=bLeft, pTop=bTop, pRight=bRight-55, pBottom=bBottom-45;
//            Element tag_7 = generateElement(doc,diagramElement,"UML:DiagramElement","");
//            generateAttribute(doc,tag_7,"geometry", "Left="+bLeft+";Top="+bTop+";Right="+bRight+";Bottom="+bBottom+";");
//            generateAttribute(doc,tag_7,"subject", components.getBlockByName(nounIndv).XmiID);
////            output += "\t\t\t<UML:DiagramElement geometry=\"Left="+bLeft+";Top="+bTop+";Right="+bRight+";Bottom="+bBottom+";\" subject=\"" + components.getBlockByName(nounIndv).XmiID + "\"/>\n";
//            ArrayList<String> ports = sentences.getSentenceByTypePort("Structural", sentences.getSentenceByStructNoun(nounIndv).structNoun, true).structNouns;
//            System.out.println(ports + "lll");
//            for (String port: ports){
//                PortProperty p;
//                if(components.getPortProperty(port, components.getBlockByName(nounIndv).XmiID) != null) {
//                    p = components.getPortProperty(port, components.getBlockByName(nounIndv).XmiID);
//                }
//                else {
//                    p = components.getPortPropertyBlockXMI(port, components.getBlockByName(nounIndv).XmiID);
//
//                }
//                Element tag_8 = generateElement(doc,diagramElement,"UML:DiagramElement","");
//                generateAttribute(doc,tag_8,"geometry", "Left="+pLeft+";Top="+pTop+";Right="+pRight+";Bottom="+pBottom+";");
//                generateAttribute(doc,tag_8,"subject",p.xmiID);
////                output += "\t\t\t<UML:DiagramElement geometry=\"Left="+pLeft+";Top="+pTop+";Right="+pRight+";Bottom="+pBottom+";\" subject=\"" + p.XmiID + "\"/>\n";
//                pTop+=20;
//                pBottom+=20;
//            }
//            bLeft+=200;
//            bRight+=200;
//        }

////        output += "\t\t</UML:Diagram.element>\n"+
////                "\t</UML:Diagram>\n";
////        return output;
    }


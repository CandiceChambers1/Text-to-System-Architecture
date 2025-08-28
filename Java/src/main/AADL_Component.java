package main;

import java.util.ArrayList;

public class AADL_Component {
    ArrayList<Instantiations> instantiations;
    ArrayList<Subcomponents> subcomponents;
    ArrayList<Features> features;
    ArrayList<Connections> connections;

    public AADL_Component() {
        instantiations = new ArrayList<Instantiations>();
        subcomponents = new ArrayList<Subcomponents>();
        features = new ArrayList<Features>();
        connections = new ArrayList<Connections>();
    }
    // Create an instantiation
    public void createInstantiation(String name, String instantiation) {
        instantiations.add(new Instantiations(name, instantiation));
    }

    // Create a subcomponent
    public void createSubcomponent(String name, String ownerName, String type) {
        subcomponents.add(new Subcomponents(name,ownerName,type));
    }

    // Create a connections
    public void createConnection(String ownerName, String srcBlockName, String destBlockName, String srcPortName,
                                 String destPortName){
        connections.add(new Connections(ownerName,srcBlockName,destBlockName, srcPortName, destPortName));}

    // Create a Feature
    public void createFeature(String name, String ownerName) {
        features.add(new Features(name,ownerName));
    }

    public String checkBlock(String name){
//        System.out.println("Name: " + name);
        for (Instantiations i : instantiations) {
//            System.out.println("I: " + i.name);
            if (i.name.equals(name)) {
                return i.instantiations;
            }
        }
        return null;
    }

    public String getFeatureOwnerName(String name){
        for(Features f: features){
            if(f.name.equals(name)){
                return f.ownerName;
            }
        }
        return null;
    }

    public String getSubcomponentType(String name){
        for(Subcomponents sub: subcomponents){
            if(sub.name.equals(name)){
                return sub.type;
            }
        }
        return null;
    }
    public String getSubcomponentOwnerName(String name){
        for(Subcomponents s: subcomponents){
            if(s.name.equals(name)){
                return s.ownerName;
            }
        }
        return null;
    }
}

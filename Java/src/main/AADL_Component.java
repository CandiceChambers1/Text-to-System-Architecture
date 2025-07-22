package main;

import java.util.ArrayList;

public class AADL_Component {
    ArrayList<Implementations> implementations;
    ArrayList<Subcomponents> subcomponents;
    ArrayList<Features> features;
    ArrayList<Connections> connections;

    public AADL_Component() {
        implementations = new ArrayList<Implementations>();
        subcomponents = new ArrayList<Subcomponents>();
        features = new ArrayList<Features>();
        connections = new ArrayList<Connections>();
    }
    // Create an implementation
    public void createImplementation(String name) {
        implementations.add(new Implementations(name));
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

package main;

public class Connections {
    String ownerName;
    String srcBlockName;
    String destBlockName;
    String srcPortName;
    String destPortName;


    public Connections(String ownerName, String srcBlockName, String destBlockName, String srcPortName, String destPortName) {
        this.ownerName = ownerName;
        this.srcBlockName = srcBlockName;
        this.destBlockName = destBlockName;
        this.srcPortName = srcPortName;
        this.destPortName = destPortName;
    }

}
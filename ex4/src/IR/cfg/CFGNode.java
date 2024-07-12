package IR.cfg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import IR.IRcommand;
import TEMP.TEMP;

public class CFGNode {
    public IRcommand command;
    public String label;
    private List<CFGNode> next;
    public Set<TEMP> in;
    public Set<TEMP> out;

    public CFGNode(IRcommand command) {
        this.command = command;
        next = new ArrayList<CFGNode>();
        in = new HashSet<TEMP>();
        out = new HashSet<TEMP>();
    }

    public boolean livenessAnalysis() {
        // Save originals for comparison
        Set<TEMP> oldIn = new HashSet<TEMP>(in);
        Set<TEMP> oldOut = new HashSet<TEMP>(out);

        // Set out to be equal to the union of its successors' in
        out.clear();

        try {
            next.forEach(nextNode -> out.addAll(nextNode.in));
        } catch (NullPointerException e) {
            System.out.println(String.format("next == null: %b", next == null));
            System.out.println(String.format("has null nextNode: %b", next.contains(null)));

            throw e;
        }

        // Set in to contain out minus the destination and add the sources
        in.addAll(out);

        in.remove(command.destination);

        command.sources.forEach(source -> in.add(source));

        System.out.print(command.getClass().getSimpleName() + ": ");
        out.forEach((TEMP t) -> System.out.print(String.format("%d, ", (t == null ?-1:t.getSerialNumber()))));
        System.out.println();

        // Return true if in or out changed
        return !(oldIn.equals(in) && oldOut.equals(out));
    }

    public List<CFGNode> getNext() {
        return new ArrayList<CFGNode>(next);
    }

    public void addNext(CFGNode node) {
        if (node != null) {
            this.next.add(node);
        }
    }
}
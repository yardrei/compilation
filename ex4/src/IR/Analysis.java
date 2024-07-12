package IR;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import IR.cfg.*;
import TEMP.TEMP;

public class Analysis {
    public static void analyseMe() {
        LinkedList<CFGNode> nodes = constructCFG();
        livenessAnalysis(nodes);
        Matrix<TEMP> matrix = constructInterferenceGraph(nodes);
        Map<TEMP, Integer> colourings = computeKColouring(matrix);
        registerAllocation(colourings);
    }

    // ************************************** //
    // ********** Control Flow Graph ******** //
    // ************************************** //
    private static LinkedList<CFGNode> constructCFG() {
        LinkedList<CFGNode> nodes = IR.getInstance().list.stream()
            .map(CFGNode::new)
            .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<CFGNode> labels = new LinkedList<>();

        extractLabels(labels, nodes);
        mapNextValues(nodes, labels);

        return nodes;
    }

    private static void mapNextValues(LinkedList<CFGNode> nodes, LinkedList<CFGNode> labels) {
        for(CFGNode node : nodes) {
            if(node.command instanceof IRcommand_Label) {
                continue;
            }

            if(node.command instanceof IRcommand_Jump_Label) {
                IRcommand_Jump_Label command = (IRcommand_Jump_Label) node.command;
                node.addNext(getLabelNode(labels, command));
                
            } else if(node.command instanceof IRcommand_Jump_If_Eq_To_Zero) {
                IRcommand_Jump_If_Eq_To_Zero command = (IRcommand_Jump_If_Eq_To_Zero) node.command;
                node.addNext(getLabelNode(labels, command));
                node.addNext(getNextNode(nodes, node));
            } else {
                node.addNext(getNextNode(nodes, node));
            }
        }
    }

    private static void extractLabels(LinkedList<CFGNode> labels, LinkedList<CFGNode> nodes) {
        LinkedList<CFGNode> queuedLabels = new LinkedList<>();

        for(CFGNode node : nodes) {
            if(node.command instanceof IRcommand_Label) {
                node.label = ((IRcommand_Label)node.command).label_name;
                labels.add(node);
                queuedLabels.add(node);
            } else {
                // add the next non-label command to labelNode.next
                queuedLabels.forEach((CFGNode label) -> {
                    label.addNext(node);
                });

                queuedLabels.clear();
            }
        }

        nodes.removeAll(labels);
    }

    /**
     * Gets the label that the command jumps to.
     * @param labels the list of labels.
     * @param command the command that jump to a spacific label.
     * @return the label that command jumps to.
     */
    private static CFGNode getLabelNode(LinkedList<CFGNode> labels, IRcommand_Jump command) {
        String label_name = command.label_name;
        return labels.stream()
                .filter((CFGNode node) -> node.label.equals(label_name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Label not found"));
    }
    
    // This function's time complexity is O(n) when the code can be optimised to O(1)
    private static CFGNode getNextNode(LinkedList<CFGNode> nodes, CFGNode node) {
        Iterator<CFGNode> iterator = nodes.iterator();
        boolean reachedNode = false;
        
        // Reach the line
        while(iterator.hasNext() && !reachedNode) {
            CFGNode next = iterator.next();
            if(next == node) {
                reachedNode = true;
            }
        }

        if(iterator.hasNext()) {
            return iterator.next();
        }
        
        return null;
    }

    // ************************************** //
    // ********** Liveness Analysis ********* //
    // ************************************** //
    private static void livenessAnalysis(LinkedList<CFGNode> nodes) {
        while(livenessAnalysisIteration(nodes));
    }

    private static boolean livenessAnalysisIteration(LinkedList<CFGNode> nodes) {
        System.out.println("======================================");
        boolean changed = false;
        Iterator<CFGNode> iterator = nodes.descendingIterator();
        while(iterator.hasNext()) {
            CFGNode node = iterator.next();
            changed |= node.livenessAnalysis();
        }

        return changed;
    }

    private static Matrix<TEMP> constructInterferenceGraph(List<CFGNode> nodes) {
        Set<TEMP> temps = nodes.stream()
            .flatMap((CFGNode node) -> node.out.stream())
            .collect(Collectors.toSet());

        // temps.forEach((TEMP temp) -> System.out.println(String.format("%b", temp == null)));

        Matrix<TEMP> matrix = new Matrix<>(temps);

        nodes.stream()
            .map((CFGNode node) -> node.out)
            .forEach((Set<TEMP> out) -> matrix.addClique(out));

        System.out.println(matrix);

        return matrix;
    }

    private static Map<TEMP, Integer> computeKColouring(Matrix<TEMP> matrix) {
        return matrix.computeKColouring(10);
    }

    private static void registerAllocation(Map<TEMP, Integer> colourings) {
        colourings.forEach((TEMP temp, Integer colour) -> {
            temp.register = colour;
        });
    }
}
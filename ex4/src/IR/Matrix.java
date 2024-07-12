package IR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Matrix<T> {
  private List<List<Boolean>> adj;
  private Map<T, Integer> coords;

  public Matrix(Set<T> nodes) {
    mapToCoords(nodes);
    initAdjacencyMatrix(nodes);
  }

  public Matrix(Matrix<T> matrix) {
    copy(matrix);
  }

  private void copy(Matrix<T> matrix) {
    initAdjacencyMatrix(matrix.adj.size());
    for(int i = 0; i < adj.size(); i++) {
      for(int j = 0; j < adj.size(); j++) {
        adj.get(i).set(j, matrix.adj.get(i).get(j));
      }
    }

    this.coords = new HashMap<>(matrix.coords);
  }

  private void mapToCoords(Set<T> nodes) {
    coords = new HashMap<>();

    int counter = 0;
    for (T node : nodes) {
      coords.put(node, counter);
      counter++;
    }
  }
  
  private void initAdjacencyMatrix(Set<T> nodes) {
    int size = nodes.size();
    initAdjacencyMatrix(size);
  }

  private void initAdjacencyMatrix(int size) {
    adj = new ArrayList<List<Boolean>>();
    for (int i = 0; i < size; i++) {
      adj.add(initList(size));
    }
  }

  private List<Boolean> initList(int size) {
    List<Boolean> list = new ArrayList<Boolean>();
    for (int i = 0; i < size; i++) {
      list.add(false);
    }
    return list;
  }

  public void addEdge(T a, T b) {
    setEdge(a, b, true);
  }

  public void removeEdge(T a, T b) {
    setEdge(a, b, false);
  }

  private void setEdge(T a, T b, boolean value) {
    int aIndex = coords.get(a);
    int bIndex = coords.get(b);
    adj.get(aIndex).set(bIndex, value);
    adj.get(bIndex).set(aIndex, value);
  }

  public void addClique(Set<T> nodes) {
    for (T a : nodes) {
      for (T b : nodes) {
        if (a != b) {
          addEdge(a, b);
        }
      }
    }
  }

  public boolean getEdge(T a, T b) {
    int aIndex = coords.get(a);
    int bIndex = coords.get(b);
    return adj.get(aIndex).get(bIndex);
  }

  /**
   * Computes a k-colouring of the graph using Chaitin's algorithm.
   */
  public Map<T, Integer> computeKColouring(int k) {
    Matrix<T> original = new Matrix<>(this);

    Map<T, Integer> colourings = new HashMap<>();

    T node;
    LinkedList<T> stack = new LinkedList<>();

    while ((node = getNodeWithLessThanKNeighbours(k, stack)) != null) {
      removeNode(node);
      stack.addFirst(node);
    }

    System.out.println("\nsecond: \n" + original);

    while (!stack.isEmpty()) {
      node = stack.removeFirst();
      int colouring = findValidColouring(original, node, colourings, k);
      if (colouring == -1) {
        return null;
      }

      colourings.put(node, colouring);
    }

    copy(original);

    return colourings;
  }

  private void removeNode(T node) {
    for (T other : getNodes()) {
      removeEdge(node, other);
    }
  }

  private static <T> int findValidColouring(Matrix<T> matrix, T node, Map<T, Integer> colourings, int k) {
    Set<Integer> neighbourColours = new HashSet<>();

    for (T neighbour : matrix.getNeighbours(node)) {
      neighbourColours.add(colourings.get(neighbour));
    }

    for (int colour = 0; colour < k; colour++) {
      if (!neighbourColours.contains(colour)) {
        System.out.println(colour);
        return colour;
      }
    }

    throw new RuntimeException("No valid colouring found for node " + node);
  }

  private Set<T> getNeighbours(T node) {
    Set<T> neighbours = new HashSet<>();
    for (T possibleNeighbour : getNodes()) {
      if (hasEdge(node, possibleNeighbour)) {
        neighbours.add(possibleNeighbour);
      }
    }
    return neighbours;
  }

  public boolean hasEdge(T a, T b) {
    int aIndex = coords.get(a);
    int bIndex = coords.get(b);
    return adj.get(aIndex).get(bIndex);
  }

  private T getNodeWithLessThanKNeighbours(int k, List<T> stack) {
    for (T node : coords.keySet()) {
      if (getNumOfNeighbours(node) < k && !stack.contains(node)) {
        return node;
      }
    }
    return null;
  }

  private int getNumOfNeighbours(T node) {
    int nodeIndex = coords.get(node);
    int counter = 0;
    for (int i = 0; i < adj.size(); i++) {
      if (adj.get(nodeIndex).get(i)) {
        counter++;
      }
    }
    return counter;
  }

  public Set<T> getNodes() {
    return coords.keySet();
  }

  public String toString() {
    String result = "";

    for(Map.Entry<T, Integer> entry : coords.entrySet()) {
      result += entry.getKey() + ": " + entry.getValue() + "\n";
    }


    result += "\n";

    for(List<Boolean> line : adj) {
      for(Boolean b : line) {
        String value = b ? "1" : "0";
        result += value + " ";
      }
      result += "\n";
    }

    return result;
  }
}

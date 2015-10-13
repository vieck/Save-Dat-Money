package edu.purdue.vieck.budgetapp.CustomObjects;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mvieck on 9/29/2015.
 */
public class AddTree {

    private Node root;

    public AddTree(AddItem addItem) {
        root = new Node(addItem);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void walkTree(Node node, ArrayList<Node> nodes) {
        nodes.add(node);
        Log.d("Tree", "Name " + node.getItem().getType());
        for (Node data : node.getChildNodes()) {
            walkTree(data, nodes);
        }
    }

    public int getNodeChildrenAmount() {
        return root.childNodes.size();
    }

    public int getNumberOfChildrenNodes() {
        return root.childNodes.size();
    }

    public ArrayList<Node> getChildNodes() {
        return root.childNodes;
    }

    public void setChildNodes(ArrayList<Node> childNodes) {
        root.childNodes = childNodes;
    }

    public void addNode(Node node) {
        if (!root.hasChildren()) {
            root.childNodes.add(node);
        } else {

        }
    }

    public static class Node {
        private AddItem addItem;
        private Node parent;
        private ArrayList<Node> childNodes;

        public Node(AddItem addItem) {
            this.addItem = addItem;
            childNodes = new ArrayList<>();
        }

        public Node(AddItem addItem, Node parent) {
            this.addItem = addItem;
            childNodes = new ArrayList<>();
            this.parent = parent;
        }

        public boolean isLeafNode() {
            return getChildNodes().isEmpty();
        }

        public ArrayList<Node> getChildNodes() {
            return childNodes;
        }

        public void setChildNodes(ArrayList<Node> childNodes) {
            this.childNodes = childNodes;
        }

        public int getNumberOfChildren() {
            return childNodes.size();
        }

        public boolean hasChildren() {
            return childNodes.size() > 0;
        }

        public void addChild(Node node) {
            childNodes.add(node);
        }

        public void removeChildAt(int index) throws IndexOutOfBoundsException {
            childNodes.remove(index);
        }

        public Node getChildAt(int index) throws IndexOutOfBoundsException {
            return childNodes.get(index);
        }

        public AddItem getItem() {
            return addItem;
        }

    }
}

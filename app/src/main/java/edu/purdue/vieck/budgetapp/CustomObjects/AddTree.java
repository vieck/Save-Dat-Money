package edu.purdue.vieck.budgetapp.CustomObjects;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvieck on 9/29/2015.
 */
public class AddTree {

    private Node root;

    public AddTree(RealmCategoryItem categoryItem) {
        root = new Node(categoryItem);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void walkTree(Node node, ArrayList<Node> nodes) {
        nodes.add(node);
        Log.d("Tree", "Name " + node.getItem().getCategory());
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

    public List<Node> getChildNodes() {
        return root.childNodes;
    }

    public void setChildNodes(ArrayList<Node> childNodes) {
        root.childNodes = childNodes;
    }

    public void addNode(Node node) {
        List<Node> parents = root.getChildNodes();
        boolean test = node.categoryItem.isChild();
        if (test) {
            boolean exists = false;
            for (Node parent : parents) {
                if (parent.categoryItem.getCategory() == node.categoryItem.getCategory()) {
                    exists = true;
                }
            }
            if (!exists) {
                node.addChild(node);
            }
        } else {
            for (Node parent : parents) {
                if (parent.categoryItem.getCategory().compareTo(node.categoryItem.getCategory()) == 0) {
                    List<Node> children = parent.getChildNodes();
                    boolean exists = false;
                    for (Node child : children) {
                        if (child.categoryItem.getSubcategory().compareTo(node.categoryItem.getSubcategory()) == 0) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        parent.addChild(node);
                    }
                }
            }
        }
    }

    public static class Node {
        private RealmCategoryItem categoryItem;
        private Node parent;
        private List<Node> childNodes;

        public Node(RealmCategoryItem categoryItem) {
            this.categoryItem = categoryItem;
            childNodes = new ArrayList<>();
        }

        public Node(RealmCategoryItem categoryItem, Node parent) {
            this.categoryItem = categoryItem;
            childNodes = new ArrayList<>();
            this.parent = parent;
        }

        public boolean isLeafNode() {
            return getChildNodes().isEmpty();
        }

        public List<Node> getChildNodes() {
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

        public RealmCategoryItem getItem() {
            return categoryItem;
        }

    }
}

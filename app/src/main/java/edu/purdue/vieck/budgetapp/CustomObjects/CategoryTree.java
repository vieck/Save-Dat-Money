package edu.purdue.vieck.budgetapp.CustomObjects;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvieck on 9/29/2015.
 */
public class CategoryTree<T> {

    private Node<T> root;

    public CategoryTree(CategoryItem categoryItem) {
        root = new Node<T>(categoryItem);
    }

    public static class Node<T> {
        private CategoryItem categoryItem;
        private Node<T> parent;
        private ArrayList<Node<T>> childNodes;

        public Node(CategoryItem categoryItem) {
            this.categoryItem = categoryItem;
            childNodes = new ArrayList<>();
        }

        public ArrayList<Node<T>> getChildNodes() {
            return childNodes;
        }

        public int getNumberOfChildren() {
            return childNodes.size();
        }

        public boolean hasChildren() {
            return childNodes.size() > 0;
        }

        public void setChildNodes(ArrayList<Node<T>> childNodes) {
            this.childNodes = childNodes;
        }

        public void addChild(Node node) { childNodes.add(node); }

        public void removeChildAt(int index) throws IndexOutOfBoundsException { childNodes.remove(index); }

        public Node getChildAt(int index) throws IndexOutOfBoundsException { return childNodes.get(index); }

        public CategoryItem getItem() { return categoryItem; }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }


    public void walkTree(Node<CategoryItem> node, ArrayList<Node> nodes) {
        nodes.add(node);
        Log.d("Tree", "Name "+node.getItem().getType());
        for (Node<CategoryItem> data : node.getChildNodes()) {
            walkTree(data, nodes);
        }
    }

    public int getNodeChildrenAmount() {
        return root.childNodes.size();
    }

    public int getNumberOfChildrenNodes() {
        return root.childNodes.size();
    }

    public void setChildNodes(ArrayList<Node<T>> childNodes) {
        root.childNodes = childNodes;
    }

    public ArrayList<Node<T>> getChildNodes() {
        return root.childNodes;
    }

    public void addNode(Node<T> node) {
        if (!root.hasChildren()) {
            root.childNodes.add(node);
        } else {

        }

    }
}

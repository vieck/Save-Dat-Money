package edu.purdue.vieck.budgetapp.CustomObjects;

import android.graphics.drawable.Drawable;
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
        root = new Node<T>();
        root.categoryItem = categoryItem;
        root.childNodes = new ArrayList<>();
    }

    public static class Node<T> {
        private CategoryItem categoryItem;
        private Node<T> parent;
        private ArrayList<Node<T>> childNodes;

        public void setCategoryItem(CategoryItem categoryItem) {
            this.categoryItem = categoryItem;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public void setChildNodes(ArrayList<Node<T>> childNodes) {
            this.childNodes = childNodes;
        }

        public CategoryItem getCategoryItem() {
            return categoryItem;
        }

        public ArrayList<Node<T>> getChildNodes() {
            return childNodes;
        }

        public Node<T> getParent() {
            return parent;
        }
    }

    public Node getRootNode() {
        return root;
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
        root.childNodes.add(node);
    }
}

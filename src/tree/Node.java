// Copyright (C) 2015 Fabio Petroni
// Contact:   http://www.fabiopetroni.com
//
// This file is part of CrawlFacebookPostTrees application.
//
// CrawlFacebookPostTrees is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// CrawlFacebookPostTrees is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with CrawlFacebookPostTrees.  If not, see <http://www.gnu.org/licenses/>.
//
// If you use CrawlFacebookPostTrees please cite the following paper:
// - Alessandro Bessi, Fabio Petroni, Michela Del Vicario, Fabiana Zollo, 
//   Aris Anagnostopoulos, Antonio Scala, Guido Caldarelli, Walter Quattrociocchi: 
//   Viral Misinformation: The Role of Homophily and Polarization. In Proceedings
//   of the 24th International Conference on World Wide Web Companion (WWW), 2015.

package tree;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;

public class Node {    
    private String id;
    private String date;
    private LinkedList<Node> childrens;
    private Node father;   
    private LinkedList<Node> children_to_kill;
    
    protected Node(String id){
        this.id = id;
        this.childrens = new LinkedList<>();
        this.children_to_kill = new LinkedList<>();
        this.father = null;
    }
    
    public Node(String id, String date, Node father){
        this.id = id;
        this.date = date;
        this.childrens = new LinkedList<Node>();
        this.children_to_kill = new LinkedList<>();
        this.father = father;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Node getFather() {
        return father;
    }    
    
    public boolean hasChildren(){
        return !childrens.isEmpty();
    }
    
    public void addChildren(Node x){
        childrens.add(x);
    }
    
    public ListIterator<Node> getIteratorChildren(){
        return childrens.listIterator();
    }
    
    public void addChildrenToKill(Node x){
        children_to_kill.add(x);
    }
    
    public void doCarnage(){
        for (Node tokill : children_to_kill) {
            childrens.remove(tokill);
        }
        children_to_kill.clear();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.date);
        hash = 23 * hash + Objects.hashCode(this.childrens);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.childrens, other.childrens)) {
            return false;
        }
        return true;
    }
}

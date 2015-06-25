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

import application.Globals;
import java.util.ListIterator;
import org.openqa.selenium.WebDriver;
import output.DatWriter;
import page.Crawl;
import post.Util;

public class Tree {
    
    public final Node root;
    public final String postfbid;
    public Globals GLOBALS;
    
    public Tree(String id, Globals G){
        this.postfbid = id;       
        root = new Node(postfbid);
        this.GLOBALS = G;
    }
    
    public String getPostId(){
        return root.getId();
    }
    
    public boolean crawl(WebDriver driver){
        boolean result = true;
        if ( DatWriter.exist(GLOBALS.OUTPUT_DIR+"/"+postfbid+".dat") ){
            result = false;
        }
        else{
            String url = Util.getUrlFromPostFbCode(postfbid);
            if (url.isEmpty()){ result = false; }
            else{ result = Crawl.run(root, url, driver); }
        }
        return result;
    }
    
    public void prune(){
        step1_dfs(root);
    }
    
    private void step1_dfs(Node x){
        if (!x.hasChildren()){
            step2_prune(x.getFather(),x);
        }
        else{
            ListIterator<Node> it = x.getIteratorChildren();
            while (it.hasNext()){
                Node y = it.next();
                step1_dfs(y);
            }
        }
        x.doCarnage(); //kill all bad childrens
    }
    
    private void step2_prune(Node father, Node to_remove){
        
        if (father==null){
            return;
        }
        
        Node grandfather = father.getFather();
         
        if (grandfather==null){
            return;
        }
        else{
            ListIterator<Node> it = grandfather.getIteratorChildren();
            while (it.hasNext()){
                Node x = it.next();
                if (x.equals(to_remove)){
                    grandfather.addChildrenToKill(x);
                    break;
                }
            }
            step2_prune(grandfather,to_remove);
            step2_prune(grandfather,father);
        }
    }
    
    
    public void print(){
        DatWriter writer = new DatWriter(GLOBALS.OUTPUT_DIR+"/"+postfbid+".dat");
        print(root,"",writer);
        writer.close();
    }
    
    private void print(Node x, String indentazione, DatWriter writer){
        String content = indentazione+x.getId()+" ("+x.getDate()+")";
        System.out.println(content);
        writer.write(content+"\n");
        String new_indentazione = indentazione+"\t";
        ListIterator<Node> it = x.getIteratorChildren();
        while (it.hasNext()){
            Node y = it.next();
            print(y,new_indentazione,writer);
        }
    }
    
    public void printEdgeList(){
        DatWriter writer = new DatWriter(GLOBALS.OUTPUT_DIR+"/"+postfbid+"_edgelist.csv");
        printEdgeList(root,writer);
        writer.close();
    }
    
    private void printEdgeList(Node x, DatWriter writer){
        ListIterator<Node> it = x.getIteratorChildren();
        while (it.hasNext()){
            Node y = it.next();
            String content = x.getId()+","+y.getId()+",'"+y.getDate()+"'\n";
            writer.write(content);
            System.out.print(content);
            printEdgeList(y,writer);
        }
    }
}

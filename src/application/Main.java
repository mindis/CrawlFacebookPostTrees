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

package application;
import crawl.CrawlThread;
import facebook.Autenticate;
import input.Input;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import tree.Tree;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("\n------------------------------------------------------------------------------------------");
        System.out.println(" CrawlFacebookPostTrees: a crawler for Facebook post trees through web browser automation.");
        System.out.println(" author: Fabio Petroni (http://www.fabiopetroni.com)");
        System.out.println("------------------------------------------------------------------------------------------\n");
        Globals GLOBALS = new Globals(args);
        System.out.println("    Parameters:");
        GLOBALS.print();        
        appSingleThread(GLOBALS);
    }
         
    public static void appSingleThread(Globals GLOBALS){
        LinkedList<String> list = Input.readFacebookPostIds(GLOBALS.FILENAME);         
        if (GLOBALS.RANDOM){ Collections.shuffle(list); }        
        String email = GLOBALS.fb_eamil;
        String pass = GLOBALS.password;      
        WebDriver driver = new FirefoxDriver(); // The Firefox driver supports javascript 
        Autenticate.perform(driver,email,pass);        
        try { Thread.sleep(Globals.TIME_TO_LOGIN_IN_FB_MS);} 
        catch (Exception ex) { System.out.println("InterruptedException in crawl.run()"); ex.printStackTrace(); System.exit(-1);}        
        for (String post_id : list){            
            System.out.println("\n****** STARTING NEW TREE: "+post_id+"******");
            try{
                //OPEN NEW TAB
                WebElement body = driver.findElement(By.tagName("body"));
                body.sendKeys(Keys.CONTROL + "t");
                Tree post = new Tree(post_id,GLOBALS);
                boolean result = post.crawl(driver);
                if (result){
                    post.prune();
                    post.print();
                    post.printEdgeList();
                    System.out.println("****** TREE "+post_id+" ACCOMPLISHED ******");
                }
                else{
                    body.sendKeys(Keys.CONTROL + "w");   
                    System.out.println("****** TREE "+post_id+" FAILED ******");
                }
                //SWITCH TAB
                ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
                driver.switchTo().window(tabs.get(tabs.size() - 1));
            }catch (Exception e){
                System.out.println("****** TREE "+post_id+" FAILED ******");
                System.out.println("EXCEPTION in MAIN "+e);
                System.out.println("...RESTARTING APPLICATION AFTER 1 minute APPLICATION...");
                driver.quit();
                try { Thread.sleep(Globals.ONE_MINUTE);} 
                catch (Exception ex) { System.out.println("InterruptedException in crawl.run()"); ex.printStackTrace(); System.exit(-1);}
                driver = new FirefoxDriver(); // The Firefox driver supports javascript 
                Autenticate.perform(driver,email,pass);
            }
        }        
        driver.quit();
    }
    
    public static final int THREADS = 4;
    public static void appMultithread(Globals GLOBALS){        
        LinkedList<String> list = Input.readFacebookPostIds(GLOBALS.FILENAME);      
        Collections.shuffle(list);
        
        int processors = THREADS;
        ExecutorService executor=Executors.newFixedThreadPool(processors);
                
        int n = list.size();
        int subSize = n / processors + 1;
        for (int t = 0; t < processors; t++) {
            final int iStart = t * subSize;
            final int iEnd = Math.min((t + 1) * subSize, n);
            if (iEnd>=iStart){
                List<String> sublist= list.subList(iStart, iEnd);
                String email = GLOBALS.fb_eamil;
                String pass = GLOBALS.password;
                Runnable x = new CrawlThread(GLOBALS,sublist);
                executor.execute(x);
            }
        }
        try { 
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.DAYS);
        } catch (InterruptedException ex) {System.out.println("InterruptedException "+ex);ex.printStackTrace();}
    }    
}


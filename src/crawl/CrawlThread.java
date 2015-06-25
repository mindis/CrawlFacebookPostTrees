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

package crawl;

import application.Globals;
import facebook.Autenticate;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import tree.Tree;

public class CrawlThread implements Runnable{
    
    private final String fb_email;
    private final String fb_password;
    private final List<String> post_list;
    private Globals GLOBALS;

    public CrawlThread(Globals G, List<String> post_list) {
        this.GLOBALS = G;
        this.fb_email = GLOBALS.fb_eamil;
        this.fb_password = GLOBALS.password;
        this.post_list = post_list;
    }

    @Override
    public void run() {
        WebDriver driver = new FirefoxDriver(); // The Firefox driver supports javascript 
        Autenticate.perform(driver,fb_email,fb_password);        
        try { Thread.sleep(Globals.TIME_TO_LOGIN_IN_FB_MS);} 
        catch (Exception ex) { System.out.println("InterruptedException in crawl.run()"); ex.printStackTrace(); System.exit(-1);}        
        for (String post_id : post_list){            
            //OPEN NEW TAB
            WebElement body = driver.findElement(By.tagName("body"));
            body.sendKeys(Keys.CONTROL + "t");        
            Tree post = new Tree(post_id,GLOBALS);
            boolean result = post.crawl(driver);            
            if (result){
                post.prune();
                post.print();
                post.printEdgeList();
            }
            else{
                body.sendKeys(Keys.CONTROL + "w");    
            }        
            //SWITCH TAB
            ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));
        }        
        driver.quit();
    }
}

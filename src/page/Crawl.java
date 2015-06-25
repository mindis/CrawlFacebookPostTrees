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

package page;

import application.Globals;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tree.Node;

public class Crawl{   

    public static boolean run(Node father, String url, WebDriver driver) {        
        System.out.println(" > crawling url: "+url);
        try{
            driver.get(url);
            try { Thread.sleep(Globals.SLEEP_WAITING_FOR_PAGE_LOAD);} 
            catch (Exception ex) { System.out.println("InterruptedException in crawl.run()"); ex.printStackTrace(); System.exit(-1);}
            List<WebElement> list = null;
            int old_size = 0;
            int new_size = 0;
            int loop = 1;
            do{ //SCROLL TILL THE END OF THE PAGE
                old_size = new_size;
                int THRESHOLD = loop*Globals.BASE_SCROLL_STEP;
                for (int i = 0; i<THRESHOLD; i++){
                    JavascriptExecutor jse = (JavascriptExecutor)driver;
                    jse.executeScript("window.scrollTo(0,document.body.scrollHeight)", "");
                    try { Thread.sleep(Globals.SLEEP_BEFORE_SCROLL_MS);} 
                    catch (Exception ex) { System.out.println("InterruptedException in crawl.run()"); ex.printStackTrace(); System.exit(-1);}
                }
                list = driver.findElements(By.xpath("//div[starts-with(@class, '"+Globals.MAIN_CLASS_NAME+"')]"));
                new_size = list.size();
                //System.out.println("old: "+old_size+", new: "+new_size+", loop: "+loop);
                loop++;
            }while (old_size != new_size);     
            
            if (new_size == 0){ return false; }
            
            for (WebElement element : list){
                String date = "";
                String id = "";

                List<WebElement> ids = element.findElements(By.xpath(".//a[contains(@class, '"+Globals.ID_CLASS_NAME+"')]"));
                for (WebElement id_element : ids){
                    String hovercard = id_element.getAttribute("data-hovercard");
                    id = hovercard.replace(Globals.HOVERCARD_ID, "");
                    //String href = id_element.getAttribute("href");
                    //System.out.println("user: "+hovercard);
                    //System.out.println("page: "+href);
                }
                List<WebElement> dates = element.findElements(By.xpath(".//abbr[contains(@class, '"+Globals.DATE_CLASS+"')]"));
                for (WebElement id_date : dates){
                    date = id_date.getAttribute("title");
                    //System.out.println("date: "+date);
                }

                if (id.equalsIgnoreCase("")){ System.out.println("ERRORE: id nullo per url "+url); return false; }

                Node current = new Node(id, date, father);

                List<WebElement> shared = element.findElements(By.xpath(".//a[contains(@class, '"+Globals.SHARE_CLASS+"')]"));
                for (WebElement id_shared : shared){
                    String href = id_shared.getAttribute("href");
                    //System.out.println("shares: "+href);
                    //String text = id_shared.getText();
                    //System.out.println(text 

                    //DFS
                    
                    //OPEN NEW TAB
                    WebElement body = driver.findElement(By.tagName("body"));
                    body.sendKeys(Keys.CONTROL + "t");
                    
                    try { Thread.sleep(Globals.SLEEP_WAITING_FOR_PAGE_LOAD);} 
                    catch (Exception ex) { System.out.println("InterruptedException in crawl.run()"); ex.printStackTrace(); System.exit(-1);}
                    
                    //driver.switchTo().window(tabs.get(1));
                    if(!run(current,href, driver)){
                        body.sendKeys(Keys.CONTROL + "w");  
                        return false;
                    }
                    
                    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
                    driver.switchTo().window(tabs.get(tabs.size() - 1));
                }

                father.addChildren(current);
            }
        }catch (Exception e){
            System.out.println("WARNING: unable to crawl url: "+url);
            e.printStackTrace();
        }
        
        //CLOSE CURRENT TAB
        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.CONTROL + "w");        
        return true;
    }
}

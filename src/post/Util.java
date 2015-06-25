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

package post;
import application.Globals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Util {
           
    public static String getUrlFromPostFbCode(String postfbcode){        
        String [] split = postfbcode.split("_");
        String page_id = split[0];
        String post_id = split[1];
        String href = "";
        String url = Globals.S1+page_id+Globals.S2+post_id;
        WebDriver driver = new FirefoxDriver();
        driver.get(url);        
        try{
            WebElement element = driver.findElement(By.xpath("//a[contains(@class, '"+Globals.link+"')]"));
            href = element.getAttribute("href");
        }catch(Exception e){
            System.out.println("WARNING: POST "+url+" -> Questo contenuto non è al momento disponibile");
            e.printStackTrace();
        }        
        driver.close();
        return href;
    }
}

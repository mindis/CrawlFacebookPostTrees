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

package facebook;

import application.Globals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Autenticate {  
    
    public static void perform(WebDriver driver, String email, String password){        
        driver.get(Globals.START_PAGE);        
        WebElement email_element = driver.findElement(By.xpath("//*[@id='email']"));      
        email_element.sendKeys(email);        
        WebElement password_element = driver.findElement(By.xpath("//*[@id='pass']"));      
        password_element.sendKeys(password);        
        WebElement click_element = driver.findElement(By.xpath("//*[@type='submit']"));      
        click_element.click();        
        return;
    }
}

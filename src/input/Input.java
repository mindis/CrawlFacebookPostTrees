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

package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Input {
  
    public static LinkedList<String> readFacebookPostIds(String FILENAME){        
        LinkedList<String> result = new LinkedList<String>();
        try {
            FileInputStream fis = new FileInputStream(new File(FILENAME));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String line;
            while((line = in.readLine())!=null){
                if (!line.startsWith("#")){
                    result.add(line);
                }
            }         
            in.close();
        } catch (Exception ex) {
            System.out.println("\nError: readFacebookPostIds.\n\n");
            ex.printStackTrace();
            System.exit(-1);
        }          
        return result;
    }
}
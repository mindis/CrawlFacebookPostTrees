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

public class Globals {    
    
    //mandatory SIMULATOR PARAMETERS
    public String FILENAME;
    public String OUTPUT_DIR;
    public String fb_eamil;
    public String password;  
    
    //optional SIMULATOR PARAMETERS
    public boolean RANDOM = false;   
    
    //STATIC VARIABLES
    public final static String START_PAGE = "https://www.facebook.com/login.php";
    public final static int TIME_TO_LOGIN_IN_FB_MS = 1000;
    public final static int ONE_MINUTE = 60000;
    public final static String S1 = "https://www.facebook.com/";
    public final static String S2 = "/posts/";
    public final static String link = "UFIShareLink";
    public final static String MAIN_CLASS_NAME = "userContentWrapper";
    public final static String ID_CLASS_NAME = "_5pb8 _5v9u _29h _303";
    public final static String DATE_CLASS = "_5ptz";
    public final static String SHARE_CLASS = "UFIShareLink";
    public final static String HOVERCARD_ID = "/ajax/hovercard/user.php?id=";
    public final static int SLEEP_BEFORE_SCROLL_MS = 100;
    public final static int SLEEP_WAITING_FOR_PAGE_LOAD = 300;
    public final static int BASE_SCROLL_STEP = 10;
    // example postfbcode = 466607330102159_495711177191774
    
    public Globals(String[] args){
        parse_arguments(args);
    }
    
    private void parse_arguments(String[] args){
        try{
            fb_eamil = args[0];
            password = args[1];
            FILENAME = args[2];
            OUTPUT_DIR = args[3];
            for(int i=4; i < args.length; i+=2){
                if(args[i].equalsIgnoreCase("-random")){
                    RANDOM = Boolean.parseBoolean(args[i+1]);
                }      
                else throw new IllegalArgumentException();
            }
        } catch (Exception e){
            System.out.println("\nInvalid arguments ["+args.length+"]. Aborting.\n");
            System.out.println("Usage:\n CrawlFacebookPostTrees fbemail fbpassword inputfile outputdir [options]\n");
            System.out.println("Parameters:");
            System.out.println(" fbemail: the email associated with a valid Facebook account.");
            System.out.println(" fbpassword: the password for the Facebook account.");
            System.out.println(" inputfile: the name of the file that stores the ids of the posts to be crawled.");
            System.out.println(" outputdir: the name of the directory where the output (several files, two for each post) will be stored.");
            System.out.println("\nOptions:");
            System.out.println(" -random boolean");
            System.out.println("\t specifies if the input file is processed sequentially (false) or in random order (true). Default false.");
            System.out.println();
            System.exit(-1);
        }   
    }
    
    public void print(){
        System.out.println(this.toString());
    }
    
    @Override
    public String toString(){
        String s = "";
        s+="\tfbemail: "+fb_eamil+"\n";
        s+="\tfbpassword: "+password+"\n";
        s+="\tinputfile: "+FILENAME+"\n";
        s+="\toutputdir: "+OUTPUT_DIR+"\n";
        s+="\trandom: "+RANDOM+"\n";
        return s;
    }
}
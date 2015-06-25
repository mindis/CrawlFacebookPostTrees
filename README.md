# CrawlFacebookPostTrees
a crawler for Facebook post trees through web browser automation.

When executing the application, don't worry if firefox seems possessed! :-)



The goal of the crawler is to download the share tree of each post in input.
See figure.pdf for an example.
In the figure the POST is shared by user A, B, C, D and E. User A, B and C share the post from the root (the original page that issued the post) while user D and E share the post through user C.
The output of the application running on this simple example is constituded by two files:

example.dat (a graphic representation of the tree)

POST
__A
__B
__C
_____D
_____E

example_edgelist.csv (the edgelist representing the tree)

POST,A

POST,B

POST,C

C,D

C,E


Notice that also the date and time of the sharing event is stored for each edge.


If you use CrawlFacebookPostTrees please cite the following paper:

Alessandro Bessi, Fabio Petroni, Michela Del Vicario, Fabiana Zollo, Aris Anagnostopoulos, Antonio Scala, Guido Caldarelli, Walter Quattrociocchi: Viral Misinformation: The Role of Homophily and Polarization. In Proceedings of the 24th International Conference on World Wide Web Companion (WWW), 2015.

Usage:

 CrawlFacebookPostTrees fbemail fbpassword inputfile outputdir [options]

Parameters:
- fbemail: the email associated with a valid Facebook account.
- fbpassword: the password for the Facebook account.
- inputfile: the name of the file that stores the ids of the posts to be crawled.
- outputdir: the name of the directory where the output (several files, two for each post) will be stored.

Options:
- -random boolean  ->  specifies if the input file is processed sequentially (false) or in random order (true). Default false.



The following libraries are required (you can find the jar files in the dist/lib folder):

- Selenium: http://www.seleniumhq.org
- Guava: https://code.google.com/p/guava-libraries/
- Apache HttpComponents: http://hc.apache.org/downloads.cgi

WARNING: sobstitute the selenium jar file with the latest version available!

# Example

java -jar dist/CrawlFacebookPostsTrees.jar facebookemail facebookpassowrd example/input_postfbcodes.dat example/output/

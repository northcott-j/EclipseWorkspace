// Assignment 5
// Lowen Zach
// zlowen
// Northcott Jonathan
// nrthcoj

import tester.Tester;

interface IItem {
    boolean sameItem(IItem that);
    boolean isImage();
    boolean isLink();
    boolean isText();
    boolean asImage();
    boolean asLink();
    boolean asText();
}

interface ILoItem {

}

class ConsLoItem implements ILoItem {
    IItem first;
    ILoItem rest;

    ConsLoItem(IItem first, ILoItem rest) {
        this.first = first;
        this.rest = rest;
    }
    
    /*  TEMPLATE
     * Feilds
     * this.first --- IItem
     * this.rest  --- ILoItem
     * 
     * Methods

     * 
     * Methods on Fields

     */

}

class MtLoItem implements ILoItem {
    MtLoItem() { 
        // end of list is empty
    }
}

class WebPage {
    String url;
    String title;
    ILoItem items;

    WebPage(String url, String title, ILoItem items) {
        this.url = url;
        this.title = title;
        this.items = items;
    }
    
    /*  TEMPLATE
     * Fields
     * this.url --- String
     * this.title --- String
     * this.items --- ILoItem
     * 
     * Methods
     * 
     * Methods on Fields
     */
}


class Text implements IItem {
    String contents;

    Text(String contents) {
        this.contents = contents;
    }
    
    /*  TEMPLATE
     * Fields
     * this.contents --- String
     * 
     * Methods
*/
    
    public boolean sameItem(IItem that);
    public boolean isImage();
    public boolean isLink();
    public boolean isText();
    public boolean asImage();
    public boolean asLink();
    public boolean asText();

}

class Image implements IItem {
    String fileName;
    int size;
    String fileType;

    Image(String fileName, int size, String fileType) {
        this.fileName = fileName;
        this.size = size;
        this.fileType = fileType;
    }
    
    /*  TEMPLATE
     * Fields
     * this.fileName --- String
     * this.size  --- int
     * this.fileType --- 
     * 
     * Methods
*/
    public boolean sameItem(IItem that);
    public boolean isImage();
    public boolean isLink();
    public boolean isText();
    public boolean asImage();
    public boolean asLink();
    public boolean asText();
}

class Link implements IItem {
    String name;
    WebPage page;


    Link(String name, WebPage page) {
        this.name = name;
        this.page = page;

    }
    
    /*  TEMPLATE
     * Fields
     * this.fileName --- String
     * this.size  --- int
     * this.fileType --- 
     * 
     * Methods
     * 
     * Methods on Fields
*/
    public boolean sameItem(IItem that);
    public boolean isImage();
    public boolean isLink();
    public boolean isText();
    public boolean asImage();
    public boolean asLink();
    public boolean asText();
}

class ExamplesWebPage {
    WebPage htdp = new WebPage("HtDP",
                               "htdp.org",
                               new ConsLoItem(
                                       new Text("How to Design Programs"),
                                       new ConsLoItem(
                                               new Image("htdp", 4300, "tiff"),
                                               new MtLoItem()
            )
        )
    );
    
    WebPage ood = new WebPage("OOD",
                              "ccs.neu.edu/OOD",
                              new ConsLoItem(
                                      new Text("Stay classy, Java"),
                                      new ConsLoItem(
                                              new Link("Back to the Future",
                                                       this.htdp),
                                              new MtLoItem()
            )
        )
    );
    
    WebPage fundiesWP = new WebPage("Fundies II", 
                               "ccs.neu.edu/Fundies2", 
                               new ConsLoItem(
                                   new Text("Home sweet home"),
                                   new ConsLoItem(
                                       new Image("wvh-lab", 400, "png"),
                                       new ConsLoItem(
                                           new Text("The staff"),
                                           new ConsLoItem(
                                               new Image("profs", 240, "jpeg"),
                                               new ConsLoItem(
                                                   new Link("A Look Back", 
                                                            this.htdp),
                                                   new ConsLoItem(
                                                       new Link("A Look Ahead", 
                                                                this.ood),
                                                       new MtLoItem()
                            ) 
                        )
                    )
                )
            )
        )
    );
    
            
                               
                                                        
}
// Assignment 5
// Lowen Zach
// zlowen
// Northcott Jonathan
// nrthcoj

import tester.Tester;

interface IItem {
    // Is this item the same as that item?
    boolean sameItem(IItem that);
    // Is this IItem an Image?
    boolean isImage();
    // Is this IItem a Link?
    boolean isLink();
    // Is this IItem a Text?
    boolean isText();
    // Is this IItem a Header?
    boolean isHeader();
    // Is this IItem a Paragraph?
    boolean isParagraph();
    // Cast this IItem as an Image
    Image asImage();
    // Cast this IItem as a link
    Link asLink();
    // Cast this IItem as a Text
    Text asText();
    // Cast this IItem as a Header
    Header asHeader();
    // Cast this IItem as a Paragraph
    Paragraph asParagraph();
}

interface ILoItem {
    // Is this ILoItem the same as that ILoItem?
    boolean sameLoItem(ILoItem items);
    // Is this MtLoItem the same as that MtLoItem?
    boolean sameMtLoItem(MtLoItem item);
    // Is this ConsLoItem the same as that ConsLoItem?
    boolean sameConsLoItem(ConsLoItem item);
}

class ConsLoItem implements ILoItem {
    IItem first;
    ILoItem rest;

    ConsLoItem(IItem first, ILoItem rest) {
        this.first = first;
        this.rest = rest;
    }

    /*  TEMPLATE
     * Fields
     * this.first --- IItem
     * this.rest  --- ILoItem
     * 
     * Methods
     * boolean sameLoItem(ILoItem items);
     * boolean sameMtLoItem(MtLoItem item);
     * boolean sameConsLoItem(ConsLoitem item);
     * 
     * Methods on Fields
     * this.first.sameItem(IItem that) --- boolean
     * this.first.isImage() --- boolean
     * this.first.isLink() --- boolean
     * this.first.isText() --- boolean
     * this.first.isHeader() --- boolean
     * this.first.isParagraph() --- boolean
     * this.first.asImage() --- Image
     * this.first.asLink() --- Link
     * this.first.asText() --- Text
     * this.first.asHeader() --- Header
     * this.first.asParagraph() -- Paragraph
     * this.rest.sameLoItem(ILoItem items) --- boolean
     * this.rest.sameMtLoItem(MtLoItem item); --- boolean
     * this.rest.sameConsLoItem(ConsLoitem item) --- boolean

     */

    // Is this ConsLoItem the same as that ILoItem?
    public boolean sameLoItem(ILoItem items) {
        return items.sameConsLoItem(this);
    }

    // Is this ConsLoItem the same as that MtLoItem?
    public boolean sameMtLoItem(MtLoItem item) {
        return false;
    }

    // Is this ConsLoItem the same as that ConsLoItem?
    public boolean sameConsLoItem(ConsLoItem item) {
        return this.first.sameItem(item.first) &&
                this.rest.sameLoItem(item.rest);
    } 
}

class MtLoItem implements ILoItem {
    MtLoItem() { 
        // end of list is empty
    }

    /*  TEMPLATE
     * Methods
     * boolean sameLoItem(ILoItem items);
     * boolean sameMtLoItem(MtLoItem item);
     * boolean sameConsLoItem(ConsLoitem item);
     */

    // Is this MtLoItem the same as that MtLoItem?
    public boolean sameMtLoItem(MtLoItem item) {
        return true;
    }

    // Is this MtLoItem the same as that ConsLoItem?
    public boolean sameConsLoItem(ConsLoItem item) {
        return false;
    }

    // Is this MtLoItem the same as that ILoItem?
    public boolean sameLoItem(ILoItem items) {
        return items.sameMtLoItem(this);
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
     * boolean sameWebPage(WebPage page);
     * 
     * Methods on Fields
     * this.items.sameLoItem(ILoItem items) --- boolean
     * this.items.sameMtLoItem(MtLoItem item); --- boolean
     * this.items.sameConsLoItem(ConsLoitem item) --- boolean
     */

    // Is this WebPage the same as that WebPage?
    boolean sameWebPage(WebPage page) {
        return this.url.equals(page.url) &&
                this.title.equals(page.title) &&
                this.items.sameLoItem(page.items);
    }
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
     * this.sameItem(IItem that) --- boolean
     * this.isImage() --- boolean
     * this.isLink() --- boolean
     * this.isText() --- boolean
     * this.isHeader() --- boolean
     * this.isParagraph() --- boolean
     * this.asImage() --- Image
     * this.asLink() --- Link
     * this.asText() --- Text
     * this.asHeader() --- Header
     * this.asParagraph() --- Paragraph
     */

    // Is this Text the same as that IItem?
    public boolean sameItem(IItem that) {
        if (that.isText()) {
            Text tThat = that.asText();
            return this.contents.equals(tThat.contents);
        }
        else {
            return false;
        }
    }

    // Is this an Image?
    public boolean isImage() {
        return false;
    }
    // Is this a Link?    
    public boolean isLink() {
        return false;
    }
    // Is this a Text?    
    public boolean isText() {
        return true;
    }
    // Cast this as an Image    
    public Image asImage() {
        throw new ClassCastException("This is a Text not an Image");
    }
    // Cast this as a Link    
    public Link asLink() {
        throw new ClassCastException("This is a Text not a Link");
    }
    // Cast this as a Text    
    public Text asText() {
        return this;
    }
    // Is this a Header?
    public boolean isHeader() {
        return false;
    }
    // Is this a Paragraph?
    public boolean isParagraph() {
        return false;
    }
    // Cast this as a Header
    public Header asHeader() {
        throw new ClassCastException("This is a Text not a Header");
    }
    // Cast this as a Paragraph
    public Paragraph asParagraph() {
        throw new ClassCastException("This is a Text not a Paragraph");
    }
}

class Header extends Text {
    Header(String contents) {
        super(contents);
    }

    /*  TEMPLATE
     * Fields
     * this.contents --- String
     * 
     * Methods
     * this.sameItem(IItem that) --- boolean
     * this.isImage() --- boolean
     * this.isLink() --- boolean
     * this.isText() --- boolean
     * this.isHeader() --- boolean
     * this.isParagraph() --- boolean
     * this.asImage() --- Image
     * this.asLink() --- Link
     * this.asText() --- Text
     * this.asHeader() --- Header
     * this.asParagraph() --- Paragraph
     */

    // Is this Header the same as that IItem?
    public boolean sameItem(IItem that) {
        if (that.isHeader()) {
            Header hThat = that.asHeader();
            return this.contents.equals(hThat.contents);
        }
        else {
            return false;
        }
    }
    // Is this a Header?    
    public boolean isHeader() {
        return true;
    }
    // Cast this as a Header    
    public Header asHeader() {
        return this;
    }
    // Is this a Text?
    public boolean isText() {
        return false;
    }

    // Cast as a Text
    public Text asText() {
        throw new ClassCastException("This is a Header not a Text");
    }
}

class Paragraph extends Text {
    Paragraph(String contents) {
        super(contents);
    }

    /*  TEMPLATE
     * Fields
     * this.contents --- String
     * 
     * Methods
     * this.sameItem(IItem that) --- boolean
     * this.isImage() --- boolean
     * this.isLink() --- boolean
     * this.isText() --- boolean
     * this.isHeader() --- boolean
     * this.isParagraph() --- boolean
     * this.asImage() --- Image
     * this.asLink() --- Link
     * this.asText() --- Text
     * this.asHeader() --- Header
     * this.asParagraph() --- Paragraph
     */

    // Is this Paragraph the same as that IItem?
    public boolean sameItem(IItem that) {
        if (that.isParagraph()) {
            Paragraph pThat = that.asParagraph();
            return this.contents.equals(pThat.contents);
        }
        else {
            return false;
        }
    }
    // Is this a Paragraph?    
    public boolean isParagraph() {
        return true;
    }
    // Cast this as a Header    
    public Paragraph asParagraph() {
        return this;
    }
    // Is this a Text?
    public boolean isText() {
        return false;
    }

    // Cast as a Text
    public Text asText() {
        throw new ClassCastException("This is a Paragraph not a Text");
    }
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
     * this.sameItem(IItem that) --- boolean
     * this.isImage() --- boolean
     * this.isLink() --- boolean
     * this.isText() --- boolean
     * this.isHeader() --- boolean
     * this.isParagraph() --- boolean
     * this.asImage() --- Image
     * this.asLink() --- Link
     * this.asText() --- Text
     * this.asHeader() --- Header
     * this.asParagraph() --- Paragraph
     */
    // Is this Image the same as that IItem?
    public boolean sameItem(IItem that) {
        if (that.isImage()) {
            Image iThat = that.asImage();
            return this.fileName.equals(iThat.fileName) &&
                    this.size == iThat.size &&
                    this.fileType.equals(iThat.fileType);
        }
        else {
            return false;
        }
    }
    // Is this an Image?
    public boolean isImage() {
        return true;
    }
    // Is this a Link?    
    public boolean isLink() {
        return false;
    }
    // Is this a Text?    
    public boolean isText() {
        return false;
    }
    // Cast this as an Image    
    public Image asImage() {
        return this;
    }
    // Cast this as a Link    
    public Link asLink() {
        throw new ClassCastException("This is an Image not a Link");
    }
    // Cast this as a Text    
    public Text asText() {
        throw new ClassCastException("This is an Image not a Text");
    }
    // Is this a Header?
    public boolean isHeader() {
        return false;
    }
    // Is this a Paragraph?
    public boolean isParagraph() {
        return false;
    }
    // Cast this as a Header
    public Header asHeader() {
        throw new ClassCastException("This is an Image not a Header");
    }
    // Cast this as a Paragraph
    public Paragraph asParagraph() {
        throw new ClassCastException("This is an Image not a Paragraph");
    }

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
     * this.sameItem(IItem that) --- boolean
     * this.isImage() --- boolean
     * this.isLink() --- boolean
     * this.isText() --- boolean
     * this.isHeader() --- boolean
     * this.isParagraph() --- boolean
     * this.asImage() --- Image
     * this.asLink() --- Link
     * this.asText() --- Text
     * this.asHeader() --- Header
     * this.asParagraph() --- Paragraph
     * 
     * Methods on Fields
     * this.page.sameWebPage(WebPage page) --- boolean
     */
    // Is this Link the same as that IItem
    public boolean sameItem(IItem that) {
        if (that.isLink()) {
            Link lThat = that.asLink();
            return this.name.equals(lThat.name) &&
                    this.page.sameWebPage(lThat.page);
        }
        else {
            return false;
        }
    }
    // Is this an Image?    
    public boolean isImage() {
        return false;
    }
    // Is this a Link?    
    public boolean isLink() {
        return true;
    }
    // Is this a Text?    
    public boolean isText() {
        return false;
    }
    // Cast this as an Image    
    public Image asImage() {
        throw new ClassCastException("This is a Link not an Image");
    }
    // Cast this as a Link    
    public Link asLink() {
        return this;
    }
    // Cast this as a Text 
    public Text asText() {
        throw new ClassCastException("This is a Link not a Text");
    }
    // Is this a Header?
    public boolean isHeader() {
        return false;
    }
    // Is this a Paragraph?
    public boolean isParagraph() {
        return false;
    }
    // Cast this as a Header
    public Header asHeader() {
        throw new ClassCastException("This is a Link not a Header");
    }
    // Cast this as a Paragraph
    public Paragraph asParagraph() {
        throw new ClassCastException("This is a Link not a Paragraph");
    }
}

class ExamplesWebPage {
    Text t1 = new Text("asd");
    Header h1 = new Header("asd");
    Paragraph p1 = new Paragraph("asd");
    Image i1 = new Image("dragons", 5, ".png");
    
    ILoItem mt = new MtLoItem();
    ILoItem list1 = new ConsLoItem(t1, 
            new ConsLoItem(i1,
                    mt
        )            
    );                

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
    
    boolean testSameItem(Tester t) {
        return  t.checkExpect(t1.sameItem(h1), false)
                && t.checkExpect(h1.sameItem(h1), true)
                && t.checkExpect(p1.sameItem(i1), false)
                && t.checkExpect(i1.sameItem(i1), true);
    }
    
    //checks the isImage() method
    boolean testIsImage(Tester t) {
        return  t.checkExpect(t1.isImage(), false)
                && t.checkExpect(h1.isImage(), false)
                && t.checkExpect(p1.isImage(), false)
                && t.checkExpect(i1.isImage(), true)
                && t.checkExpect(h1.isImage(), false);
    }
    
    //checks the isLink() method
    boolean testIsLink(Tester t) {
        return  t.checkExpect(t1.isLink(), false)
                && t.checkExpect(h1.isLink(), false)
                && t.checkExpect(p1.isLink(), false)
                && t.checkExpect(i1.isLink(), false)
                && t.checkExpect(h1.isLink(), false);
    }
    
    //checks the isText() method
    boolean testIsText(Tester t) {
        return  t.checkExpect(t1.isText(), true)
                && t.checkExpect(h1.isText(), false)
                && t.checkExpect(p1.isText(), false)
                && t.checkExpect(i1.isText(), false)
                && t.checkExpect(h1.isText(), false);
    }

    //checks the isHeader() method
    boolean testIsHeader(Tester t) {
        return  t.checkExpect(t1.isHeader(), false)
                && t.checkExpect(h1.isHeader(), true)
                && t.checkExpect(p1.isHeader(), false)
                && t.checkExpect(i1.isHeader(), false)
                && t.checkExpect(h1.isHeader(), true);
    }
    
    //checks the isParagraph() method
    boolean testIsParagraph(Tester t) {
        return  t.checkExpect(t1.isParagraph(), false)
                && t.checkExpect(h1.isParagraph(), false)
                && t.checkExpect(p1.isParagraph(), true)
                && t.checkExpect(i1.isParagraph(), false)
                && t.checkExpect(h1.isParagraph(), false);
    }
    
    
    boolean testSameLoItem(Tester t) {
        return t.checkExpect(list1.sameLoItem(mt), false)
                && t.checkExpect(list1.sameLoItem(list1), true)
                && t.checkExpect(mt.sameLoItem(list1), false)
                && t.checkExpect(mt.sameLoItem(mt), true);
    /*
    boolean testBadCast(Tester t) {
        return t.checkException(
                new ClassCastException("Text cannot be cast to Image"), 
                t1,                                                     
                "asImage")                                               
                && t.checkException(
                        new ClassCastException("Header cannot be cast to Image"), 
                        h1,
                        "asImage")
                && t.checkException(
                        new ClassCastException("Header cannot be cast to Image"), 
                        h1,
                        "asImage")
               
                && t.checkException(
                        new ClassCastException("Header cannot be cast to Image"), 
                        h1,
                        "asImage")         
                        
                        ;    
      */
    }



}
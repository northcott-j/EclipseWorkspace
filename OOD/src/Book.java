/**
 * Public book class
 */
public class Book implements Publication {
    // FIELDS PRIVATE FINAL
    // Iterators are allowed to change
    // Cyclic data is the other
    private final String author, title, genre;
    private final String pub, loc, isbn;
    private final int year, pages;

    /** FILL IN FOR ASSIGNMENT
     *
     * @param author
     * @param title
     * @param genre
     * @param pub
     * @param loc
     * @param isbn
     * @param year
     * @param pages
     */

    public Book(String author, String title, String genre, String pub, String loc, String isbn, int year, int pages) {
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.pub = pub;
        this.loc = loc;
        this.isbn = isbn;
        this.year = year;
        this.pages = pages;
    }
    /**
     * Formats the current citation in APA style
     *
     * @return the formatted citation (purpose statement of return value)
     */
    @Override
    public String citeAPA() {
        return null;
    }

    /**
     * Formats the current citation in MLA style
     *
     * @return the formatted citation (purpose statement of return value)
     */
    @Override
    public String citeMLA() {
        return null;
    }
}

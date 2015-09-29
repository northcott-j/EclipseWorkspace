package cs3500.hw01.publication;

/**
 * Represents bibliographic information for a Webpage
 */
public class Webpage implements Publication {
    private final String title, url, retrieved;

    /**
     * Constructs a {@code Webpage} object.
     *
     * @param title     the title of the webpage
     * @param url       the url of the webpage
     * @param retrieved the date the webpage was accessed
     */

    public Webpage(String title, String url, String retrieved) {
        this.title = title;
        this.url = url;
        this.retrieved = retrieved;
    }

    /**
     * Formats a citation in APA style.
     *
     * @return the formatted citation
     */
    @Override
    public String citeApa() {
        return title + "." + " Retrieved " + retrieved + ", " + "from " + url + ".";
    }

    /**
     * Formats a citation in MLA style.
     *
     * @return the formatted citation
     */
    @Override
    public String citeMla() {
        return "\"" + title + ".\" " + "Web. " + retrieved + " <" + url + ">.";
    }
}

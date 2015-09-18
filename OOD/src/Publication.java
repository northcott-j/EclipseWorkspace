/**
 * Created by Jonathan on 9/18/2015.
 */
interface Publication {
        /**
         * Formats the current citation in APA style
         *@return the formatted citation (purpose statement of return value)
         */
        String citeAPA();
        /**
         * Formats the current citation in MLA style
         *@return the formatted citation (purpose statement of return value)
         */
        String citeMLA();
    }
}

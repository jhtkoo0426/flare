package flare;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class OCCFormatter {

    public static String formatOCC(String symbol, LocalDate expiryDate, double strikePrice, String optionType) {
        String formattedDate = formatDate(expiryDate, "yyMMdd");

        // Format strike price to 8-digit integer (multiplied by 1000 to avoid decimal)
        String formattedStrike = String.format("%08d", (int)(strikePrice * 1000));

        // Combine all parts into the OCC format
        return String.format("%s%s%s%s", symbol.toUpperCase(), formattedDate, optionType.toUpperCase(), formattedStrike);
    }

    /**
     * Format expiry date to YYMMDD.
     * @param expiryDate
     * @return
     */
    public static String formatDate(LocalDate expiryDate, String pattern) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        return expiryDate.format(dateFormatter);
    }

}

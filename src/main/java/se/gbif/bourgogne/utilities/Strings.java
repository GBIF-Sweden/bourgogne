/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne.utilities;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author korbinus
 */
public class Strings {

    /**
     * Represents a failed index search. Stolen from
     * org.apache.commons.lang3.StringUtils
     *
     */
    private final static int INDEX_NOT_FOUND = -1;

    /**
     * Replace awful Unicode errors with beautiful letters.
     * Although StringUtils.replace() checks if a sequence is present in the provided
     * string, we check first in order to avoid useless calls.
     *
     * @param String
     * @return String
     */
    public static String unscramble(String str) {

        // Å å
        if (str.indexOf("Ã…") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã…", "Å");
        }
        if (str.indexOf("Ã¥") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¥", "å");
        }

        // Ä ä
        if (str.indexOf("Ã„") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã„", "Ä");
        }
        if (str.indexOf("Ã¤") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¤", "ä");
        }

        // Ö ö
        if (str.indexOf("Ã–") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã–", "Ö");
        }
        if (str.indexOf("Ã¶") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¶", "ö");
        }

        // Ø ø
        if (str.indexOf("Ã˜") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã˜", "Ø");
        }
        if (str.indexOf("Ã¸") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¸", "ø");
        }

        // É é
        if (str.indexOf("Ã‰") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã‰", "É");
        }
        if (str.indexOf("Ã©") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã©", "é");
        }

        // Ë ë (yes Mickaël writes here)
        if (str.indexOf("Ã‹") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã‹", "Ë");
        }
        if (str.indexOf("Ã«") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã«", "ë");
        }

        // È è
        if (str.indexOf("Ãˆ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãˆ", "È");
        }
        if (str.indexOf("Ã¨") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¨", "è");
        }

        // Ê ê
        if (str.indexOf("ÃŠ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "ÃŠ", "Ê");
        }
        if (str.indexOf("Ãª") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãª", "ê");
        }

        // À à
        if (str.indexOf("Ã€") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã€", "À");
        }
        if (str.indexOf("\u00C3\u00A0") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "\u00C3\u00A0", "à");
        }

        // Ã ã
        if (str.indexOf("Ãƒ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãƒ", "Ã");
        }
        if (str.indexOf("Ã£") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã£", "ã");
        }

        // Á á
        if (str.indexOf("\u00C3\u0081") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "\u00C3\u0081", "Á");
        }
        if (str.indexOf("Ã¡") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¡", "á");
        }

        // Â â
        if (str.indexOf("Ã‚") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã‚", "Â");
        }
        if (str.indexOf("Ã¢") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¢", "â");
        }

        // Ü ü
        if (str.indexOf("Ãœ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãœ", "Ü");
        }
        if (str.indexOf("Ã¼") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¼", "ü");
        }

        // Ù ù
        if (str.indexOf("Ã™") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã™", "Ù");
        }
        if (str.indexOf("Ã¹") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¹", "ù");
        }

        // Ú ú
        if (str.indexOf("Ãš") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãš", "Ú");
        }
        if (str.indexOf("Ãº") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãº", "ú");
        }

        // Û û
        if (str.indexOf("Ã›") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã›", "Û");
        }
        if (str.indexOf("Ã»") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã»", "û");
        }

        // Ñ ñ
        if (str.indexOf("Ã‘") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã‘", "Ñ");
        }
        if (str.indexOf("Ã±") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã±", "ñ");
        }

        // Ç ç
        if (str.indexOf("Ã‡") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã‡", "Ç");
        }
        if (str.indexOf("Ã§") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã§", "ç");
        }

        // Ò ò
        if (str.indexOf("Ã’") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã’", "Ò");
        }
        if (str.indexOf("Ã²") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã²", "ò");
        }

        // Ó ó
        if (str.indexOf("Ã“") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã“", "Ó");
        }
        if (str.indexOf("Ã³") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã³", "ó");
        }

        // Ô ô
        if (str.indexOf("Ã”") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã”", "Ô");
        }
        if (str.indexOf("Ã´") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã´", "ô");
        }

        // Õ õ
        if (str.indexOf("Ã•") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã•", "Õ");
        }
        if (str.indexOf("Ãµ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãµ", "õ");
        }

        // Ì ì
        if (str.indexOf("ÃŒ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "ÃŒ", "Ì");
        }
        if (str.indexOf("Ã¬") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¬", "ì");
        }

        // Í í
        if (str.indexOf("\u00C3\u008D") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "\u00C3\u008D", "Í");
        }
        if (str.indexOf("Ã­") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã­", "í");
        }

        // Ï ï
        if (str.indexOf("\u00C3\u008F") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "\u00C3\u008F", "Ï");
        }
        if (str.indexOf("Ã¯") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¯", "ï");
        }

        // Î î
        if (str.indexOf("ÃŽ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "ÃŽ", "Î");
        }
        if (str.indexOf("Ã®") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã®", "î");
        }

        // Ð
        if (str.indexOf("\u00C3\u0090") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "\u00C3\u0090", "Ð");
        }

        // Æ æ
        if (str.indexOf("Ã†") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã†", "Æ");
        }
        if (str.indexOf("Ã¦") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¦", "æ");
        }

        // Œ œ
        if (str.indexOf("Å’") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Å’", "Œ");
        }
        if (str.indexOf("Å“") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Å“", "œ");
        }

        // ð
        if (str.indexOf("Ã°") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã°", "ð");
        }

        // ß
        if (str.indexOf("ÃŸ") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "ÃŸ", "ß");
        }

        // Ý ý
        if (str.indexOf("\u00C3\u009D") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "\u00C3\u009D", "Ý");
        }
        if (str.indexOf("Ã½") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã½", "ý");
        }

        // Ÿ ÿ
        if (str.indexOf("Å¸") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Å¸", "Ÿ");
        }
        if (str.indexOf("Ã¿") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¿", "ÿ");
        }

        // Ž ž
        if (str.indexOf("Å½") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Å½", "Ž");
        }
        if (str.indexOf("Å¾") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Å¾", "ž");
        }

        // Þ
        if (str.indexOf("Ãž") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ãž", "Þ");
        }

        // þ
        if (str.indexOf("Ã¾") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã¾", "þ");
        }

        // ×
        if (str.indexOf("Ã—") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã—", "×");
        }

        // ÷
        if (str.indexOf("Ã·") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Ã·", "÷");
        }

        // °
        if (str.indexOf("Â°") > INDEX_NOT_FOUND) {
            str = StringUtils.replace(str, "Â°", "°");
        }

        return str;
    }
}

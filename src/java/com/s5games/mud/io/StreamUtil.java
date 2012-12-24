package com.s5games.mud.io;

import java.util.Map;
import java.util.HashMap;

/**
 * User: george
 * Date: Feb 19, 2008
 * Time: 10:10:09 PM
 */
public class StreamUtil {

    public final static String color_x = "\033[0m";
    public final static String reset = "\033[0m";
    public final static String bold = "\033[1m";
    public final static String dim = "\033[2m";
    public final static String under = "\033[4m";
    public final static String reverse = "\033[7m";
    public final static String hide = "\033[8m";

    public final static String clearscreen = "\033[2J";
    public final static String clearline = "\033[2K";

    public final static String black = "\033[30m";
    public final static String red = "\033[31m";
    public final static String green = "\033[32m";
    public final static String yellow = "\033[33m";
    public final static String blue = "\033[34m";
    public final static String magenta = "\033[35m";
    public final static String cyan = "\033[36m";
    public final static String white = "\033[37m";

    public final static String bblack = "\033[40m";
    public final static String bred = "\033[41m";
    public final static String bgreen = "\033[42m";
    public final static String byellow = "\033[43m";
    public final static String bblue = "\033[44m";
    public final static String bmagenta = "\033[45m";
    public final static String bcyan = "\033[46m";
    public final static String bwhite = "\033[47m";

    public final static String defaultEscapeCode = "{";

    private static Map<String,String> colorMap;

    public static Map<String,String> getColorMap() {

        if( colorMap == null) {
            createColorMap();
        }
        return colorMap;
    }

    private static void createColorMap() {
        colorMap = new HashMap<String,String>();
        colorMap.put("0",reset);
        colorMap.put("1",red);
        colorMap.put("2",yellow);
        colorMap.put("3",green);
        colorMap.put("4",blue);
        colorMap.put("5",magenta);
        colorMap.put("6",cyan);
        colorMap.put("7",white);
        colorMap.put("8",black);
        colorMap.put("a",bred);
        colorMap.put("b",byellow);
        colorMap.put("c",bgreen);
        colorMap.put("d",bblue);
        colorMap.put("e",bmagenta);
        colorMap.put("f",bcyan);
        colorMap.put("g",bwhite);
        colorMap.put("h",bblack);
    }
    
    /**
     * Strip the telnet escape sequences out of the input.
     */
    public static String stripEscape(String source) {
    	if( source.indexOf(0xff) == -1) {
            return source;
        }

        StringBuffer result = new StringBuffer(source.length());
        char[] cset = source.toCharArray();

        for( int a = 0; a < cset.length; ++a) {
            if( cset[a] == 0xff ) {
            	// Log what was stripped?
                // skip 3 characters.
            	a += 2;
            } else {
                result.append(cset[a]);
            }
        }
        return result.toString();
    }
    
    /**
     *
     * @param escapeCode What character means that the next character is an escape character.
     * @param colors a map of char -> color code
     * @param source the string we want to 'colorize'  6
     * @return a colored string (or other replacements)
     */
    public static String colorString(String escapeCode, Map<String,String> colors, String source) {

        /**
         * Leave early if no codes.
         */
        if( source.indexOf(escapeCode) == -1) {
            return source;
        }

        StringBuffer result = new StringBuffer(source.length());
        char[] cset = source.toCharArray();

        for( int a = 0; a < cset.length; ++a) {
            if( escapeCode.equalsIgnoreCase(String.valueOf(cset[a]))) {
                ++a;

                String val = colors.get(String.valueOf(cset[a]));
                if( val == null)
                    val = "";

                // escape code twice to show it.
                if(escapeCode.equalsIgnoreCase(String.valueOf(cset[a])))
                    val = escapeCode;

                result.append(val);
            } else {
                result.append(cset[a]);
            }
        }
        result.append(reset);
        return result.toString();

    }

    public static String colorString(String escapeCode, String source) {
        return colorString(escapeCode, getColorMap(), source);
    }

    public static String colorString(String source) {
        return colorString(StreamUtil.defaultEscapeCode,getColorMap(), source);
    }
}

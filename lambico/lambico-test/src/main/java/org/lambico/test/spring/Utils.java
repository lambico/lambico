/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico test.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lambico.test.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Some utility methods.
 *
 * @author Paolo Dona paolo.dona@seesaw.it
 * @author Michele Franzin paolo.dona@seesaw.it
 * @author Andrea Nasato <mailto:andrea.nasato@jugpadova.it/>
 */
public class Utils {

    /** The logger for this class. */
    private static Logger logger = Logger.getLogger(Utils.class);
    /** The UTF8 preamble. */
    private static final byte[] UTF8_PREAMBLE = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    /** The UTF8 Unicode preamble. */
    private static final String UTF8_UNICODE_PREAMBLE = "﻿";

    /**
     * A default protected constructor.
     */
    protected Utils() {
    }

    /**
     * Converts a map in a list of name/value pairs.
     *
     * @param map The map.
     * @return A list of name/value pairs (name=value).
     */
    @SuppressWarnings(value = "unchecked")
    public static List<String> convertToNameValueList(final Map map) {
        return convertToNameValueList(map, false);
    }

    /**
     * Converts a map in a list of name/value pairs.
     *
     * @param map The map.
     * @param urlEncode if true, encode the values as URLs.
     * @return A list of name/value pairs (name=value).
     */
    @SuppressWarnings(value = "unchecked")
    public static List<String> convertToNameValueList(final Map map, final boolean urlEncode) {
        List<String> result = new ArrayList<String>();
        for (String key : (Iterable<String>) map.keySet()) {
            Object tmp = map.get(key);
            if (tmp instanceof String[]) {
                String[] values = (String[]) tmp;
                for (String value : values) {
                    if (urlEncode) {
                        try {
                            result.add(key + "="
                                    + java.net.URLEncoder.encode(value, "UTF-8"));
                        } catch (UnsupportedEncodingException ex) {
                            logger.warn("Your OS doesn't support UTF-8, so we can encode",
                                    ex);
                            result.add(key + "=" + value);
                        }
                    } else {
                        result.add(key + "=" + value);
                    }
                }
            } else if (tmp instanceof String) {
                String value = (String) tmp;
                if (urlEncode) {
                    try {
                        result.add(key + "="
                                + java.net.URLEncoder.encode(value, "UTF-8"));
                    } catch (UnsupportedEncodingException ex) {
                        logger.warn("Your OS doesn't support UTF-8, so we can encode",
                                ex);
                        result.add(key + "=" + value);
                    }
                } else {
                    result.add(key + "=" + value);
                }
            }
        }
        return result;
    }

    /**
     * Loads a binary file from the classpath.
     *
     * @param classpathResource The resource path.
     * @return The content of the classpath resource.
     */
    public static byte[] loadBinary(final String classpathResource) {
        return loadBinary(new ClassPathResource(classpathResource));
    }

    /**
     * Loads a binary file from the classpath.
     *
     * @param classpathResource The resource path.
     * @return The content of the classpath resource.
     */
    public static byte[] loadBinary(final Resource classpathResource) {
        InputStream stream = null;
        try {
            stream = classpathResource.getInputStream();
            return unsafeLoadBinary(stream);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare la risorsa binaria '"
                    + classpathResource.getDescription() + "' dal classpath.");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Loads a binary file from a stream.
     *
     * @param stream The stream from where to load. It is open,
     *               and it won't be closed by this method.
     * @return The content of the classpath resource.
     * @throws IOException In case of error.
     */
    protected static byte[] unsafeLoadBinary(final InputStream stream) throws IOException {
        return IOUtils.toByteArray(stream);
    }

    /**
     * Loads a text file from the classpath.
     * NB: UTF-8 header are removed!
     *
     * @param classpathResource The resource path.
     * @return The content of the classpath resource.
     */
    public static String loadString(final String classpathResource) {
        return loadString(new ClassPathResource(classpathResource));
    }

    /**
     * Loads a text file from the classpath.
     * NB: UTF-8 header are removed!
     *
     * @param classpathResource The resource path.
     * @return The content of the classpath resource.
     */
    public static String loadString(final Resource classpathResource) {
        InputStream stream = null;
        try {
            stream = classpathResource.getInputStream();
            return unsafeLoadString(stream);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare la risorsa testuale '"
                    + classpathResource.getDescription() + "' dal classpath.");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Loads a text file from a stream.
     *
     * @param stream The stream from where to load. It is open,
     *               and it won't be closed by this method.
     * @return The content of the classpath resource.
     * @throws IOException In case of error.
     */
    protected static String unsafeLoadString(final InputStream stream) throws IOException {
        byte[] byteResult =
                IOUtils.toByteArray(stream);
        return stripUTF8preamble(new String(byteResult, "UTF-8"));
    }

    /**
     * Strips the UTF8 preamble, if present.
     *
     * NB: the conversion to byte array, and a following strip won't work. The reason is that:
     * <code>new String(UTF8_PREAMBLE).getBytes() != UTF8_PREAMBLE</code>;
     *
     * <a href="http://mail.python.org/pipermail/python-list/2004-February/250798.html">Here</a>
     * an explanation.
     *
     * @param s The string to strip.
     * @return The string without the UTF8 preamble.
     * @throws UnsupportedEncodingException If the UTF-8 encoding is not supported.
     */
    public static String stripUTF8preamble(final String s) throws UnsupportedEncodingException {
        if (hasUTF8preamble(s)) {
            logger.debug("Rimosso header UTF-8 dalla stringa");
            return s.substring(1);
        }
        return s;
    }

    /**
     * Strips the UTF8 preamble, if present.
     *
     * @param b The array of bytes (characters).
     * @return The stripped array.
     */
    public static byte[] stripUTF8preamble(final byte[] b) {
        byte[] result = null;
        if (hasUTF8preamble(b)) {
            logger.debug("Rimosso header UTF-8 dall'array");
            result = ArrayUtils.subarray(b, UTF8_PREAMBLE.length, b.length);
        }
        return result;
    }

    /**
     * Checks if the array starts with the UTF-8 preamble.
     *
     * @param b The array to inspect.
     * @return true if the array starts with the UTF-8 preamble.
     */
    public static boolean hasUTF8preamble(final byte[] b) {
        return b[0] == UTF8_PREAMBLE[0] && b[1] == UTF8_PREAMBLE[1]
                && b[2] == UTF8_PREAMBLE[2];
    }

    /**
     * Checks if the string starts with the UTF-8 preamble.
     *
     * @param s The string to inspect.
     * @return true if the string starts with the UTF-8 preamble.
     */
    public static boolean hasUTF8preamble(final String s) {
        return s.startsWith(UTF8_UNICODE_PREAMBLE);
    }

    /**
     * Return an array containing all the substrings of
     * <code>camelString</code>, according to this rule: divide <code>camelString</code>
     * into tokens with every token starting from a capital letter to another.
     *
     * Some examples:
     * <ol>
     *  <li>'MyCamelString' gives: {my,camel,string}</li>
     *  <li>'myFantasticCamelString' gives: {my,fantastic,camel,string}</li>
     *  <li>'MYCAMELSTRING' gives: {m,y,c,a,m,e,l,s,t,r,i,n,g}</li>
     * </ol>
     *
     * @param camelString The string to uncamelize.
     * @return The string uncamelized.
     */
    public static String[] uncamelize(final String camelString) {

        if (camelString == null || camelString.trim().equals("")) {
            throw new IllegalArgumentException("camelString cannot be null or empty");
        }
        List<Integer> idxList = new ArrayList<Integer>();
        List<String> strList = new ArrayList<String>();

        //\p{Lu} is the unicode pattern for capital letters
        Pattern p = Pattern.compile("\\p{Lu}");
        Matcher m = p.matcher(camelString);

        //find all occurences of an uppercase letter and put their position on a list
        while (m.find()) {
            idxList.add(m.start());
        }

        // no upper case found: we return the entire word
        if (idxList.size() == 0) {
            return new String[]{camelString};
        }

        Integer[] idx = idxList.toArray(new Integer[idxList.size() - 1]);

        for (int i = 0; i < idx.length; i++) {

            //if the first character of camelString is lower case,
            //substring from 0 to index[1]
            if (i == 0 && idx[i] > 0) {
                strList.add(camelString.substring(0, idx[i]).toLowerCase());
            }

            //the last part of the word hasn't got an end guard
            if (i == idx.length - 1) {
                strList.add(camelString.substring(idx[i], camelString.length()).toLowerCase());
            } else {
                strList.add(camelString.substring(idx[i], idx[i + 1]).toLowerCase());
            }
        }

        return strList.toArray(new String[strList.size() - 1]);
    }
}

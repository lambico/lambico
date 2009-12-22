/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of lambico-test.
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
 * @author Paolo Dona paolo.dona@seesaw.it
 * @author Michele Franzin paolo.dona@seesaw.it
 * @author Andrea Nasato <mailto:andrea.nasato@jugpadova.it/>
 */
public class Utils {

    private static final Logger logger =
            Logger.getLogger(Utils.class);

    private static final byte[] UTF8_PREAMBLE =
            new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    private static final String UTF8_UNICODE_PREAMBLE = "﻿";

    @SuppressWarnings(value = "unchecked")
    public static List<String> convertToNameValueList(Map map) {
        return convertToNameValueList(map, false);
    }

    @SuppressWarnings(value = "unchecked")
    public static List<String> convertToNameValueList(Map map, boolean urlEncode) {
        List<String> result = new ArrayList<String>();
        for (String key : (Iterable<String>) map.keySet()) {
            Object tmp = map.get(key);
            if (tmp instanceof String[]) {
                String[] values = (String[]) tmp;
                for (String value : values) {
                    if (urlEncode) {
                        try {
                            result.add(key + "=" +
                                    java.net.URLEncoder.encode(value, "UTF-8"));
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
                        result.add(key + "=" +
                                java.net.URLEncoder.encode(value, "UTF-8"));
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
     * Carica un file binario dal classpath
     *
     * @param classpathResource
     * @return
     */
    public static byte[] loadBinary(String classpathResource) {
        return loadBinary(new ClassPathResource(classpathResource));
    }

    public static byte[] loadBinary(Resource classpathResource) {
        InputStream stream = null;
        try {
            return unsafeLoadBinary(stream = classpathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare la risorsa binaria '" +
                    classpathResource.getDescription() + "' dal classpath.");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    protected static byte[] unsafeLoadBinary(InputStream stream) throws IOException {
        return IOUtils.toByteArray(stream);
    }

    /**
     * Carica un file testuale dal classpath.<br/> NB: UTF-8 header are
     * removed!
     *
     * @param classpathResource
     *            file relative path
     * @return textual content of resource file
     */
    public static String loadString(String classpathResource) {
        return loadString(new ClassPathResource(classpathResource));
    }

    public static String loadString(Resource classpathResource) {
        InputStream stream = null;
        try {
            return unsafeLoadString(stream = classpathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare la risorsa testuale '" +
                    classpathResource.getDescription() + "' dal classpath.");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    protected static String unsafeLoadString(InputStream stream) throws IOException {
        byte[] byteResult =
                IOUtils.toByteArray(stream);
        return stripUTF8preamble(new String(byteResult, "UTF-8"));
    }

    /**
     * Rimuove il preambolo UTF se presente nella stringa<br/>NB: la convesione
     * in array di byte e sucessivo strip della stringa non funziona
     * correttamente perch&agrave;
     * <code>new String(UTF8_PREAMBLE).getBytes() != UTF8_PREAMBLE</code>;
     * alcune info qui:
     * http://mail.python.org/pipermail/python-list/2004-February/250798.html
     */
    public static String stripUTF8preamble(String s) throws UnsupportedEncodingException {
        if (hasUTF8preamble(s)) {
            logger.debug("Rimosso header UTF-8 dalla stringa");
            return s.substring(1);
        }
        return s;
    }

    /**
     * rimuove i 3 bytes di preambolo dagli UTF se presenti
     */
    public static byte[] stripUTF8preamble(byte[] b) {
        if (hasUTF8preamble(b)) {
            logger.debug("Rimosso header UTF-8 dall'array");
            b = ArrayUtils.subarray(b, 3, b.length);
        }
        return b;
    }

    public static boolean hasUTF8preamble(byte[] b) {
        return b[0] == UTF8_PREAMBLE[0] && b[1] == UTF8_PREAMBLE[1] &&
                b[2] == UTF8_PREAMBLE[2];
    }

    public static boolean hasUTF8preamble(String s) {
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
     */
    public static String[] uncamelize(String camelString) {

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

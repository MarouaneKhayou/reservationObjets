/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Marouane
 */
public class RandomId {

    public static String getRandomAlphanumricId(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}

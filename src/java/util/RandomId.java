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

    /**
     * Génerer une chaine de caractere aléatoire de longueur définée
     *
     * @param length
     * @return: la chaine de caractere aléatoire
     */
    public static String getRandomAlphanumricId(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}

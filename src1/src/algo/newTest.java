/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algo;

import java.util.Random;

/**
 *
 * @author vision soft
 */
public class newTest {
    public static void main(String[] args) {
        Random rnd = new Random(java.util.UUID.randomUUID().hashCode());
        for (int i = 0; i < 10; i++) {
            System.out.println(rnd.nextFloat());
        }
    }
}

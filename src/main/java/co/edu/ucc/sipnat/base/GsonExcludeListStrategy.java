/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.ucc.sipnat.base;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 *
 * @author Alvaro Padilla
 */
public class GsonExcludeListStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        //System.out.println(f.getDeclaredType().toString());
        if (f.getDeclaredType().toString().startsWith("java.util.List")) {
            System.out.println("skiping " + f.getDeclaredType().toString());
            return true;
        }
        return false;
    }
}
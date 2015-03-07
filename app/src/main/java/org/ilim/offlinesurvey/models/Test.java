package org.ilim.offlinesurvey.models;

import java.util.Arrays;

/**
 * Created by ilimturan on 27/02/15.
 */
public class Test {

    public String[] trotKeys;

    public Test(String[] trotKeys) {
        this.trotKeys = trotKeys;
    }

    @Override
    public String toString() {
        return "Test{" +
                "trotKeys=" + Arrays.toString(trotKeys) +
                '}';
    }
}

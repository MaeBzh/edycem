/*
 * SettingsUtilsBase.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 5, 2019
 *
 */
package com.imie.edycem.test.utils.base;


import junit.framework.Assert;
import com.imie.edycem.entity.Settings;



import com.imie.edycem.test.utils.TestUtils;


/** Settings utils test class base. */
public abstract class SettingsUtilsBase {

    // If you have enums, you may have to override this method to generate the random enums values
    /**
     * Generate a random entity
     *
     * @return The randomly generated entity
     */
    public static Settings generateRandom(android.content.Context ctx){
        Settings settings = new Settings();

        settings.setId(TestUtils.generateRandomInt(0,100) + 1);
        settings.setIdServer(TestUtils.generateRandomInt(0,100));
        settings.setRgpd("rgpd_"+TestUtils.generateRandomString(10));

        return settings;
    }

    public static boolean equals(Settings settings1,
            Settings settings2){
        return equals(settings1, settings2, true);
    }
    
    public static boolean equals(Settings settings1,
            Settings settings2,
            boolean checkRecursiveId){
        boolean ret = true;
        Assert.assertNotNull(settings1);
        Assert.assertNotNull(settings2);
        if (settings1!=null && settings2 !=null){
            Assert.assertEquals(settings1.getId(), settings2.getId());
            Assert.assertEquals(settings1.getIdServer(), settings2.getIdServer());
            Assert.assertEquals(settings1.getRgpd(), settings2.getRgpd());
        }

        return ret;
    }
}


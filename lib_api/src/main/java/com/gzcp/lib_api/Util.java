package com.gzcp.lib_api;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by leo on 2018-02-10.
 */

public class Util {
    private static final String ADD_SUFFIX = "_Add";

    private Util() {

    }

    public static void inject(Object object) {
        try {
            Class bindingClass = Class.forName(object.getClass().getCanonicalName() + ADD_SUFFIX);
            //noinspection unchecked
            Constructor constructor = bindingClass.getConstructor(object.getClass());
            constructor.newInstance(object);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

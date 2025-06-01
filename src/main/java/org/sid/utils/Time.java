package org.sid.utils;

public class Time {

    public static float timeStarted = System.nanoTime();

    // time since app started
    public static float getTime(){
        return (float)((System.nanoTime() - timeStarted) * 1E-9 );
    }
}

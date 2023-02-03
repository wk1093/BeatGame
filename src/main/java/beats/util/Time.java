package beats.util;

import beats.Loggable;

public class Time implements Loggable {
    public static float timeStarted = System.nanoTime();
    public static float getTime() { return (float)((System.nanoTime() - timeStarted) / 1000000000); }
    public static TimeVal get() { return new TimeVal(System.nanoTime() - timeStarted, TimeType.NS); }
}

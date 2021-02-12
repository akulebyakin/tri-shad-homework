package utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MemoryHelper {

    public static long getTotalMemory_Mb() {
        return Runtime.getRuntime().totalMemory() / 1_000_000;
    }

    public static long getFreeMemory_Mb() {
        return Runtime.getRuntime().freeMemory() / 1_000_000;
    }

    public static long getUsedMemory_Mb() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1_000_000;
    }

    public static void logTotalMemory() {
        log.info("Total memory: {} MB", getTotalMemory_Mb());
    }

    public static void logUsageMemory() {
        log.info("Usage memory: {} MB", getUsedMemory_Mb());
    }

    public static void logFreeMamory() {
        log.info("Free memory: {} MB", getFreeMemory_Mb());
    }

    public static void logAllMemory() {
        logTotalMemory();
        logUsageMemory();
        logFreeMamory();
    }

}

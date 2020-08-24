package org.robbie.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import one.profiler.AsyncProfiler;
import sun.misc.Timer;

public class AgentBootstrap {

    private static AsyncProfiler asyncProfiler = null;

    private static String soPath = "/home/robbie/workspace/java/jagent/libasyncProfiler.so";
    public static void agentmain(String args, Instrumentation ins) {
        // implement
        asyncProfiler = AsyncProfiler.getInstance(soPath);
        //System.out.println("start profile");
//        Thread profileThread = new Thread(()->{
//            try {
//                System.out.println("start profile");
//                asyncProfiler.execute("start,event=cpu,file=/home/robbie/workspace/java/async-profiler/profile.svg,");
//            } catch (IOException ex) {
//                System.out.println(ex.getMessage());
//            }
//        });
//        profileThread.start();
        if (asyncProfiler == null) {
            System.out.println("asyncProfiler is null");
            return;
        }

        try {
            System.out.println("start profile");

            asyncProfiler.execute("start,event=cpu,file=/home/robbie/workspace/java/async-profiler/profile.svg,");
        } catch (IOException ex) {
            System.out.println("start exception:" + ex.getMessage());
            return;
        }

        ScheduledExecutorService scheduledExecutorService =  Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(() -> {
            try {
                System.out.println("stop profile");
                asyncProfiler.execute("stop,event=cpu,file=/home/robbie/workspace/java/async-profiler/profile.svg,");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }, 10, TimeUnit.SECONDS);
    }

    public static void premain(String args, Instrumentation inst){

    }

    public static String execute(String cmd) {
        String result = null;
        try {
            result = asyncProfiler.execute(execute(cmd));
        } catch (IOException ex) {
            return ex.getMessage();
        }
        return result;
    }
}

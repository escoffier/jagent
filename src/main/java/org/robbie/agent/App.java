package org.robbie.agent;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            String pid = descriptor.id();
            System.out.println(pid  + " " +  descriptor.displayName());
        }
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNext()) {
            String pid = scanner.next();
            try {
                VirtualMachine virtualMachine = VirtualMachine.attach(pid);

                Properties props = virtualMachine.getSystemProperties();
                String home = props.getProperty("java.home");
                System.out.println("java.home: " + home);
                virtualMachine.loadAgent("/home/robbie/workspace/java/jagent/agent/target/agent-jar-with-dependencies.jar");

                virtualMachine.detach();

            } catch (AttachNotSupportedException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        // =
        System.out.println( "Hello World!" );
    }
}

package healthcare;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8081;
        Server server = new Server(port);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        
        // Point to the JSP and static files directory
        webapp.setResourceBase("src/main/webapp");
        
        // Point Jetty to the compiled classes folder so it can find your Servlets!
        webapp.setExtraClasspath("target/classes");
        
        // -------------------------------------------------------------
        // IMPORTANT: JSP Engine Configuration
        // Embedded Jetty needs a "scratch" directory to compile JSPs
        // -------------------------------------------------------------
        java.io.File tempDir = new java.io.File(System.getProperty("java.io.tmpdir"), "jetty-jsp-scratch");
        tempDir.mkdirs();
        webapp.setAttribute("javax.servlet.context.tempdir", tempDir);
        
        // Tell Jetty to scan all JARs and the classes folder for annotations and JSP Tags (JSTL)
        webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", 
                            ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*\\.jar$|.*/classes/.*");

        
        // Configure Jetty to support Servlets, Web.xml, and Annotations
        webapp.setConfigurations(new Configuration[]{
            new org.eclipse.jetty.webapp.WebInfConfiguration(),
            new org.eclipse.jetty.webapp.WebXmlConfiguration(),
            new org.eclipse.jetty.webapp.MetaInfConfiguration(),
            new org.eclipse.jetty.webapp.FragmentConfiguration(),
            new org.eclipse.jetty.annotations.AnnotationConfiguration()
        });

        server.setHandler(webapp);

        System.out.println("=========================================================");
        System.out.println("Starting Medical Appointment System Embedded Server...");
        System.out.println("Access the application at: http://localhost:" + port);
        System.out.println("=========================================================");

        server.start();
        server.join();
    }
}

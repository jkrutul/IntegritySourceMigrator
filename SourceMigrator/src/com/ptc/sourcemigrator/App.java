package com.ptc.sourcemigrator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ptc.sourcemigrator.database.Database;
import com.ptc.sourcemigrator.models.Member;
import com.ptc.sourcemigrator.models.Project;
import com.ptc.sourcemigrator.models.Sandbox;

public class App{ //
	public static final String PROPERTIES_FILE = "rict.properties";
	
	
	public static final String SERVER_SRC = "192.168.153.29";
	public static final String SERVER_DEST = "192.168.153.56";
	
	public static String oldClientDir, installAppDir, appDir, userHome, mksDir, SIDistDir, serverHostname, serverPort, newIntegrityClientDir, hotfixesDir;
	public static List<String> oldServerHostnames;
	public static PropertiesConfiguration clientInstallProp;
	
	public static CompositeConfiguration config;
	private static final Logger l = LogManager.getLogger(App.class.getName());

	
	private static Timestamp mainStart;
	private static APIUtils src, dest;
	
	static {

		System.out.println("********** Integrity Source Migratoin Tool **********");
		config = new CompositeConfiguration();
		
		config.addConfiguration(new SystemConfiguration());
		
		File f = null;
		appDir = System.getProperty("user.dir");
		userHome = System.getProperty("user.home");
		mksDir = userHome + File.separator + ".mks";
		SIDistDir = userHome + File.separator + "SIDist";
		
		if (appDir != null) {
			f = new File(appDir+File.separator+PROPERTIES_FILE);
			if (f.canRead()) {
				try {
					config.addConfiguration(new PropertiesConfiguration(f.getAbsoluteFile()));
					l.debug("Added configuration from: " + f.getAbsoluteFile());
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}

			} else {
				l.error("Configuration file: "+ f.getAbsoluteFile()+ " doesn't exist");
			}
		}
				
		src = new APIUtils();
		dest = new APIUtils();
	
	}
	
    public static void main( String[] args ){    	

        mainStart = new Timestamp(new java.util.Date().getTime());
        
        //connect to Integrtity servers
        src.connectToIntegrity(	"admin","almalm",SERVER_SRC, "7001");
        dest.connectToIntegrity("admin","almalm",SERVER_DEST, "7001");
        Database db = new Database();
        
        List<Project> projects = dest.getProjects();
        for (Project project : projects) {
        	Database.insertProject(project);
        }
        
        projects = Database.getProjectsData();
        for (Project project : projects) {
        	System.out.println(project);
        }
        
        
     
        
        
        
        /*

        String projectToBeMigrated = "/16_09/GenericSourceMigrator/project.pj";
        String dirToFiles = "C:\\projects\\testing\\16_09\\SOURCE";
        
        List<Sandbox> sandboxes = APIUtils.getSandboxes("/TEMP/Migrated_11/project.pj", null);
        Sandbox sandbox = null;
        if (!sandboxes.isEmpty()){
        	sandbox = sandboxes.get(0);
        }
        
        List<Member> members = src.getMembers(sandbox.getName(), dest.getHostname());
        Member member = members.get(0);
        //member.get
        //APIUtils.addLabel("prototype2", members, null, null, null, null);
        
        src.viewhistory(member.getName(), sandbox.getName(), null, null);
        
        //dest.viewRevision("c:\\sandboxes\\migrated\\2014_09_16_16_32_05\\Utils.java", null, "c:\\sandboxes\\migrated\\2014_09_16_16_32_05\\project.pj",null);
        dest.memberInfo("c:\\sandboxes\\migrated\\2014_09_16_16_32_05\\Utils.java", "/TEMP/Migrated_11/project.pj");
        
         */
        /*            
        APIUtils.dropAllSandboxes(APIUtils.DELETION_POLICY_ALL);
        src.dropProject(projectToBeMigrated);
        src.deleteProject(projectToBeMigrated);
  

        src.dropProject(projectToBeMigrated);
        src.deleteProject(projectToBeMigrated);
        APIUtils.dropSandbox(dirToFiles, APIUtils.DELETION_POLICY_NONE);
        
        dest.dropAndDeleteAllProjects();
   
        src.createNewProjectAndSandbox(projectToBeMigrated, null, null, dirToFiles, true);
   */    
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Calendar cal = Calendar.getInstance();
        //migrateProject(projectToBeMigrated, src.getHostname(), "C:\\sandboxes\\migrated\\" + dateFormat.format(cal.getTime()));
        
        
     
    	src.endSession();
    	dest.endSession();
    	
    	//exitAppSuccessfull();
    }    
    
    /***
     * Function migrate project from source server to destination server.
     * @param projectName -- project name to be migrated
     * @param migratedProjectLocation -- location on destination server, where project will be saved
     */
    private static void migrateProject(String projectName, String hostname, String migratedProjectLocation) {
    	Project projectImported, projectMigrated;
    	Sandbox sandboxImported, sandboxMigrated;

    	projectImported= src.getProject(projectName);
    	
    	if ( projectImported != null ) {
    		l.info("Found "+projectName+" on " + src.getHostname());
    		l.info(projectImported);
    		
    	} else {
    		l.error("Project "+ projectName+ " not exists on " +src.getHostname());
    		return;
    	}
    	
    	// Create sandbox (if not exist) -> imported project
    	List<Sandbox> sandboxesToSrc = APIUtils.getSandboxes(projectImported.getName(), src.getHostname());

    	if (sandboxesToSrc.size()>0) {
        	l.info("Found " +sandboxesToSrc.size()+ " sandboxes point to " + projectName);
    		for (Sandbox s : sandboxesToSrc) {
    			l.info(s);
    		}
    		sandboxImported = sandboxesToSrc.get(0);
    		
    	} else {
    		l.info("Not found any sanboxes pointing to " + projectName+ ". Creating new one");
    		String sandboxname = new File(projectName).getParent();
    		sandboxImported = APIUtils.createSandbox(projectName, hostname, null, null, "c:\\sandboxes\\"+sandboxname);
    		l.info(sandboxImported);
    	}

    	// Create project on destination server
    	String appendix =  "/TEMP/Migrated";
    	String migratedProjectName = appendix +"/project.pj";

    	int i= 0;
    	if (dest.getProject(migratedProjectName) != null){ // Check whether the project of the same name already exists
	    	for ( ; true; i++) {
	    		l.info("Already found project named "+migratedProjectName +" on "+src.getHostname()+" server");
	    		if (dest.getProject(appendix+"_"+Integer.toString(i)+"/project.pj") == null) {
	    			break;
	    		}
	    	}
	    	migratedProjectName = appendix+"_"+Integer.toString(i)+"/project.pj";
    		l.info("Creating new " + migratedProjectName);
    		projectMigrated = dest.createProject(migratedProjectName);
    	} else {
    		l.info("Creating new " + migratedProjectName);
    		projectMigrated = dest.createProject(migratedProjectName);
    	}

    	// Create sandbox -> migrated project
    	sandboxMigrated = APIUtils.createSandbox(migratedProjectName, dest.getHostname(), null, null, migratedProjectLocation);
    	l.info("Sandbox to " +migratedProjectName + " has been created");
    	l.info(sandboxMigrated);    	
    	
    	// Copy members to new project dir    	
        String importedDir = new File(sandboxImported.getName()).getParent();
        String migratedDir = new File(sandboxMigrated.getName()).getParent();
        System.out.println("Coping files...");
        try {
			Utils.copyFolder(new File(importedDir), new File(migratedDir));
			File f = new File(sandboxMigrated.getName());
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	// Add members to migrated sandbox
        l.info("Getting memers...");
        List<Member> members = src.getMembers(sandboxImported.getName(), sandboxImported.getHostname());
        
        for (Member member : members) {
        	File oldPath = new File(member.getName());
        	String newPath = oldPath.getAbsolutePath().replace(importedDir, migratedDir);
        	member.setName(newPath);
        	dest.addMember("migrated", sandboxMigrated.getName(), sandboxMigrated.getHostname(), member.getName(), null);
        }

        // Drop importedSandbox, delete importedProject
        l.info("Droping/removing sandbox and project");
    	APIUtils.dropSandbox(sandboxImported.getName(), APIUtils.DELETION_POLICY_NONE);
    	dest.dropProject(projectImported.getName());
    	dest.deleteProject(projectImported.getName());

    }

}

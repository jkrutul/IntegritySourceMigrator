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

import models.Member;
import models.Project;
import models.Sandbox;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class App{ //
	public static final String PROPERTIES_FILE = "rict.properties";
	public static final String PROPERTY_OLD_CLIENT_DIR = "OLD_CLIENT_DIR";
	public static final String PROPERTY_NEW_CLIENT_INSTALLATOR_DIR = "NEW_CLIENT_INSTALL_APP_DIR";
	public static final String PROPERTY_PROCESS_WORK_TIME_LIMIT = "WORKING_TIME_LIMIT_OF_PROCESS";
	public static final String PROPERTY_PROCESS_CHECK_TIME_INTERVAL = "PROCESS_CHECK_INTERVAL";
	public static final String PROPERTY_SERVER_HOSTNAME = "SERVER_HOSTNAME";
	public static final String PROPERTY_SERVER_PORT = "SERVER_PORT";
	public static final String PROPERTY_OLD_SERVER_HOSTNAMES = "OLD_SERVER_HOSTNAMES";
	public static final String PROPERTY_HOTFIXES_DIR = "HOTFIXES_DIR";
	
	public static final String SERVER_SRC = "192.168.153.29";
	public static final String SERVER_DEST = "192.168.153.56";
	
	public static String oldClientDir, installAppDir, appDir, userHome, mksDir, SIDistDir, serverHostname, serverPort, newIntegrityClientDir, hotfixesDir;
	public static List<String> oldServerHostnames;
	public static PropertiesConfiguration clientInstallProp;
	
	public static CompositeConfiguration config;
	static final Logger l = LogManager.getLogger(App.class.getName());

	private static int processWorkTimeLimit = 60*20;
	private static int processCheckInterval = 10;
	
	private static List<Project> projects;
	private static List<Sandbox> sandboxes;

	
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
		
		processWorkTimeLimit = config.getInt(PROPERTY_PROCESS_WORK_TIME_LIMIT, 60*20);
		processCheckInterval = config.getInt(PROPERTY_PROCESS_CHECK_TIME_INTERVAL, 4);
		oldClientDir = config.getString(PROPERTY_OLD_CLIENT_DIR);
		installAppDir = config.getString(PROPERTY_NEW_CLIENT_INSTALLATOR_DIR);
		serverHostname = config.getString(PROPERTY_SERVER_HOSTNAME);
		serverPort = config.getString(PROPERTY_SERVER_PORT);
		List<Object> objects = config.getList(PROPERTY_OLD_SERVER_HOSTNAMES);
		oldServerHostnames = new LinkedList<String>();
		for (Object o : objects) {
			oldServerHostnames.add((String)o);
		}
		hotfixesDir = config.getString(PROPERTY_HOTFIXES_DIR,installAppDir + File.separator+"hotfixes");
		
		src = new APIUtils();
		dest = new APIUtils();
	
	}
	
    public static void main( String[] args ){    	

        mainStart = new Timestamp(new java.util.Date().getTime());
        
        //connect to Integrtity servers
        src.connectToIntegrity(	"admin","almalm",SERVER_DEST, "7001");
        dest.connectToIntegrity("admin","almalm",SERVER_DEST, "7001");
        String projectToBeMigrated = "/16_09/Testing03/GenericSourceMigrator/project.pj";
        String dirToFiles = "C:\\projects\\testing\\16_09\\SOURCE";


        dest.createNewProjectAndSandbox(projectToBeMigrated, null, null, dirToFiles, true);
       
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Calendar cal = Calendar.getInstance();
        migrateProject(projectToBeMigrated, "C:\\sandboxes\\migrated\\" + dateFormat.format(cal.getTime()));
        
        
        /*
        
        dest.dropSandbox(sandbox.getName(), APIUtils.DELETION_POLICY_NONE);
        dest.dropProject(projectToBeMigrated);
        dest.deleteProject(projectToBeMigrated);
        */
        
/*
        List<Sandbox> sandboxes = APIUtils.getSandboxes(dirToFiles+"\\project.pj", null);
        if (!sandboxes.isEmpty()) {
        	dest.addMembersFromDir(dirToFiles, sandboxes.get(0).getName());
            //dest.addMember("add", sandboxes.get(0), memberLocation, changePackageId)
        }
  */      

        
        

        //migrateProject("/Migrated/SourceCode/GenericSourceMigrator/project.pj");
        
        /*
        
        //creating sandbox
 
        System.out.println("dest sandboxes");
        List<Sandbox> sandboxes = APIUtils.getSandboxes(null, null);
        for (Sandbox sandbox : sandboxes) {
        	System.out.println(sandbox);
        }
        
 
        String projectName = "/SourceCode/GenericSourceMigrator/project.pj";
        String sandboxPath = "c:\\sandboxes\\imported\\project.pj";
        Sandbox sandbox;
        if (!APIUtils.getSandboxes(projectName,null).isEmpty()) {
        	for (Sandbox s : APIUtils.getSandboxes(projectName,null)) {
        		if (s.getName().equals(sandboxPath)) {
        			sandbox = s;
        		}
        	}
        }

       
        Project migratedProject = null; 
        Sandbox migratedSandbox = null;
        String migratedPath = "/Migrated";
        String migratedSandboxPath = "c:\\sandboxes\\migrated\\";
                System.out.println("getting memers...");
        List<Member> members = src.getMembers(sandboxPath+File.separator+"project.pj");
        
        if(!dest.getProjects(true, migratedPath+projectName).isEmpty()) {
        	migratedProject = dest.getProjects(true, migratedPath+projectName).get(0);
        }
        /*
        System.out.println("creating sandbox...");
        src.createSandbox(projectName, null, null, sandboxPath);
        

        
        System.out.println("creating project...");
        dest.createProject(migratedPath+projectName);
        


        System.out.println("creating sandbox..."); 
        dest.createSandbox(migratedProject.getName(), migratedProject.getBuildRevision(), migratedProject.getDevelopmentPath(),migratedSandboxPath);
        
        if (!dest.getSandboxes(migratedProject.getName(),null).isEmpty()) {
        	migratedSandbox = dest.getSandboxes(migratedProject.getName(), null).get(0);
        }
       
        System.out.println("coping files...");
        try {
			Utils.copyFolder(new File(sandboxPath), new File(migratedSandboxPath));
			File f = new File(migratedSandboxPath+File.separator + "project.pj");
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        File importedDir, migratedDir;
        importedDir = new File(sandboxPath);
        migratedDir = new File(migratedSandboxPath);
        
        for (Member member : members) {
        	File oldPath = new File(member.getName());
        	
        	String newPath = oldPath.getAbsolutePath().replace(importedDir.getAbsolutePath().toLowerCase(), migratedDir.getAbsolutePath().toLowerCase());
        	member.setName(newPath);
        	dest.addMember("desc", migratedSandbox.getName(), member.getName(), null);
        }
        
        System.out.println("adding members to sanbox...");
        System.out.println("checking in...");
        
        
        List<Project> projects = dest.getProjects(false, null);
        
        List<String> projectNames = new LinkedList<String>();
        for (Project project : projects) {
        	projectNames.add(project.getName());
        }
  
        if (!src.getProjects(true, "/SourceCode/GenericSourceMigrator/project.pj").isEmpty()) {
            Project project = src.getProjects(true, "/SourceCode/GenericSourceMigrator/project.pj").get(0);
            System.out.println(project);
            dest.createProject("/SourceCode/GenericSourceMigrator/project.pj");
           // dest.create
        }
        
        		
        //src.deleteProjects(projectNames);
        
        //dest.deleteProjects(projectNames);
        
        
        
    	//copyProject(project);
    	//dest.createSandbox(project.getName(), project.getBuildRevision(), project.getDevelopmentPath());
    	
    	//Sandbox sandbox = src.getSandboxes(project.getName()).get(0);
    	//List<Sandbox> sandboxes = dest.getSandboxes("/SourceCode/GenericSourceMigrator/project.pj");
    	//Sandbox destSandbox = null;
    	//if (!sandboxes.isEmpty()) {
    	//	destSandbox = sandboxes.get(0);
    	//}
    	/*
    	List<Member> members = src.getMembers(sandbox.getName());
    	
    	for (Member member : members) {
        	dest.addMember(null, destSandbox.getName(), member.getName(), null);
    	}
        
        /*
        for (Project project : dest.getProjects()) {
        	dest.dropProject(project.getName());
        }
        */
        

        
        /*
    	if( !checkProperties()) {
    		abortApp();
    	}

       // String userName = readFromConsole("Enter User Name:");
    	//String password = readFromConsole("Enter your password:");
        Project project;
        Sandbox sandbox;
        List<Member> members = new LinkedList<Member>();

    	//api.createProject("c:\\lalalala\\project.pj");
    	
    	project = src.getProjects(true, "/SourceCode/GenericSourceMigrator/project.pj").get(0);
    	sandbox = src.getSandboxes(project.getName()).get(0);
    	members = src.getMembers(sandbox.getName());
    	
    	System.out.println(sandbox);
  
    	
    	//Project project = projects.get(0);
    	//api.createSubProject(project.getName(), "c:\\subprojects\\project.pj");
    	//api.createSandbox(project.getName(), project.getBuildRevision(), project.getDevelopmentPath());
    	
    	//api.createSandbox(sandboxDirectory, projectRevision, devPath)
    	//api.dropSanboxes(sandboxes);
    	

    	//Sandbox sandbox = sandboxes.get(0);
    	//api.checksInMembersOfSandbox(label, description, members)
    	
    	copyProject(project);
    	dest.createSandbox(project.getName(), project.getBuildRevision(), project.getDevelopmentPath());
    	
    	for (Member member : members) {
        	dest.addMember(null, sandbox.getName(), member.getName(), null);
    	}
    	
    	*/

    	
    	//System.out.println(project);
    	//dest.createNewProject("c:\\temp\\myproject\\project.pj");
    	
    	
    	
    	src.endSession();
    	dest.endSession();
    	
    	//exitAppSuccessfull();
    }    
    
    /***
     * Function migrate project from source server to destination server.
     * @param projectName -- project name to be migrated
     * @param migratedProjectLocation -- location on destination server, where project will be saved
     */
    private static void migrateProject(String projectName, String migratedProjectLocation) {
    	Project projectImported, projectMigrated;
    	Sandbox sandboxImported, sandboxMigrated;

    	if ( src.getProject(projectName) != null ) {
    		l.info("Found "+projectName+" on " + src.getHostname());
    		projectImported = projects.get(0);
    		l.info(projectImported);
    		
    	} else {
    		l.error("Project "+ projectName+ " not exists on " +src.getHostname());
    		return;
    	}
    	
    	// Create sandbox (if not exist) -> scr project
    	List<Sandbox> sandboxesToSrc = APIUtils.getSandboxes(projectName, src.getHostname());

    	if (sandboxesToSrc.size()>0) {
        	l.info("Found " +sandboxesToSrc.size()+ " sandboxes point to " + projectName);
    		for (Sandbox s : sandboxesToSrc) {
    			l.info(s);
    		}
    		sandboxImported = sandboxesToSrc.get(0);
    		
    	} else {
    		l.info("Not found any sanboxes pointing to " + projectName+ ". Creating new one");
    		String sandboxname = projectName.replaceAll("/", "");
    		sandboxImported = APIUtils.createSandbox(projectName, null, null, "c:\\sandboxes\\"+sandboxname);
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
    	sandboxMigrated = APIUtils.createSandbox(migratedProjectName, null, null, migratedProjectLocation);
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
        List<Member> members = src.getMembers(sandboxImported.getName());
        
        for (Member member : members) {
        	File oldPath = new File(member.getName());
        	String newPath = oldPath.getAbsolutePath().replace(importedDir.toLowerCase(), migratedDir.toLowerCase());
        	member.setName(newPath);
        	dest.addMember("migrated", sandboxMigrated.getName(), member.getName(), null);
        }

        // Drop importedSandbox, delete importedProject
        l.info("Droping/removing sandbox and project");
    	APIUtils.dropSandbox(sandboxImported.getName(), APIUtils.DELETION_POLICY_NONE);
    	dest.dropProject(projectImported.getName());
    	dest.deleteProject(projectImported.getName());

    }

}

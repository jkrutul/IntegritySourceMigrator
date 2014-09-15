import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import org.apache.commons.httpclient.methods.DeleteMethod;
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
        String projectName = "/GenericSourceMigrator/project.pj";
        String dirToFiles = "C:\\SourceMigratorTest\\src";
        

        //dest.createProject(projectName);
        APIUtils.createSandbox(projectName, null, null, "C:\\SourceMigratorTest\\src");

        List<Sandbox> sandboxes = APIUtils.getSandboxes(dirToFiles+"\\project.pj", null);
        if (!sandboxes.isEmpty()) {
        	dest.addMembersFromDir(dirToFiles, sandboxes.get(0).getName());
            //dest.addMember("add", sandboxes.get(0), memberLocation, changePackageId)
        }
        

        
        

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
    private static void migrateProject(String projectName) {
    	Project projectImported, projectMigrated;
    	Sandbox sandboxImported, sandboxMigrated;
    	
    	// Check if project exist on source server
    	List<Project> projects = src.getProjects(true, projectName);
    	if (!projects.isEmpty()) {
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
    	List<Project> destProjects = dest.getProjects(true, migratedProjectName);
    	int i= 0;
    	if (!destProjects.isEmpty()){
	    	for ( ; true; i++) {
	    		l.info("Already found project named "+migratedProjectName +" on "+src.getHostname()+" server");
	    		destProjects = dest.getProjects(true, appendix+"_"+Integer.toString(i)+"/project.pj");
	    		if (destProjects.isEmpty()) {
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

    	// Create sandbox -> new project
    	sandboxMigrated = APIUtils.createSandbox(migratedProjectName, null, null, null);
    	l.info("Sandbox to " +migratedProjectName + " has been created");
    	l.info(sandboxMigrated);    	
    	
    	// Copy members to new project dir    	
        
        System.out.println("Coping files...");
        try {
			Utils.copyFolder(new File(sandboxImported.getName()), new File(sandboxMigrated.getName()));
			File f = new File(sandboxImported.getName()+File.separator + "project.pj");
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	// Add members to sandbox
        l.info("Getting memers...");
        List<Member> members = src.getMembers(sandboxImported.getName());
        File importedDir, migratedDir;
        importedDir = new File(sandboxImported.getName());
        migratedDir = new File(sandboxMigrated.getName());
        
        for (Member member : members) {
        	File oldPath = new File(member.getName());
        	String newPath = oldPath.getAbsolutePath().replace(importedDir.getAbsolutePath().toLowerCase(), migratedDir.getAbsolutePath().toLowerCase());
        	member.setName(newPath);
        	dest.addMember("migrated", sandboxMigrated.getName(), member.getName(), null);
        }
        
       

        
        // Drop importedSandbox, delete importedProject
        l.info("Droping/removing sandbox and project");
        List<Sandbox> sandboxedToDrop = new LinkedList<Sandbox>();
        sandboxedToDrop.add(sandboxImported);
        
        List<String> projectToDelete = new LinkedList<String>();
        projectToDelete.add(projectImported.getName());
        
    	APIUtils.dropSanboxes(sandboxedToDrop, "none");
    	dest.dropProject(projectImported.getName());
    	dest.deleteProjects(projectToDelete);
        
    	
    }
    
    private static void copyProject(Project project) {
    	if (project.getIsSubproject().equals("true")) {
    		Project parent = src.getProjects(true, project.getParent()).get(0);
    		copyProject(parent);
    		dest.createSubProject(parent.getPath(), project.getName());
    	} else {
    		dest.createProject(project.getPath());
    	}
    }
    
    private static String readFromConsole(String message){
    	 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         System.out.print(message);
         try {
			return br.readLine();
		} catch (IOException e) {
			l.error(e);
		}
		return null;

    }
     
    public static void exitAppSuccessfull(){

    	System.out.println("Total time duration: " + Utils.timeDuration(mainStart)+"\nAll roll-out steps run successfully. Press enter to exit RICT");
    	l.info("Total time duration: " + Utils.timeDuration(mainStart));
    	try {
			System.in.read();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void installHotfixes(){
    	Timestamp start;
	    start = new Timestamp(new java.util.Date().getTime());
	    System.out.println("[" + start + "] Installing hotfixes...");
	    String pathToPatchClient = newIntegrityClientDir +File.separator +"bin"+File.separator+"PatchClient.exe";

	    
	    File patchClient = new File(pathToPatchClient);
	    if (!patchClient.exists() ){
	    	l.error("PatchClient.exe not found under: "+pathToPatchClient );
	    	abortApp();
	    	return;
	    }
	   
	    File hotfixesFolder = new File(hotfixesDir);
	    File[] hotfixes = hotfixesFolder.listFiles();

	    for ( File hotfix : hotfixes) {
	    	try {
				WindowsUtils.startProcess(pathToPatchClient, "\""+hotfix.getAbsolutePath()+"\"", processWorkTimeLimit, processCheckInterval);
			} catch (IOException e) {
				l.error(e);
				abortApp();
			}	    	
	    }
	    
	
	    	
    }
    
    public static void abortApp(){
    	System.out.println("Total time duration: " + Utils.timeDuration(mainStart)+"\nError occurred. Please check log file. Press enter to exit RICT");
    	l.info("Total time duration: " + Utils.timeDuration(mainStart));
    	try {
			System.in.read();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static void uninstallIntegrityClient() {
    	// UNINSTALL OLD CLIENT
    	Timestamp start, stop;
		try {
	    	start = new Timestamp(new java.util.Date().getTime());
	    	System.out.println("[" + start + "] Uninstalling old Integrity Client...");
	    	
	    	String oldClientDir = config.getString(PROPERTY_OLD_CLIENT_DIR) + File.separator + "uninstall" + File.separator + "IntegrityClientUninstall.exe";
	    	String pathToProcess = oldClientDir;
			File f = new File(pathToProcess);
			if (!f.exists()){
				l.error(f.getAbsolutePath() + " doesn't exist, uninstall abort");
			}
			
			if (oldClientDir.contains("\\")) {
				oldClientDir = f.getName();
			}
			
		    int numberOfProcesses = WindowsUtils.numberOfRunningProcesses(oldClientDir);
		    if (numberOfProcesses > 0 ) {
		    	if (numberOfProcesses == 1){
		    		l.warn("find already running process "+ oldClientDir + " process will be terminated");
		    	} else {
		       		l.warn("find already running "+numberOfProcesses+" \""+ oldClientDir + "\" processes, all of them will be terminated");
		    	}
		       	WindowsUtils.killProcess(oldClientDir);
		    }    		
		    WindowsUtils.startProcess(pathToProcess, "-i silent", processWorkTimeLimit , processCheckInterval);
	    	stop = new Timestamp(new java.util.Date().getTime());
			if (WindowsUtils.checkExitCode() == 0){
				String message = "[" + stop + "] Old Integrity Client was successful uninstalled. Time duration: " +Utils.timeDuration(start);
				WindowsUtils.deleteFolder(new File(config.getString(PROPERTY_OLD_CLIENT_DIR)));
				System.out.println(message);
				l.info(message);
				
			}else {
				String message = "[" + stop + "] Error occurred while uninstalling Integrity Client. Time duration: " +Utils.timeDuration(start);
				System.out.println(message);
				l.error(message);
				abortApp(); //TODO uncomment
			}
		} catch (IOException e) {
			l.error(e);
			l.error("Please check if \""+PROPERTY_OLD_CLIENT_DIR+"\" property is properly set and points to OLD IntegrityClient dir");
		}
		
		File oldClientDir = new File(config.getString(PROPERTY_OLD_CLIENT_DIR));
		if (oldClientDir!= null && oldClientDir.isDirectory()) {
			if (WindowsUtils.deleteFolder(oldClientDir)) {
				l.info("Old client directory ["+oldClientDir.getAbsolutePath()+"] was removed");
			} else {
				l.warn("Cannot delete Old Client directory ["+oldClientDir.getAbsolutePath()+"]" );
			}
		}
    }
    
    private static void installNewIntegrityClient() {
    	// INSTALL NEW CLIENT
    	Timestamp start, stop;
    	try {
        	start = new Timestamp(new java.util.Date().getTime());
        	System.out.println("[" + start + "] Installing new Integrity Client...");
        	String integrityInstallator = config.getString(PROPERTY_NEW_CLIENT_INSTALLATOR_DIR) + File.separator + "mksclient.exe";
        	
        	String propertiesFileName= "mksclient.properties";   
        	String pathToProcess = integrityInstallator;
        	
    		if (integrityInstallator.contains("\\")) {
    			File f = new File(pathToProcess);
    			integrityInstallator = f.getName();
    		}
    		
    	    int numberOfProcesses = WindowsUtils.numberOfRunningProcesses(integrityInstallator);
    	    if (numberOfProcesses > 0 ) {
    	    	if (numberOfProcesses == 1){
    	    		l.warn("find already running process "+ integrityInstallator + " process will be terminated");
    	    	} else {
    	       		l.warn("find already running "+numberOfProcesses+" \""+ integrityInstallator + "\" processes, all of them will be terminated");
    	       		
    	    	}
    	       	WindowsUtils.killProcess(integrityInstallator);
    	    }    			
    	    WindowsUtils.startProcess(pathToProcess, "-f "+propertiesFileName, processWorkTimeLimit , processCheckInterval);      
        	stop = new Timestamp(new java.util.Date().getTime());
    		if (WindowsUtils.checkExitCode() == 0){
    			String message = "[" + stop + "] New Integrity Client was successful installed. Time duration: " +Utils.timeDuration(start);
    			System.out.println(message);
    			l.info(message);
    			
    		}else {
    			String message = "[" + stop + "] Error occurred while installing Integrity Client. Time duration: " +Utils.timeDuration(start);
    			System.out.println(message);
    			l.error(message);
    			return;
    		}
		} catch (IOException e) {
			l.error(e);
			l.error("Please check if \""+PROPERTY_NEW_CLIENT_INSTALLATOR_DIR+"\" property is properly set");
		}
    }
    
    
    private static void backUpMksDirs(){
    	File mks = new File(mksDir);
    	File sidist = new File(SIDistDir);
    	String backupDir = "mks_backups" + File.separator+ new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

    	File mksBackup = new File(appDir+File.separator+backupDir+ File.separator+".mks");
    	File SIDistBackup = new File(appDir+File.separator+backupDir+File.separator+"SIDist");
    	
    	System.out.println("Backuping .mks, SIDist folders to " + appDir+File.separator+backupDir);
    	l.info("Backuping .mks, SIDist folders to " + appDir+File.separator+backupDir);
    	mksBackup.mkdirs();
    	SIDistBackup.mkdirs();
    	
    	if(mks.isDirectory()){
    		try {
				Utils.copyFolder(mks, mksBackup);
				l.info("Directory " +mks+" was copied to "+mksBackup);
			} catch (IOException e) {
				l.error(e);
				abortApp();
				e.printStackTrace();
			}
    	} else {
    		l.error("Cannot find "+mks.getAbsolutePath() + " directory");
    	}
    	
    	if(sidist.isDirectory()){
    		try {
				Utils.copyFolder(sidist, SIDistBackup);
				l.info("Directory " +sidist+" was copied to "+SIDistBackup);
			} catch (IOException e) {
				l.error(e);
				abortApp();
				e.printStackTrace();
			}
    	} else {
    		l.error("Cannot find "+sidist.getAbsolutePath() + " directory");
    	}
    	
    }
    
    private static void replaceViewsetsSettings(File dir, String hostname, String port){
    	
	   	 //String p1 = "<Setting name=\"server.port\">";
	   	 String p3 = "<Setting name=\"server.hostname\">";
	   	 String p2 = "</Setting>";
	   	 
	   	 String hostSetting = p3 + hostname + p2;
	   	// String portSetting = p1 + port +p2;
	   	 
	   	// String portRegex = Pattern.quote(p1) + "(.*?)"  + Pattern.quote(p2);
	   	 String hostRegex = Pattern.quote(p3) + "(.*?)"  + Pattern.quote(p2);
	   	 
	   	 Pattern hostPattern = Pattern.compile(hostRegex);
	   	// Pattern portPatter = Pattern.compile(portRegex);


    	if (!dir.isDirectory()) {
    		l.error("Error while replacing hostnames and ports in viewsets directory. The directory " + dir.getAbsolutePath() + " doesn't exists");
    		return;
    	}
    	
    	try {
		    	File[] arrayOfViewsets = dir.listFiles();
		    	for (File viewset : arrayOfViewsets) {
		    		String tmpFile = dir+File.separator+"tmp_"+viewset.getName();
		    		BufferedWriter tmpBw = new BufferedWriter(new FileWriter(tmpFile));
		    		
		    		try {
		    			Scanner sc = new Scanner(viewset);
		    			String line; 
		    			while(sc.hasNextLine()) {
		    				line = sc.nextLine();
			    		   	Matcher hostMatcher = hostPattern.matcher(line);
			    		   	if (hostMatcher.find()) {
			    		   		String oldHostname = hostMatcher.group(1);
			    		   		if (oldServerHostnames.contains(oldHostname)) {
			    		   			l.info("Replacing hostname in file: " + viewset.getName() + "[OLD Value: "+line.trim() + " -> NEW Value: " + hostSetting+" ]");
			    		   			line = hostSetting;
			    		   		}
			    		   	}
		    				tmpBw.write(line+"\n");
		    			}
		    			sc.close();
		    			tmpBw.flush();
		    			tmpBw.close();
		    		} catch (FileNotFoundException e){
		    			l.error(e);
		    		} 
		    		
		    		viewset.delete();
		    		File newFile = new File(tmpFile);
		    		newFile.renameTo(viewset);
		    		/*
		    		
		    		FileInputStream fs = new FileInputStream(viewset.getAbsoluteFile());
		    		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		    		FileWriter writer = new FileWriter(viewset.getAbsoluteFile());
		    		
		    		String line = br.readLine();
		    		while (line != null) {
		    		   	Matcher hostMatcher = hostPattern.matcher(line);
		    		   	if (hostMatcher.find()) {
		    		   		String oldHostname = hostMatcher.group(1);
		    		   		if (oldServerHostnames.contains(oldHostname)) {
		    		   			//line = line.replaceAll(hostRegex, hostSetting)
		    		   			log.info("Replacing hostname in file: " + viewset.getName() + "[OLD Value: "+line + " -> NEW Value: " + hostSetting);
		    		   			line = hostSetting;
		    		   		}
		    		   	}
		    			//line = line.replaceAll(portRegex, portSetting);
		    			// line = line.replaceAll(hostRegex, hostSetting);
		    			writer.write(line);
		    			writer.write(System.getProperty("line.separator"));
		    			line = br.readLine();
		    		}
		    		writer.flush();
		    		writer.close();	
		    		br.close();
		    		fs.close();
		    	}   
		    	
		    	 		*/
		    	}
    	} catch (IOException e) {
    		l.error(e);
    	}

    }
    
    private static boolean checkProperties(){
    	File oldClientFolder = new File(oldClientDir);
    	File installatorFolder = new File(installAppDir);
		File mksclient = new File(installatorFolder + File.separator + "mksclient.exe");
		File mksprop = new File(installatorFolder + File.separator + "mksclient.properties");
		
    	if (oldClientFolder.isDirectory()){
    		l.info("Found old Client app directory [" + oldClientFolder.getAbsolutePath()+"]");
    	} else {
    		l.error("Old Client app directory not found under [" + oldClientFolder.getAbsolutePath() +"]");
    		return false;
    	}
    	if (installatorFolder.isDirectory()) {
    		if (mksclient.exists()) {
    			l.info("Found mksclinet.exe");
    		} else {
    			l.error("Can't find mksclient.exe in the specified folder ["+mksclient.getAbsolutePath()+"]");
    			return false;
    		}
    		if (mksprop.exists()) {
    			l.info("Found mksclient.properties");
    			try {
					clientInstallProp = new PropertiesConfiguration(mksprop);
					newIntegrityClientDir = clientInstallProp.getString("USER_INSTALL_DIR");
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}
    		} else {
    			l.error("Can't find mksclient.properties in the specified folder ["+mksprop.getAbsolutePath()+"]");
    		}
    	} else {
    		l.error("Can't find the specified folder ["+installatorFolder.getAbsolutePath()+"] Please check property " + PROPERTY_NEW_CLIENT_INSTALLATOR_DIR);
    		return false;
    	}
    	return true;
    }
}

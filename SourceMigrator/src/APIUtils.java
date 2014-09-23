

import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.Member;
import models.Project;
import models.Sandbox;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.SelectionList;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.mks.api.util.APIVersion;


public class APIUtils{
	
	public static final String DELETION_POLICY_NONE= "none";
	public static final String DELETION_POLICY_ALL= "all";
	public static final String DELETION_POLICY_MEMBER= "member";
		
	private static final Logger log = LogManager.getLogger(APIUtils.class.getName());
	private String hostname, username, password;
	private int port;
	private List<Project> projects;
	private static CmdRunner clientCr;
	private CmdRunner serverCr;
	
	public static Sandbox createSandbox(String projectName, String hostname, String projectRevision,String devPath, String location) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("createsandbox");
		cmd.addOption(new Option("project", projectName));
		
		if (hostname != null) {
			cmd.addOption(new Option("hostname", hostname));
		}
				
		if (location != null) {
			cmd.addSelection(location);
		} else {
			cmd.addSelection("c:\\ptc_temp\\sandboxes\\");
			log.warn("Sandbox was created in default path");
		}
		
		if (projectRevision != null) {
			cmd.addOption(new Option("projectRevision", projectRevision));
		}
		
		if (devPath != null) {
			cmd.addOption(new Option("devpath", devPath));
		}
	

		Response res = null;
		try {
			// Human readable version of the command for logging purposes
			String[] command = cmd.toStringArray();
			String thecmd = "";
			for (int i = 0; i < command.length; i++) {
				thecmd = thecmd + " " + command[i];
			}
			log.info(thecmd);
			res = clientCr.execute(cmd);
		} catch (APIException e) {
			log.error(e);
		}
		List<Sandbox> sandboxes = getSandboxes(projectName, null);
		if (!sandboxes.isEmpty()) {
			return sandboxes.get(0);
		}
		
		return null;
		

	}
	public static void dropAllSandboxes(String deletionPolicy) {
		List<Sandbox> sandboxes = getSandboxes(null, null);
		List<String> sandboxesNames = new LinkedList<String>();
		
		for (Sandbox s : sandboxes) {
			sandboxesNames.add(s.getName());
		}
		dropSanboxes(sandboxesNames, deletionPolicy);
	}
	public static void dropSanboxes(List<String> sandboxes, String deletionPolicy){
    	
    	for (String sandbox : sandboxes) {
    		Command cmd = new Command();
    		cmd.setApp(Command.SI);
    		cmd.setCommandName("dropsandbox");
    		cmd.addOption(new Option("yes"));
    		cmd.addOption(new Option("delete", deletionPolicy));
    		cmd.addSelection(sandbox);
			
			Response res = null;
			try {
				// Human readable version of the command for logging purposes
				String[] command = cmd.toStringArray();
				String thecmd = "";
				for (int i = 0; i < command.length; i++) {
					thecmd = thecmd + " " + command[i];
				}
				log.info(thecmd);

					res = clientCr.execute(cmd);
				
			
			} catch (APIException e) {
				log.error(e);
			}
			
    	}
    }
	
	
	public static void dropSandbox(String sandbox, String deletionPolicy) {
		LinkedList<String> ll = new LinkedList<String>();
		ll.add(sandbox);
		dropSanboxes(ll, deletionPolicy);
	}
	public static void exitIntegrityClient() throws InterruptedException{
    	try {
			Process p = Runtime.getRuntime().exec("cmd /c im exit --noshutdown");
    		//Process p = Runtime.getRuntime().exec("im exit --noshutdown");
			p.waitFor();
		} catch (IOException e1) {
			log.error(e1);
		}
    }
	
	public static List<Sandbox> getAllSandboxes() {
		return getSandboxes(null, null);
	}
	 
	public static Sandbox getSandbox(String sandboxName) {
		List<Sandbox> all = getAllSandboxes();
		
		for (Sandbox sandbox : all) {
			if (sandbox.getName().equals(sandboxName)) {
				return sandbox;
			}
		}
		return null;
	}
	
	public static List<Sandbox> getSandboxes(String project, String hostname) {
		//Map<String, String> opt = new HashMap<String, String>();
		List<Sandbox> sandboxes = new LinkedList<Sandbox>();
		Command cmd = new Command(Command.SI , "sandboxes");

		String[] command = cmd.toStringArray();
		String thecmd = "";
		for (int i = 0; i < command.length; i++) {
			thecmd = thecmd + " " + command[i];
		}
		log.info(thecmd);
		Response response;
		try {
			response = clientCr.execute(cmd);
			if (response != null) {
				WorkItemIterator wii = response.getWorkItems();
				while (wii.hasNext()) {
					boolean addToList = true;
					Sandbox sandbox = null;
					Map<String, String> item = new HashMap<String, String>();
					WorkItem wi = wii.next();
					Iterator<Field> iterator = wi.getFields();
					while (iterator.hasNext()) {
						Field field = iterator.next();
						item.put(field.getName(), field.getValueAsString());	
					}
					sandbox = new Sandbox(item);
					if (hostname != null) {

						if (project != null) {
							if (hostname.equals(sandbox.getHostname()) && project.equals(sandbox.getProject()) ) {
								sandboxes.add(sandbox);
							}
						} else if (hostname.equals(sandbox.getHostname())) {
							sandboxes.add(sandbox);
						}
					} else if (project != null) {
						if ( project.equals(sandbox.getProject())) {
							sandboxes.add(sandbox);
						}
					} else {
						sandboxes.add(sandbox);
					}
				}
			}
		} catch (APIException e) {
			log.error(e);
		}

		return sandboxes;
	}

	
    
	public APIUtils() {
	
	}
    
	public static void addLabel(String label, List<Member> members, String sandbox, String project, String hostname, String projectRev) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("addlabel");
		cmd.addOption(new Option("label",label));
		
		if (sandbox != null) {
			cmd.addOption(new Option("sandbox", sandbox));
		}
		if (project != null) {
			cmd.addOption(new Option("project", project));
		}
		if (hostname != null) {
			cmd.addOption(new Option("hostname", hostname));
		}
		
		if (projectRev != null) {
			cmd.addOption(new Option("projectRevision", projectRev));
		}
		
		for (Member member : members) {
			cmd.addSelection(member.getName());
		}
		Response res = null;
		try {
			// Human readable version of the command for logging purposes
			String[] command = cmd.toStringArray();
			String thecmd = "";
			for (int i = 0; i < command.length; i++) {
				thecmd = thecmd + " " + command[i];
			}
			log.info(thecmd);
			res = clientCr.execute(cmd);

		
		} catch (APIException e) {
			log.error(e);
		}
	}

	public void addMember(String description, String sandbox, String hostname, String memberLocation, String changePackageId) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("add");
		cmd.addOption(new Option("hostname", hostname));
		if (description != null) {
			cmd.addOption(new Option("description", description));			
		}

		cmd.addOption(new Option("sandbox", sandbox));
		cmd.addSelection(memberLocation);
		
		if (changePackageId != null) {
			cmd.addOption(new Option("cpid", changePackageId));
		}
		
		runCommand(cmd, false);

	}
    
    public void addMemberAttr() {
		
		
	}

	public void addMemberFromArchive() {
		// TODO Auto-generated method stub
		
	}
    
    
	/**
	 * Adds all members from specified directory, except *.pj files
	 * @param dir -- directory containing members, must be subdir of sandbox
	 * @param sandbox -- sandbox where members will be added
	 */
	public void addMembersFromDir(String dir, String sandbox, String hostname){
		File memberDir = new File(dir);
		if (!memberDir.exists()) {
			log.error("Directory " + memberDir+ " not exist");
			return;
		} 
		
		List<File> files = Utils.getListOfFiles(new File(dir));
		for (File file : files) {
			String filename = file.getName();
			String extentionTab[] = filename.split("\\.");
			if (extentionTab.length == 2) {
				String extention = extentionTab[1];
				if (!extention.equals("pj")) {
					addMember(null, sandbox, hostname, file.getAbsolutePath(), null);
				}
			}
		}
	
		
	}
    
	
	public void addProject(String projectName) {
    	Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("addproject");
		cmd.addSelection(projectName);
		runCommand(cmd, true);
	}

	public void addProjectAttr() {
		// TODO Auto-generated method stub
		
	}

	
	public void addProjectAttributes(String project, Map<String, String> attrs) {
		if (attrs.isEmpty()) {
			return;
		}
		
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("addprojectattr");
		cmd.addOption(new Option("project", project));
		
		String attrsVals = new String();
		for (String attr : attrs.keySet()) {
			String value = attrs.get(attr);
			attrsVals+=" "+attr+"="+value;
		}
		cmd.addOption(new Option("attr", attrsVals));
		

			runCommand(cmd, true);

	}
	
	public void addProjectLabel(String label, String project) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("addprojectlabel");
		cmd.addOption(new Option("project", project));
		cmd.addOption(new Option("label", label));
		runCommand(cmd, true);
	}
	
	public static void addSandboxAttr(String sandbox, String attrKey, String attrVal) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("addsandboxattr");
		cmd.addOption(new Option("sandbox", sandbox));
		MultiValue mv = new MultiValue("=");
		mv.add(attrKey);
		mv.add(attrVal);
		cmd.addOption(new Option("attribute", mv));
		Response res = null;
		try {
			// Human readable version of the command for logging purposes
			String[] command = cmd.toStringArray();
			String thecmd = "";
			for (int i = 0; i < command.length; i++) {
				thecmd = thecmd + " " + command[i];
			}
			log.info(thecmd);
			res = clientCr.execute(cmd);
		} catch (APIException e) {
			log.error(e);
		}
	}

	public void appendCheckpointDesc(String projectRev, String description, String project) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("appendcheckpointdesc");
		cmd.addOption(new Option("projectRevison", projectRev));
		cmd.addOption(new Option("description", description));
		cmd.addOption(new Option("project", project));
		runCommand(cmd, true);		
	}
	
	public static void  deleteLabel(String label, String member, String project, String sandbox, String projectRevision) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("deletelabel");
		cmd.addOption(new Option("label", label));
		
		if(sandbox != null) {
			cmd.addOption(new Option("sandbox", sandbox));
		}
		if (project != null) {
			cmd.addOption(new Option("project", project));
		}
		if (projectRevision != null) {
			cmd.addOption(new Option("projectRevision", projectRevision));
		}
		
		cmd.addSelection(member);
		Response res = null;
		try {
			// Human readable version of the command for logging purposes
			String[] command = cmd.toStringArray();
			String thecmd = "";
			for (int i = 0; i < command.length; i++) {
				thecmd = thecmd + " " + command[i];
			}
			log.info(thecmd);
			res = clientCr.execute(cmd);
		} catch (APIException e) {
			log.error(e);
		}		
	}
	
	public static void createMetricInfo(String name, String description) {
		
	}
	
 	public void addProjectMetric() {
		// TODO Auto-generated method stub
		
	}
	
	public void addSubproject() {
	
	}
	
	public void checkOutMembers(List<String> members, String sandbox){
		Command cmd = new Command();  
		cmd.setApp(Command.SI);
		cmd.setCommandName("co");
		cmd.addOption(new Option("sandbox", sandbox));
		if (members != null) {
			for (String member : members) {
				cmd.addSelection(member);
			}
		}
		
		
			runCommand(cmd, false);

		
	}

	public void checksInMembersOfSandbox(String label, String description,String members[]) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("ci");
		cmd.addOption(new Option("label", label));
		cmd.addOption(new Option("description", description));
		
		if (members != null) {
			for (String member : members) {
				cmd.addSelection(member);
			}
		}
		
		
			runCommand(cmd, false);

		
	}
	
	public void connectToIntegrity(String userName, String password, String hostname, String port){
		this.username = userName;
		this.password = password;
		this.hostname = hostname;
		this.port = Integer.parseInt(port);
		
		String VERSION = APIVersion.getAPIReleaseVersion();
		int MAJOR_VERSION = Integer.parseInt(VERSION.substring(0, VERSION.indexOf('.'))); 
		int MINOR_VERSION = Integer.parseInt(VERSION.substring(VERSION.indexOf('.')+1, VERSION.indexOf(' ')));
		
		
		//client integration
		try {
			clientCr = IntegrationPointFactory.getInstance().createLocalIntegrationPoint(MAJOR_VERSION, MINOR_VERSION).getCommonSession().createCmdRunner();
			clientCr.setDefaultHostname(hostname);
			clientCr.setDefaultUsername(userName);
			clientCr.setDefaultPassword(password);
			clientCr.setDefaultPort(Integer.parseInt(port));
			
			//server integration
			serverCr = IntegrationPointFactory.getInstance().createIntegrationPoint(hostname, Integer.parseInt(port), MAJOR_VERSION, MINOR_VERSION).createSession(userName, password).createCmdRunner();
			serverCr.setDefaultHostname(hostname);
			serverCr.setDefaultUsername(userName);
			serverCr.setDefaultPassword(password);
			serverCr.setDefaultPort(Integer.parseInt(port));
			
		} catch (APIException e) {
			e.printStackTrace();
		}
		

	}
	
	public void createDevPath(String project, String projectRevision,String devpath) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("createdevpath");
		cmd.addOption(new Option("project", project));
		
		if (projectRevision != null) {
			cmd.addOption(new Option("projectRevision", projectRevision));
		}
		
		if (devpath != null) {
			cmd.addOption(new Option("devpath", devpath));
		}
		
			runCommand(cmd, true);

		
	}

	/***
	 *  Crates new project on server, then creates sandbox in the specified folder and add all files
	 * @param projectName - project name
	 * @param projectRevision - project revision
	 * @param devPath - development path
	 * @param projectLocation - project location
	 * @param addAllMembersFromProjectLocation - whether the files from project locatiion should be added to sandbox
	 * @return null if error occured or Sandbox to new created project.
	 */
	public Sandbox createNewProjectAndSandbox(String projectName, String projectRevision,String devPath, String projectLocation, boolean addAllMembersFromProjectLocation) {
		if (getProject(projectName)!=null) {
			log.error("Project "+projectName+ " already exist on" +getHostname()+", creation aborted");
			return null;
		}
		
		Project project = createProject(projectName);
		if (project != null) {
			log.info("Project " + projectName + " has been created on " + getHostname());
		} else {
			log.error("Error occure when trying to create project "+projectName+" on "+getHostname());
			return null;
		}
		
		Sandbox sandbox = createSandbox(project.getName(),getHostname(), projectRevision, devPath, projectLocation);
		if (sandbox != null) {
			log.info("Sandbox " +sandbox.getName() + " -> "+sandbox.getProject()+ " has been created");
		} else {
			log.error("Error occure when creating sandbox");
			return null;
		}
		if (addAllMembersFromProjectLocation) {
	        addMembersFromDir(projectLocation, sandbox.getName(), sandbox.getHostname());  
		}
		
		return sandbox;
		
	}

	public Project createProject(String projectName) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("createproject");
		cmd.addSelection(projectName);
		runCommand(cmd, true);
		List<Project> projects =  getProjects(true,projectName);
		if (!projects.isEmpty()) {
			return projects.get(0);
		}
		
		return null;

		
	}
	
	public void createSubProject(String projectName, String subprojectLocation) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("createsubproject");
		cmd.addOption(new Option("project", projectName));
		cmd.addSelection(subprojectLocation);
		
			runCommand(cmd, true);

	}
	
	public void viewhistory(String member, String sandbox, String devPath, String projectRev) {
		Map<String, String> options = new HashMap<String, String>();
		if (sandbox != null) {
			options.put("sandbox", sandbox);			
		}
		if (devPath != null) {
			options.put("devpath", devPath);
		}
		if (projectRev != null) {
			options.put("projectRevision", projectRev);
		}		
		List<Map<String, String>> siElements = getSIItems("viewhistory", options, null, member, false);
		
		for (Map<String, String> siElement : siElements) {
			for (String key : siElement.keySet()) {
				log.info("key= "+key+" value= " +siElement.get(key));
			}
		}
		
	}
	
	public void viewRevision(String member, String revision, String sandbox, String project) {
		Map<String, String> options = new HashMap<String, String>();
		if (sandbox != null)
			options.put("sandbox", sandbox);
		if (revision != null)
			options.put("revision", revision);
		if (project != null)
			options.put("project", project);
		if (hostname != null) {
			options.put("hostname", hostname);
		} else {
			options.put("hostname", getHostname());
		}
		
		List<Map<String, String>> contents = getSIItems("viewrevision", options, null, member, false);
		for( Map<String, String> content : contents) {
			for (String key : content.keySet()) {
				log.info("key: " + key + " val: "+ content.get(key));
			}
		}
	}
	
	public void deleteProject(String projectName) {
		LinkedList<String> projects = new LinkedList<String>();
		projects.add(projectName);
		deleteProjects(projects);
	}

	public void deleteProjects(List<String> projectNames){
		
		for (String projectName : projectNames) {
			String command = "cmd /c si deleteproject --yes --forceConfirm=yes --hostname="+hostname+" --password="+password+" --user="+username+" --port="+port+" --batch --mark "+projectName;
			
			try {
				Process p = Runtime.getRuntime().exec(command);
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				log.error(e1);
			}	
			
			command = "cmd /c si deleteproject --yes --forceConfirm=yes --hostname="+hostname+" --password="+password+" --user="+username+" --port="+port+" --batch --dump";
			
			try {
				Process p = Runtime.getRuntime().exec(command);
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				log.error(e1);
			}	
			
			command = "cmd /c si deleteproject --yes --forceConfirm=yes --hostname="+hostname+" --password="+password+" --user="+username+" --port="+port+" --batch --commit";
			
			try {
				Process p = Runtime.getRuntime().exec(command);
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				log.error(e1);
			}	
			
			
		}
	}
	
	public void deleteRevision(String range, String member) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("deleterevision");
		cmd.addOption(new Option("range", range));
		cmd.addOption(new Option("confirm"));
		cmd.addOption(new Option("confirminuse"));
		cmd.addSelection(member);
		runCommand(cmd, true);
		
	}
	
	public void demoteProject(String project, String state) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("demoteproject");
		if (state != null) {
			cmd.addOption(new Option("state", state));
		}
		cmd.addOption(new Option("project",project));
		runCommand(cmd, true);
	}
	
	public static void diffFiles(String file1, String file2) {
		
	}
	
	public void displayData() {
		getProjects();
		  for(Project project : projects) {
	        	System.out.println(project);
	        	List<Sandbox> sandboxes = getSandboxes(project.getName(),this.getHostname());
	        	for (Sandbox sandbox : sandboxes){
	        		System.out.println("\t"+sandbox);
	        		List<Member> members = getMembers(sandbox.getName(), sandbox.getHostname());
	        		for (Member member : members) {
	        			System.out.println("\t\t"+member);
	        		}
	        	}
	        }
	}

	public void dropAndDeleteAllProjects() {
		List<Project> projects = getProjects();
		for (Project project : projects) {
			String projectName = project.getName();
			dropProject(projectName);
			deleteProject(projectName);
		}
	
	}

	public void dropDevPath(String project, String devPath) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("dropdevpath");
		cmd.addOption(new Option("forceconfirm", "yes"));
		cmd.addOption(new Option("project", project));
		cmd.addOption(new Option("devpath", devPath));
		runCommand(cmd, true);
	}
	
	public void dropProjectAttr(String attrKey, String attrVal, String project) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("dropprojectattr");
		cmd.addOption(new Option("attribute", attrKey +"="+attrVal));
		runCommand(cmd, true);
	}

	public void freezMember(String member, String project, String sandbox) {
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("freeze");
		if (project != null) {
			cmd.addOption(new Option("project",project));
		}
		if (sandbox != null) {
			cmd.addOption(new Option("sandbox", sandbox));
		}
		cmd.addSelection(member);
		runCommand(cmd, true);
	}
	
	public void dropProject(String projectName){
		Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName("dropproject");
		cmd.addOption(new Option("forceConfirm", "yes"));
		cmd.addSelection(projectName);
		
		runCommand(cmd, true);
		
	}
	
	public void memberInfo(String member, String project) {
		Map<String,String> options = new HashMap<String, String>();
		options.put("project", project);
		getSIItem("memberinfo", options, null, member, true);
	}

	public void endSession(){
		try {
			this.serverCr.release();
			clientCr.release();
			
		} catch (APIException e) {
			log.error(e);
		}
	}

	public String getHostname() {
		return hostname;
	}

	/***
	 * 
	 * @param sandboxName - name of sandbox on client
	 * @return list of members on the specified sandbox
	 */
	public List<Member> getMembers(String sandboxName, String hostname) {
		List<Member> listOfMembers = new LinkedList<Member>();
		Map<String, String> options = new HashMap<String, String>();
		options.put("sandbox", sandboxName);
		if (hostname != null) {
			options.put("hostname", hostname);
		} else {
			options.put("hostname", getHostname());
		}
		List<Map<String, String>> membersPrefs  = getSIItems("viewsandbox", options,null,null, false);
		for (Map<String,String> memberPrefs : membersPrefs) {
			listOfMembers.add(new Member(memberPrefs));
		}
		return listOfMembers;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}
	
	public Project getProject(String projectName) {
		Project project = null;
		List<Project> projects = getProjects(true, projectName);
    	if (!projects.isEmpty()) {
    		project = projects.get(0);
    	}
		return project;
	}
	
	/***
	 * Returns list of all projects
	 * @return list of all projects on server
	 */
	public List<Project> getProjects() {
		this.projects = getProjects(true, null);
		return projects;
	}

	/**
	 * Return one project if projectName was specified or all projects if not.
	 * @param displaySubprojects - if should include subprojects
	 * @param projectName - project name
	 * @return list of projects
	 */
	public List<Project> getProjects(boolean displaySubprojects, String projectName ) {
		List<String> opts2 = new LinkedList<String>();
		if (displaySubprojects) {
			opts2.add("displaysubs");
		}
		List<Project> projects = new LinkedList<Project>();
		
		List<Map<String, String>> items = getSIItems("projects", null, opts2, null, true);
		if (projectName != null) {
			for (Map<String,String> item : items) {
				Project project = new Project(item);
				if (project.getName().equals(projectName)) {
					projects.add(new Project(item));
					break;
				}	
			}
		} else {
			for (Map<String,String> item : items) {
				projects.add(new Project(item));
			}			
		}
		return projects;
	}

	private Map<String,String> getSIItem(String commandName, Map<String,String> options, List<String> options2, String selection, boolean onServer) {
		List<Map<String, String>> items = getSIItems(commandName, options, options2, selection, onServer);
		if (!items.isEmpty()) {
			return getSIItems(commandName, options, options2, selection, onServer).get(0);
		} 
		return null;
	}
	
	private List<Map<String, String>> getSIItems(String commandName, Map<String,String> options, List<String> options2, String selection, boolean onServer) {

		List<Map<String, String>> items = new LinkedList<>();
	    Command cmd = new Command();
		cmd.setApp(Command.SI);
		cmd.setCommandName(commandName);
		if (options != null ){
			for ( String option : options.keySet()) {
				String optonVal = options.get(option);
				cmd.addOption(new Option(option, optonVal));
			}
		}
		
		if (options2 != null) {
			for (String option : options2) {
				cmd.addOption(new Option(option));
			}
		}
		
		if (selection != null) {
			cmd.addSelection(selection);
		}

		
		try {
			Response response = runCommand(cmd, onServer);
			if (response != null) {
				WorkItemIterator wii = response.getWorkItems();
				while (wii.hasNext()) { 
					Map<String, String> item = new HashMap<String, String>();
					WorkItem wi = wii.next();
					Iterator<Field> iterator = wi.getFields();
					while (iterator.hasNext()) {
						Field field = iterator.next();
						item.put(field.getName(), field.getValueAsString());	
					}
					items.add(item);
				}
			}
		} catch (APIException e) {
			e.printStackTrace();
		}
		return items;
	}

	public String getUsername() {
		return username;
	}

	public void reImportSandboxes(List<String> sandboxes, String user, String password, String hostname, String port){
    	for (String sandbox : sandboxes) {
    		Command cmd = new Command();
    		cmd.setApp(Command.SI);
    		cmd.setCommandName("importsandbox");
    		cmd.addOption(new Option("yes"));
    		cmd.addOption(new Option("user", user));
    		cmd.addOption(new Option("password", password));
    		cmd.addOption(new Option("hostname", hostname));
    		cmd.addOption(new Option("port", port));
    		cmd.addSelection(sandbox);
		
				Response response = runCommand(cmd,false);
			
    	}
    }

	/**
	 * Runs the provided command object and returns the response after the command execution has completed.
	 * 
	 * @param cmd
	 *            com.mks.api.Command object
	 * @return Response object
	 * @throws APIException
	 *             if command encounters error
	 *             <p>
	 *             Create a <code>com.mks.api.Command</code> object and load options/selection, then feed to runCommand to receive response.
	 *             </p>
	 *             <p>
	 *             e.g.<br>
	 *             <code>Command myCmd = new Command(Command.IM, "issues");<br>
	 * myCmd.addOption(new Option("fields","ID,Summary"));<br>
	 * myCmd.addOption(new Option("sortField","ID"));<br>
	 * <br>
	 * Response myRes = api.runCommand(myCmd);</code>
	 *             </p>
	 */
	public Response runCommand(Command cmd, boolean onServer){

		Response res = null;
		try {
			// Human readable version of the command for logging purposes
			String[] command = cmd.toStringArray();
			String thecmd = "";
			for (int i = 0; i < command.length; i++) {
				thecmd = thecmd + " " + command[i];
			}
			log.info(thecmd);
			if (onServer) {
				res = serverCr.execute(cmd);
			} else {
				res = clientCr.execute(cmd);
			}
		
		} catch (APIException e) {
			log.error(e);
		}

		return res;
	}

	public void setDefaultServerConnection(String servername) throws InterruptedException{
    	
    	try {
			Process p = Runtime.getRuntime().exec("cmd /c si setprefs --save --command=connect server.hostname=\""+servername+"\"");
			p.waitFor();
		} catch (IOException e1) {
			log.error(e1);
		}
    	 try {
 			Process p = Runtime.getRuntime().exec("cmd /c im setprefs --save --command=connect server.hostname=\""+servername+"\"");
 			p.waitFor();
 		} catch (IOException e1) {
 			log.error(e1);
 		}	

    }

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}
	
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}




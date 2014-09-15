import java.util.List;

import models.Member;


public interface SI {
	
	public void addMember(String description, String sandbox, String memberLocation, String changePackageId);
	
	public void addLabel();
	public void addMemberAttr();
	public void addMemberFromArchive();
	
	/**
	 * Adds an existing project to the list of top-level projects
	 * @param projectLocation - identifiels a location on the Integrity Server for the existing project to be added,
	 *  use spaces to specify more than one project location
	 */
	public void addProject(String projectLocation);
	
	/**
	 * Creates a new, empty project on the Integrity Server in a specified directory.
	 * @param projectLocation new project directory
	 */
	public void createProject(String projectLocation);
	
	public void createSubProject(String projectName, String subprojectLocation);
	
	/**
	 * Creates a new Sandbox on the client machine in a specified directory
	 * @param sandboxDirectory
	 */
	public void createSandbox( String projectName, String projectRevision,String devPath);
	
	/**
	 * Creates a development path from a specific project checkpoint
	 * @param project
	 * @param projectRevision
	 * @param devpath
	 */
	public void createDevPath( String projectName, String projectRevision,String devPath);

	public void addProjectAttr();
	public void addProjectLabel();
	public void addProjectMetric();
	public void addSubproject();
	
	/***
	 * Checks in and saves changes to Sandbox members. If any members is not specified, applies to all member of an associated Sandbox.
	 * @param label
	 * @param description
	 * @param member
	 */
	public void checksInMembersOfSandbox(String label, String description, String members[] );
	
	//public void checksOutMembers()
	
	/***
	 * Returns list of members in sandbox
	 * @param sandboxName
	 * @return
	 */
	public List<Member> getMembers(String sandboxName);
	
	/**
	 * Checks out members, typically for modification, if no members are specified, si co applies to all Sandbox members;
	 * @param members
	 */
	public void checkOutMembers(List<String> members, String sandbox);
}


public interface SI {
	public void addMember();
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
	
	/**
	 * 
	 * @param sandboxDirectory
	 */
	public void createSandbox(String sandboxDirectory);
	
	public void addProjectAttr();
	public void addProjectLabel();
	public void addProjectMetric();
	public void addSubproject();

}

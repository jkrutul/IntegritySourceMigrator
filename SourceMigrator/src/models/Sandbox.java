package models;

import java.util.Map;

public class Sandbox {
	private String name, project, parent, isSubsandbox, server,
			developmentPath, buildRevision;

	public static final String fieldNames[] = { "sandboxName", "projectName",
			"parentSandbox", "isSubsandbox", "server", "developmentPath",
			"buildRevision" };

	public Sandbox(String name, String project, String parent,
			String isSubsandbox, String server, String developmentPath,
			String buildRevision) {
		super();
		this.name = name;
		this.project = project;
		this.parent = parent;
		this.isSubsandbox = isSubsandbox;
		this.server = server;
		this.developmentPath = developmentPath;
		this.buildRevision = buildRevision;
	}

	public Sandbox() {

	}

	public Sandbox(Map<String, String> sandboxProp) {
		for (String field : sandboxProp.keySet()) {
			String value = sandboxProp.get(field);
			if (field.equals(fieldNames[0])) {
				setName(value);
			} else if (field.equals(fieldNames[1])) {
				setProject(value);
			} else if (field.equals(fieldNames[2])) {
				setParent(value);
			} else if (field.equals(fieldNames[3])) {
				setIsSubsandbox(value);
			} else if (field.equals(fieldNames[4])) {
				setServer(value);
			} else if (field.equals(fieldNames[5])) {
				setDevelopmentPath(value);
			} else if (field.equals(fieldNames[6])) {
				setBuildRevision(value);
			}
		}
	}

	@Override
	public String toString() {
		return "Sandbox [name=" + name + ", project=" + project + ", parent="
				+ parent + ", isSubsandbox=" + isSubsandbox + ", server="
				+ server + ", developmentPath=" + developmentPath
				+ ", buildRevision=" + buildRevision + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getIsSubsandbox() {
		return isSubsandbox;
	}

	public void setIsSubsandbox(String isSubsandbox) {
		this.isSubsandbox = isSubsandbox;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDevelopmentPath() {
		return developmentPath;
	}

	public void setDevelopmentPath(String developmentPath) {
		this.developmentPath = developmentPath;
	}

	public String getBuildRevision() {
		return buildRevision;
	}

	public void setBuildRevision(String buildRevision) {
		this.buildRevision = buildRevision;
	}
	
	public String getHostname() {
		return getServer().split(":")[0];
	}

}

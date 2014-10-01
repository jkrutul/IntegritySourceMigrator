package com.ptc.sourcemigrator.models;

import java.util.Map;

public class Sandbox {
	private String name, project, parent,
			isSubsandbox, server, developmentPath,
			buildRevision, isShared, sandboxScope,
			isReferenced, isSparse;

	public Sandbox() {

	}

	public Sandbox(Map<String, String> sandboxProp) {
		for (String field : sandboxProp.keySet()) {
			String value = sandboxProp.get(field);
			if (field.equals("sandboxName")) {
				setName(value);
			} else if (field.equals("projectName")) {
				setProject(value);
			} else if (field.equals("parentSandbox")) {
				setParent(value);
			} else if (field.equals("isSubsandbox")) {
				setIsSubsandbox(value);
			} else if (field.equals("server")) {
				setServer(value);
			} else if (field.equals("developmentPath")) {
				setDevelopmentPath(value);
			} else if (field.equals("buildRevision")) {
				setBuildRevision(value);
			} else if (field.equals("shared")) {
				setIsShared(value);
			} else if (field.equals("sandboxScope")) {
				setSandboxScope(value);
			} else if (field.equals("isReferenced")) {
				setIsReferenced(value);
			} else if (field.equals("sparse")) {
				setIsSparse(value);
			}
		}
	}

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
	
	public void addSandboxProps(Map<String,String> sandboxProps) {
		for (String field : sandboxProps.keySet()) {
			String value = sandboxProps.get(field);
			if (field.equals("sandboxName")) {
				setName(value);
			} else if (field.equals("projectName")) {
				setProject(value);
			} else if (field.equals("parentSandbox")) {
				setParent(value);
			} else if (field.equals("isSubsandbox")) {
				setIsSubsandbox(value);
			} else if (field.equals("server")) {
				setServer(value);
			} else if (field.equals("developmentPath")) {
				setDevelopmentPath(value);
			} else if (field.equals("buildRevision")) {
				setBuildRevision(value);
			} else if (field.equals("shared")) {
				setIsShared(value);
			} else if (field.equals("sandboxScope")) {
				setSandboxScope(value);
			} else if (field.equals("isReferenced")) {
				setIsReferenced(value);
			} else if (field.equals("sparse")) {
				setIsSparse(value);
			}
		}
	}



	public String getBuildRevision() {
		return buildRevision;
	}

	public String getDevelopmentPath() {
		return developmentPath;
	}

	public String getHostname() {
		return getServer().split(":")[0];
	}

	public String getIsReferenced() {
		return isReferenced;
	}

	public String getIsShared() {
		return isShared;
	}

	public String getIsSparse() {
		return isSparse;
	}

	public String getIsSubsandbox() {
		return isSubsandbox;
	}

	public String getName() {
		return name;
	}

	public String getParent() {
		return parent;
	}

	public String getProject() {
		return project;
	}

	public String getSandboxScope() {
		return sandboxScope;
	}

	public String getServer() {
		return server;
	}

	public void setBuildRevision(String buildRevision) {
		this.buildRevision = buildRevision;
	}

	public void setDevelopmentPath(String developmentPath) {
		this.developmentPath = developmentPath;
	}

	public void setIsReferenced(String isReferenced) {
		this.isReferenced = isReferenced;
	}
	
	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public void setIsSparse(String isSparse) {
		this.isSparse = isSparse;
	}

	public void setIsSubsandbox(String isSubsandbox) {
		this.isSubsandbox = isSubsandbox;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setSandboxScope(String sandboxScope) {
		this.sandboxScope = sandboxScope;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@Override
	public String toString() {
		return "Sandbox [name=" + name + ", project=" + project + ", parent="
				+ parent + ", isSubsandbox=" + isSubsandbox + ", server="
				+ server + ", developmentPath=" + developmentPath
				+ ", buildRevision=" + buildRevision + ", isShared=" + isShared
				+ ", sandboxScope=" + sandboxScope + ", isReferenced="
				+ isReferenced + ", isSparse=" + isSparse + "]";
	}

}

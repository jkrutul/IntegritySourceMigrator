package com.ptc.sourcemigrator.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Project {
	private String name, path, isSubproject, isShared, parent, isVariant, isBuild, buildRevision; //TODO support lastCheckpoint
	private List<String> developmentPaths = new LinkedList<>();
	public Project() {
		
	}
	

	public Project(Map<String,String> projectProps) {
		for (String field : projectProps.keySet()) {
			String value = projectProps.get(field);
			if (field.equals("projectName")) {
				setName(value);
			} else if (field.equals("canonicalPath")) {
				setPath(value);
			} else if (field.equals("isSubproject")) {
				setIsSubproject(value);
			} else if (field.equals("isShared")) {
				setIsShared(value);
			} else if (field.equals("parentProject")) {
				setParent(value);
			} else if (field.equals("isVariant")) {
				setIsVariant(value);
			} else if (field.equals("developmentPath") || field.equals("developmentPaths")) {
				setDevelopmentPath(value);
			}else if (field.equals("isBuild")) {
				setIsBuild(value);
			}else if (field.equals("buildRevision") || field.equals("revision") ) {
				setBuildRevision(value);
			}
		}
	}


	public Project(String name, String path, String isSubproject,
			String isShared, String parent, String isVariant,
			String developmentPath, String isBuild, String buildRevision) {
		super();
		this.name = name;
		this.path = path;
		this.isSubproject = isSubproject;
		this.isShared = isShared;
		this.parent = parent;
		this.isVariant = isVariant;
		String[] devPaths = developmentPath.split(",");
		for(String devPath : devPaths) {
			developmentPaths.add(devPath);
		}
		this.isBuild = isBuild;
		this.buildRevision = buildRevision;
	}
	
	public void addProjectProps(Map<String,String> projectProps) {
		for (String field : projectProps.keySet()) {
			String value = projectProps.get(field);
			
			if (field.equals("projectName")) {
				setName(value);
			} else if (field.equals("canonicalPath")) {
				setPath(value);
			} else if (field.equals("isSubproject")) {
				setIsSubproject(value);
			} else if (field.equals("isShared")) {
				setIsShared(value);
			} else if (field.equals("parentProject")) {
				setParent(value);
			} else if (field.equals("isVariant")) {
				setIsVariant(value);
			} else if (field.equals("developmentPath") || field.equals("developmentPaths")) {
				setDevelopmentPath(value);
			}else if (field.equals("isBuild")) {
				setIsBuild(value);
			}else if (field.equals("buildRevision") || field.equals("revision") ) {
				setBuildRevision(value);
			}
		}
	}

	public String getBuildRevision() {
		return buildRevision;
	}

	public List<String> getDevelopmentPaths() {
		return developmentPaths;
	}

	public String getIsBuild() {
		return isBuild;
	}

	public String getIsShared() {
		return isShared;
	}

	public String getIsSubproject() {
		return isSubproject;
	}

	public String getIsVariant() {
		return isVariant;
	}

	public String getName() {
		return name;
	}

	public String getParent() {
		return parent;
	}

	public String getPath() {
		return path;
	}

	public void setBuildRevision(String buildRevision) {
		this.buildRevision = buildRevision;
	}

	public void setDevelopmentPath(String developmentPath) {
		if (developmentPath != null)
		this.developmentPaths.add(developmentPath);
	}

	public void setIsBuild(String isBuild) {
		this.isBuild = isBuild;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public void setIsSubproject(String isSubproject) {
		this.isSubproject = isSubproject;
	}

	public void setIsVariant(String isVariant) {
		this.isVariant = isVariant;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Project [name=" + name + ", path=" + path + ", isSubproject="
				+ isSubproject + ", isShared=" + isShared + ", parent="
				+ parent + ", isVariant=" + isVariant + ", isBuild=" + isBuild
				+ ", buildRevision=" + buildRevision + ", developmentPaths="
				+ developmentPaths + "]";
	}
}

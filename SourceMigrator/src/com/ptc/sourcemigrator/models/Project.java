package com.ptc.sourcemigrator.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Project {
	private String name, path, isSubproject, isShared, parent, isVariant, isBuild, buildRevision; //TODO support lastCheckpoint
	private final static String fieldNames[] = {"projectName","canonicalPath","isSubproject","isShared","parentProject","isVariant","developmentPath","isBuild","buildRevision", "revision", "developmentPaths"};
	private List<String> developmentPaths = new LinkedList<>();
	public Project() {
		
	}
	
	@Override
	public String toString() {
		String devPaths = new String();
		for (String devPath : developmentPaths) {
			devPaths +=devPath+",";
		}
		
		return "Project [name=" + name + ", path=" + path + ", isSubproject="
				+ isSubproject + ", isShared=" + isShared + ", parent="
				+ parent + ", isVariant=" + isVariant + ", developmentPath="
				+ devPaths + ", isBuild=" + isBuild + ", buildRevision="
				+ buildRevision + "]";
	}

	public Project(Map<String,String> projectProps) {
		for (String field : projectProps.keySet()) {
			String value = projectProps.get(field);
			if (field.equals(fieldNames[0])) {
				setName(value);
			} else if (field.equals(fieldNames[1])) {
				setPath(value);
			} else if (field.equals(fieldNames[2])) {
				setIsSubproject(value);
			} else if (field.equals(fieldNames[3])) {
				setIsShared(value);
			} else if (field.equals(fieldNames[4])) {
				setParent(value);
			} else if (field.equals(fieldNames[5])) {
				setIsVariant(value);
			} else if (field.equals(fieldNames[6]) || field.equals(fieldNames[10])) {
				setDevelopmentPath(value);
			}else if (field.equals(fieldNames[7])) {
				setIsBuild(value);
			}else if (field.equals(fieldNames[8]) || field.equals(fieldNames[9]) ) {
				setBuildRevision(value);
			}
		}
	}
	
	public void addProjectProps(Map<String,String> projectProps) {
		for (String field : projectProps.keySet()) {
			String value = projectProps.get(field);
			
			if (value == null) {
				continue;
			}
			
			if (field.equals(fieldNames[0])) {
				setName(value);
			} else if (field.equals(fieldNames[1])) {
				setPath(value);
			} else if (field.equals(fieldNames[2])) {
				setIsSubproject(value);
			} else if (field.equals(fieldNames[3])) {
				setIsShared(value);
			} else if (field.equals(fieldNames[4])) {
				setParent(value);
			} else if (field.equals(fieldNames[5])) {
				setIsVariant(value);
			} else if (field.equals(fieldNames[6])) {
				setDevelopmentPath(value);
			}else if (field.equals(fieldNames[7])) {
				setIsBuild(value);
			}else if (field.equals(fieldNames[8])) {
				setBuildRevision(value);
			} else if (field.equals(fieldNames[9])) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIsSubproject() {
		return isSubproject;
	}

	public void setIsSubproject(String isSubproject) {
		this.isSubproject = isSubproject;
	}

	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getIsVariant() {
		return isVariant;
	}

	public void setIsVariant(String isVariant) {
		this.isVariant = isVariant;
	}

	public List<String> getDevelopmentPaths() {
		return developmentPaths;
	}

	public void setDevelopmentPath(String developmentPath) {
		if (developmentPath != null)
		this.developmentPaths.add(developmentPath);
	}

	public String getIsBuild() {
		return isBuild;
	}

	public void setIsBuild(String isBuild) {
		this.isBuild = isBuild;
	}

	public String getBuildRevision() {
		return buildRevision;
	}

	public void setBuildRevision(String buildRevision) {
		this.buildRevision = buildRevision;
	}
}

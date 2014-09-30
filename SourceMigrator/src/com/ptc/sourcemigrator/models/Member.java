package com.ptc.sourcemigrator.models;

import java.util.Map;

public class Member {
	private String 	name, memberName, parent, type, memberRev,
					memberRevLocedByMe, workingRevLockedByMe,
					lockrecord, wfdelta, revsyncDelta,
					newRevDelta, merge, canonicalSandbox,
					canonicalMember,projectDevpath, date,
					frozen, author, projectName;
	
	private static final String[] fieldNames = {
												"name",	"parent","type",
												"memberrev","memberrevlockedbyme","workingrevlockedbyme",
												"lockrecord", "wfdelta","revsyncdelta",
												"newrevdelta","merge","canonicalSandbox",
												"canonicalMember","projectDevpath", "date"};
	public String getFrozen() {
		return frozen;
	}

	public void setFrozen(String frozen) {
		this.frozen = frozen;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Member() {
		
	}
	
	public Member(Map<String, String> memberPrefs) {
		for (String field : memberPrefs.keySet()) {
			String value = memberPrefs.get(field);
			if (field.equals("name")) {
				setName(value);
			} else if (field.equals("membername")) {
				setMemberName(value);
			} else if (field.equals("parent")) {
				setParent(value);
			} else if (field.equals("type")) {
				setType(value);
			} else if (field.equals("memberrev") || field.equals("memberrevision")) {
				setMemberRev(value);
			} else if (field.equals("memberrevlockedbyme")) {
				setMemberRevLocedByMe(value);
			} else if (field.equals("workingrevlockedbyme")) {
				setWorkingRevLockedByMe(value);
			}else if (field.equals("lockrecord")) {
				setLockrecord(value);
			}else if (field.equals("wfdelta")) {
				setWfdelta(value);
			}else if (field.equals("revsyncdelta")) {
				setRevsyncDelta(value);
			}else if (field.equals("newrevdelta")) {
				setNewRevDelta(value);
			}else if (field.equals("merge")) {
				setMerge(value);
			}else if (field.equals("canonicalSandbox")) {
				setCanonicalSandbox(value);
			}else if (field.equals("canonicalMember")) {
				setCanonicalMember(value);
			}else if (field.equals("projectDevpath")) {
				setProjectDevpath(value);
			}else if (field.equals("date")) {
				setDate(value);
			}else if (field.equals("frozen")) {
				setFrozen(value);
			}else if (field.equals("author")) {
				setAuthor(value);
			}else if (field.equals("projectname")) {
				setProjectName(value);
			}
			
		}
	}
	
	public void addMemberProps(Map<String, String> memberProps) {
		for (String field : memberProps.keySet()) {
			String value = memberProps.get(field);

			
			if (field.equals("name")) {
				setName(value);
			} else if (field.equals("membername")) {
				setMemberName(value);
			} else if (field.equals("parent")) {
				setParent(value);
			} else if (field.equals("type")) {
				setType(value);
			} else if (field.equals("memberrev") || field.equals("memberrevision")) {
				setMemberRev(value);
			} else if (field.equals("memberrevlockedbyme")) {
				setMemberRevLocedByMe(value);
			} else if (field.equals("workingrevlockedbyme")) {
				setWorkingRevLockedByMe(value);
			}else if (field.equals("lockrecord")) {
				setLockrecord(value);
			}else if (field.equals("wfdelta")) {
				setWfdelta(value);
			}else if (field.equals("revsyncdelta")) {
				setRevsyncDelta(value);
			}else if (field.equals("newrevdelta")) {
				setNewRevDelta(value);
			}else if (field.equals("merge")) {
				setMerge(value);
			}else if (field.equals("canonicalSandbox")) {
				setCanonicalSandbox(value);
			}else if (field.equals("canonicalMember")) {
				setCanonicalMember(value);
			}else if (field.equals("projectDevpath")) {
				setProjectDevpath(value);
			}else if (field.equals("date")) {
				setDate(value);
			}else if (field.equals("frozen")) {
				setFrozen(value);
			}else if (field.equals("author")) {
				setAuthor(value);
			}else if (field.equals("projectname")) {
				setProjectName(value);
			}
		}
	}
	
	

	@Override
	public String toString() {
		return "Member [name=" + name + ", memberName=" + memberName
				+ ", parent=" + parent + ", type=" + type + ", memberRev="
				+ memberRev + ", memberRevLocedByMe=" + memberRevLocedByMe
				+ ", workingRevLockedByMe=" + workingRevLockedByMe
				+ ", lockrecord=" + lockrecord + ", wfdelta=" + wfdelta
				+ ", revsyncDelta=" + revsyncDelta + ", newRevDelta="
				+ newRevDelta + ", merge=" + merge + ", canonicalSandbox="
				+ canonicalSandbox + ", canonicalMember=" + canonicalMember
				+ ", projectDevpath=" + projectDevpath + ", date=" + date
				+ ", frozen=" + frozen + ", author=" + author
				+ ", projectName=" + projectName + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemberRev() {
		return memberRev;
	}

	public void setMemberRev(String memberRev) {
		this.memberRev = memberRev;
	}

	public String getMemberRevLocedByMe() {
		return memberRevLocedByMe;
	}

	public void setMemberRevLocedByMe(String memberRevLocedByMe) {
		this.memberRevLocedByMe = memberRevLocedByMe;
	}

	public String getWorkingRevLockedByMe() {
		return workingRevLockedByMe;
	}

	public void setWorkingRevLockedByMe(String workingRevLockedByMe) {
		this.workingRevLockedByMe = workingRevLockedByMe;
	}

	public String getLockrecord() {
		return lockrecord;
	}

	public void setLockrecord(String lockrecord) {
		this.lockrecord = lockrecord;
	}

	public String getWfdelta() {
		return wfdelta;
	}

	public void setWfdelta(String wfdelta) {
		this.wfdelta = wfdelta;
	}

	public String getRevsyncDelta() {
		return revsyncDelta;
	}

	public void setRevsyncDelta(String revsyncDelta) {
		this.revsyncDelta = revsyncDelta;
	}

	public String getNewRevDelta() {
		return newRevDelta;
	}

	public void setNewRevDelta(String newRevDelta) {
		this.newRevDelta = newRevDelta;
	}

	public String getMerge() {
		return merge;
	}

	public void setMerge(String merge) {
		this.merge = merge;
	}

	public String getCanonicalSandbox() {
		return canonicalSandbox;
	}

	public void setCanonicalSandbox(String canonicalSandbox) {
		this.canonicalSandbox = canonicalSandbox;
	}

	public String getCanonicalMember() {
		return canonicalMember;
	}

	public void setCanonicalMember(String canonicalMember) {
		this.canonicalMember = canonicalMember;
	}

	public String getProjectDevpath() {
		return projectDevpath;
	}

	public void setProjectDevpath(String projectDevpath) {
		this.projectDevpath = projectDevpath;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}

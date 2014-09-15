package models;

import java.util.Map;

public class Member {
	private String name, parent, type, memberRev, memberRevLocedByMe, workingRevLockedByMe, lockrecord, wfdelta, revsyncDelta, newRevDelta, merge, canonicalSandbox,canonicalMember,projectDevpath;
	private static final String[] fieldNames = {"name",	"parent","type","memberrev","memberrevlockedbyme","workingrevlockedbyme","lockrecord", "wfdelta","revsyncdelta","newrevdelta","merge","canonicalSandbox", "canonicalMember","projectDevpath"};
	public Member() {
		
	}
	
	public Member(Map<String, String> memberPrefs) {
		for (String field : memberPrefs.keySet()) {
			String value = memberPrefs.get(field);
			if (field.equals(fieldNames[0])) {
				setName(value);
			} else if (field.equals(fieldNames[1])) {
				setName(value);
			} else if (field.equals(fieldNames[2])) {
				setParent(value);
			} else if (field.equals(fieldNames[3])) {
				setType(value);
			} else if (field.equals(fieldNames[4])) {
				setMemberRev(value);
			} else if (field.equals(fieldNames[5])) {
				setMemberRevLocedByMe(value);
			} else if (field.equals(fieldNames[6])) {
				setWorkingRevLockedByMe(value);
			}else if (field.equals(fieldNames[7])) {
				setLockrecord(value);
			}else if (field.equals(fieldNames[8])) {
				setWfdelta(value);
			}else if (field.equals(fieldNames[9])) {
				setRevsyncDelta(value);
			}else if (field.equals(fieldNames[10])) {
				setNewRevDelta(value);
			}else if (field.equals(fieldNames[11])) {
				setMerge(value);
			}else if (field.equals(fieldNames[12])) {
				setCanonicalSandbox(value);
			}else if (field.equals(fieldNames[13])) {
				setCanonicalMember(value);
			}else if (field.equals(fieldNames[14])) {
				setProjectDevpath(value);
			}
		}
	}
	
	@Override
	public String toString() {
		return "Member [name=" + name + ", parent=" + parent + ", type=" + type
				+ ", memberRev=" + memberRev + ", memberRevLocedByMe="
				+ memberRevLocedByMe + ", workingRevLockedByMe="
				+ workingRevLockedByMe + ", lockrecord=" + lockrecord
				+ ", wfdelta=" + wfdelta + ", revsyncDelta=" + revsyncDelta
				+ ", newRevDelta=" + newRevDelta + ", merge=" + merge
				+ ", canonicalSandbox=" + canonicalSandbox
				+ ", canonicalMember=" + canonicalMember + ", projectDevpath="
				+ projectDevpath + "]";
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

}

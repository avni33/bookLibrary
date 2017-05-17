package com.epam.library.domain;

import java.io.Serializable;

public class Role implements Serializable {

	private static final long serialVersionUID = -7880686868763554191L;
	
	private int id;
	private String role;
	
	public Role() {}
	
	public Role(int id, String role) {
		this.id = id;
		this.role = role;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Role other = (Role) obj;
		if (id != other.id) {
			return false;
		}
		if (role == null) {
			if (other.role != null) {
				return false;
			}
		} else if (!role.equals(other.role)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + "]";
	}
	
}

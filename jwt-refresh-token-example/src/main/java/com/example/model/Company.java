//package com.example.model;
//
//import org.hibernate.annotations.Cascade;
//import org.hibernate.annotations.CascadeType;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//
//@Entity
//public class Company {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int companyId;
//	private String companyName;
//	private String companyAddress;
//	private Long companyPhone;
//	
//	@OneToMany
//	@Cascade(CascadeType.ALL)
//	private Employee employee;
//
//	public int getCompanyId() {
//		return companyId;
//	}
//
//	public void setCompanyId(int companyId) {
//		this.companyId = companyId;
//	}
//
//	public String getCompanyName() {
//		return companyName;
//	}
//
//	public void setCompanyName(String companyName) {
//		this.companyName = companyName;
//	}
//
//	public String getCompanyAddress() {
//		return companyAddress;
//	}
//
//	public void setCompanyAddress(String companyAddress) {
//		this.companyAddress = companyAddress;
//	}
//
//	public Long getCompanyPhone() {
//		return companyPhone;
//	}
//
//	public void setCompanyPhone(Long companyPhone) {
//		this.companyPhone = companyPhone;
//	}
//
//	public Employee getEmployee() {
//		return employee;
//	}
//
//	public void setEmployee(Employee employee) {
//		this.employee = employee;
//	}	
//}

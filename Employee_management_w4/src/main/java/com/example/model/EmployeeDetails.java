package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmployeeDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;
	private String Name;
    private double salary;
    private String address;
    

    public EmployeeDetails( int employeeId, String Name, double salary, String address) {
        this.employeeId = employeeId;
        this.Name=Name;
        this.salary = salary;
        this.address = address;
    }


	public int getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public double getSalary() {
		return salary;
	}


	public void setSalary(double salary) {
		this.salary = salary;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

    
}

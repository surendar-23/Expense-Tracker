package com.expensetracker.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Expense> expenses;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append(String.format("%-10s | %-20s\n", "ID", "Name"));
	    sb.append(String.format("%-10s | %-20s\n", "----------", "--------------------"));
	    sb.append(String.format("%-10d | %-20s\n", id, name));
	    return sb.toString();
	}

}

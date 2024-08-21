package com.expensetracker.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private double amount;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private LocalDate date;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append(String.format("%-5s | %-20s | %-10s | %-15s | %-15s | %-10s\n", "ID", "Description", "Amount", "Category", "User", "Date"));
	    sb.append(String.format("%-5s | %-20s | %-10s | %-15s | %-15s | %-10s\n", "-----", "--------------------", "----------", "---------------", "---------------", "----------"));
	    sb.append(String.format("%-5d | %-20s | %-10.2f | %-15s | %-15s | %-10s\n", id, description, amount, category.getName(), user.getName(), date.toString()));
	    return sb.toString();
	}

}

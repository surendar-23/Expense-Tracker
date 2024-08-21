package com.expensetracker.test;

import com.expensetracker.entity.Category;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;

import javax.persistence.*;

import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TestApp {
	private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			EntityManager em = entityManagerFactory.createEntityManager();
			boolean running = true;

			while (running) {
				System.out.println("1. Add User");
				System.out.println("2. Add Expense");
				System.out.println("3. View Users");
				System.out.println("4. View Expenses");
				System.out.println("5. Generate User Report");
				System.out.println("6. Generate Category Report");
				System.out.println("7. Update User");
				System.out.println("8. Update Expense");
				System.out.println("9. Delete User");
				System.out.println("10. Delete Expense");
				System.out.println("11. Exit");
				System.out.print("Choose an option: ");

				int option = scanner.nextInt();
				scanner.nextLine();

				switch (option) {
				case 1:
					addUser(scanner, em);
					break;
				case 2:
					addExpense(scanner, em);
					break;
				case 3:
					viewUsers(em);
					break;
				case 4:
					viewExpenses(em);
					break;
				case 5:
					generateUserReport(em);
					break;
				case 6:
					generateCategoryReport(em);
					break;
				case 7:
					updateUser(scanner, em);
					break;
				case 8:
					updateExpense(scanner, em);
					break;
				case 9:
					deleteUser(scanner, em);
					break;
				case 10:
					deleteExpense(scanner, em);
					break;
				case 11:
					running = false;
					break;
				default:
					System.out.println("Invalid option, please try again.");
					break;
				}
			}

			em.close();
			entityManagerFactory.close();
		}
	}

	private static void addUser(Scanner scanner, EntityManager em) {
		System.out.print("Enter User Name: ");
		String name = scanner.nextLine();

		User user = new User();
		user.setName(name);

		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.persist(user);
			transaction.commit();
			System.out.println("User added successfully!");
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			System.out.println("Failed to add user: " + e.getMessage());
		}
	}

	private static void addExpense(Scanner scan, EntityManager em) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();

			System.out.print("Enter Description: ");
			String description = scan.nextLine();

			System.out.print("Enter Amount: ");
			double amount = scan.nextDouble();
			scan.nextLine(); // consume newline

			System.out.print("Enter Category: ");
			String categoryName = scan.nextLine();

			System.out.print("Enter Date (YYYY-MM-DD): ");
			String dateString = scan.nextLine();
			LocalDate date = LocalDate.parse(dateString);

			System.out.print("Enter User ID: ");
			Long userId = scan.nextLong();
			scan.nextLine(); // consume newline

			Category category = getCategoryByName(categoryName, em);
			if (category == null) {
				category = new Category();
				category.setName(categoryName);
				em.persist(category);
			}

			User user = em.find(User.class, userId);
			if (user == null) {
				throw new EntityNotFoundException("User not found with ID: " + userId);
			}

			Expense expense = new Expense();
			expense.setDescription(description);
			expense.setAmount(amount);
			expense.setCategory(category);
			expense.setUser(user);
			expense.setDate(date);

			em.persist(expense);

			transaction.commit();
			System.out.println("Expense added successfully!");

		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			System.out.println("Failed to add expense: " + e.getMessage());
		}
	}

	private static Category getCategoryByName(String name, EntityManager em) {
		try {
			return em.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
					.setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	private static void viewUsers(EntityManager em) {
		String query = "SELECT u FROM User u";
		List<User> users = em.createQuery(query, User.class).getResultList();
		for (User user : users) {
			System.out.println(user);
		}
	}

	private static void viewExpenses(EntityManager em) {
		String query = "SELECT e FROM Expense e";
		List<Expense> expenses = em.createQuery(query, Expense.class).getResultList();
		for (Expense expense : expenses) {
			System.out.println(expense);
		}
	}

	@SuppressWarnings("unchecked")
	private static void generateUserReport(EntityManager em) {
		String query = "SELECT u.name, COUNT(e.id) FROM User u LEFT JOIN u.expenses e GROUP BY u.name";
		List<Object[]> report = em.createQuery(query).getResultList();
		for (Object[] row : report) {
			System.out.println("User: " + row[0] + ", Total Expenses: " + row[1]);
		}
	}

	@SuppressWarnings("unchecked")
	private static void generateCategoryReport(EntityManager em) {
		String query = "SELECT c.name, SUM(e.amount) FROM Category c LEFT JOIN c.expenses e GROUP BY c.name";
		List<Object[]> report = em.createQuery(query).getResultList();
		for (Object[] row : report) {
			System.out.println("Category: " + row[0] + ", Total Amount: " + row[1]);
		}
	}

	private static void updateUser(Scanner scanner, EntityManager em) {
		System.out.print("Enter User ID to update: ");
		Long userId = scanner.nextLong();
		scanner.nextLine();

		User user = em.find(User.class, userId);

		if (user != null) {
			System.out.print("Enter new User Name: ");
			String name = scanner.nextLine();
			user.setName(name);

			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.merge(user);
				transaction.commit();
				System.out.println("User updated successfully!");
			} catch (ConstraintViolationException e) {
				transaction.rollback();
				System.out.println("Failed to update user: " + e.getMessage());
			}
		} else {
			System.out.println("User not found!");
		}
	}

	private static void updateExpense(Scanner scanner, EntityManager em) {
		System.out.print("Enter Expense ID to update: ");
		Long expenseId = scanner.nextLong();
		scanner.nextLine();

		Expense expense = em.find(Expense.class, expenseId);

		if (expense != null) {
			System.out.println("1. Update Description");
			System.out.println("2. Update Amount");
			System.out.println("3. Update User");
			System.out.println("4. Update Category");
			System.out.println("5. Update Date");
			System.out.print("Choose an option: ");
			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1:
				System.out.print("Enter new Description: ");
				expense.setDescription(scanner.nextLine());
				break;
			case 2:
				System.out.print("Enter new Amount: ");
				expense.setAmount(scanner.nextDouble());
				scanner.nextLine();
				break;
			case 3:
				System.out.print("Enter new User ID: ");
				Long userId = scanner.nextLong();
				scanner.nextLine();
				User user = em.find(User.class, userId);
				if (user != null) {
					expense.setUser(user);
				} else {
					System.out.println("User not found!");
					return;
				}
				break;
			case 4:
				System.out.print("Enter new Category ID: ");
				Long categoryId = scanner.nextLong();
				scanner.nextLine();
				Category category = em.find(Category.class, categoryId);
				if (category != null) {
					expense.setCategory(category);
				} else {
					System.out.println("Category not found!");
					return;
				}
				break;
			case 5:
				System.out.print("Enter new Date (YYYY-MM-DD): ");
				expense.setDate(LocalDate.parse(scanner.nextLine()));
				break;
			default:
				System.out.println("Invalid option!");
				return;
			}

			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.merge(expense);
				transaction.commit();
				System.out.println("Expense updated successfully!");
			} catch (ConstraintViolationException e) {
				transaction.rollback();
				System.out.println("Failed to update expense: " + e.getMessage());
			}
		} else {
			System.out.println("Expense not found!");
		}
	}

	private static void deleteUser(Scanner scanner, EntityManager em) {
		System.out.print("Enter User ID to delete: ");
		Long userId = scanner.nextLong();
		scanner.nextLine();

		User user = em.find(User.class, userId);

		if (user != null) {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.remove(user);
				transaction.commit();
				System.out.println("User deleted successfully!");
			} catch (ConstraintViolationException e) {
				transaction.rollback();
				System.out.println("Failed to delete user: " + e.getMessage());
			}
		} else {
			System.out.println("User not found!");
		}
	}

	private static void deleteExpense(Scanner scanner, EntityManager em) {
		System.out.print("Enter Expense ID to delete: ");
		Long expenseId = scanner.nextLong();
		scanner.nextLine();

		Expense expense = em.find(Expense.class, expenseId);

		if (expense != null) {
			EntityTransaction transaction = em.getTransaction();
			try {
				transaction.begin();
				em.remove(expense);
				transaction.commit();
				System.out.println("Expense deleted successfully!");
			} catch (ConstraintViolationException e) {
				transaction.rollback();
				System.out.println("Failed to delete expense: " + e.getMessage());
			}
		} else {
			System.out.println("Expense not found!");
		}
	}
}

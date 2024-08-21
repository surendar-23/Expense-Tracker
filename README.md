# Expense Tracker

## Overview

- The **Expense Tracker** is a simple Java-based application for managing users, expenses, and categories.
- It uses Hibernate for ORM and MySQL as the database.
- This application allows users to add, update, view, and delete users and expenses, as well as generate reports based on users and categories.

## Features

- **User Management**

  - Add, update, and delete users
  - View list of users

- **Expense Management**

  - Add, update, and delete expenses
  - View list of expenses

- **Reports**
  - Generate user reports showing the total number of expenses per user
  - Generate category reports showing the total amount spent per category

## Technologies Used

- Java
- Hibernate (JPA implementation)
- MySQL
- Maven (for dependency management and build)

## Project Structure

- `src/main/java/com/expensetracker/entity/`: Contains JPA entity classes (`User`, `Expense`, `Category`).
- `src/main/java/com/expensetracker/test/`: Contains the main application class (`TestApp`) with methods for interacting with the application.
- `src/main/resources/META-INF/persistence.xml`: JPA configuration file for Hibernate.

## Prerequisites

- Java 11 or later
- MySQL Server
- Maven

## Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/surendar-23/Expense-Tracker.git
   cd expensetracker
   ```

2. **Configure Database**

   - Create a new MySQL database named `expensetracker`.
   - Update `src/main/resources/META-INF/persistence.xml` with your MySQL username and password.

3. **Build the Project**

   Ensure Maven is installed, then build the project:

   ```bash
   mvn clean install
   ```

4. **Run the Application**

   After building the project, you can run the application:

   ```bash
   mvn exec:java -Dexec.mainClass="com.expensetracker.test.TestApp"
   ```

## Usage

1. **Add User**: Enter the user name to create a new user.
2. **Add Expense**: Provide description, amount, category, date, and user ID to add a new expense.
3. **View Users**: List all users in the database.
4. **View Expenses**: List all expenses in the database.
5. **Generate User Report**: View a report of total expenses per user.
6. **Generate Category Report**: View a report of total amount spent per category.
7. **Update User**: Modify details of an existing user.
8. **Update Expense**: Modify details of an existing expense.
9. **Delete User**: Remove a user from the database.
10. **Delete Expense**: Remove an expense from the database.
11. **Exit**: Close the application.

## Contributing

Feel free to fork the repository and submit pull requests. Contributions are welcome!

## Contact

For any questions or issues, please open an issue on GitHub or contact surendarsenthilvelan@gmail.com

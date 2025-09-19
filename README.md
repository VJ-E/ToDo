# Todo Application

A Java-based Todo application with a graphical user interface that helps you manage your tasks efficiently. The application uses a SQLite database for data persistence and provides a user-friendly interface for managing your todo items.

## Features

- **Task Management**: Create, read, update, and delete todo items
- **Task Status**: Mark tasks as complete or incomplete
- **Detailed View**: View task details including title and description
- **Timestamps**: Automatic tracking of task creation and last update times
- **Filtering**: Filter tasks by completion status (All/Completed/Incomplete)
- **Responsive GUI**: Modern and intuitive user interface

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6.0 or higher
- SQLite (included with the application)

## Getting Started

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/todo-app.git
   cd todo-app
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

### Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.todo.Main"
```

The application will automatically create a SQLite database file (`todo.db`) in the project directory on first run.

## Project Structure

```
src/
└── main/
    └── java/
        └── com/
            └── todo/
                ├── Main.java               # Application entry point
                ├── dao/
                │   └── TodoAppDAO.java     # Data Access Object for Todo operations
                ├── gui/
                │   └── TodoAppGUI.java     # Main GUI implementation
                ├── model/
                │   └── Todo.java           # Todo model class
                └── util/
                    └── DatabaseConnection.java  # Database connection management
```

## Features in Detail

- **Add New Task**: Create new todo items with a title and description
- **Update Tasks**: Modify existing tasks or mark them as complete
- **Delete Tasks**: Remove tasks you no longer need
- **Refresh View**: Update the task list to reflect any changes
- **Status Filtering**: Filter tasks by completion status
- **Automatic Timestamping**: Each task tracks when it was created and last updated

## Database Schema

The application uses a SQLite database with the following schema:

- `todos` table:
  - `id` (INTEGER PRIMARY KEY)
  - `title` (TEXT NOT NULL)
  - `description` (TEXT)
  - `completed` (BOOLEAN DEFAULT 0)
  - `created_at` (TIMESTAMP)
  - `updated_at` (TIMESTAMP)

## Todo Model

The `Todo` class contains the following properties:
- `id`: Unique identifier for each todo item
- `title`: Short description of the task
- `description`: Detailed description of the task
- `completed`: Boolean flag indicating if the task is completed
- `created_at`: Timestamp when the task was created
- `updated_at`: Timestamp when the task was last updated


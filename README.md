# Todo Application

A simple Java-based Todo application that helps you manage your tasks efficiently.

## Features

- Create, read, update, and delete todo items
- Track task completion status
- Timestamps for task creation and updates
- Simple command-line interface

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6.0 or higher

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

## Project Structure

```
src/
└── main/
    └── java/
        └── com/
            └── todo/
                ├── Main.java          # Application entry point
                └── model/
                    └── Todo.java      # Todo model class
```

## Todo Model

The `Todo` class contains the following properties:
- `id`: Unique identifier for each todo item
- `title`: Short description of the task
- `description`: Detailed description of the task
- `completed`: Boolean flag indicating if the task is completed
- `created_at`: Timestamp when the task was created
- `updated_at`: Timestamp when the task was last updated

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

[Your Name] - Initial work

# IgniteTestAssignment

Book management system as specified in BookManagementSystem.pdf

## Tech Stack

* Java 8
* Spring boot
* H2 in memory database
* Thymeleaf for views
* Maven as dependency manager

### How to run

java -jar IgniteTestAssignment-0.0.1-SNAPSHOT.jar

### How to build

You can import it your IDE as a Maven project.  
In Eclipse: Import -> Maven -> Existing Maven projects  
You can find more in: https://spring.io/guides/gs/serving-web-content/

#### Addresses

http://localhost:8080/home - This is the main page where you can add books with "Add book" button  
Added book comes with two links: Edit and Delete  
Edit -> Opens the view of book where you can update its fields or add comment  
Delete -> Deletes the book from the list

#### Rules

1. While adding a book: Isbn, Title and Author fields must be entered and a unique Isbn must be used otherwise the book will not be added
2. While editing a book: Isbn, Title and Author fields must be entered otherwise it will return to home page without changing the book  
  2.1. If Isbn field is not changed then the existing book is updated with the other fields and its comments preserved  
  2.2. If Isbn field is changed then the existing book is deleted and book with new isbn is inserted with empty comments.
3. While adding a comment: Textarea must be entered otherwise it will return to homepage without a change
4. Last entered comment is the first comment in the comments list
5. Any operation with non-existing isbn will return to homepage without a change

##### Last comments

Liquibase is used in both dev and test environments. When you open the home page, you will notice there are books in the list and some comments in these books.  
They are loaded from "book.csv" and "comment.csv". You can just edit these files and use them in the application.


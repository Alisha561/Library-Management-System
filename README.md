This project is a menu-driven Java application designed to manage books, members, and transactions for a city library. It includes:

Book management

Member management

Issue/return operations

Search & sort functionality

Persistent storage using File Handling

Use of Java Collections Framework

Features
📖 Book Management

Add new books

Mark books as issued or returned

Search by title, author, category

Sorting by title (Comparable) and author (Comparator)

👤 Member Management

Add new members

Track issued books

Email validation (basic format)

🔄 Transactions

Issue a book

Return a book

Automatically prevents issuing an already issued book

💾 File Handling

Books stored in books.txt

Members stored in members.txt

Uses:

FileWriter, FileReader

BufferedWriter, BufferedReader

Automatic file creation if missing

🧰 Collections Used

Map<Integer, Book> → store books

Map<Integer, Member> → store members

List<Integer> → issued books for each member

Set<String> → unique categories

List<Book> → for sorting/searching

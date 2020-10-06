# Scripts

## db_generate.py
### Prerequisites:
* Have a folder with the same name as your database
* Have your GTFS files inside the folder
* Have Python 3.x installed

### Usage:
* Open your command line and type:
```
python db_generate.py *database_type* *database_name* *[host]* *[username]* *[password]*
```
*Creditentials are only needed, when you are generating a MySQL database

### Supported databases:
* MySQL
* SQLite
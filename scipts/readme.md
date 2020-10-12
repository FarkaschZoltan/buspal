# Scripts

## db_generate.py
### Prerequisites:
* Have a folder with the same name as your database
* Have your GTFS files inside the folder
* Have Python 3.x installed
* Have Go installed (optional)

### Usage:
* Open your command line and type:
```
python db_generate.py database_type database_name [host] [username] [password] [-nogo]
```
* Creditentials are only needed, when you are generating a MySQL database
* If you don't have Go installed, write "-nogo" at the end of the command. In this case expect worse performance and bigger output database

### Supported databases:
* MySQL
* SQLite

### Disclaimer
The script uses [@patricbr's gtfstidy script](https://github.com/patrickbr/gtfstidy) to compress the GTFS database size. All rights of that script goes to @patricbr.
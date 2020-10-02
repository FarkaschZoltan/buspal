import os
import time
##import sqlite3
import mysql.connector

## .py sqlite miskolc

start_time = time.time()
##args = sys.argv
##
##if len(args) < 2:
##    raise BaseException("Not enough parameters. Needed at least 2, got: " + len(args))
##
##if not os.path.exists("/" + args[1]):
##    raise FileNotFoundError("No directory exists: " + argv[1])
##
##if args[0] != "mysql" || args[0] != "sqlite":
##    raise BaseException("Unknown database type: " + argv[0])
##
##if args[0] == "mysql" && len(args) < 5

db_name = "miskolc"#input("Add meg az adatbazis nevet: ")

## régi törlése
if os.path.exists(db_name + ".db"):
    os.remove(db_name + ".db")

## fájlnevek összegyűjtése
files = os.listdir(db_name)
input_files = []
for i in files:
    if ".txt" in i:
        input_files.append(i)
print(input_files)

##conn = sqlite3.connect(db_name + ".db")
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="miskolc3")

c = conn.cursor()

for i in range(len(input_files)):
    with open(db_name + "/" + input_files[i], encoding="utf-8") as data:

        col_names = data.readline().strip().split(";")

        create_table_query = "CREATE TABLE IF NOT EXISTS `" + input_files[i][:-4] + "` ("
        for j in range(len(col_names)-1):
            create_table_query += "`" + col_names[j] + "` VARCHAR(255) NOT NULL, "
        create_table_query += "`" + col_names[-1] + "` VARCHAR(255) NOT NULL)"

        c.execute(create_table_query)

        data_to_insert = data.readline().strip().split(";")

        count = 0
        while len(data_to_insert) > 1:
            
            start = 0
            stop = 0
            for j in range(len(data_to_insert)):
                if len(data_to_insert[j]) == 0:
                   continue
                if data_to_insert[j][0] == '"':
                    start = j
                if data_to_insert[j][-1] == '"':
                   stop = j
            if start != 0:
                for j in range(start+1, stop+1):
                    data_to_insert[start] += "," + data_to_insert[j]
                for j in range(start+1, stop+1):
                    data_to_insert.pop(j)
                data_to_insert[start] = data_to_insert[start][1:-1]
            
            insert_query = "INSERT INTO `" + input_files[i][:-4] +"` VALUES ("
            for j in range(len(data_to_insert)-1):
                insert_query += "'" + data_to_insert[j] + "', "
            insert_query += "'" + data_to_insert[-1] + "')"

            c.execute(insert_query)

            if(count == 5000):
                conn.commit()
                
            count += 1
            data_to_insert = data.readline().strip().split(";")
        conn.commit()
                
conn.close()
print("--- {} seconds ---".format(time.time() - start_time))

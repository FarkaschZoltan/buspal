import os
import time
import sys
import sqlite3
import mysql.connector

## .py sqlite db
## .py mysql db host username password

def delete_old(db_type, db_name, cursor):
    if args[1] == "sqlite" and os.path.exists(db + ".db"):
        os.remove(db + ".db")
    else:
        files = os.listdir(db_name)
        for i in files:
            if ".txt" in i:
                cursor.execute("DROP TABLE " + i[:-4])

start_time = time.time()
args = sys.argv

## validating input
if args[1] == "-help":
    print("Usage: " + __name__ + ".py database_type database_name [host] [username] [password]")
    sys.exit()

if len(args) < 3:
    raise BaseException("Not enough parameters. Needed at least 2, got: " + str(len(args)-1) + ". Type " + __name__ + ".py -help for help.")

if not os.path.exists(args[2]):
    raise FileNotFoundError("No directory exists: " + args[2])

if args[1] != "mysql" and args[1] != "sqlite":
    raise BaseException("Unknown database type: " + args[1])

if args[1] == "mysql" and len(args) < 6:
    raise BaseException("Not enough parameters. Needed 5, got: " + str(len(args)-1))

## gathering connection data
conn = None
host = ""
user = ""
password = ""
db = ""
if args[1] == "sqlite":
    db = args[2]
    conn = sqlite3.connect(db + ".db")

if args[1]== "mysql":
    host = args[3]
    user = args[4]
    password = args[5]
    db = args[2]

    conn = mysql.connector.connect(
        host=host,
        user=user,
        password=password,
        database=db)

## deleting database if outdated
database_version = None
cursor = conn.cursor()
try:
    cursor.execute("SELECT version FROM database_version")
    database_version = cursor.fetchone()
    print(int(database_version[0]))

    with open(db + "/database_version.txt") as data:
        data.readline()
        current_version = data.readline().strip()
        print(current_version)
except:
    print("Database version not found")

if database_version != None:
    if int(database_version[0]) != int(current_version):
        delete_old(args[1], db, cursor)
    elif int(database_version[0]) == int(current_version):
        raise SystemExit("Database exists with the current version number")

## gathering files
files = os.listdir(db)
input_files = []
for i in files:
    if ".txt" in i:
        input_files.append(i)

## creating tables and inserting data
for i in range(len(input_files)):
    with open(db + "/" + input_files[i], encoding="utf-8") as data:
        print("Working on " + input_files[i])
        col_names = data.readline().strip().split(",")

        create_table_query = "CREATE TABLE IF NOT EXISTS `" + input_files[i][:-4] + "` ("
        for j in range(len(col_names)-1):
            create_table_query += "`" + col_names[j] + "` VARCHAR(255) NOT NULL, "
        create_table_query += "`" + col_names[-1] + "` VARCHAR(255) NOT NULL)"

        cursor.execute(create_table_query)

        data_to_insert = data.readline().strip().split(",")

        count = 0
        while len(data_to_insert) >= 1 and data_to_insert[0] != "":
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
            cursor.execute(insert_query)

            if(count == 5000):
                conn.commit()
                
            count += 1
            data_to_insert = data.readline().strip().split(",")
        conn.commit()

## finalizing     
conn.close()
print("--- {} seconds ---".format(time.time() - start_time))

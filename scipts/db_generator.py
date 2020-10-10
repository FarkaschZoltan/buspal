import os
import csv
import time
import sys
import sqlite3
import mysql.connector

## .py sqlite db
## .py mysql db host username password

## TODO query feed info from database, check if db is outdated and download the newer version + add database_version.txt

def compress(city, is_enabled):
    if is_enabled:
        os.system("go get github.com/patrickbr/gtfstidy")
        command = "gtfstidy -SCRmTcisOeD -o "
        command += city + " " + city + ".zip"
        print(command)
        print("Compression in progress ...")
        os.system(command)

def delete_old(db_type, db_name, cursor):
    if args[1] == "sqlite" and os.path.exists(db + ".db"):
        os.remove(db + ".db")
    else:
        files = os.listdir(db_name)
        for i in files:
            if ".txt" in i:
                cursor.execute("DROP TABLE " + i[:-4])

def determine_column_type(column_name, database_type):
    type_name = ""
    size = ""

    if "date" in column_name:
        type_name = "INT"
        size = "8"
    elif column_name == "monday" or column_name == "tuesday" or column_name == "wednesday" or \
        column_name == "thursday" or column_name == "friday" or column_name == "saturday" or \
        column_name == "sunday":
        type_name = "INT"
        size = "1"
    elif column_name == "exception_type":
        type_name = "INT"
        size = "4"
    elif "id" in column_name:
        type_name = "INT"
        size = "8"
    elif "time" in column_name and column_name != "exact_times" and column_name != "traversal_time" and database_type == "mysql":
        type_name = "TIME"
    elif column_name == "is_bidirectional":
        type_name = "BOOL"
    elif column_name == "traversal_time":
        type_name = "INT"
        size = "4"
    elif column_name == "route_color" or column_name == "route_text_color":
        type_name = "VARCHAR"
        size = "6"
    elif column_name == "shape_pt_lat" or column_name == "shape_pt_lon" or column_name == "stop_lat" or column_name == "stop_lon":
        type_name = "VARCHAR"
        size = "9"
    elif column_name == "stop_sequence":
        type_name = "VARCHAR"
        size = "3"
    else:
        type_name = "VARCHAR"
        size = "255"
    return "{}({}) NOT NULL".format(type_name, size)

def add_keys(table):
    keys = ""

    if table == "agency":
        keys = ", PRIMARY_KEY(`agency_id`)"
    elif table == "calendar":
        keys = ", PRIMARY_KEY(`service_id`)"
    elif table == "pathways":
        keys = ", PRIMARY_KEY(`pathway_id`)"
    elif table == "routes":
        keys = ", PRIMARY_KEY(`route_id`)"
    elif table == "stop_times":
        keys = ", PRIMARY_KEY(`stop_id`), FOREIGN_KEY(`trip_id`) REFERENCES trips(`trip_id`)"
    elif table == "stops":
        keys = ", PRIMARY_KEY(`stop_id`)"
    elif table == "trips":
        keys = ", PRIMARY_KEY(`trip_id`), FOREIGN_KEY(`route_id`) REFERENCES routes(`route_id`)"

    return keys

start_time = time.time()
args = sys.argv

## validating input
if args[1] == "-help":
    print("Usage: " + __name__ + ".py database_type database_name [host] [username] [password]")
    sys.exit()

if len(args) < 3:
    raise BaseException("Not enough parameters. Needed at least 2, got: " + str(len(args)-1) + ". Type " + __name__ + ".py -help for help.")

if not os.path.exists(args[2]) and not os.path.exists(args[2] + ".zip"):
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

## compressing input data
compress(db, args[-1] != "-nogo")

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
        print("Working on " + input_files[i] + "...", end="")
        col_names = data.readline().strip().split(",")

        create_table_query = "CREATE TABLE IF NOT EXISTS `" + input_files[i][:-4] + "` ("
        for j in range(len(col_names)-1):
            column_type = determine_column_type(col_names[j], args[1])
            create_table_query += "`" + col_names[j] + "` " + column_type + ", "
        column_type = determine_column_type(col_names[-1], args[1])
        create_table_query += "`" + col_names[-1] + "` " + column_type + add_keys(input_files[i][:-4]) + ")"

        cursor.execute(create_table_query)

        data_read = data.readline().strip()
        data_to_insert = [ '{}'.format(x) for x in list(csv.reader([data_read], delimiter=',', quotechar='"'))[0] ]

        count = 0
        while len(data_to_insert) >= 1 and data_to_insert[0] != "":
            
            insert_query = "INSERT INTO `" + input_files[i][:-4] +"` VALUES ("
            for j in range(len(data_to_insert)-1):
                insert_query += "'" + data_to_insert[j] + "', "
            insert_query += "'" + data_to_insert[-1] + "')"
            
            cursor.execute(insert_query)

            if(count == 5000):
                conn.commit()
                
            count += 1
            data_read = data.readline().strip()
            data_to_insert = [ '{}'.format(x) for x in list(csv.reader([data_read], delimiter=',', quotechar='"'))[0] ]

        conn.commit()
        print(" done")

## finalizing     
conn.close()
print("--- {} seconds ---".format(time.time() - start_time))

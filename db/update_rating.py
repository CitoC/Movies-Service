import re

# open the file
with open('movies_movie.sql', 'r', encoding='utf-8') as file:
    # read the contents of the file
    contents = file.read()

# extract the values using regex
# extract the id and rating from: 
# (5, 'Four Rooms', 1995, 3110, 5.7000, 2145, 4000000, 4257354, ...)
#  ^ id                            ^ rating
matches = re.findall(r"\((\d+),\s+'[^']*',\s+\d+,\s+\d+,\s+([\d.]+),", contents)

# iterate through the matches and print the first and fifth values
with open('update_rating.sql', 'w', encoding='utf-8') as out:
    for match in matches:
        sql = f"UPDATE movies.movie SET rating = {match[1]} WHERE id = {match[0]};\n\n"
        out.write(sql);
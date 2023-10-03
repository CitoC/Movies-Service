import re

# open the file
with open('movies_person.sql', 'r', encoding='utf-8') as file:
    # read the contents of the file
    contents = file.read()

# (id, name, birthday, biography, birthplace, popularity, profile_path)
# extract the values using regex
# extract the id and popularity from:  
pattern = r"\((\d+), '(?:[^']|'')*', (?:'(?:[^']|'')*'|NULL), (?:'(?:[^']|'')*'|NULL), (?:'(?:[^']|'')*'|NULL), ([\d.]+)"
matches = re.findall(pattern, contents)

with open('update_popularity.sql', 'w', encoding='utf-8') as out:
    for match in matches:
        sql = f"UPDATE movies.person SET popularity = {match[1]} WHERE id = {match[0]};\n\n"
        out.write(sql);
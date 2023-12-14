import json
import sys
import datetime

def select_keys_from_list_dict(list_of_dict, keys):
    sel_list_dict = list()
    for d in list_of_dict:
        sel_list_dict.append({k:d[k] for k in keys})
    return sel_list_dict

def str_sql_insert_generator(table, keys, list_dict_to_insert):

    insert_str = 'INSERT INTO ' + table + '('

    for k in keys:
            str_key = k.replace('\'', '\'\'').replace('\n', '\\n')
            insert_str += str_key
            if k != keys[-1]:
                insert_str += ', '
    
    insert_str += ') VALUES'

    for dict_to_insert in list_dict_to_insert:
        values = list(dict_to_insert.values())

        insert_str += '('

        for v in values:
            str_val = str(v).replace('\'', ' ').replace('\n', '\\n')
            insert_str += '\'' + str_val + '\''
            if v != values[-1]:
                insert_str += ', '
        if dict_to_insert != list_dict_to_insert[-1]:
            insert_str += '), \n'
        else:
            insert_str += ');\n'
        
    return insert_str


sql_insert_file_p = 'db/setup/podweb-podcasts-population.sql'
sql_insert_file_e = 'db/setup/podweb-episodes-population.sql'

json_podcasts = 'podcasts.json'

print('\nRécupération des podcasts...\n\n')

try:
    with open(json_podcasts, 'r') as json_data:
        data_p = json.load(json_data)
except IOError:
    print('Aucun ficher nommé: ' + json_podcasts + 'trouvé!!\n\n')
    sys.exit(1)

keys_podcast = ['title', 'description', 'url', 'imageUrl', 'itunesAuthor', 'episodeCount']
keys_episode = ['title', 'description', 'duration', 'datePublished', 'enclosureUrl']

keys_insert_podcast = ['title', 'description', 'rss_feed', 'image', 'author', 'episodes_count', 'id']
keys_insert_episode = ['title', 'description', 'duration', 'released_at', 'audio_url', 'podcast_id']

podcasts_data = select_keys_from_list_dict(data_p, keys_podcast)
i = 0
for podcast in podcasts_data:
    podcast['id'] = i
    i += 1

with open(sql_insert_file_p, 'w+', encoding='utf-8') as f:
    print('Création des requêtes SQL d\'insertion des podcasts')
    f.write('SET search_path TO podweb;\n')
    f.write('\n--INSERT PODCASTS\n')
    f.write(str_sql_insert_generator('podcasts', keys_insert_podcast, podcasts_data))


with open(sql_insert_file_e, 'w+', encoding='utf-8') as f:
    print('\nCréation des requêtes SQL d\'insertion des épisodes')
    f.write('SET search_path TO podweb;\n')
    f.write('\n--INSERT EPISODES\n')
    i = 0
    for podcast in podcasts_data:
        name_f = data_p[i]
        episodes_file = 'Episodes/' + name_f['id'] + '.json'
        try:
            with open(episodes_file, 'r') as json_e:
                data_e = json.load(json_e)
        except IOError:
            print('\nAucun ficher nommé: ' + episodes_file + ' trouvé!!')
            print('On saute ce podcast\n\n')
            continue

        episodes_data = select_keys_from_list_dict(data_e['items'], keys_episode)

        for episode in episodes_data:
            episode['title'] = episode['title'][:250]
            episode['datePublished'] = datetime.datetime.fromtimestamp(episode['datePublished'])
            episode['podcast_id'] = podcast['id']

        f.write(str_sql_insert_generator('episodes', keys_insert_episode, episodes_data))
        i += 1
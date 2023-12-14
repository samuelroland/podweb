from os.path import exists as file_exists
from tqdm import tqdm
import podcastindex
import json
import time
import sys

config = {
    "api_key": "",
    "api_secret": ""
}

index = podcastindex.init(config)

json_podcasts = 'podcasts.json'

print('\nRécupération des podcasts...\n\n')

try:
    with open(json_podcasts, 'r') as json_data:
        data = json.load(json_data)
except IOError:
    print('Aucun ficher nommé: ' + json_podcasts + 'trouvé!!\n\n')
    sys.exit(1)

print('Lancement de la récupération des épisodes...\n')

for pInd in tqdm(range(len(data)), desc='Podcasts récupérés'):
    podcast = data[pInd]
    episodes_file = 'Episodes/' + podcast['id'] + '.json'

    if file_exists(episodes_file):
        print('\n\nLes épisodes du podcast ' + podcast['id'] + ' ont déjà été récupérés...\n')
        continue

    nb_episode = 1 + int(podcast['episodeCount'])
    results = index.episodesByFeedId(podcast['id'], max_results=nb_episode)

    with open(episodes_file, 'w+', encoding='utf-8') as f:
        json.dump(results, f, ensure_ascii=False, indent=4)
    
    time.sleep(2)

print('\n')

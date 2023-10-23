# Modèle conceptuel de données - Podweb

En se basant sur les données et fonctionnalités de Podweb décrites lors de la phase 1, nous avons conçu le modèle conceptuel suivant:

![mcd](mcd.svg)

**Remarques:**
1. Podcast
	- `episodes_count`: ce champ ne peut pas être déduit du nombre d'épisodes stockés car c'est le nombre total. Nous n'allons pas forcèment stocker la totalité des épisodes parce que certains podcasts ont des milliers d'épisodes. Cette valeur provient de la base de données Podcastindex.org et sert à l'affichage dans la liste des podcasts.
	- `author`: Dans la base de données PodcastIndex, nous avons uniquement le nom (`itunesAuthor`) par ex. `Kevin Zade` et (`itunesOwnerName`) par ex. `One Brew Over the Cuckoos Nest`. Nous avons considéré le fait d'avoir une entité `authors` mais il n'y aurait que 2 champs, et s'il y a 2 personnes avec les mêmes noms on ne pourrait pas les différencier. Nous allons donc juste garder la valeur de `itunesAuthor` dans `author` et cela n'empêche pas la recherche de podcasts par le nom d'auteur.

1. Listening
   - `listening_count` est le nombre total d'écoute entre un utilisateur et un épisode. Cette valeur est incrémentée chaque fois que l'écoute atteint 100%.
1. Playlist
   - La combinaison du nom de la playlist et de l'email de l'utilisateur est unique. Cela ne fait pas de sens d'avoir 2 playlists du même nom sur son compte.

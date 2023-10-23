# Modèle conceptuel de données - Podweb

```plantuml
scale 1.5
hide circle
' skinparam linetype ortho
skinparam fixCircleLabelOverlapping true
skinparam classAttributeIconSize 0
hide methods

'Entities
entity User {
	firstname
	lastname
	{static} email
	password
}

entity Podcast {
	title
	description
	{static} rss_feed
	image
	author
	episodes_count 
	'ok ??
}

entity Episode {
	title
	description
	duration
	released_at
	{static} audio_url
}

entity Listening {
	progression
	listening_count
}

entity Category {
	{static} name
}

entity Playlist {
	'unique seulement sur la combinaison name+auteur.
	description
}

entity QueuedEpisode {
	index
}

'Relationships
Podcast "1" o-"*" Episode
'TODO: arrow direction is correct ?
Podcast "*"--"*" Category: categorize <
Episode "*"--"*" User: listen >
(Episode, User) . Listening
User [name] "1"-"*" Playlist: create
Episode "*"--"*" User: queue <
Playlist "*"--"*" Episode: list >

' note "CI: Users cannot have \na given episode \nmore than one time in \ntheir listening queue." as N1
' note "CI: The combination of QueuedEpisode's index \nand associated user is unique." as N2
' N1 .. QueuedEpisode
' N2 .. QueuedEpisode
' QueuedEpisode .. (User, Episode)
' note "CI: There's at max one listening entry linking\na podcast episode and a user" as N3

```

**Remarques:**
1. Podcast
   1. `episodes_count`: ce champ ne peut pas être déduit du nombre d'épisodes stockés car c'est le nombre total. Nous n'allons pas forcèment stocker la totalité des épisodes parce que certains podcasts ont des milliers d'épisodes. Cette valeur provient de la base de données Podcastindex.org et sert à l'affichage dans la liste des podcasts.
   1. `author`: Dans la base de données PodcastIndex, nous avons uniquement le nom (`itunesAuthor`) par ex. `Kevin Zade` et (`itunesOwnerName`) par ex. `One Brew Over the Cuckoos Nest`. Nous avons considéré le fait d'avoir une entité `authors` mais il n'y aurait que 2 champs, et s'il y a 2 personnes avec les mêmes noms on ne pourrait pas les différencier. Nous allons donc juste garder la valeur de `itunesAuthor` dans `author` et cela n'empêche pas la recherche de podcasts par le nom d'auteur.

1. Listening
    1.  `listening_count` est le nombre total d'écoute entre un utilisateur et un épisode. Cette valeur est incrémentée chaque fois que l'écoute atteint 100%.
1. Playlist
   1. La combinaison du nom de la playlist et de l'email de l'utilisateur est unique. Cela ne fait pas de sens d'avoir 2 playlists du même nom sur son compte.



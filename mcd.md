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
}

entity Category {
	{static} name
}

entity Playlist {
	{static} name
	'unique seulement sur la combinaison name+auteur.
	description
}

'Relationships
Podcast "1" o-"*" Episode: own
'TODO: arrow direction is correct ?
Podcast "*"--"*" Category: categorize
Episode "*"--"*" User: listen
(Episode, User) . Listening

```
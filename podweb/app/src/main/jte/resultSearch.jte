@import podweb.models.EpisodeSearch
@import java.util.ArrayList

@param ArrayList<EpisodeSearch> episodes

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Podweb</title>
    <link rel="stylesheet" href="out.css">
</head>

<body class="font-sans antialiased">


<div class="flex flex-wrap">
    @for(EpisodeSearch episode : episodes)

        <p class="font-bold line-clamp-1 mt-1 mb-2">${episode.title}</p>
        <div class="flex text-gray-600">
            <p class="flex-1">${episode.description}</p>
            <p class="">${episode.duration}</p>
            <p class="">${episode.podcast_title}</p>
            <p class="">${episode.podcast_author}</p>
            <p class="">${episode.podcast_description}</p>
        </div>

        <audio controls>
            <source src="${episode.audio_url}" type="audio/mp3">

        </audio>


    @endfor
</div>

</body>

</html>
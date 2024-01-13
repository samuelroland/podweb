@import podweb.models.Episode
@import podweb.models.Podcast
@import java.util.ArrayList

@param Podcast podcast
@param ArrayList<Episode> episodes

    <!DOCTYPE html>
    <html lang="fr">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Podweb</title>
        <link rel="stylesheet" href="out.css">
    </head>

    <body class="font-sans antialiased">
        <div class="mx-2 my-1 w-64 hover:bg-blue-100 p-1">
            <%-- <img src="${podcast.image}" alt="" class="w-full">--%>
                <p class="font-bold line-clamp-1 mt-1 mb-2">${podcast.title}</p>
                <div class="flex text-gray-600">
                    <p class="flex-1">${podcast.author}</p>
                    <p class="">${podcast.episodes_count}</p>
                </div>
        </div>

        <div class="flex flex-wrap">


            @for(Episode episode : episodes)
            <p class="font-bold line-clamp-1 mt-1 mb-2">${episode.title}</p>
            <div class="flex text-gray-600">
                <p class="flex-1">${episode.description}</p>
                <p class="">${episode.duration}</p>
            </div>

            <audio controls>
                <source src="${episode.audio_url}" type="audio/mp3">

            </audio>

            @endfor
        </div>

    </body>

    </html>
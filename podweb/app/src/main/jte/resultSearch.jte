@import podweb.models.Episode
@import podweb.models.EpisodeSearch
@import java.util.ArrayList

@param Object loggedUser
@param ArrayList<EpisodeSearch> episodes
@param String query

    @template.layout(
    content = @`
    <h1>Search</h1>
        <form class="flex" action="/search" method="GET">
            <input value="${query}" type="text" name="q" class="p-1 w-full flex-1" placeholder="Search in title, description and author texts!">
            <button type="submit" class="ml-3">Search</button>
        </form>
    @if (!episodes.isEmpty())
    <h3 class="">Episodes trouvés : </h3>
    <div class="">
        @if (episodes.isEmpty())
            <h4 class="">${"Aucun! Cherche autre chose!!!"}</h4>
        @endif
        @for(EpisodeSearch episode : episodes)
            <h4 class="">${episode.title}</h4>
        <div class="my-3">
            <div class="text-gray-700">
                <p class="">$unsafe{episode.description.replace("\\n", "")}</p>
                <p class="">Durée: ${episode.duration()}</p>

                <a href="/podcasts/${episode.podcast_id}" class="text-blue-500">
                    <h4 class="text-blue-500">${"Viens du podcast : " + episode.podcast_title}</h4>
                </a>
                <p class="">$unsafe{"Description : " + episode.podcast_description}</p>
                <p class="">${"Auteur : " + episode.podcast_author}</p>
                <a href="/podcasts/${episode.podcast_id}/comments" class="text-blue-500">
                    <h4 class="text-blue-500">${"Cliquez ici pour acceder aux commentaires"}</h4>
                </a>

            <audio controls class="w-full my-1" src="${episode.audio_url}"></audio>
        </div>
    </div>
        @endfor
    @endif

        `,
        loggedUser
        )
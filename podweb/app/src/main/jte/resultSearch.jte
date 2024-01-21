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
                @template.episode-details(episode=episode)
                <a href="/podcasts/${episode.podcast_id}">
                    <h4>Sur le podcast <span class="text-blue-500">${episode.podcast_title}</span></h4>
                </a>
                <div class="flex">
                    <div class="mr-3 w-28 h-28">
                        <img src="${episode.podcast_image}" class="">
                    </div>
                    <div class="flex-1">
                        <p class="">$unsafe{episode.podcast_description.replace("\\n", "")}</p>
                        <p class="">${episode.podcast_author}</p>
                    </div>
                </div>
            @endfor
        </div>
    @endif
`,
loggedUser
)
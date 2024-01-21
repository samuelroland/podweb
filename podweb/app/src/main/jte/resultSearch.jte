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
        <input value="${query}" type="text" name="q" class="p-1 w-full flex-1"
               placeholder="Search in title, description and author texts!">
        <button type="submit" class="ml-3">Search</button>
    </form>
    @if (episodes.isEmpty() && query != "")
        <h4 class="">${"Aucun! Cherche autre chose!!!"}</h4>
    @elseif (!episodes.isEmpty())

        <h3 class="">Episodes trouvés : </h3>
        <br>
        <div class="">
            @for(EpisodeSearch episode : episodes)

                @template.episode-details(episode=episode)

                <a href="/podcasts/${episode.podcast_id}">
                    <h4>Information sur le podcast : <span
                                class="text-blue-500 hover:text-blue-600 transition duration-200">${episode.podcast_title}</span>
                    </h4>
                </a>
                <div class="podcast-block">
                    <a href="/podcasts/${episode.podcast_id}" class="">
                    <div>


                            <img src="${episode.podcast_image}" class="w-100 h-100" alt="Image du podcast">

                    </div>
                    </a>
                    <div>
                        <p class="text-xl font-bold">${episode.podcast_title}</p>
                        <p class="text-justify">$unsafe{episode.podcast_description.replace("\\n", "")}</p>
                        <p class="text-sm">${episode.podcast_author}</p>
                    </div>
                </div>
                <br>
                <br>
            @endfor
        </div>
    @endif
`,
loggedUser
)

<style>
    .podcast-block {
        background-color: #f2f2f2;
        padding: 20px;
        margin-bottom: 20px;
        border: 1px solid #ccc;
        border-radius: 10px;
    }

    .podcast-block:not(:last-child) {
        margin-bottom: 0;
    }

    .podcast-block div {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .podcast-block img {
        width: 100px;
        height: 100px;
        margin-bottom: 20px;
        align-self: center;
    }

    .podcast-block p {
        margin-bottom: 20px;
    }


</style>
@import podweb.models.Episode
@import podweb.models.EpisodeSearch
@import java.util.ArrayList

@param Object loggedUser
@param ArrayList<EpisodeSearch> episodes

    @template.layout(
    content = @`

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
                <p class="" id="formattedDuration_${episode.id}"></p>

                <script>
                    const durationInSeconds_${episode.id} = ${episode.duration};
                    const hours_${episode.id} = Math.floor(durationInSeconds_${episode.id} / 3600);
                    const minutes_${episode.id} = Math.floor((durationInSeconds_${episode.id} % 3600) / 60);
                    const seconds_${episode.id} = durationInSeconds_${episode.id} % 60;

                    const formattedDuration_${episode.id} = "Durée : " + hours_${episode.id} + "h " + minutes_${episode.id} + "m " + seconds_${episode.id} + "s";
                    const durationElement_${episode.id} = document.getElementById("formattedDuration_${episode.id}");
                    durationElement_${episode.id}.textContent = formattedDuration_${episode.id};
                </script>

                <a href="/podcasts/${episode.podcast_id}" class="text-blue-500">
                    <h4 class="text-blue-500">${"Viens du podcast : " + episode.podcast_title}</h4>
                </a>
                <p class="">$unsafe{"Description : " + episode.podcast_description}</p>
                <p class="">${"Auteur : " + episode.podcast_author}</p>

            <audio controls class="w-full my-1" src="${episode.audio_url}"></audio>
        </div>
        @endfor

        `,
        loggedUser
        )
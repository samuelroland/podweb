@import podweb.models.Episode
@import podweb.models.Podcast
@import podweb.models.User
@import java.util.ArrayList

@param Podcast podcast
@param Object loggedUser
@param ArrayList<Episode> episodes

    @template.layout(
    content = @`
    @template.podcast-square(podcast, true)

    <h3 class="">Episodes</h3>
    <div class="">
        @for(Episode episode : episodes)
        <div class=" my-3">
            <p class="font-bold line-clamp-2 my-2 text-2xl">${episode.title}</p>
            <div class="text-gray-600 flex-1 block line-clamp-6">${episode.description}</div>
            <audio controls class="w-full my-1" src="${episode.audio_url}"></audio>
        </div>
        @endfor

        `,
        loggedUser
        )
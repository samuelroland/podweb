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
            @template.episode-details(episode=episode)
        @endfor

        `,
        loggedUser
        )
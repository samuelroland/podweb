@import podweb.models.Podcast
@import podweb.models.User
@import java.util.ArrayList

@param Object loggedUser
@param ArrayList<Podcast> podcasts

    @template.layout(
    content = @`
    <h1>Podcasts</h1>
    <div class="flex flex-wrap">
        @for(Podcast podcast : podcasts)
        @template.podcast-square(podcast, false)
        @endfor
    </div>
    `,
    loggedUser
    )
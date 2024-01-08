@import podweb.models.Podcast
@import podweb.models.User
@import java.util.ArrayList

@param User loggedUser = null
@param ArrayList<Podcast> podcasts

    @template.layout(
    content = @`
    <h1>salut</h1>
    <div class="flex flex-wrap">
        @for(Podcast podcast : podcasts)
        @template.podcast(podcast)
        @endfor
    </div>
    `,
    loggedUser
    )